package animatedledstrip.leds

/**
 * Class for running an LED strip from a Raspberry Pi.
 *
 * @param numLEDs Number of LEDs in the strip
 * @param pin GPIO pin connected for signal
 * @param imageDebugging Should a csv file be created containing all renders of
 * the strip?
 */
class AnimatedLEDStripKotlinPi(numLEDs: Int,
                               pin: Int,
                               imageDebugging: Boolean = false): AnimatedLEDStrip(numLEDs, pin, imageDebugging) {

    override var ledStrip: LEDStripInterface = WS281xCompat(pin, 255, numLEDs)
}