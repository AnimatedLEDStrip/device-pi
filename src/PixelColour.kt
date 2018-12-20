import java.awt.Color

/*
 * This file was converted to Kotlin by IntelliJ from a file with this copyright.
 *
 * #%L
 * Organisation: mattjlewis
 * Project:      Device I/O Zero - WS281x Java Wrapper
 * Filename:     PixelColour.java
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

/**
 * 0x00RRGGBB
 */
object PixelColour {
    val RED = 0x00200000
    val ORANGE = 0x00201000
    val YELLOW = 0x00202000
    val GREEN = 0x00002000
    val LIGHT_BLUE = 0x00002020
    val BLUE = 0x00000020
    val PURPLE = 0x00100010
    val PINK = 0x00200010

    val RAINBOW = intArrayOf(RED, ORANGE, YELLOW, GREEN, LIGHT_BLUE, BLUE, PURPLE, PINK)

    private val WHITE_MASK = 0x00ffffff
    private val RED_MASK = 0x00ff0000
    private val GREEN_MASK = 0x0000ff00
    private val BLUE_MASK = 0x000000ff

    private val RED_OFF_MASK = 0x0000ffff
    private val GREEN_OFF_MASK = 0x00ff00ff
    private val BLUE_OFF_MASK = 0x00ffff00

    val COLOUR_COMPONENT_MAX = 0xff

    /**
     * Input a value 0 to 255 to get a colour value.
     * The colours are a transition r - g - b - back to r.
     * @param wheelPos Position on the colour wheel (range 0..255).
     * @return 24-bit RGB colour value
     */
    fun wheel(wheelPos: Int): Int {
        val max = COLOUR_COMPONENT_MAX
        val one_third = COLOUR_COMPONENT_MAX / 3
        val two_thirds = COLOUR_COMPONENT_MAX * 2 / 3

        var wheel_pos = max - wheelPos
        if (wheel_pos < one_third) {
            return createColourRGB(max - wheel_pos * 3, 0, wheel_pos * 3)
        }
        if (wheel_pos < two_thirds) {
            wheel_pos -= one_third
            return createColourRGB(0, wheel_pos * 3, max - wheel_pos * 3)
        }
        wheel_pos -= two_thirds
        return createColourRGB(wheel_pos * 3, max - wheel_pos * 3, 0)
    }

    /**
     * Create a colour from relative RGB values
     * @param red Red %, `0 to 1`
     * @param green Green %, `0 to 1`
     * @param blue Blue %, `0 to 1`
     * @return RGB colour integer value
     */
    fun createColourRGB(red: Float, green: Float, blue: Float): Int {
        return createColourRGB(Math.round(COLOUR_COMPONENT_MAX * red),
                Math.round(COLOUR_COMPONENT_MAX * green), Math.round(COLOUR_COMPONENT_MAX * blue))
    }

    /**
     * Create a colour from int RGB values
     * @param red Red component `0 to 255`
     * @param green Green component `0 to 255`
     * @param blue Blue component `0 to 255`
     * @return RGB colour integer value
     */
    fun createColourRGB(red: Int, green: Int, blue: Int): Int {
        validateColourComponent("Red", red)
        validateColourComponent("Green", green)
        validateColourComponent("Blue", blue)
        return red shl 16 or (green shl 8) or blue
    }

    /**
     * Creates a colour based on the specified values in the HSB colour model.
     *
     * @param hue The hue, in degrees, `0.0 to 1.0`
     * @param saturation The saturation %, `0.0 to 1.0`
     * @param brightness The brightness %, `0.0 to 1.0`
     * @return RGB colour integer value
     * @throws IllegalArgumentException if `hue`, `saturation`, `brightness` are out of range
     */
    fun createColourHSB(hue: Float, saturation: Float, brightness: Float): Int {
        // Take advantage of Hue Saturation Brightness utility method in java.awt.Color
        return Color.HSBtoRGB(hue, saturation, brightness) and WHITE_MASK
    }

    /**
     * Creates a colour based on the specified values in the HSL colour model.
     *
     * @param hue The hue, in degrees, `0.0 to 360.0`
     * @param saturation The saturation %, `0.0 to 1.0`
     * @param luminance The luminance %, `0.0 to 1.0`
     * @return RGB colour integer value
     * @throws IllegalArgumentException if `hue`, `saturation`, `brightness` are out of range
     */
    fun createColourHSL(hue: Float, saturation: Float, luminance: Float): Int {
        var hue = hue
        var saturation = saturation
        // TODO Not sure this is correct - needs testing!

        // Hue Saturation Luminance - see https://tips4java.wordpress.com/2009/07/05/hsl-color/
        // Or javafx
        //javafx.scene.paint.Color c = javafx.scene.paint.Color.web("hsl(270,100%,100%)");// blue as an hsl web value, implicit alpha

        if (saturation < 0.0f) {
            saturation = 0f
        }
        if (saturation > 1.0f) {
            saturation = 1f
        }

        if (luminance < 0.0f || luminance > 1.0f) {
            val message = "Color parameter outside of expected range - Luminance"
            throw IllegalArgumentException(message)
        }

        // Formula needs all values between 0 - 1.
        hue = hue % 360.0f
        hue /= 360f

        var q = 0f

        if (luminance < 0.5)
            q = luminance * (1 + saturation)
        else
            q = luminance + saturation - saturation * luminance

        val p = 2 * luminance - q

        var r = Math.max(0f, HueToRGB(p, q, hue + 1.0f / 3.0f))
        var g = Math.max(0f, HueToRGB(p, q, hue))
        var b = Math.max(0f, HueToRGB(p, q, hue - 1.0f / 3.0f))

        r = Math.min(r, 1.0f)
        g = Math.min(g, 1.0f)
        b = Math.min(b, 1.0f)

        return createColourRGB(r, g, b)
    }

    private fun HueToRGB(p: Float, q: Float, h: Float): Float {
        var h = h
        if (h < 0)
            h += 1f

        if (h > 1)
            h -= 1f

        if (6 * h < 1) {
            return p + (q - p) * 6f * h
        }

        if (2 * h < 1) {
            return q
        }

        return if (3 * h < 2) {
            p + (q - p) * 6f * (2.0f / 3.0f - h)
        } else p

    }

    fun validateColourComponent(colour: String, value: Int) {
        if (value < 0 || value >= 256) {
            throw IllegalArgumentException("Illegal colour value (" + value +
                    ") for '" + colour + "' - must be 0.." + COLOUR_COMPONENT_MAX)
        }
    }

    fun getRedComponent(colour: Int): Int {
        return colour and RED_MASK shr 16
    }

    fun setRedComponent(colour: Int, red: Int): Int {
        validateColourComponent("Red", red)
        var new_colour = colour and RED_OFF_MASK
        new_colour = new_colour or (red shl 16)
        return new_colour
    }

    fun getGreenComponent(colour: Int): Int {
        return colour and GREEN_MASK shr 8
    }

    fun setGreenComponent(colour: Int, green: Int): Int {
        validateColourComponent("Green", green)
        var new_colour = colour and GREEN_OFF_MASK
        new_colour = new_colour or (green shl 8)
        return new_colour
    }

    fun getBlueComponent(colour: Int): Int {
        return colour and BLUE_MASK
    }

    fun setBlueComponent(colour: Int, blue: Int): Int {
        validateColourComponent("Blue", blue)
        var new_colour = colour and BLUE_OFF_MASK
        new_colour = new_colour or blue
        return new_colour
    }
}
