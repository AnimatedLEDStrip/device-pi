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


import com.github.mbelling.ws281x.LedStripType
import com.github.mbelling.ws281x.Ws281xLedStrip


/**
 * Connection between the WS281x class and the LEDStripInterface interface.
 *
 * @param pin Pin the strip is connected to
 * @param brightness Brightness of the strip
 * @param numLEDs Number of LEDs in the strip
 */
class WS281xCompat(pin: Int, brightness: Int, override val numLEDs: Int) : Ws281xLedStrip(
        numLEDs,
        pin,
        800000,
        10,
        brightness,
        0,
        false,
        LedStripType.WS2811_STRIP_RGB,
        false
), LEDStripInterface {

    override fun close() {}
    override fun getPixelColor(pixel: Int): Int = getPixel(pixel).toInt()
    override fun setPixelColor(pixel: Int, color: Int) =
            setPixel(pixel,
                    color shr 16 and 0xFF,
                    color shr 8 and 0xFF,
                    color and 0xFF)
}