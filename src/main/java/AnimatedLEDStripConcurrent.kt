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


import com.diozero.ws281xj.PixelAnimations.delay
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.Math.random


open class AnimatedLEDStripConcurrent(numLEDs: Int, pin: Int, emulated: Boolean = false) :
    LEDStripConcurrent(numLEDs, pin, emulated) {

    private var shuffleArray = mutableListOf<Int>()
    private val locks = mutableMapOf<Int, Mutex>()

    private val shuffleLock = Mutex()

    init {
        for (i in 0 until numLEDs) locks += Pair(i, Mutex())
        runBlocking {
            shuffleLock.withLock {
                for (i in 0 until numLEDs) shuffleArray.add(i)
            }
        }
    }

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

    fun alternate(
        r1In: Int,
        g1In: Int,
        b1In: Int,
        r2In: Int,
        g2In: Int,
        b2In: Int,
        delay: Int = 1000,
        delayMod: Double = 1.0
    ) = alternate(ColorContainer(r1In, g1In, b1In), ColorContainer(r2In, g2In, b2In), delay, delayMod)

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

    fun multiAlternate(
        point1: Int,
        point2: Int,
        r1In: Int,
        g1In: Int,
        b1In: Int,
        r2In: Int,
        g2In: Int,
        b2In: Int,
        delay: Int = 1000,
        delayMod: Double = 1.0
    ) = multiAlternate(
        point1,
        point2,
        ColorContainer(r1In, g1In, b1In),
        ColorContainer(r2In, g2In, b2In),
        delay,
        delayMod
    )

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

    fun multiPixelRun(
        spacing: Int,
        chaseDirection: Direction,
        r1In: Int,
        g1In: Int,
        b1In: Int,
        r2In: Int,
        g2In: Int,
        b2In: Int,
        delay: Int = 100,
        delayMod: Double = 1.0
    ) = multiPixelRun(
        spacing,
        chaseDirection,
        ColorContainer(r1In, g1In, b1In),
        ColorContainer(r2In, g2In, b2In),
        delay,
        delayMod
    )

    fun multiPixelRunToColor(
        spacing: Int,
        chaseDirection: Direction,
        colorValues1: ColorContainer,
        delay: Int = 150,
        delayMod: Double = 1.0
    ) {
        if (chaseDirection == Direction.BACKWARD) {
            for (q in 0 until spacing) {
//                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    colorValues1
                )
                show()
                delay((delay * delayMod).toInt())
//                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues2)
            }
        } else if (chaseDirection == Direction.FORWARD) {
            for (q in spacing - 1 downTo 0) {
//                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(
                    i + (-(q - (spacing - 1))),
                    colorValues1
                )
                show()
                delay((delay * delayMod).toInt())
//                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues2)
            }
        }
    }

    fun multiPixelRunToColor(
        spacing: Int,
        chaseDirection: Direction,
        r1In: Int,
        g1In: Int,
        b1In: Int,
        delay: Int = 150,
        delayMod: Double = 1.0
    ) = multiPixelRunToColor(spacing, chaseDirection, ColorContainer(r1In, g1In, b1In), delay, delayMod)

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

    fun pixelRun(
        movementDirection: Direction,
        r1In: Int,
        g1In: Int,
        b1In: Int,
        r2In: Int,
        g2In: Int,
        b2In: Int,
        delay: Int = 50,
        delayMod: Double = 1.0
    ) = pixelRun(movementDirection, ColorContainer(r1In, g1In, b1In), ColorContainer(r2In, g2In, b2In), delay, delayMod)

    fun pixelRunWithTrail(
        movementDirection: Direction,
        colorValues1: ColorContainer,
        colorValues2: ColorContainer = CCBlack,
        delay: Int = 50,
        delayMod: Double = 1.0
    ) {
        if (movementDirection == Direction.FORWARD) {
            for (q in 0 until ledStrip.numPixels) {
                for (i in 0 until ledStrip.numPixels) {
                    setPixelColor(i, blend(getPixelColor(i), colorValues2, 60))
                }
                setPixelColor(q, colorValues1)
                delay((delay * delayMod).toInt())
            }
        } else if (movementDirection == Direction.BACKWARD) {
            for (q in ledStrip.numPixels - 1 downTo 0) {
                for (i in 0 until ledStrip.numPixels) {
                    setPixelColor(i, blend(getPixelColor(i), colorValues2, 60))
                }
                setPixelColor(q, colorValues1)
                delay((delay * delayMod).toInt())
            }
        }
    }

    fun pixelRunWithTrail(
        movementDirection: Direction,
        r1In: Int,
        g1In: Int,
        b1In: Int,
        r2In: Int,
        g2In: Int,
        b2In: Int,
        delay: Int = 50,
        delayMod: Double = 1.0
    ) = pixelRunWithTrail(
        movementDirection,
        ColorContainer(r1In, g1In, b1In),
        ColorContainer(r2In, g2In, b2In),
        delay,
        delayMod
    )

    fun pixelMarathon(
        pixelColor1: ColorContainer,
        pixelColor2: ColorContainer,
        pixelColor3: ColorContainer,
        pixelColor4: ColorContainer,
        pixelColor5: ColorContainer,
        delay: Int = 8
    ) {
        // TODO: Modify thread names to be different
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
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-1")) {
            pixelRun(Direction.FORWARD, pixelColor1, delay = delay)
        }
        delay(500)
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-2")) {
            pixelRun(Direction.FORWARD, pixelColor2, delay = delay)
        }
        delay(100)
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-1")) {
            pixelRun(Direction.BACKWARD, pixelColor5, delay = delay)
        }
        delay(200)
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-2")) {
            pixelRun(Direction.BACKWARD, pixelColor3, delay = delay)
        }
        delay(300)
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-1")) {
            pixelRun(Direction.FORWARD, pixelColor4, delay = delay)
        }
        delay(5000)
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-2")) {
            pixelRun(Direction.BACKWARD, pixelColor1, delay = delay / 4)
        }
    }

    fun smoothChase(
        palette: List<ColorContainer>,
        movementDirection: Direction,
        brightness: Int = 255,
        delay: Int = 50,
        delayMod: Double = 1.0
    ) {
        val palette2 = colorsFromPalette(palette, numLEDs)

        if (movementDirection == Direction.FORWARD)
            for (m in 0 until numLEDs) {
                palette2.forEach { i, j ->
                    setPixelColor((i + m) % numLEDs, j)
                }
                show()
                delay((delay * delayMod).toInt())
            }
        else
            for (m in numLEDs - 1 downTo 0) {
                palette2.forEach { i, j ->
                    setPixelColor((i + m) % numLEDs, j)
                }
                show()
                delay((delay * delayMod).toInt())
            }
    }

    fun sparkle(sparkleColor: ColorContainer, delay: Int = 50, delayMod: Double = 1.0, concurrent: Boolean = true) {

        if (concurrent) {
            val deferred = (0 until ledStrip.numPixels).map { n ->
                GlobalScope.async(newSingleThreadContext(random().toString())) {
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

            var originalColor: ColorContainer
            val myShuffleArray = runBlocking {
                shuffleLock.withLock { shuffleArray.shuffle() }
                return@runBlocking shuffleArray
            }
            myShuffleArray.shuffle()
            for (i in 0 until ledStrip.numPixels) {
                originalColor = getPixelColor(myShuffleArray[i])
                setPixelColor(myShuffleArray[i], sparkleColor)
                show()
                delay((delay * delayMod).toInt())
                setPixelColor(myShuffleArray[i], originalColor)
            }
        }
    }

    fun sparkle(rIn: Int, gIn: Int, bIn: Int, delay: Int = 50, delayMod: Double = 1.0, concurrent: Boolean = true) =
        sparkle(ColorContainer(rIn, gIn, bIn), delay, delayMod, concurrent)

    fun sparkleToColor(
        destinationColor: ColorContainer,
        delay: Int = 50,
        delayMod: Double = 1.0,
        concurrent: Boolean = true
    ) {

        if (concurrent) {
            val deferred = (0 until ledStrip.numPixels).map { n ->
                GlobalScope.async(newSingleThreadContext(random().toString())) {
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

    fun sparkleToColor(
        rIn: Int,
        gIn: Int,
        bIn: Int,
        delay: Int = 50,
        delayMod: Double = 1.0,
        concurrent: Boolean = true
    ) =
        sparkleToColor(ColorContainer(rIn, gIn, bIn), delay, delayMod, concurrent)

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

    fun stackOverflow(stackColor1: ColorContainer, stackColor2: ColorContainer) {
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-1")) {
            stack(Direction.FORWARD, stackColor1, delay = 2)
        }
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-2")) {
            stack(Direction.BACKWARD, stackColor2, delay = 2)
        }
    }

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

    fun wipe(rIn: Int, gIn: Int, bIn: Int, wipeDirection: Direction, delay: Int = 10, delayMod: Double = 1.0) =
        wipe(ColorContainer(rIn, gIn, bIn), wipeDirection, delay, delayMod)

}
