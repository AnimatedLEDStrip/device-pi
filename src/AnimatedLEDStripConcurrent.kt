import com.diozero.ws281xj.PixelAnimations.delay
import kotlinx.coroutines.*
import java.lang.Math.random

class AnimatedLEDStripConcurrent(numLEDs: Int, pin: Int) : LEDStripConcurrent(numLEDs, pin) {

    private var shuffleArray = mutableListOf<Int>()

    init {
        for (i in 0 until numLEDs) shuffleArray.add(i)
    }

    fun alternate(colorValues1: ColorContainer, colorValues2: ColorContainer, delay: Int = 1000, delayMod: Double = 1) {
        setStripColor(colorValues1)
        delay(delay * delayMod)
        setStripColor(colorValues2)
        delay(delay * delayMod)
    }

    fun alternate(r1In: Int, g1In: Int, b1In: Int, r2In: Int, g2In: Int, b2In: Int, delay: Int = 1000, delayMod: Double = 1) = alternate(ColorContainer(r1In, g1In, b1In), ColorContainer(r2In, g2In, b2In), delay, delayMod)

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

    fun multiPixelRun(spacing: Int, chaseDirection: Direction, colorValues1: ColorContainer, colorValues2: ColorContainer = CCBlack, delay: Int = 100, delayMod: Double = 1) {
        if (chaseDirection == Direction.BACKWARD) {
            for (q in 0 until spacing) {
                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues1)
                show()
                delay(delay * delayMod)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues2)
            }
        } else if (chaseDirection == Direction.FORWARD) {
            for (q in spacing - 1 downTo 0) {
                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues1)
                show()
                delay(delay * delayMod)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues2)
            }
        }
    }

    fun multiPixelRun(spacing: Int, chaseDirection: Direction, r1In: Int, g1In: Int, b1In: Int, r2In: Int, g2In: Int, b2In: Int, delay: Int = 100, delayMod: Double = 1) = multiPixelRun(spacing, chaseDirection, ColorContainer(r1In, g1In, b1In), ColorContainer(r2In, g2In, b2In), delay, delayMod)

    fun multiPixelRunToColor(spacing: Int, chaseDirection: Direction, colorValues1: ColorContainer, delay: Int = 150, delayMod: Double = 1) {
        if (chaseDirection == Direction.BACKWARD) {
            for (q in 0 until spacing) {
//                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues1)
                show()
                delay(delay * delayMod)
//                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues2)
            }
        } else if (chaseDirection == Direction.FORWARD) {
            for (q in spacing - 1 downTo 0) {
//                setStripColor(colorValues2)
                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues1)
                show()
                delay(delay * delayMod)
//                for (i in 0 until ledStrip.numPixels - 1 step spacing) setPixelColor(i + (-(q - (spacing - 1))), colorValues2)
            }
        }
    }

    fun multiPixelRunToColor(spacing: Int, chaseDirection: Direction, r1In: Int, g1In: Int, b1In: Int, delay: Int = 150, delayMod: Double = 1) = multiPixelRunToColor(spacing, chaseDirection, ColorContainer(r1In, g1In, b1In), delay, delayMod)

    fun pixelRun(movementDirection: Direction, colorValues1: ColorContainer, colorValues2: ColorContainer = CCBlack, delay: Int = 50, delayMod: Double = 1) {
        setStripColor(colorValues2)
        if (movementDirection == Direction.FORWARD) {
            for (q in 0 until ledStrip.numPixels) {
                setPixelColor(q, colorValues1)
                show()
                delay(delay * delayMod)
                setPixelColor(q, colorValues2)
            }
        } else if (movementDirection == Direction.BACKWARD) {
            for (q in ledStrip.numPixels - 1 downTo 0) {
                setPixelColor(q, colorValues1)
                show()
                delay(delay * delayMod)
                setPixelColor(q, colorValues2)
            }
        }
    }

    fun pixelRun(movementDirection: Direction, r1In: Int, g1In: Int, b1In: Int, r2In: Int, g2In: Int, b2In: Int, delay: Int = 50, delayMod: Double = 1) = pixelRun(movementDirection, ColorContainer(r1In, g1In, b1In), ColorContainer(r2In, g2In, b2In), delay, delayMod)

    fun pixelRunWithTrail(movementDirection: Direction, colorValues1: ColorContainer, colorValues2: ColorContainer = CCBlack, delay: Int = 50, delayMod: Double = 1) {
        if (movementDirection == Direction.FORWARD) {
            for (q in 0 until ledStrip.numPixels) {
                for (i in 0 until ledStrip.numPixels - 1) {
                    setPixelColor(i, blend(getPixelColor(i), colorValues2, 60))
                }
                setPixelColor(q, colorValues1)
                delay(delay * delayMod)
            }
        } else if (movementDirection == Direction.BACKWARD) {
            for (q in ledStrip.numPixels - 1 downTo 0) {
                for (i in 0 until ledStrip.numPixels - 1) {
                    setPixelColor(i, blend(getPixelColor(i), colorValues2, 60))
                }
                setPixelColor(q, colorValues1)
                delay(delay * delayMod)
            }
        }
    }

    fun pixelRunWithTrail(movementDirection: Direction, r1In: Int, g1In: Int, b1In: Int, r2In: Int, g2In: Int, b2In: Int, delay: Int = 50, delayMod: Double = 1) = pixelRunWithTrail(movementDirection, ColorContainer(r1In, g1In, b1In), ColorContainer(r2In, g2In, b2In, delay, delayMod))

    fun smoothChase(palette: RGBPalette16, movementDirection: Direction, brightness: Int = 255, delay: Int = 50, delayMod: Double = 1) {
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

    fun sparkle(sparkleColor: ColorContainer, delay: Int = 50, delayMod: Double = 1) {
        var originalColor: ColorContainer
        shuffleArray.shuffle()
        for (i in 0 until ledStrip.numPixels) {
            originalColor = getPixelColor(shuffleArray[i])
            setPixelColor(shuffleArray[i], sparkleColor)
            show()
            delay(delay * delayMod)
            setPixelColor(shuffleArray[i], originalColor)
        }
    }

    fun sparkle(rIn: Int, gIn: Int, bIn: Int, delay: Int = 50, delayMod: Double = 1) = sparkle(ColorContainer(rIn, gIn, bIn), delay, delayMod)

    fun sparkleCC(sparkleColor: ColorContainer, delay: Int = 50, delayMod: Double = 1) {
        val deferred = (0 until ledStrip.numPixels).map { n ->
            GlobalScope.async {
                val originalColor: ColorContainer = getPixelColor(n)
                delay(random().toInt() % 4950)
                setPixelColor(n, sparkleColor)
                show()
                delay(delay * delayMod)
                setPixelColor(n, originalColor)
            }
        }
        runBlocking {
            deferred.awaitAll()
        }
    }

    fun sparkleCC(rIn: Int, gIn: Int, bIn: Int, delay: Int = 50, delayMod: Double = 1) = sparkleCC(ColorContainer(rIn, gIn, bIn), delay, delayMod)

    fun sparkleToColor(destinationColor: ColorContainer, delay: Int = 50, delayMod: Double = 1) {
        shuffleArray.shuffle()
        for (i in 0 until ledStrip.numPixels) {
            setPixelColor(shuffleArray[i], destinationColor)
            show()
            delay(delay * delayMod)
        }
    }

    fun sparkleToColor(rIn: Int, gIn: Int, bIn: Int, delay: Int = 50, delayMod: Double = 1) = sparkleToColor(ColorContainer(rIn, gIn, bIn), delay, delayMod)

    fun stack(stackDirection: Direction, colorValues1: ColorContainer, colorValues2: ColorContainer = CCBlack, delay: Int = 10, delayMod: Double = 1) {
        if (stackDirection == Direction.FORWARD) {
            setStripColor(colorValues2)
            for (q in ledStrip.numPixels - 1 downTo 0) {
                for (i in 0 until q) {
                    setPixelColor(i, colorValues1)
                    show()
                    delay(delay * delayMod)
                    setPixelColor(i, colorValues2)
                }
                setPixelColor(q, colorValues1)
                show()
            }
        } else if (stackDirection == Direction.BACKWARD) {
            setStripColor(colorValues2)
            for (q in 0 until ledStrip.numPixels) {
                for (i in q - 1 downTo 0) {
                    setPixelColor(i, colorValues1)
                    show()
                    delay(delay * delayMod)
                    setPixelColor(i, colorValues2)
                }
                setPixelColor(q, colorValues1)
                show()
            }
        }
    }

    fun stackOverflow(stackColor1: ColorContainer, stackColor2: ColorContainer) {
        GlobalScope.launch {
            stack(Direction.FORWARD, stackColor1)
        }
        GlobalScope.launch {
            stack(Direction.BACKWARD, stackColor2)
        }
    }

    private fun wipe(colorValues: ColorContainer, wipeDirection: Direction, delay: Int = 10, delayMod: Double = 1) {
        if (wipeDirection == Direction.BACKWARD) {
            for (i in ledStrip.numPixels - 1 downTo 0) {
                setPixelColor(i, colorValues)
                show()
                delay(delay * delayMod)
            }
        } else if (wipeDirection == Direction.FORWARD) {
            for (i in 0 until ledStrip.numPixels) {
                setPixelColor(i, colorValues)
                show()
                delay(delay * delayMod)
            }
        }
    }

    fun wipe(rIn: Int, gIn: Int, bIn: Int, wipeDirection: Direction, delay: Int = 10, delayMod: Double = 1) = wipe(ColorContainer(rIn, gIn, bIn), wipeDirection, delay, delayMod)


}