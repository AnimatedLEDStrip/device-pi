package animatedledstrip.leds

/*
 *  Copyright (c) 2019 AnimatedLEDStrip
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */


import animatedledstrip.ccpresets.CCBlack
import com.diozero.ws281xj.rpiws281x.WS281x
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.pmw.tinylog.Logger
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.StringBuilder


/**
 * A LED Strip with concurrency added. Bridge between the AnimatedLEDStrip class and the WS281x class
 *
 * @param numLEDs Number of leds in the strip
 * @param pin GPIO pin connected for signal
 * @param emulated Is this strip real or emulated?
 */
open class LEDStrip(
    var numLEDs: Int,
    pin: Int,
    private val emulated: Boolean = false,
    private val constantRender: Boolean = true,
    private val imageDebugging: Boolean = false
) {

    /**
     * The LED Strip. Chooses between WS281x and EmulatedWS281x based on value of emulated.
     */
    var ledStrip = when (emulated) {
        true -> EmulatedWS281x(pin, 255, numLEDs)
        false -> WS281x(pin, 255, numLEDs)
    }

    /**
     * Map containing Mutex instances for locking access to each led while it is
     * being used
     */
    private val locks = mutableMapOf<Int, Mutex>()

    /**
     * Mutex that tracks if a thread is sending data to the LEDs
     */
    private val renderLock = Mutex()

    var stopRender = false

    init {
        val historyFile = if (imageDebugging) FileWriter(
            "colors_${SimpleDateFormat("MMDDYY_hhmmss").format(Date())}.csv",
            true
        ) else null
        val buffer = if (imageDebugging) StringBuilder() else null
        for (i in 0 until numLEDs) locks += Pair(i, Mutex())
        Logger.info("numLEDs: $numLEDs")
        Logger.info("using GPIO pin $pin")
        var a = 0
        if (constantRender) GlobalScope.launch(newSingleThreadContext("Render Loop")) {
            while (!stopRender) {
                ledStrip.render()
                if (imageDebugging) {
                    getPixelColorList().forEach { buffer!!.append("${(it and 0xFF0000 shr 16).toInt()},${(it and 0x00FF00 shr 8).toInt()},${(it and 0x0000FF).toInt()},") }

                    buffer!!.append("0,0,0\n")
                    if (a++ >= 1000) {
                        historyFile!!.append(buffer)
                        buffer.clear()
                        a = 0
                    }

                    historyFile!!.append(buffer)
                }
            }
        }
    }

    /**
     * Returns true if this is an emulated LED strip
     */
    fun isEmulated() = emulated


    /**
     * Sets a pixel's color. If another thread has locked the pixel's Mutex,
     * this skips setting the pixel's color and returns.
     *
     * @param pixel The pixel to change
     * @param colorValues The color to set the pixel to
     */
    fun setPixelColor(pixel: Int, colorValues: ColorContainer) {
        try {
            runBlocking {
                locks[pixel]!!.tryWithLock(owner = "Pixel $pixel") {
                    ledStrip.setPixelColourRGB(pixel, colorValues.r, colorValues.g, colorValues.b)
                }
            }
        } catch (e: Exception) {
            Logger.error("ERROR in setPixelColor: $e\npixel: $pixel to color ${colorValues.hexString}")
        }
    }


    /**
     * Set a pixel's color with r, g, b (ranges 0-255). If another thread has
     * locked the pixel's Mutex, this skips setting the pixel's color and returns.
     *
     * @param pixel The pixel to change
     * @param rIn Red intensity of the color
     * @param gIn Green intensity of the color
     * @param bIn Blue intensity of the color
     */
    fun setPixelColor(pixel: Int, rIn: Int, gIn: Int, bIn: Int) {
        setPixelColor(pixel, ColorContainer(rIn, gIn, bIn))
    }


    /**
     * Set a pixel's color with a Long, such as a 24-bit integer. If another
     * thread has locked the pixel's Mutex, this skips setting the pixel's color
     * and returns.
     *
     * @param pixel The pixel to change
     * @param hexIn The color to set the pixel to
     */
    fun setPixelColor(pixel: Int, hexIn: Long) {
        setPixelColor(pixel, ColorContainer(hexIn))
    }

    fun setPixelRed(pixel: Int, rIn: Int) {
        try {
            runBlocking {
                locks[pixel]!!.tryWithLock {
                    ledStrip.setRedComponent(pixel, rIn)
                }
            }
        } catch (e: Exception) {
            Logger.error("ERROR in setPixelRed: $e")
        }
    }

    fun setPixelGreen(pixel: Int, gIn: Int) {
        try {
            runBlocking {
                locks[pixel]!!.tryWithLock {
                    ledStrip.setGreenComponent(pixel, gIn)
                }
            }
        } catch (e: Exception) {
            Logger.error("ERROR in setPixelGreen: $e")
        }
    }

    fun setPixelBlue(pixel: Int, bIn: Int) {
        try {
            runBlocking {
                locks[pixel]!!.tryWithLock {
                    ledStrip.setBlueComponent(pixel, bIn)
                }
            }
        } catch (e: Exception) {
            Logger.error("ERROR in setPixelBlue")
        }
    }


    /**
     * Loops through all pixels and sets their color to colorValues. If a pixel's
     * Mutex is locked by another thread, it is skipped.
     *
     * @param colorValues The color to set the strip to
     */
    fun setStripColor(colorValues: ColorContainer) {
        for (i in 0 until numLEDs) setPixelColor(i, colorValues.r, colorValues.g, colorValues.b)
        show()
    }


    /**
     * Set the strip color with a Long, such as a 24-bit integer. If a pixel's
     * Mutex is locked by another thread, it is skipped.
     *
     * @param hexIn The color to set the strip to
     */
    fun setStripColor(hexIn: Long) {
        for (i in 0 until numLEDs) setPixelColor(i, hexIn)
        show()
    }


    /**
     * Set the strip color with r, g, b (ranges 0-255). If a pixel's Mutex is
     * locked by another thread, it is skipped.
     *
     * @param rIn Red intensity of the color
     * @param gIn Green intensity of the color
     * @param bIn Blue intensity of the color
     */
    fun setStripColor(rIn: Int, gIn: Int, bIn: Int) {
        for (i in 0 until numLEDs) ledStrip.setPixelColourRGB(i, rIn, gIn, bIn)
        show()
    }

    /**
     * Set the color of a section of the strip. Loops through all leds between start
     * and end (inclusive) and sets their color to colorValues. If a pixel's Mutex
     * is locked by another thread, it is skipped.
     *
     * @param start First pixel in section
     * @param end Last pixel in section
     * @param colorValues The color to set the section to
     */
    fun setSectionColor(start: Int, end: Int, colorValues: ColorContainer) {
        for (i in start..end) setPixelColor(i, colorValues.r, colorValues.g, colorValues.b)
        show()
    }


    /**
     * Set a section's color with a Long, such as a 24-bit integer. If a pixel's
     * Mutex is locked by another thread, it is skipped.
     *
     * @param start First pixel in section
     * @param end Last pixel in section
     * @param hexIn The color to set the section to
     */
    fun setSectionColor(start: Int, end: Int, hexIn: Long) {
        for (i in start..end) setPixelColor(i, hexIn)
        show()
    }


    /**
     * Set a section's color with r, g, b (ranges 0-255). If a pixel's Mutex is
     * locked by another thread, it is skipped.
     *
     * @param start First pixel in section
     * @param end Last pixel in section
     * @param rIn Red intensity of the color
     * @param gIn Green intensity of the color
     * @param bIn Blue intensity of the color
     */
    fun setSectionColor(start: Int, end: Int, rIn: Int, gIn: Int, bIn: Int) {
        for (i in start..end) ledStrip.setPixelColourRGB(i, rIn, gIn, bIn)
        show()
    }

    @Deprecated("Use getPixelColor and r property of the resulting ColorContainer", ReplaceWith("getPixelColor().r"))
    fun getPixelRed(pixel: Int): Int {
        try {
            return runBlocking {
                locks[pixel]!!.withLock {
                    return@runBlocking ledStrip.getRedComponent(pixel)
                }
            }
        } catch (e: Exception) {
            Logger.error("ERROR in getPixelRed: $e")
        }
        return 0
    }

    @Deprecated("Use getPixelColor and g property of the resulting ColorContainer", ReplaceWith("getPixelColor().g"))
    fun getPixelGreen(pixel: Int): Int {
        try {
            return runBlocking {
                locks[pixel]!!.withLock {
                    return@runBlocking ledStrip.getGreenComponent(pixel)
                }
            }
        } catch (e: Exception) {
            Logger.error("ERROR in getPixelGreen: $e")
        }
        return 0
    }

    @Deprecated("Use getPixelColor and b property of the resulting ColorContainer", ReplaceWith("getPixelColor().b"))
    fun getPixelBlue(pixel: Int): Int {
        try {
            return runBlocking {
                locks[pixel]!!.withLock {
                    return@runBlocking ledStrip.getBlueComponent(pixel)
                }
            }
        } catch (e: Exception) {
            Logger.error("ERROR in getPixelBlue: $e")
        }
        return 0
    }


    /**
     * Get the color of a pixel. Waits until the pixel's Mutex is unlocked.
     *
     * @param pixel The pixel to find the color of
     * @return The color of the pixel
     */
    fun getPixelColor(pixel: Int): ColorContainer {
        try {
            return runBlocking {
                locks[pixel]!!.withLock {
                    return@runBlocking ColorContainer(ledStrip.getPixelColour(pixel).toLong())
                }
            }
        } catch (e: Exception) {
            Logger.error("ERROR in getPixelColor: $e")
        }
        Logger.warn("Color not retrieved")
        return CCBlack
    }


    /**
     * Get the color of a pixel as a Long. Waits until the pixel's Mutex is
     * unlocked.
     *
     * @param pixel The pixel to find the color of
     * @return The color of the pixel as a Long
     */
    fun getPixelLong(pixel: Int): Long {
        return getPixelColor(pixel).hex
    }


    /**
     * Get the color of a pixel as a hexadecimal string. Waits until the pixel's
     * Mutex is unlocked.
     *
     * @param pixel The pixel to find the color of
     * @return A string containing the color of the pixel in hexadecimal
     */
    fun getPixelHexString(pixel: Int): String {
        return getPixelLong(pixel).toString(16)
    }


    /**
     * Get the colors of all pixels as a List of Longs. Waits until each pixel's
     * Mutex is unlocked.
     */
    fun getPixelColorList(): List<Long> {
        val temp = mutableListOf<Long>()
        for (i in 0 until numLEDs) temp.add(getPixelLong(i))
        return temp
    }


    /**
     * Set the color of the strip using a map with each pixel index mapped to a
     * ColorContainer.
     *
     * @param palette The map of colors
     * @param offset The index of the pixel that will be set to the color at
     * index 0
     */
    fun setStripColorWithPalette(palette: Map<Int, ColorContainer>, offset: Int = 0) =
        palette.forEach { i, j ->
            setPixelColor((i + offset) % numLEDs, j)
        }


    /**
     * Sets the color of the strip with a list. The list is converted to a map
     * before that map is sent to [setStripColorWithPalette] with an offset of 0.
     *
     * @param colorList The list of colors
     */
    fun setStripColorWithGradient(colorList: List<ColorContainer>) {
        val palette = colorsFromPalette(colorList, numLEDs)
        setStripColorWithPalette(palette)
    }


    /**
     * Attempt to lock renderLock and send data to the LEDs. Returns if another
     * thread has locked renderLock.
     */
    fun show() {
        if (constantRender) return
        try {
            runBlocking {
                renderLock.tryWithLock(owner = "Render") {
                    ledStrip.render()
                }
            }
        } catch (e: Exception) {
            Logger.error("ERROR in show: $e")
        }
    }
}
