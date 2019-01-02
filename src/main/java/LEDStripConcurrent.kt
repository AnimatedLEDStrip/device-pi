package animatedledstrip.leds

/*
 *  Copyright (c) 2018 AnimatedLEDStrip
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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


/**
 * A LED Strip with concurrency added. Bridge between the AnimatedLEDStripConcurrent class and the WS281x class
 *
 * @param numLEDs Number of leds in the strip
 * @param pin GPIO pin connected for signal
 * @param emulated Is this strip real or emulated?
 */
open class LEDStripConcurrent(var numLEDs: Int, pin: Int, private val emulated: Boolean = false) {

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

    init {
        for (i in 0 until numLEDs) locks += Pair(i, Mutex())
//        ledStrip = WS281x(pin, 255, numLEDs)
        println("numLEDs: $numLEDs")
        println("using GPIO pin: $pin")
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
                locks[pixel]!!.tryWithLock {
                    ledStrip.setPixelColourRGB(pixel, colorValues.r, colorValues.g, colorValues.b)
                }
            }
        } catch (e: Exception) {
            println("ERROR in setPixelColor: $e")
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
            println("ERROR in setPixelRed: $e")
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
            println("ERROR in setPixelGreen: $e")
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
            println("ERROR in setPixelBlue")
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

    fun getPixelRed(pixel: Int): Int {
        try {
            return runBlocking {
                locks[pixel]!!.withLock {
                    return@runBlocking ledStrip.getRedComponent(pixel)
                }
            }
        } catch (e: Exception) {
            println("ERROR in getPixelRed: $e")
        }
        return 0
    }

    fun getPixelGreen(pixel: Int): Int {
        try {
            return runBlocking {
                locks[pixel]!!.withLock {
                    return@runBlocking ledStrip.getGreenComponent(pixel)
                }
            }
        } catch (e: Exception) {
            println("ERROR in getPixelGreen: $e")
        }
        return 0
    }

    fun getPixelBlue(pixel: Int): Int {
        try {
            return runBlocking {
                locks[pixel]!!.withLock {
                    return@runBlocking ledStrip.getBlueComponent(pixel)
                }
            }
        } catch (e: Exception) {
            println("ERROR in getPixelBlue: $e")
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
            println("ERROR in getPixelColor: $e")
        }
        println("Color not retrieved")
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

    // TODO: Upgrade all of these to use List and colorsFromPalette()
    // Not thread safe!
//    fun setStripFromPalette(
//        paletteType: RGBPalette16,
//        startIndex: Int,
//        blendType: TBlendType = TBlendType.LINEARBLEND,
//        brightness: Int = 255
//    ) {
//
//        var index = startIndex
//        val stringBuilder = StringBuilder()
//        for (i in 0 until ledStrip.numPixels) {
//
//            val color = colorFromPalette(paletteType, index, brightness, blendType)
//            stringBuilder.append("$i: ${color.hex.toString(16)}")
//            ledStrip.setPixelColourRGB(i, color.r, color.g, color.b)
//            index += 3
//
//        }
//
//        println(stringBuilder)
//    }
    // TODO: Convert these to work with both emulated and real LEDStrips
//    fun setStripWithGradient(colorValues1: ColorContainer, colorValues2: ColorContainer) =
//            fillGradientRGB(ledStrip, numLEDs, colorValues1, colorValues2)
//
//    fun setStripWithGradient(colorValues1: ColorContainer, colorValues2: ColorContainer, colorValues3: ColorContainer) =
//            fillGradientRGB(ledStrip, numLEDs, colorValues1, colorValues2, colorValues3)
//
//    fun setStripWithGradient(colorValues1: ColorContainer, colorValues2: ColorContainer, colorValues3: ColorContainer, colorValues4: ColorContainer) =
//            fillGradientRGB(ledStrip, numLEDs, colorValues1, colorValues2, colorValues3, colorValues4)


//    fun colorListFromPalette(pal: RGBPalette16, offset: Int) {
//        val one16 = (numLEDs / 16)
//        val two16 = ((numLEDs * 2) / 16)
//        val three16 = ((numLEDs * 3) / 16)
//        val four16 = ((numLEDs * 4) / 16)
//        val five16 = ((numLEDs * 5) / 16)
//        val six16 = ((numLEDs * 6) / 16)
//        val seven16 = ((numLEDs * 7) / 16)
//        val eight16 = ((numLEDs * 8) / 16)
//        val nine16 = ((numLEDs * 9) / 16)
//        val ten16 = ((numLEDs * 10) / 16)
//        val eleven16 = ((numLEDs * 11) / 16)
//        val twelve16 = ((numLEDs * 12) / 16)
//        val thirteen16 = ((numLEDs * 13) / 16)
//        val fourteen16 = ((numLEDs * 14) / 16)
//        val fifteen16 = ((numLEDs * 15) / 16)
//        val sixteen16 = numLEDs
//        colorsFromPalette(ledStrip, numLEDs, 0, pal[0], one16, pal[1], offset)
//        colorsFromPalette(ledStrip, numLEDs, one16, pal[1], two16, pal[2], offset)
//        colorsFromPalette(ledStrip, numLEDs, two16, pal[2], three16, pal[3], offset)
//        colorsFromPalette(ledStrip, numLEDs, three16, pal[3], four16, pal[4], offset)
//        colorsFromPalette(ledStrip, numLEDs, four16, pal[4], five16, pal[5], offset)
//        colorsFromPalette(ledStrip, numLEDs, five16, pal[5], six16, pal[6], offset)
//        colorsFromPalette(ledStrip, numLEDs, six16, pal[6], seven16, pal[7], offset)
//        colorsFromPalette(ledStrip, numLEDs, seven16, pal[7], eight16, pal[8], offset)
//        colorsFromPalette(ledStrip, numLEDs, eight16, pal[8], nine16, pal[9], offset)
//        colorsFromPalette(ledStrip, numLEDs, nine16, pal[9], ten16, pal[10], offset)
//        colorsFromPalette(ledStrip, numLEDs, ten16, pal[10], eleven16, pal[11], offset)
//        colorsFromPalette(ledStrip, numLEDs, eleven16, pal[11], twelve16, pal[12], offset)
//        colorsFromPalette(ledStrip, numLEDs, twelve16, pal[12], thirteen16, pal[13], offset)
//        colorsFromPalette(ledStrip, numLEDs, thirteen16, pal[13], fourteen16, pal[14], offset)
//        colorsFromPalette(ledStrip, numLEDs, fourteen16, pal[14], fifteen16, pal[15], offset)
//        colorsFromPalette(ledStrip, numLEDs, fifteen16, pal[15], sixteen16, pal[0], offset)
//
//        show()
//    }


    /**
     * Attempt to lock renderLock and send data to the LEDs. Returns if another
     * thread has locked renderLock.
     */
    fun show() {
        try {
            runBlocking {
                renderLock.tryWithLock {
                    ledStrip.render()
                }
            }
        } catch (e: Exception) {
            println("ERROR in show: $e")
        }
    }
}
