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


import javafx.scene.paint.Color

open class ColorContainer(var r: Int, var g: Int, var b: Int) {
    constructor(hexIn: Long) : this(
        (hexIn and 0xFF0000 shr 16).toInt(),
        (hexIn and 0x00FF00 shr 8).toInt(),
        (hexIn and 0x0000FF).toInt()
    )

    var hex: Long
        get() {
            return (r shl 16).toLong() or (g shl 8).toLong() or b.toLong()
        }
        set(hexIn) {
            setRGBFromHex(hexIn)
        }

    var hexString = hex.toString(16)

    fun setRGB(rIn: Int, gIn: Int, bIn: Int) {
        r = rIn
        g = gIn
        b = bIn
    }

    fun setRGBFromHex(hexIn: Long) {
        setRGB((hexIn and 0xFF0000 shr 16).toInt(), (hexIn and 0x00FF00 shr 8).toInt(), (hexIn and 0x0000FF).toInt())
    }

    @Deprecated("Use hex property instead", ReplaceWith("hex"))
    fun getColorHex() = hex

    fun toColor() = Color.color((hex shr 16 and 0xFF) / 255.0, (hex shr 8 and 0xFF) / 255.0, (hex and 0xFF) / 255.0)

    fun invert() = ColorContainer(255 - this.r, 255 - this.g, 255 - this.b)

    fun grayscale() = ((r + g + b) / 3).toLong()

}
