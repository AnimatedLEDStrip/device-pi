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

data class AnimationInfo(
    val color1: ReqLevel = ReqLevel.NOTUSED,
    val color2: ReqLevel = ReqLevel.NOTUSED,
    val color2Default: ColorContainer = CCBlack,
    val color3: ReqLevel = ReqLevel.NOTUSED,
    val color4: ReqLevel = ReqLevel.NOTUSED,
    val color5: ReqLevel = ReqLevel.NOTUSED,
    val colorList: ReqLevel = ReqLevel.NOTUSED,
    val delay: ReqLevel = ReqLevel.NOTUSED,
    val delayDefault: Int = 0,
    val direction: ReqLevel = ReqLevel.NOTUSED,
    val spacing: ReqLevel = ReqLevel.NOTUSED
)

enum class ReqLevel {
    REQUIRED,
    OPTIONAL,
    NOTUSED
}

val Alternate = AnimationInfo(
    color1 = ReqLevel.REQUIRED,
    color2 = ReqLevel.REQUIRED,
    delay = ReqLevel.OPTIONAL,
    delayDefault = 1000
)

val MultiPixelRun = AnimationInfo(
    color1 = ReqLevel.REQUIRED,
    color2 = ReqLevel.OPTIONAL,
    delay = ReqLevel.OPTIONAL,
    delayDefault = 100,
    direction = ReqLevel.REQUIRED,
    spacing = ReqLevel.REQUIRED
)

val MultiPixelRunToColor = AnimationInfo(
    color1 = ReqLevel.REQUIRED,
    delay = ReqLevel.OPTIONAL,
    delayDefault = 150,
    direction = ReqLevel.REQUIRED,
    spacing = ReqLevel.REQUIRED
)

val PixelMarathon = AnimationInfo(
    color1 = ReqLevel.REQUIRED,
    color2 = ReqLevel.REQUIRED,
    color3 = ReqLevel.REQUIRED,
    color4 = ReqLevel.REQUIRED,
    color5 = ReqLevel.REQUIRED,
    delay = ReqLevel.OPTIONAL,
    delayDefault = 8
)

val PixelRun = AnimationInfo(
    color1 = ReqLevel.REQUIRED,
    color2 = ReqLevel.OPTIONAL,
    delay = ReqLevel.OPTIONAL,
    delayDefault = 10,
    direction = ReqLevel.REQUIRED
)

val PixelRunWithTrail = AnimationInfo(
    color1 = ReqLevel.REQUIRED,
    color2 = ReqLevel.OPTIONAL,
    delay = ReqLevel.OPTIONAL,
    delayDefault = 10,
    direction = ReqLevel.REQUIRED
)

val SmoothChase = AnimationInfo(
    colorList = ReqLevel.REQUIRED,
    delay = ReqLevel.OPTIONAL,
    delayDefault = 50,
    direction = ReqLevel.REQUIRED
)

val Sparkle = AnimationInfo(
    color1 = ReqLevel.REQUIRED,
    delay = ReqLevel.OPTIONAL,
    delayDefault = 50
)

val SparkleToColor = AnimationInfo(
    color1 = ReqLevel.REQUIRED,
    delay = ReqLevel.OPTIONAL,
    delayDefault = 50
)

val Stack = AnimationInfo(
    color1 = ReqLevel.REQUIRED,
    delay = ReqLevel.OPTIONAL,
    delayDefault = 10,
    direction = ReqLevel.REQUIRED
)

val StackOverflow = AnimationInfo(
    color1 = ReqLevel.REQUIRED,
    color2 = ReqLevel.REQUIRED
)

val Wipe = AnimationInfo(
    color1 = ReqLevel.REQUIRED,
    delay = ReqLevel.OPTIONAL,
    delayDefault = 10,
    direction = ReqLevel.REQUIRED
)

val animationInfoMap = mapOf(
    Animation.ALTERNATE to Alternate,
    Animation.MULTIPIXELRUN to MultiPixelRun,
    Animation.MULTIPIXELRUNTOCOLOR to MultiPixelRunToColor,
    Animation.PIXELMARATHON to PixelMarathon,
    Animation.PIXELRUN to PixelRun,
    Animation.PIXELRUNWITHTRAIL to PixelRunWithTrail,
    Animation.SMOOTHCHASE to SmoothChase,
    Animation.SPARKLE to Sparkle,
    Animation.SPARKLETOCOLOR to SparkleToColor,
    Animation.STACK to Stack,
    Animation.STACKOVERFLOW to StackOverflow,
    Animation.WIPE to Wipe
)