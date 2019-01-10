package animatedledstrip.leds

import animatedledstrip.ccpresets.CCBlack

class LEDStripSection(val startPixel: Int, val endPixel: Int, val ledStrip: AnimatedLEDStrip) {
    /**
     * Function to run an Alternate animation.
     *
     * Strip alternates between two colors at the specified rate (delay between changes).
     *
     * @param colorValues1 First color to be displayed
     * @param colorValues2 Second color to be displayed
     * @param delay Delay in milliseconds before color changes from first to
     * second color and between second color and returning
     * @param delayMod Multiplier for delay
     */
    fun alternate(
        colorValues1: ColorContainer,
        colorValues2: ColorContainer,
        delay: Int = 1000,
        delayMod: Double = 1.0
    ) = ledStrip.alternate(colorValues1, colorValues2, delay, delayMod, startPixel, endPixel)


    /**
     * TODO (Will)
     * @param point1
     * @param point2
     * @param color1
     * @param color2
     * @param delay
     * @param delayMod
     */
    fun multiAlternate(
        point1: Int,
        point2: Int,
        color1: ColorContainer,
        color2: ColorContainer,
        delay: Int = 1000,
        delayMod: Double = 1.0
    ) {
        TODO()
    }


    /**
     * Function to run a Multi Pixel Run animation.
     *
     * Similar to Pixel Run but with multiple leds at a specified spacing.
     *
     * @param spacing Spacing between lit leds (for example, if spacing is 3
     * and led 0 is lit, led 3 will also be lit)
     * @param chaseDirection [Direction] of animation
     * @param colorValues1 Color of moving pixels
     * @param colorValues2 Color of background pixels
     * @param delay Delay between moves
     * @param delayMod Multiplier for delay
     */
    fun multiPixelRun(
        spacing: Int,
        chaseDirection: Direction,
        colorValues1: ColorContainer,
        colorValues2: ColorContainer = CCBlack,
        delay: Int = 100,
        delayMod: Double = 1.0
    ) = ledStrip.multiPixelRun(
        spacing,
        chaseDirection,
        colorValues1,
        colorValues2,
        delay,
        delayMod,
        startPixel,
        endPixel
    )


    /**
     * A non-repetitive function to run a Multi Pixel Run To Color animation.
     *
     * @param spacing Spacing between lit leds (for example, if spacing is 3
     * and led 0 is lit, led 3 will also be lit)
     * @param chaseDirection [Direction] of animation
     * @param destinationColor Color of moving pixels and color strip will
     * be at end of animation
     * @param delay Delay between moves
     * @param delayMod Multiplier for delay
     */
    fun multiPixelRunToColor(
        spacing: Int,
        chaseDirection: Direction,
        destinationColor: ColorContainer,
        delay: Int = 150,
        delayMod: Double = 1.0
    ) = ledStrip.multiPixelRunToColor(spacing, chaseDirection, destinationColor, delay, delayMod, startPixel, endPixel)


    /**
     * TODO(Katie)
     * @param pixelColor1
     * @param pixelColor2
     * @param pixelColor3
     * @param pixelColor4
     * @param pixelColor5
     * @param delay
     */
    fun pixelMarathon(
        pixelColor1: ColorContainer,
        pixelColor2: ColorContainer,
        pixelColor3: ColorContainer,
        pixelColor4: ColorContainer,
        pixelColor5: ColorContainer,
        delay: Int = 8
    ) {
        TODO()
    }


    /**
     * Function to run a Pixel Run animation.
     *
     * The strip is set to colorValues2, then a pixel 'runs' along the strip.
     * Similar to Multi Pixel Run but with only one pixel.
     *
     * @param movementDirection [Direction] of animation
     * @param colorValues1 Color of 'running' pixel
     * @param colorValues2 Background color
     * @param delay Delay between moves
     * @param delayMod Multiplier for delay
     */
    fun pixelRun(
        movementDirection: Direction,
        colorValues1: ColorContainer,
        colorValues2: ColorContainer = CCBlack,
        delay: Int = 10,
        delayMod: Double = 1.0
    ) = ledStrip.pixelRun(movementDirection, colorValues1, colorValues2, delay, delayMod, startPixel, endPixel)


    /**
     * Function to run a Pixel Run With Trail animation.
     *
     * Like a Pixel Run animation, but the 'running' pixel has a trail behind it
     * where the pixels fade from colorValues1 to colorValues2 over ~20 iterations.
     *
     * @param movementDirection [Direction] of animation
     * @param colorValues1 Color of 'running' pixel
     * @param colorValues2 Background color
     * @param delay Time between moves
     * @param delayMod Multiplier for delay
     */
    fun pixelRunWithTrail(
        movementDirection: Direction,
        colorValues1: ColorContainer,
        colorValues2: ColorContainer = CCBlack,
        delay: Int = 10,
        delayMod: Double = 1.0
    ) = ledStrip.pixelRunWithTrail(movementDirection, colorValues1, colorValues2, delay, delayMod, startPixel, endPixel)


