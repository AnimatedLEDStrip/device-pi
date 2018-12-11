import com.diozero.ws281xj.PixelAnimations.delay
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.io.PrintWriter
import java.lang.Math.random
import java.lang.StringBuilder

class AnimatedLEDStripConcurrent(numLEDs: Int, pin: Int) : LEDStripConcurrent(numLEDs, pin) {

    private var shuffleArray = mutableListOf<Int>()

    private val shuffleLock = Mutex()

    init {
        runBlocking {
            shuffleLock.withLock {
                for (i in 0 until numLEDs) shuffleArray.add(i)
            }
        }
    }

    fun alternate(colorValues1: ColorContainer, colorValues2: ColorContainer, delay: Int = 1000, delayMod: Double = 1.0) {
        setStripColor(colorValues1)
        delay(delay * delayMod.toInt())
        setStripColor(colorValues2)
        delay(delay * delayMod.toInt())
    }

    fun alternate(r1In: Int, g1In: Int, b1In: Int, r2In: Int, g2In: Int, b2In: Int, delay: Int = 1000, delayMod: Double = 1.0) = alternate(ColorContainer(r1In, g1In, b1In), ColorContainer(r2In, g2In, b2In), delay, delayMod)

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

    fun multiPixelRun(spacing: Int, chaseDirection: Direction, colorValues1: ColorContainer, colorValues2: ColorContainer = CCBlack, delay: Int = 100, delayMod: Double = 1.0) {
        if (chaseDirection == Direction.BACKWARD) {
            for (q in 0 until spacing) {
                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues1)
                show()
                delay(delay * delayMod.toInt())
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues2)
            }
        } else if (chaseDirection == Direction.FORWARD) {
            for (q in spacing - 1 downTo 0) {
                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues1)
                show()
                delay(delay * delayMod.toInt())
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues2)
            }
        }
    }

    fun multiPixelRun(spacing: Int, chaseDirection: Direction, r1In: Int, g1In: Int, b1In: Int, r2In: Int, g2In: Int, b2In: Int, delay: Int = 100, delayMod: Double = 1.0) = multiPixelRun(spacing, chaseDirection, ColorContainer(r1In, g1In, b1In), ColorContainer(r2In, g2In, b2In), delay, delayMod)

    fun multiPixelRunToColor(spacing: Int, chaseDirection: Direction, colorValues1: ColorContainer, delay: Int = 150, delayMod: Double = 1.0) {
        if (chaseDirection == Direction.BACKWARD) {
            for (q in 0 until spacing) {
//                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues1)
                show()
                delay(delay * delayMod.toInt())
//                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues2)
            }
        } else if (chaseDirection == Direction.FORWARD) {
            for (q in spacing - 1 downTo 0) {
//                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues1)
                show()
                delay(delay * delayMod.toInt())
//                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues2)
            }
        }
    }

    fun multiPixelRunToColor(spacing: Int, chaseDirection: Direction, r1In: Int, g1In: Int, b1In: Int, delay: Int = 150, delayMod: Double = 1.0) = multiPixelRunToColor(spacing, chaseDirection, ColorContainer(r1In, g1In, b1In), delay, delayMod)

    fun pixelRun(movementDirection: Direction, colorValues1: ColorContainer, colorValues2: ColorContainer = CCBlack, delay: Int = 50, delayMod: Double = 1.0) {
        setStripColor(colorValues2)
        if (movementDirection == Direction.FORWARD) {
            for (q in 0 until ledStrip.numPixels) {
                setPixelColor(q, colorValues1)
                show()
                delay(delay * delayMod.toInt())
                setPixelColor(q, colorValues2)
            }
        } else if (movementDirection == Direction.BACKWARD) {
            for (q in ledStrip.numPixels - 1 downTo 0) {
                setPixelColor(q, colorValues1)
                show()
                delay(delay * delayMod.toInt())
                setPixelColor(q, colorValues2)
            }
        }
    }

    fun pixelRun(movementDirection: Direction, r1In: Int, g1In: Int, b1In: Int, r2In: Int, g2In: Int, b2In: Int, delay: Int = 50, delayMod: Double = 1.0) = pixelRun(movementDirection, ColorContainer(r1In, g1In, b1In), ColorContainer(r2In, g2In, b2In), delay, delayMod)

    fun pixelRunWithTrail(movementDirection: Direction, colorValues1: ColorContainer, colorValues2: ColorContainer = CCBlack, delay: Int = 50, delayMod: Double = 1.0) {
        if (movementDirection == Direction.FORWARD) {
            for (q in 0 until ledStrip.numPixels) {
                for (i in 0 until ledStrip.numPixels - 1) {
                    setPixelColor(i, blend(getPixelColor(i), colorValues2, 60))
                }
                setPixelColor(q, colorValues1)
                delay(delay * delayMod.toInt())
            }
        } else if (movementDirection == Direction.BACKWARD) {
            for (q in ledStrip.numPixels - 1 downTo 0) {
                for (i in 0 until ledStrip.numPixels - 1) {
                    setPixelColor(i, blend(getPixelColor(i), colorValues2, 60))
                }
                setPixelColor(q, colorValues1)
                delay(delay * delayMod.toInt())
            }
        }
    }

    fun pixelRunWithTrail(movementDirection: Direction, r1In: Int, g1In: Int, b1In: Int, r2In: Int, g2In: Int, b2In: Int, delay: Int = 50, delayMod: Double = 1.0) = pixelRunWithTrail(movementDirection, ColorContainer(r1In, g1In, b1In), ColorContainer(r2In, g2In, b2In), delay, delayMod)

    fun smoothChase(palette: RGBPalette16, movementDirection: Direction, brightness: Int = 255, delay: Int = 50, delayMod: Double = 1.0) {
        for (i in 0 until numLEDs) {
            colorListFromPalette(palette, i)
        }
//        if (movementDirection == Direction.FORWARD) {
//            for (startIndex in 255 downTo 1) {
//                setStripFromPalette(palette, startIndex, TBlendType.LINEARBLEND, brightness)
//                println(ledStrip.getPixelColour(0).toString(16))
//                delay(delay * delayMod)
        show()
//            }
//        } else if (movementDirection == Direction.BACKWARD) {
//            for (startIndex in 0 until 256) {
//                setStripFromPalette(palette, startIndex, TBlendType.LINEARBLEND, brightness)
//                delay(delay * delayMod)
        show()
//            }
//        }

    }

    fun sparkle(sparkleColor: ColorContainer, delay: Int = 50, delayMod: Double = 1.0) {
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
            delay(delay * delayMod.toInt())
            setPixelColor(myShuffleArray[i], originalColor)
        }
    }

    fun sparkle(rIn: Int, gIn: Int, bIn: Int, delay: Int = 50, delayMod: Double = 1.0) = sparkle(ColorContainer(rIn, gIn, bIn), delay, delayMod)

    fun sparkleCC(sparkleColor: ColorContainer, delay: Int = 50, delayMod: Double = 1.0) {
        val deferred = (0 until ledStrip.numPixels).map { n ->
            GlobalScope.async {
                val originalColor: ColorContainer = getPixelColor(n)
                delay(random().toInt() % 4950)
                setPixelColor(n, sparkleColor)
                show()
                delay(delay * delayMod.toInt())
                setPixelColor(n, originalColor)
            }
        }
        runBlocking {
            deferred.awaitAll()
        }
    }

    fun sparkleCC(rIn: Int, gIn: Int, bIn: Int, delay: Int = 50, delayMod: Double = 1.0) = sparkleCC(ColorContainer(rIn, gIn, bIn), delay, delayMod)

    fun sparkleToColor(destinationColor: ColorContainer, delay: Int = 50, delayMod: Double = 1.0) {
        val myShuffleArray = runBlocking {
            shuffleLock.withLock {
                shuffleArray.shuffle()
                return@runBlocking shuffleArray
            }

        }
        myShuffleArray.shuffle()
        println(System.identityHashCode(myShuffleArray))
        File("Log_${Thread.currentThread().name}-start").writeText(myShuffleArray.toString())
        val stringBuilder = StringBuilder()
        stringBuilder.append("[")
        for (i in 0 until ledStrip.numPixels) {
            stringBuilder.append("${myShuffleArray[i]}")
            if (i < ledStrip.numPixels - 1) stringBuilder.append(", ")
            setPixelColor(myShuffleArray[i], destinationColor)
            show()
            delay(delay * delayMod.toInt())
        }
        stringBuilder.append("]")
        File("Log_${Thread.currentThread().name}-end").writeText(stringBuilder.toString())
    }

    fun sparkleToColor(rIn: Int, gIn: Int, bIn: Int, delay: Int = 50, delayMod: Double = 1.0) = sparkleToColor(ColorContainer(rIn, gIn, bIn), delay, delayMod)

    fun stack(stackDirection: Direction, colorValues1: ColorContainer, colorValues2: ColorContainer = CCBlack, delay: Int = 10, delayMod: Double = 1.0) {
        if (stackDirection == Direction.FORWARD) {
//            setStripColor(colorValues2)
            for (q in ledStrip.numPixels - 1 downTo 0) {
                var originalColor: ColorContainer
                for (i in 0 until q) {
//                    runBlocking {
//                        locks[i]!!.tryWithLock {
                            originalColor = getPixelColor(i)
                            setPixelColor(i, colorValues1)
                            show()
                            delay(delay * delayMod.toInt())
                            setPixelColor(i, originalColor)
//                    setPixelColor(i, colorValues2)
//                        }
//                    }
                }
                setPixelColor(q, colorValues1)
                show()
            }
        } else if (stackDirection == Direction.BACKWARD) {
//            setStripColor(colorValues2)
            for (q in 0 until ledStrip.numPixels) {
                var originalColor: ColorContainer
                for (i in numLEDs - 1 downTo q) {
//                    runBlocking {
//                        locks[i]!!.tryWithLock {
                            originalColor = getPixelColor(i)
                            setPixelColor(i, colorValues1)
                            show()
                            delay(delay * delayMod.toInt())
                            setPixelColor(i, originalColor)
//                    setPixelColor(i, colorValues2)
//                        }
//                    }
                }
                setPixelColor(q, colorValues1)
                show()
            }
        }
    }

    fun stackOverflow(stackColor1: ColorContainer, stackColor2: ColorContainer) {
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-1")) {
            stack(Direction.FORWARD, stackColor1, CCRed, delay = 2)
        }
        GlobalScope.launch(newSingleThreadContext("Thread ${Thread.currentThread().name}-2")) {
            stack(Direction.BACKWARD, stackColor2, CCYellow, delay = 2)
        }
    }

    fun wipe(colorValues: ColorContainer, wipeDirection: Direction, delay: Int = 10, delayMod: Double = 1.0) {
        if (wipeDirection == Direction.BACKWARD) {
            for (i in ledStrip.numPixels - 1 downTo 0) {
                setPixelColor(i, colorValues)
                show()
                delay(delay * delayMod.toInt())
            }
        } else if (wipeDirection == Direction.FORWARD) {
            for (i in 0 until ledStrip.numPixels) {
                setPixelColor(i, colorValues)
                show()
                delay(delay * delayMod.toInt())
            }
        }
    }

    fun wipe(rIn: Int, gIn: Int, bIn: Int, wipeDirection: Direction, delay: Int = 10, delayMod: Double = 1.0) = wipe(ColorContainer(rIn, gIn, bIn), wipeDirection, delay, delayMod)


}