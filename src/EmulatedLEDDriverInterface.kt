import java.io.Closeable

/*-
 * This file was converted to Kotlin by IntelliJ from a file with this copyright.
 *
 * #%L
 * Organisation: mattjlewis
 * Project:      Device I/O Zero - WS281x Java Wrapper
 * Filename:     LedDriverInterface.java
 *
 * This file is part of the diozero project. More information about this project
 * can be found at http://www.diozero.com/
 * %%
 * Copyright (C) 2016 - 2017 mattjlewis
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

interface EmulatedLEDDriverInterface : Closeable {

    val numPixels: Int
    override fun close()

    /**
     * Push any updated colours to the LED strip.
     */
    fun render()

    /**
     * Turn off all pixels and render.
     */
    fun allOff()

    /**
     * Get the current colour for the specified pixel.
     * @param pixel Pixel number.
     * @return 24-bit RGB colour value.
     */
    fun getPixelColour(pixel: Int): Int

    /**
     * Set the colour for the specified pixel.
     * @param pixel Pixel number.
     * @param colour Colour represented as a 24bit RGB integer (0x0RGB).
     */
    fun setPixelColour(pixel: Int, colour: Int)

    /**
     * Set the colour for the specified pixel using individual red / green / blue 8-bit values.
     * @param pixel Pixel number.
     * @param red 8-bit value for the red component.
     * @param green 8-bit value for the green component.
     * @param blue 8-bit value for the blue component.
     */
    fun setPixelColourRGB(pixel: Int, red: Int, green: Int, blue: Int) {
        setPixelColour(pixel, PixelColour.createColourRGB(red, green, blue))
    }

    /**
     * Set the colour for the specified pixel using Hue Saturation Brightness (HSB) values.
     * @param pixel Pixel number.
     * @param hue Float value in the range 0..1 representing the hue.
     * @param saturation Float value in the range 0..1 representing the colour saturation.
     * @param brightness Float value in the range 0..1 representing the colour brightness.
     */
    fun setPixelColourHSB(pixel: Int, hue: Float, saturation: Float, brightness: Float) {
        setPixelColour(pixel, PixelColour.createColourHSB(hue, saturation, brightness))
    }

    /**
     *
     * Set the colour for the specified pixel using Hue Saturation Luminance (HSL) values.
     *
     * HSL colour mapping code taken from [this HSL Color class by Rob Camick](https://tips4java.wordpress.com/2009/07/05/hsl-color/).
     * @param pixel Pixel number.
     * @param hue Represents the colour (think colours of the rainbow), specified in degrees from 0 - 360. Red is 0, green is 120 and blue is 240.
     * @param saturation Represents the purity of the colour. Range is 0..1 with 1 fully saturated and 0 gray.
     * @param luminance Represents the brightness of the colour. Range is 0..1 with 1 white 0 black.
     */
    fun setPixelColourHSL(pixel: Int, hue: Float, saturation: Float, luminance: Float) {
        setPixelColour(pixel, PixelColour.createColourHSL(hue, saturation, luminance))
    }

    /**
     * Get the 8-bit red component value for the specified pixel.
     * @param pixel Pixel number.
     * @return 8-bit red component value.
     */
    fun getRedComponent(pixel: Int): Int {
        return PixelColour.getRedComponent(getPixelColour(pixel))
    }

    /**
     * Set the 8-bit red component value for the specified pixel.
     * @param pixel Pixel number.
     * @param red 8-bit red component value.
     */
    fun setRedComponent(pixel: Int, red: Int) {
        setPixelColour(pixel, PixelColour.setRedComponent(getPixelColour(pixel), red))
    }

    /**
     * Get the 8-bit green component value for the specified pixel.
     * @param pixel Pixel number.
     * @return 8-bit green component value.
     */
    fun getGreenComponent(pixel: Int): Int {
        return PixelColour.getGreenComponent(getPixelColour(pixel))
    }

    /**
     * Set the 8-bit green component value for the specified pixel.
     * @param pixel Pixel number.
     * @param green 8-bit green component value.
     */
    fun setGreenComponent(pixel: Int, green: Int) {
        setPixelColour(pixel, PixelColour.setGreenComponent(getPixelColour(pixel), green))
    }

    /**
     * Get the 8-bit blue component value for the specified pixel.
     * @param pixel Pixel number.
     * @return 8-bit blue component value.
     */
    fun getBlueComponent(pixel: Int): Int {
        return PixelColour.getBlueComponent(getPixelColour(pixel))
    }

    /**
     * Set the 8-bit blue component value for the specified pixel.
     * @param pixel Pixel number.
     * @param blue 8-bit blue component value.
     */
    fun setBlueComponent(pixel: Int, blue: Int) {
        setPixelColour(pixel, PixelColour.setBlueComponent(getPixelColour(pixel), blue))
    }
}
