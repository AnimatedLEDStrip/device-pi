package animatedledstrip.leds

/**
 * Class for running a non-concurrent LED strip from a Raspberry Pi.
 *
 * @param numLEDs Number of LEDs in the strip
 * @param pin GPIO pin connected for signal
 */
class AnimatedLEDStripKotlinPiNonConcurrent(numLEDs: Int,
                                            pin: Int): AnimatedLEDStripNonConcurrent(numLEDs) {

    override var ledStrip: LEDStripInterface = WS281xCompat(pin, 255, numLEDs)
}