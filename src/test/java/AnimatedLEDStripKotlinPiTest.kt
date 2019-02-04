import animatedledstrip.leds.AnimatedLEDStripKotlinPi
import org.junit.Test

class AnimatedLEDStripKotlinPiTest {

    @Test
    fun testConstruction() {
        val testLEDs = AnimatedLEDStripKotlinPi(50, 10, false)
    }

}