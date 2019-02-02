package animatedledstrip.leds

import com.diozero.ws281xj.rpiws281x.WS281x

class WS281xCompat(pin: Int, brightness: Int, numPixels: Int): WS281x(pin, brightness, numPixels), LEDStripInterface {
    override val numLEDs: Int = numPixels

    override fun getPixelColor(pixel: Int): Int = getPixelColour(pixel)
    override fun setPixelColor(pixel: Int, color: Int) = setPixelColour(pixel, color)

}