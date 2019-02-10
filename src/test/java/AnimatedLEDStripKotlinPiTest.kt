import animatedledstrip.leds.AnimatedLEDStripKotlinPi
import animatedledstrip.leds.AnimatedLEDStripKotlinPiNonConcurrent
import org.junit.Test
import kotlin.test.assertFailsWith

class AnimatedLEDStripKotlinPiTest {

    @Test
    fun testAnimatedLEDStripEvenThoughItWillFail() {
        assertFailsWith(UnsatisfiedLinkError::class) {
            AnimatedLEDStripKotlinPi(50, 10)
        }
        assertFailsWith(UnsatisfiedLinkError::class) {
            AnimatedLEDStripKotlinPiNonConcurrent(50, 10)
        }
    }

}