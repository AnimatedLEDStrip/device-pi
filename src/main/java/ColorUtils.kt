package animatedledstrip.leds

/*
 *  Copyright (c) 2018 AnimatedLEDStrip
 *  Copyright (c) 2013 FastLED
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
import kotlin.math.roundToInt

enum class TBlendType {
    LINEARBLEND, NOBLEND
}

fun nblend(existing: ColorContainer, overlay: ColorContainer, amountOfOverlay: Int): ColorContainer {
    if (amountOfOverlay == 0) return existing

    if (amountOfOverlay == 255) return overlay

    val r = blend8(existing.r, overlay.r, amountOfOverlay)
    val g = blend8(existing.g, overlay.g, amountOfOverlay)
    val b = blend8(existing.b, overlay.b, amountOfOverlay)

    return ColorContainer(r, g, b)

}


fun colorsFromPalette(palette: List<ColorContainer>, numLEDs: Int): Map<Int, ColorContainer> {

    val returnMap = mutableMapOf<Int, ColorContainer>()

    val spacing = numLEDs.toDouble() / palette.size.toDouble()

    val pureColors = mutableListOf<Int>()
    for (i in 0 until palette.size) {
        pureColors.add((spacing * i).roundToInt())
    }

    for (i in 0 until numLEDs) {
        for (j in pureColors) {
            if ((i - j) < spacing) {
                if ((i - j) == 0) returnMap[i] = palette[pureColors.indexOf(j)]
                else {
                    returnMap[i] = blend(
                        palette[pureColors.indexOf(j)],
                        palette[(pureColors.indexOf(j) + 1) % pureColors.size],
                        if (pureColors.indexOf(j) < pureColors.size - 1) (((i - j) / ((pureColors[pureColors.indexOf(j) + 1]) - j).toDouble()) * 255).toInt() else (((i - j) / (numLEDs - j).toDouble()) * 255).toInt()
                    )
                }
                break
            }
        }
    }
    return returnMap
}


fun fillGradientRGB(leds: WS281x, startpos: Int, startcolor: ColorContainer, endpos: Int, endcolor: ColorContainer) {
    var endColor = endcolor
    var startColor = startcolor
    var endPos = endpos
    var startPos = startpos

    if (endpos < startpos) {
        val t = endPos
        val tc = endColor
        endColor = startColor
        endPos = startPos
        startPos = t
        startColor = tc
    }

    val rdistance87 = (endColor.r - startColor.r) shl 7
    val gdistance87 = (endColor.g - startColor.g) shl 7
    val bdistance87 = (endColor.b - startColor.b) shl 7

    val pixeldistance = endPos - startPos
    val divisor = if (pixeldistance != 0) pixeldistance else 1

    val rdelta87 = (rdistance87 / divisor) * 2
    val gdelta87 = (gdistance87 / divisor) * 2
    val bdelta87 = (bdistance87 / divisor) * 2

    var r88 = startColor.r shl 8
    var g88 = startColor.g shl 8
    var b88 = startColor.b shl 8

    for (i in startPos until endPos) {
        leds.setPixelColourRGB(i, r88 shr 8, g88 shr 8, b88 shr 8)
        r88 += rdelta87
        g88 += gdelta87
        b88 += bdelta87

    }
}

fun fillGradientRGB(leds: WS281x, numLEDs: Int, c1: ColorContainer, c2: ColorContainer) {
    fillGradientRGB(leds, 0, c1, numLEDs, c2)
    leds.render()
}

fun fillGradientRGB(leds: WS281x, numLEDs: Int, c1: ColorContainer, c2: ColorContainer, c3: ColorContainer) {
    val half = (numLEDs / 2)
    fillGradientRGB(leds, 0, c1, half, c2)
    fillGradientRGB(leds, half, c2, numLEDs, c3)
    leds.render()
}

fun fillGradientRGB(
    leds: WS281x,
    numLEDs: Int,
    c1: ColorContainer,
    c2: ColorContainer,
    c3: ColorContainer,
    c4: ColorContainer
) {
    val onethird = (numLEDs / 3)
    val twothirds = ((numLEDs * 2) / 3)
    fillGradientRGB(leds, 0, c1, onethird, c2)
    fillGradientRGB(leds, onethird, c2, twothirds, c3)
    fillGradientRGB(leds, twothirds, c3, numLEDs, c4)
    leds.render()
}
