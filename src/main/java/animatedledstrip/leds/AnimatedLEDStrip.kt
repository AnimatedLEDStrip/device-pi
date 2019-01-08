package animatedledstrip.leds

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


import animatedledstrip.ccpresets.*
import com.diozero.ws281xj.PixelAnimations.delay
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.Math.random

/**
 * A subclass of [LEDStrip] adding animations
 *
 * @param numLEDs Number of leds in the strip
 * @param pin GPIO pin connected for signal
 * @param emulated Is this strip real or emulated?
 */
open class AnimatedLEDStrip(numLEDs: Int, pin: Int, emulated: Boolean = false) :
    LEDStrip(numLEDs, pin, emulated) {

    /**
     * Array used for shuffle animation
     */
    private var shuffleArray = mutableListOf<Int>()

    /**
     * Map containing Mutex instances for locking access to each led while it is
     * being used
     */
    private val locks = mutableMapOf<Int, Mutex>()

    /**
     * Mutex that tracks if a thread is using the shuffleArray
     */
    private val shuffleLock = Mutex()


    /**
     * A pool of threads to be used for animations that spawn new sub-threads
     * (with the exception of sparkle-type animations, those use the
     * [sparkleThreadPool]).
     */
    private val animationThreadPool = newFixedThreadPoolContext(50, "Animation Pool")

    /**
     * A pool of threads to be used for sparkle animations due to the
     * number of threads a concurrent sparkle animation uses. This prevents
     * memory leaks caused by the overhead associated with creating new threads.
     */
    private val sparkleThreadPool = newFixedThreadPoolContext(numLEDs + 1, "Sparkle Pool")

    init {
        for (i in 0 until numLEDs) locks += Pair(i, Mutex())        // Initialize locks map
        runBlocking {
            shuffleLock.withLock {
                for (i in 0 until numLEDs) shuffleArray.add(i)      // Initialize shuffleArray
            }
        }
    }


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
    ) {
        setStripColor(colorValues1)
        delay((delay * delayMod).toInt())
        setStripColor(colorValues2)
        delay((delay * delayMod).toInt())
    }


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
        val endptA = 0
        val endptB: Int
        val endptC: Int
        val endptD = numLEDs - 1

        if (point1 <= point2 && point1 > endptA && point2 < endptD) {
            endptB = point1
            endptC = point2
        } else if (point2 > endptA && point1 < endptD) {
            endptB = point2
            endptC = point1
        } else {
            endptB = numLEDs / 3
            endptC = (numLEDs * 2 / 3) - 1
        }

        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-1")) {
            setSectionColor(endptA, endptB, color1)
            delay((delay * delayMod).toInt())
            setSectionColor(endptA, endptB, color2)
            delay((delay * delayMod).toInt())
        }
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-2")) {
            setSectionColor(endptC, endptD, color1)
            delay((delay * delayMod).toInt())
            setSectionColor(endptC, endptD, color2)
            delay((delay * delayMod).toInt())
        }
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-3")) {
            setSectionColor(endptB, endptC, color2)
            delay((delay * delayMod).toInt())
            setSectionColor(endptB, endptC, color1)
            delay((delay * delayMod).toInt())
        }
    }


    fun fadePixelRed(pixel: Int, startIntensity: Int, endIntensity: Int, revertAtCompletion: Boolean) {
        val fadeDirection: Int = if (startIntensity > endIntensity) -1 else if (startIntensity < endIntensity) 1 else 0

        val originalValues = getPixelColor(pixel)

        when (fadeDirection) {
            1 -> for (i in startIntensity..endIntensity) {
                setPixelRed(pixel, i)
                show()
            }
            -1 -> for (i in startIntensity downTo endIntensity) {
                setPixelRed(pixel, i)
                show()
            }
            0 -> return
        }

        if (revertAtCompletion) {
            setPixelColor(pixel, originalValues)
            show()
        }


    }

    fun fadePixelGreen(pixel: Int, startIntensity: Int, endIntensity: Int, revertAtCompletion: Boolean) {
        val fadeDirection: Int = if (startIntensity > endIntensity) -1 else if (startIntensity < endIntensity) 1 else 0

        val originalValues = getPixelColor(pixel)

        when (fadeDirection) {
            1 -> for (i in startIntensity..endIntensity) {
                setPixelGreen(pixel, i)
                show()
            }
            -1 -> for (i in startIntensity downTo endIntensity) {
                setPixelGreen(pixel, i)
                show()
            }
            0 -> return
        }

        if (revertAtCompletion) {
            setPixelColor(pixel, originalValues)
            show()
        }

    }

    fun fadePixelBlue(pixel: Int, startIntensity: Int, endIntensity: Int, revertAtCompletion: Boolean) {
        val fadeDirection: Int = if (startIntensity > endIntensity) -1 else if (startIntensity < endIntensity) 1 else 0

        val originalValues = getPixelColor(pixel)

        when (fadeDirection) {
            1 -> for (i in startIntensity..endIntensity) {
                setPixelBlue(pixel, i)
                show()
            }
            -1 -> for (i in startIntensity downTo endIntensity) {
                setPixelBlue(pixel, i)
                show()
            }
            0 -> return
        }

        if (revertAtCompletion) {
            setPixelColor(pixel, originalValues)
            show()
        }

    }

    fun fadePixelAll() {
        //Todo: fill in function
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
    ) {
        if (chaseDirection == Direction.BACKWARD) {
            for (q in 0 until spacing) {
                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    colorValues1
                )
                show()
                delay((delay * delayMod).toInt())
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    colorValues2
                )
            }
        } else if (chaseDirection == Direction.FORWARD) {
            for (q in spacing - 1 downTo 0) {
                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    colorValues1
                )
                show()
                delay((delay * delayMod).toInt())
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    colorValues2
                )
            }
        }
    }


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
    ) {
        if (chaseDirection == Direction.BACKWARD) {
            for (q in 0 until spacing) {
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    destinationColor
                )
                show()
                delay((delay * delayMod).toInt())
            }
        } else if (chaseDirection == Direction.FORWARD) {
            for (q in spacing - 1 downTo 0) {
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    destinationColor
                )
                show()
                delay((delay * delayMod).toInt())
            }
        }
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
        delay: Int = 50,
        delayMod: Double = 1.0
    ) {
        setStripColor(colorValues2)
        if (movementDirection == Direction.FORWARD) {
            for (q in 0 until ledStrip.numPixels) {
                runBlocking {
                    locks[q]!!.tryWithLock {
                        setPixelColor(q, colorValues1)
                        show()
                        delay((delay * delayMod).toInt())
                        setPixelColor(q, colorValues2)
                    }
                }
            }
        } else if (movementDirection == Direction.BACKWARD) {
            for (q in ledStrip.numPixels - 1 downTo 0) {
                runBlocking {
                    locks[q]!!.tryWithLock {
                        setPixelColor(q, colorValues1)
                        show()
                        delay((delay * delayMod).toInt())
                        setPixelColor(q, colorValues2)
                    }
                }
            }
        }
    }


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
    ) {
        if (movementDirection == Direction.FORWARD) {
            for (q in 0 until ledStrip.numPixels) {
                for (i in 0 until ledStrip.numPixels) {
                    setPixelColor(i, blend(getPixelColor(i), colorValues2, 60))
                }
                setPixelColor(q, colorValues1)
                show()
                delay((delay * delayMod).toInt())
            }
        } else if (movementDirection == Direction.BACKWARD) {
            for (q in ledStrip.numPixels - 1 downTo 0) {
                for (i in 0 until ledStrip.numPixels) {
                    setPixelColor(i, blend(getPixelColor(i), colorValues2, 60))
                }
                setPixelColor(q, colorValues1)
                show()
                delay((delay * delayMod).toInt())
            }
        }
    }


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
        // TODO: Modify thread names to be different. Maybe create a runBlocking block to launch these in rather than GlobalScope.
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-1")) {
            pixelRun(Direction.FORWARD, pixelColor5, delay = delay)
        }
        delay(100)
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-2")) {
            pixelRun(Direction.BACKWARD, pixelColor4, delay = delay)
        }
        delay(200)
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-1")) {
            pixelRun(Direction.BACKWARD, pixelColor2, delay = delay)
        }
        delay(300)
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-2")) {
            pixelRun(Direction.FORWARD, pixelColor3, delay = delay)
        }
        delay(400)
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-3")) {
            pixelRun(Direction.FORWARD, pixelColor1, delay = delay)
        }
        delay(500)
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-4")) {
            pixelRun(Direction.FORWARD, pixelColor2, delay = delay)
        }
        delay(100)
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-5")) {
            pixelRun(Direction.BACKWARD, pixelColor5, delay = delay)
        }
        delay(200)
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-6")) {
            pixelRun(Direction.BACKWARD, pixelColor3, delay = delay)
        }
        delay(300)
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-7")) {
            pixelRun(Direction.FORWARD, pixelColor4, delay = delay)
        }
        delay(200)
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-8")) {
            pixelRun(Direction.BACKWARD, pixelColor1, delay = delay)
        }
    }


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
    ) {
        val palette = colorsFromPalette(colorList, numLEDs)

        if (movementDirection == Direction.FORWARD)
            for (m in 0 until numLEDs) {
                setStripColorWithPalette(palette, m)
                show()
                delay((delay * delayMod).toInt())
            }
        else
            for (m in numLEDs - 1 downTo 0) {
                setStripColorWithPalette(palette, m)
                show()
                delay((delay * delayMod).toInt())
            }
    }


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
    ) {

        if (concurrent) {
            val deferred = (0 until ledStrip.numPixels).map { n ->
                GlobalScope.async(sparkleThreadPool) {
                    val originalColor: ColorContainer = getPixelColor(n)
                    delay((random() * 5000).toInt() % 4950)
                    setPixelColor(n, sparkleColor)
                    show()
                    delay((delay * delayMod).toInt())
                    setPixelColor(n, originalColor)
                }
            }
            runBlocking {
                deferred.awaitAll()
            }
        } else {
            val myShuffleArray = runBlocking {
                shuffleLock.withLock { shuffleArray.shuffle() }
                return@runBlocking shuffleArray
            }
            myShuffleArray.shuffle()
            var originalColor: ColorContainer
            for (i in 0 until ledStrip.numPixels) {
                originalColor = getPixelColor(myShuffleArray[i])
                setPixelColor(myShuffleArray[i], sparkleColor)
                show()
                delay((delay * delayMod).toInt())
                setPixelColor(myShuffleArray[i], originalColor)
            }

        }
    }


    fun animation005(
        sparkleColor: ColorContainer,
        delay: Int = 50,
        delayMod: Double = 1.0,
        concurrent: Boolean = true
    ) {
        if (concurrent) {
            var complete = false
            val deferred = (0 until ledStrip.numPixels).map { n ->
                GlobalScope.async(sparkleThreadPool) {
                    delay((random() * 5000).toInt())
                    setPixelColor(n, sparkleColor)
                    show()
                    delay((delay * delayMod).toInt())
                }
            }
            GlobalScope.launch(sparkleThreadPool) {
                while (!complete) {
                    for (j in 0 until ledStrip.numPixels) {
                        setPixelColor(j, blend(getPixelColor(j), CCBlack, 10))
                    }
                    delay(10)
                }
            }
            runBlocking {
                deferred.awaitAll()
                complete = true
            }
        } else {
            val myShuffleArray = runBlocking {
                shuffleLock.withLock { shuffleArray.shuffle() }
                return@runBlocking shuffleArray
            }
            myShuffleArray.shuffle()
            for (i in 0 until ledStrip.numPixels) {
                for (j in 0 until ledStrip.numPixels) {
                    setPixelColor(myShuffleArray[j], blend(getPixelColor(myShuffleArray[j]), CCBlack, 10))
                }
                setPixelColor(myShuffleArray[i], sparkleColor)
                show()
                delay((delay * delayMod).toInt())
            }
        }
    }

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
    ) {

        if (concurrent) {
            val deferred = (0 until ledStrip.numPixels).map { n ->
                GlobalScope.async(sparkleThreadPool) {
                    delay((random() * 5000).toInt() % 4950)
                    setPixelColor(n, destinationColor)
                    show()
                    delay((delay * delayMod).toInt())
                }
            }
            runBlocking {
                deferred.awaitAll()
            }
        } else {
            val myShuffleArray = runBlocking {
                shuffleLock.withLock {
                    shuffleArray.shuffle()
                    return@runBlocking shuffleArray
                }

            }
            myShuffleArray.shuffle()
            for (i in 0 until ledStrip.numPixels) {
                setPixelColor(myShuffleArray[i], destinationColor)
                show()
                delay((delay * delayMod).toInt())
            }
        }
    }


    fun multiSparkleToColor(
        point1: Int,
        point2: Int,
        color1: ColorContainer,
        color2: ColorContainer,
        delay: Int = 1000,
        delayMod: Double = 1.0
    ) {
        val endptA = 0
        val endptB: Int
        val endptC: Int
        val endptD = numLEDs - 1

        if (point1 <= point2 && point1 > endptA && point2 < endptD) {
            endptB = point1
            endptC = point2
        } else if (point2 > endptA && point1 < endptD) {
            endptB = point2
            endptC = point1
        } else {
            endptB = numLEDs / 3
            endptC = (numLEDs * 2 / 3) - 1
        }

        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-1")) {
            setSectionColor(endptA, endptB, color1)
            delay((delay * delayMod).toInt())
            setSectionColor(endptA, endptB, color2)
            delay((delay * delayMod).toInt())
        }
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-2")) {
            setSectionColor(endptC, endptD, color1)
            delay((delay * delayMod).toInt())
            setSectionColor(endptC, endptD, color2)
            delay((delay * delayMod).toInt())
        }
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-3")) {
            setSectionColor(endptB, endptC, color2)
            delay((delay * delayMod).toInt())
            setSectionColor(endptB, endptC, color1)
            delay((delay * delayMod).toInt())
        }
    }


    /**
     * TODO(Katie)
     * @param stackDirection
     * @param colorValues1
     * @param delay
     * @param delayMod
     */
    fun stack(stackDirection: Direction, colorValues1: ColorContainer, delay: Int = 10, delayMod: Double = 1.0) {
        if (stackDirection == Direction.FORWARD) {
//            setStripColor(colorValues2)
            for (q in ledStrip.numPixels - 1 downTo 0) {
                var originalColor: ColorContainer
                for (i in 0 until q) {
                    runBlocking {
                        locks[i]!!.tryWithLock {
                            originalColor = getPixelColor(i)
                            setPixelColor(i, colorValues1)
                            show()
                            delay((delay * delayMod).toInt())
                            setPixelColor(i, originalColor)
//                    setPixelColor(i, colorValues2)
                        }
                    }
                }
                setPixelColor(q, colorValues1)
                show()
            }
        } else if (stackDirection == Direction.BACKWARD) {
//            setStripColor(colorValues2)
            for (q in 0 until ledStrip.numPixels) {
                var originalColor: ColorContainer
                for (i in numLEDs - 1 downTo q) {
                    runBlocking {
                        locks[i]!!.tryWithLock {
                            originalColor = getPixelColor(i)
                            setPixelColor(i, colorValues1)
                            show()
                            delay((delay * delayMod).toInt())
                            setPixelColor(i, originalColor)
//                    setPixelColor(i, colorValues2)
                        }
                    }
                }
                setPixelColor(q, colorValues1)
                show()
            }
        }
    }

    /**
     * TODO(Katie)
     * @param stackColor1
     * @param stackColor2
     */
    fun stackOverflow(stackColor1: ColorContainer, stackColor2: ColorContainer) {
        // TODO: Maybe put these in a runBlocking block instead of GlobalScope
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-1")) {
            stack(Direction.FORWARD, stackColor1, delay = 2)
        }
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-2")) {
            stack(Direction.BACKWARD, stackColor2, delay = 2)
        }
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
    fun wipe(colorValues: ColorContainer, wipeDirection: Direction, delay: Int = 10, delayMod: Double = 1.0) {
        if (wipeDirection == Direction.BACKWARD) {
            for (i in ledStrip.numPixels - 1 downTo 0) {
                setPixelColor(i, colorValues)
                show()
                delay((delay * delayMod).toInt())
            }
        } else if (wipeDirection == Direction.FORWARD) {
            for (i in 0 until ledStrip.numPixels) {
                setPixelColor(i, colorValues)
                show()
                delay((delay * delayMod).toInt())
            }
        }
    }

}