    /**
     * Function to run a Smooth Chase animation.
     *
     * The [colorsFromPalette] function is used to create a collection of colors
     * for the strip:
     * *The palette colors are spread out along the strip at approximately equal
     * intervals. All pixels between these 'pure' pixels are a blend between the
     * colors of the two nearest pure pixels. The blend ratio is determined by the
     * location of the pixel relative to the nearest pure pixels.*
     *
     * The collection created, palette2, is a map of integers to ColorContainers
     * where each integer is a pixel index. Each pixel is set to palette2&#91;i&#93;,
     * where i is the pixel index. Then, if the direction is [Direction].FORWARD,
     * each pixel is set to palette2&#91;i + 1&#93;, then palette&#91;i + 2&#93;, etc.
     * to create the illusion that the animation is 'moving'. If the direction is
     * [Direction].BACKWARD, the same happens but with indices i, i-1, i-2, etc.
     * The index is found with (i + a) mod s, where i is the pixel index, a is the
     * offset for this iteration and s is the number of pixels in the strip.
     *
     * @param colorList A list of [ColorContainer]s to be used as pure colors when
     * creating the palette
     * @param movementDirection [Direction] of the animation
     * @param delay Time between moves
     * @param delayMod Multiplier for delay
     */
    fun smoothChase(
        colorList: List<ColorContainer>,
        movementDirection: Direction,
        delay: Int = 50,
        delayMod: Double = 1.0
    ) = ledStrip.smoothChase(colorList, movementDirection, delay, delayMod, startPixel, endPixel)


    /**
     * Function to run a Sparkle animation.
     *
     * Each LED is changed to sparkleColor for delay milliseconds before reverting
     * to its original color.
     *
     * If concurrent = true (default), a separate thread will be created for each
     * pixel. Each thread saves its pixel's original color, then waits for 0-5
     * seconds before sparkling its pixel.
     *
     * If concurrent = false, shuffleArray is shuffled and used to determine the
     * order in which the LEDs are sparkled. Unlike the concurrent version, only
     * one pixel will sparkle at any given time.
     *
     * @param sparkleColor The color the pixels will sparkle with
     * @param delay Duration of each sparkle
     * @param delayMod Multiplier for delay
     * @param concurrent Use concurrent sparkle algorithm?
     */
    fun sparkle(
        sparkleColor: ColorContainer,
        delay: Int = 50,
        delayMod: Double = 1.0,
        concurrent: Boolean = true
    ) = ledStrip.sparkle(sparkleColor, delay, delayMod, concurrent, startPixel, endPixel)


    /**
     * A non-repetitive function to run a Sparkle To Color animation.
     *
     * Very similar to the Sparkle animation, but the LEDs are not reverted to their
     * original color after the sparkle.
     *
     * If concurrent = true (default), a separate thread will be created for each
     * pixel. Each thread waits for 0-5 seconds before sparkling its pixel.
     *
     * If concurrent = false, shuffleArray is shuffled and used to determine the
     * order in which the LEDs are sparkled. Unlike the concurrent version, only
     * one pixel will sparkle at any given time.
     *
     * @param destinationColor The color the pixels will sparkle with
     * @param delay Duration of each sparkle
     * @param delayMod Multiplier for delay
     * @param concurrent Use concurrent sparkle algorithm?
     */
    fun sparkleToColor(
        destinationColor: ColorContainer,
        delay: Int = 50,
        delayMod: Double = 1.0,
        concurrent: Boolean = true
    ) = ledStrip.sparkleToColor(destinationColor, delay, delayMod, concurrent, startPixel, endPixel)


    /**
     * TODO(Katie)
     * @param stackDirection
     * @param colorValues1
     * @param delay
     * @param delayMod
     */
    fun stack(
        stackDirection: Direction,
        colorValues1: ColorContainer,
        delay: Int = 10,
        delayMod: Double = 1.0
    ) = ledStrip.stack(stackDirection, colorValues1, delay, delayMod, startPixel, endPixel)


    /**
     * TODO(Katie)
     * @param stackColor1
     * @param stackColor2
     */
    fun stackOverflow(stackColor1: ColorContainer, stackColor2: ColorContainer) {
        TODO()
    }


    /**
     * A non-repetitive function to run a Wipe animation.
     *
     * Similar to a Pixel Run animation, but the pixels do not revert to their
     * original color.
     *
     * @param colorValues Color of moving pixel and color strip will be at end
     * of animation
     * @param wipeDirection [Direction] of animation
     * @param delay Delay between moves
     * @param delayMod Multiplier for delay
     */
    fun wipe(
        colorValues: ColorContainer,
        wipeDirection: Direction,
        delay: Int = 10,
        delayMod: Double = 1.0
    ) = ledStrip.wipe(colorValues, wipeDirection, delay, delayMod, startPixel, endPixel)
}