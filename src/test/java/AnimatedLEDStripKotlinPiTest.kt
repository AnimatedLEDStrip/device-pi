import animatedledstrip.leds.AnimatedLEDStripKotlinPi
import org.junit.Test
import kotlin.test.assertFailsWith

class AnimatedLEDStripKotlinPiTest {

    @Test
    fun testAnimatedLEDStripEvenThoughItWillFail() {
        assertFailsWith(UnsatisfiedLinkError::class) {
            AnimatedLEDStripKotlinPi(50, 10)
        }
    }

}