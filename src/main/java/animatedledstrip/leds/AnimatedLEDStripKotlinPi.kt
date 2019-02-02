package animatedledstrip.leds

class AnimatedLEDStripKotlinPi(numLEDs: Int,
                               pin: Int,
                               emulated: Boolean = false,
                               imageDebugging: Boolean = false): AnimatedLEDStrip(numLEDs, pin, emulated, imageDebugging) {

    override var ledStrip: LEDStripInterface = WS281xCompat(pin, 255, numLEDs)
}