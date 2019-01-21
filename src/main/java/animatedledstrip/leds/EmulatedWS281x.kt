package animatedledstrip.leds

/*
 * Parts of this file were converted to Kotlin by IntelliJ from a file with this
 * copyright.
 *
 * #%L
 * Organisation: mattjlewis
 * Project:      Device I/O Zero - WS281x Java Wrapper
 * Filename:     WS281x.java
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
 *
 *
 *
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


import com.diozero.ws281xj.LedDriverInterface
import java.nio.ByteBuffer


/**
 * Modification on the WS281x class from the diozero-ws281x-java library that
 * doesn't attempt to send data to any LEDs.
 *
 * @property pin Pin the strip is connected to
 * @property brightness Brightness of the strip
 * @property numPixels Number of pixels in the strip
 */
open class EmulatedWS281x(val pin: Int, val brightness: Int, private val numPixels: Int) : LedDriverInterface {
    override fun getNumPixels() = numPixels

    override fun render() {}

    override fun allOff() {}

    override fun close() {}

    private val SIZE_OF_INT = 4

    private var ledArray: ByteBuffer = ByteBuffer.allocate(numPixels * SIZE_OF_INT)

    private fun validatePixel(pixel: Int) {
        if (pixel < 0 || pixel >= numPixels) {
            throw IllegalArgumentException("pixel must be 0.." + (numPixels - 1))
        }
    }

    override fun getPixelColour(pixel: Int): Int {
        validatePixel(pixel)
        return ledArray.getInt(pixel * SIZE_OF_INT)
    }

    override fun setPixelColour(pixel: Int, colour: Int) {
        validatePixel(pixel)
        ledArray.putInt(pixel * SIZE_OF_INT, colour)
    }
}
