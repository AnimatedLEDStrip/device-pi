package animatedledstrip.leds

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