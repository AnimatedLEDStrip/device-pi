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

fun blend(p1: ColorContainer, p2: ColorContainer, amountOfP2: Int) = nblend(p1, p2, amountOfP2)

@Deprecated("Use colorsFromPalette(List<ColorContainer>, Int) instead")
fun colorsFromPalette(
    leds: WS281x,
    numLEDs: Int,
    startpos: Int,
    startcolor: ColorContainer,
    endpos: Int,
    endcolor: ColorContainer,
    offset: Int
) {
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
        leds.setPixelColourRGB((i + offset) % numLEDs, r88 shr 8, g88 shr 8, b88 shr 8)
        r88 += rdelta87
        g88 += gdelta87
        b88 += bdelta87

    }

}

@Deprecated("Use colorsFromPalette(List<ColorContainer>, Int) instead")
fun colorFromPalette(pal: RGBPalette16, index: Int, brightness: Int, blendType: TBlendType): ColorContainer {
    val hi4 = index shr 4
    val lo4 = index and 0x0F


    var currentIndex = index

    var red1 = pal[(((currentIndex + 16) % 256) / 16)].r
    var green1 = pal[(((currentIndex + 16) % 256) / 16)].g
    var blue1 = pal[(((currentIndex + 16) % 256) / 16)].b


    if (lo4 != 0 && blendType != TBlendType.NOBLEND) {

//        if (hi4 == 15) {
//            currentIndex = 0
//        } else {
//            currentIndex++
//        }
        val f2 = (index % 256)
        val f1 = (255 - f2)
        var red2 = pal[((((currentIndex) % 256) / 16)) % 16].r
        red1 = scale8(red1, f1)
        red2 = scale8(red2, f2)
        red1 += red2
        var green2 = pal[((((currentIndex) % 256) / 16)) % 16].g
        green1 = scale8(green1, f1)
        green2 = scale8(green2, f2)
        green1 += green2
        var blue2 = pal[((((currentIndex) % 256) / 16) + 1) % 16].b
        blue1 = scale8(blue1, f1)
        blue2 = scale8(blue2, f2)
        blue1 += blue2

//        if ((doPrint++)%100 == 0) println("f1:$f1, f2:$f2, red1:$red1, red2:$red2, green1:$green1, green2:$green2, blue1:$blue1, blue2:$blue2" )

    }
    if (brightness != 255) {
        var brightness2 = brightness
        if (brightness2 != 0) {
            brightness2++

            if (red1 != 0) {
                red1 = scale8(red1, brightness2)
            }

            if (green1 != 0) {
                green1 = scale8(green1, brightness2)
            }

            if (blue1 != 0) {
                blue1 = scale8(blue1, brightness2)
            }
        } else {
            red1 = 0
            green1 = 0
            blue1 = 0
        }
    }
    return ColorContainer(red1, green1, blue1)
}

fun colorsFromPalette(palette: List<ColorContainer>, numLEDs: Int): Map<Int, ColorContainer> {

    val returnMap = mutableMapOf<Int, ColorContainer>()

    val spacing = numLEDs.toDouble() / palette.size.toDouble()
    println(spacing)
    val pureColors = mutableListOf<Int>()

    for (i in 0 until palette.size) {
        pureColors.add((spacing * i).roundToInt())
    }

    for (i in 0 until numLEDs) {
        for (j in pureColors) {
            if ((i - j) < spacing) {
                if ((i - j) == 0) returnMap[i] = palette[pureColors.indexOf(j)]
                else {
                    returnMap[i] = nblend(
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

@Deprecated("Use a list of ColorContainers")
class RGBPalette16(
    c00: ColorContainer, c01: ColorContainer, c02: ColorContainer, c03: ColorContainer,
    c04: ColorContainer, c05: ColorContainer, c06: ColorContainer, c07: ColorContainer,
    c08: ColorContainer, c09: ColorContainer, c10: ColorContainer, c11: ColorContainer,
    c12: ColorContainer, c13: ColorContainer, c14: ColorContainer, c15: ColorContainer
) {

    constructor(
        c00: Long, c01: Long, c02: Long, c03: Long,
        c04: Long, c05: Long, c06: Long, c07: Long,
        c08: Long, c09: Long, c10: Long, c11: Long,
        c12: Long, c13: Long, c14: Long, c15: Long
    ) : this(
        ColorContainer(c00), ColorContainer(c01), ColorContainer(c02), ColorContainer(c03),
        ColorContainer(c04), ColorContainer(c05), ColorContainer(c06), ColorContainer(c07),
        ColorContainer(c08), ColorContainer(c09), ColorContainer(c10), ColorContainer(c11),
        ColorContainer(c12), ColorContainer(c13), ColorContainer(c14), ColorContainer(c15)
    )

    private var entries = arrayOf(c00, c01, c02, c03, c04, c05, c06, c07, c08, c09, c10, c11, c12, c13, c14, c15)

    operator fun get(i: Int) = entries[i]
    operator fun set(i: Int, b: ColorContainer) {
        entries[i] = b
    }

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
