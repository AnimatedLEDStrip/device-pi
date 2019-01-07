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


import com.diozero.ws281xj.rpiws281x.WS281x
import org.pmw.tinylog.Logger


/**
 * Class that represents a led strip.
 *
 * @param numLEDs Number of leds in the strip
 * @param pin GPIO pin connected for signal
 */
open class LEDStrip(var numLEDs: Int, pin: Int, private val emulated: Boolean = false) {

    /**
     * The LED Strip. Chooses between WS281x and EmulatedWS281x based on value of emulated.
     */
    var ledStrip = when (emulated) {
        true -> EmulatedWS281x(pin, 255, numLEDs)
        false -> WS281x(pin, 255, numLEDs)
    }

    init {
        Logger.info("using GPIO pin $pin")
    }

    fun setPixelColor(pixel: Int, colorValues: ColorContainer) {
        ledStrip.setPixelColourRGB(pixel, colorValues.r, colorValues.g, colorValues.b)
    }

    fun setPixelColor(pixel: Int, rIn: Int, gIn: Int, bIn: Int) {
        setPixelColor(pixel, ColorContainer(rIn, gIn, bIn))
    }

    fun setPixelColor(pixel: Int, hexIn: Long) {
        setPixelColor(pixel, ColorContainer(hexIn))
    }

    fun setPixelRed(pixel: Int, rIn: Int) {
        ledStrip.setRedComponent(pixel, rIn)
    }

    fun setPixelGreen(pixel: Int, gIn: Int) {
        ledStrip.setGreenComponent(pixel, gIn)
    }

    fun setPixelBlue(pixel: Int, bIn: Int) {
        ledStrip.setBlueComponent(pixel, bIn)
    }

    fun setStripColor(colorValues: ColorContainer) {
        for (i in 0 until numLEDs) setPixelColor(i, colorValues)
        show()
    }

    fun setStripColor(hexIn: Long) {
        for (i in 0 until numLEDs) setPixelColor(i, hexIn)
//        for (i in 0 until numLEDs) ledStrip.setPixelColourRGB(i, (hexIn and 0xFF0000 shr 16).toInt(), (hexIn and 0x00FF00 shr 8).toInt(), (hexIn and 0x0000FF).toInt())
        show()
    }

    fun setStripColor(rIn: Int, gIn: Int, bIn: Int) {
        for (i in 0 until numLEDs) setPixelColor(i, rIn, gIn, bIn)
        show()
    }

    fun getPixelRed(pixel: Int): Int {
        return ledStrip.getRedComponent(pixel)
    }

    fun getPixelGreen(pixel: Int): Int {
        return ledStrip.getGreenComponent(pixel)
    }

    fun getPixelBlue(pixel: Int): Int {
        return ledStrip.getBlueComponent(pixel)
    }

    fun getPixelColor(pixel: Int): ColorContainer =
        ColorContainer(getPixelRed(pixel), getPixelGreen(pixel), getPixelBlue(pixel))

    fun getPixelLong(pin: Int): Long {
        return getPixelColor(pin).hex
    }

    fun getPixelHexString(pin: Int): String {
        return getPixelLong(pin).toString(16)
    }

    // TODO: Upgrade all of these to use List and colorsFromPalette()
//    fun setStripFromPalette(
//        paletteType: RGBPalette16,
//        startIndex: Int,
//        blendType: TBlendType = TBlendType.LINEARBLEND,
//        brightness: Int = 255
//    ) {
//
//        var index = startIndex
//
//        for (i in 0 until ledStrip.numPixels) {
//
//            val color = colorFromPalette(paletteType, index, brightness, blendType)
//
//            ledStrip.setPixelColourRGB(i, color.r, color.g, color.b)
//            index += 3
//
//        }
//
//    }

//    fun setStripWithGradient(colorValues1: ColorContainer, colorValues2: ColorContainer) =
//        fillGradientRGB(ledStrip, numLEDs, colorValues1, colorValues2)
//
//    fun setStripWithGradient(colorValues1: ColorContainer, colorValues2: ColorContainer, colorValues3: ColorContainer) =
//        fillGradientRGB(ledStrip, numLEDs, colorValues1, colorValues2, colorValues3)
//
//    fun setStripWithGradient(
//        colorValues1: ColorContainer,
//        colorValues2: ColorContainer,
//        colorValues3: ColorContainer,
//        colorValues4: ColorContainer
//    ) =
//        fillGradientRGB(ledStrip, numLEDs, colorValues1, colorValues2, colorValues3, colorValues4)
//
//
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

    fun show() {
        ledStrip.render()
    }
}
