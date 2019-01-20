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
import org.pmw.tinylog.Logger

/**
 * Class for processing a map received by a server to start an animation.
 *
 */
class AnimationData() {

    /* parameters */

    lateinit var animation: Animation

    var color1: ColorContainer = CCBlack
    var color2: ColorContainer = CCBlack
    var color3: ColorContainer = CCBlack
    var color4: ColorContainer = CCBlack
    var color5: ColorContainer = CCBlack

    var colorList = mutableListOf<ColorContainer>()

    var continuous = false

    var delay = 0
        get() {
            return (when (field) {
                0 -> {
                    when (animationInfoMap[animation]?.delay) {
                        ReqLevel.REQUIRED -> throw Exception("Animation delay required for $animation")
                        ReqLevel.OPTIONAL -> animationInfoMap[animation]?.delayDefault ?: 3
                        ReqLevel.NOTUSED -> 3
                        null -> 3
                    }
                }
                else -> field
            } * delayMod).toInt()
        }

    var delayMod = 1.0

    var direction = Direction.FORWARD

    var endPixel = 0

    var id = ""

    var spacing = 3 // TODO: Add default spacing option to animationInfo and check for it here

    var startPixel = 0


    /* Helper functions for setting values */

    fun animation(animation: Animation): AnimationData {
        this.animation = animation
        return this
    }

    fun color1(color: ColorContainer): AnimationData {
        this.color1 = color
        return this
    }

    fun color1(color: Long): AnimationData {
        this.color1 = ColorContainer(color)
        return this
    }

    fun color1(color: Int): AnimationData {
        this.color1 = ColorContainer(color.toLong())
        return this
    }

    fun color1(color: String): AnimationData {
        this.color1 = ColorContainer(parseHex(color))
        return this
    }

    fun color(color: ColorContainer): AnimationData {
        this.color1 = color
        return this
    }

    fun color(color: Long): AnimationData {
        this.color1 = ColorContainer(color)
        return this
    }

    fun color(color: Int): AnimationData {
        this.color1 = ColorContainer(color.toLong())
        return this
    }

    fun color(color: String): AnimationData {
        this.color1 = ColorContainer(parseHex(color))
        return this
    }

    fun color2(color: ColorContainer): AnimationData {
        this.color2 = color
        return this
    }

    fun color2(color: Long): AnimationData {
        this.color2 = ColorContainer(color)
        return this
    }

    fun color2(color: Int): AnimationData {
        this.color2 = ColorContainer(color.toLong())
        return this
    }

    fun color2(color: String): AnimationData {
        this.color2 = ColorContainer(parseHex(color))
        return this
    }

    fun color3(color: ColorContainer): AnimationData {
        this.color3 = color
        return this
    }

    fun color3(color: Long): AnimationData {
        this.color3 = ColorContainer(color)
        return this
    }

    fun color3(color: Int): AnimationData {
        this.color3 = ColorContainer(color.toLong())
        return this
    }

    fun color3(color: String): AnimationData {
        this.color3 = ColorContainer(parseHex(color))
        return this
    }

    fun color4(color: ColorContainer): AnimationData {
        this.color4 = color
        return this
    }

    fun color4(color: Long): AnimationData {
        this.color4 = ColorContainer(color)
        return this
    }

    fun color4(color: Int): AnimationData {
        this.color4 = ColorContainer(color.toLong())
        return this
    }

    fun color4(color: String): AnimationData {
        this.color4 = ColorContainer(parseHex(color))
        return this
    }

    fun color5(color: ColorContainer): AnimationData {
        this.color5 = color
        return this
    }

    fun color5(color: Long): AnimationData {
        this.color5 = ColorContainer(color)
        return this
    }

    fun color5(color: Int): AnimationData {
        this.color5 = ColorContainer(color.toLong())
        return this
    }

    fun color5(color: String): AnimationData {
        this.color5 = ColorContainer(parseHex(color))
        return this
    }

    fun colorList(colorList: List<ColorContainer>): AnimationData {
        this.colorList = colorList.toMutableList()
        return this
    }

    fun continuous(continuous: Boolean): AnimationData {
        this.continuous = continuous
        return this
    }

    fun delay(delay: Int): AnimationData {
        this.delay = delay
        return this
    }

    fun delayMod(delayMod: Double): AnimationData {
        this.delayMod = delayMod
        return this
    }

    fun direction(direction: Direction): AnimationData {
        this.direction = direction
        return this
    }

    fun direction(direction: Char): AnimationData {
        this.direction = when (direction) {
            'F', 'f' -> Direction.FORWARD
            'B', 'b' -> Direction.BACKWARD
            else -> throw Exception("Direction chars can be 'F' or 'B'")
        }
        return this
    }

    fun endPixel(endPixel: Int): AnimationData {
        this.endPixel = endPixel
        return this
    }

    fun id(id: String): AnimationData {
        this.id = id
        return this
    }

    fun spacing(spacing: Int): AnimationData {
        this.spacing = spacing
        return this
    }

    fun startPixel(startPixel: Int): AnimationData {
        this.startPixel = startPixel
        return this
    }


