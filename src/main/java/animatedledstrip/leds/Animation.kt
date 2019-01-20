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


/**
 * A list of animations used when communicating between clients and servers.
 */
enum class Animation {
    /**
     * @see LEDStrip.setStripColor
     */
    @NonRepetitive
    COLOR,
    /**
     * @see LEDStrip.setStripColorWithGradient
     */
    @NonRepetitive
    MULTICOLOR,
    /**
     * @see AnimatedLEDStrip.alternate
     * @see Alternate
     */
    ALTERNATE,
    /**
     * @see AnimatedLEDStrip.bounce
     * @see Bounce
     */
    BOUNCE,
    /**
     * @see AnimatedLEDStrip.bounceToColor
     * @see BounceToColor
     */
    @NonRepetitive
    BOUNCETOCOLOR,
    /**
     * @see AnimatedLEDStrip.multiPixelRun
     * @see MultiPixelRun
     */
    MULTIPIXELRUN,
    /**
     * @see AnimatedLEDStrip.multiPixelRunToColor
     * @see MultiPixelRunToColor
     */
    @NonRepetitive
    MULTIPIXELRUNTOCOLOR,
    /**
     *
     * @see PixelMarathon
     */
    PIXELMARATHON,
    /**
     * @see AnimatedLEDStrip.pixelRun
     * @see PixelRun
     */
    PIXELRUN,
    /**
     * @see AnimatedLEDStrip.pixelRunWithTrail
     * @see PixelRunWithTrail
     */
    PIXELRUNWITHTRAIL,
    /**
     * @see AnimatedLEDStrip.smoothChase
     * @see SmoothChase
     */
    SMOOTHCHASE,
    /**
     * @see AnimatedLEDStrip.sparkle
     * @see Sparkle
     */
    SPARKLE,
    /**
     * @see AnimatedLEDStrip.sparkleFade
     * @see SparkleFade
     */
    SPARKLEFADE,
    /**
     * @see AnimatedLEDStrip.sparkleToColor
     * @see SparkleToColor
     */
    @NonRepetitive
    SPARKLETOCOLOR,
    /**
     * @see AnimatedLEDStrip.stack
     * @see Stack
     */
    @NonRepetitive
    STACK,
    /**
     *
     * @see StackOverflow
     */
    STACKOVERFLOW,
    /**
     * @see AnimatedLEDStrip.wipe
     * @see Wipe
     */
    @NonRepetitive
    WIPE,
    /**
     * Special 'animation' sent by the GUI to stop a continuous animation
     */
    ENDANIMATION
}