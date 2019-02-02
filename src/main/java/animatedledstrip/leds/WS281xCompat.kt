package animatedledstrip.leds

import com.diozero.ws281xj.rpiws281x.WS281x


/**
 * Connection between the WS281x class and the LEDStripInterface interface.
 *
 * @param pin Pin the strip is connected to
 * @param brightness Brightness of the strip
 * @param numLEDs Number of LEDs in the strip
 */
class WS281xCompat(pin: Int, brightness: Int, override val numLEDs: Int): WS281x(pin, brightness, numLEDs), LEDStripInterface {

    override fun getPixelColor(pixel: Int): Int = getPixelColour(pixel)
    override fun setPixelColor(pixel: Int, color: Int) = setPixelColour(pixel, color)

}