    constructor(params: Map<*, *>) : this() {
        animation = params["Animation"] as Animation? ?: throw Exception("Animation not defined")
        color1 = ColorContainer(params["Color1"] as Long? ?: 0x0)
        color2 = ColorContainer(params["Color2"] as Long? ?: 0x0)
        color3 = ColorContainer(params["Color3"] as Long? ?: 0x0)
        color4 = ColorContainer(params["Color4"] as Long? ?: 0x0)
        color5 = ColorContainer(params["Color5"] as Long? ?: 0x0)
        if (params["ColorList"] as List<*>? != null) {
            (params["ColorList"] as List<*>).forEach { c -> colorList.add(ColorContainer(c as Long)) }
        }
        continuous = params["Continuous"] as Boolean? ?: false
        delay = params["Delay"] as Int? ?: when (animationInfoMap[animation]?.delay) {
            ReqLevel.REQUIRED -> throw Exception("Animation delay required for $animation")
            else -> 0
        }
        delayMod = params["DelayMod"] as Double? ?: 1.0
        direction = when (params["Direction"] as Char?) {
            'F', 'f' -> Direction.FORWARD
            'B', 'b' -> Direction.BACKWARD
            else -> Direction.FORWARD
        }
        endPixel = params["EndPixel"] as Int? ?: 0
        id = params["ID"] as String? ?: ""
        spacing = params["Spacing"] as Int? ?: 3
        startPixel = params["StartPixel"] as Int? ?: 0
    }


    /**
     * Create a string out of the values of this instance.
     *
     */
    override fun toString() =
        "$animation: $color1, $color2, $color3, $color4, $color5, $colorList, $continuous, $delay, $direction, $id, $spacing"

    fun toMap() = mutableMapOf<String, Any?>(
        "Animation" to animation,
        "Color1" to color1.hex,
        "Color2" to color2.hex,
        "Color3" to color3.hex,
        "Color4" to color4.hex,
        "Color5" to color5.hex,
        "ColorList" to mutableListOf<Long>().apply {
            colorList.forEach {
                this.add(it.hex)
            }
        },
        "Continuous" to continuous,
        "Delay" to delay,
        "DelayMod" to delayMod,
        "Direction" to when (direction) {
            Direction.FORWARD -> 'F'
            Direction.BACKWARD -> 'B'
        },
        "EndPixel" to endPixel,
        "ID" to id,
        "Spacing" to spacing,
        "StartPixel" to startPixel
    )

}

/*init {
    try {
        params["Animation"]!!
    } catch (e: Exception) {
        Logger.warn("Animation not defined $e")
    }
}

/**
 * The animation specified by the map
 */
var animation: Animation = params["Animation"] as Animation

/**
 * The first color as specified by the map. Defaults to 0x0.
 */
var color1 = ColorContainer(params["Color1"] as Long? ?: 0x0)

/**
 * The second color as specified by the map. Defaults to animation's default
 * or 0x0 if no default.
 */
var color2 = ColorContainer(
    params["Color2"] as Long? ?: when (animationInfoMap[animation]?.color2) {
        ReqLevel.OPTIONAL -> animationInfoMap[animation]?.color2Default!!.hex
        ReqLevel.REQUIRED -> throw Exception()
        ReqLevel.NOTUSED -> 0x0
        null -> 0x0
    }
)

/**
 * The third color as specified by the map.
 */
var color3 = ColorContainer(params["Color3"] as Long? ?: 0x0)

/**
 * The fourth color as specified by the map.
 */
var color4 = ColorContainer(params["Color4"] as Long? ?: 0x0)

/**
 * The fifth color as specified by the map.
 */
var color5 = ColorContainer(params["Color5"] as Long? ?: 0x0)

/**
 * A list of colors as specified by the map.
 */
var colorList = mutableListOf<ColorContainer>()

init {
    if (params["ColorList"] as List<*>? != null) {
        val tempList = params["ColorList"] as List<*>
        tempList.forEach { c -> colorList.add(ColorContainer(c as Long)) }
    }
}

/**
 * Should the animation run continuously or not as specified by the map.
 * Defaults to false.
 */
var continuous: Boolean = params["Continuous"] as Boolean? ?: false

/**
 * Delay as specified by the map. Defaults to animation's default or 0 if no
 * default.
 */
var delay: Int = params["Delay"] as Int? ?: when (animationInfoMap[animation]?.delay) {
    ReqLevel.REQUIRED -> throw Exception()
    ReqLevel.OPTIONAL -> animationInfoMap[animation]?.delayDefault!!
    ReqLevel.NOTUSED -> 0
    null -> 0
}

/**
 * Direction as specified by the map. Defaults to Direction.Forward.
 */
var direction: Direction = if (params["Direction"] as Char? != null) {
    when (params["Direction"] as Char) {
        'F', 'f' -> Direction.FORWARD
        'B', 'b' -> Direction.BACKWARD
        else -> Direction.FORWARD
    }
} else Direction.FORWARD

/**
 * ID of the animation as specified by the map. Defaults to an empty string.
 */
var id: String = params["ID"] as String? ?: ""

/**
 * Spacing as specified by the map. Defaults to 3.
 */
var spacing: Int? = params["Spacing"] as Int? ?: 3

var startPixel: Int? = null
var endPixel: Int? = null

var delayMod = 1.0

/**
 * Create a string out of the values of this instance.
 *
 */
override fun toString() =
    "$animation: $color1, $color2, $color3, $color4, $color5, $colorList, $continuous, $delay, $direction, $id, $spacing"

/**
 * Returns the params map.
 *
 */
fun toMap() = params*/
