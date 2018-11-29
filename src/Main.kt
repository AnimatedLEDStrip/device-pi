import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.Math.random

fun main(args: Array<String>) {

    val leds = AnimatedLEDStrip(180, 12)
//    println("Constructed")
//    while (true) {
//        leds.multiPixelRun(3, Direction.FORWARD, ColorContainer(255, 0, 0))
//        leds.sparkle(ColorContainer(0,255,255))
//        leds.alternate(ColorContainer(0,0,255), ColorContainer(255,0,0),250)
//        leds.setStripColor(0xFF00FF)
//    GlobalScope.launch {
////        println("MPR Run")
//        while (input == "") {
////            println("Loop S")
//            leds.multiPixelRun(3, Direction.FORWARD, 0, 0, 255, 0, 0, 0)
////            println("Loop E")
//        }
//    }
//    GlobalScope.launch {
////        println("SPK Run")
//        while (input == "") {
////            println("Loop S")
//            leds.sparkle(0, 0, 0)
////            println("Loop E")
//        }
//    }
//    GlobalScope.launch {
//        //        println("SPK Run")
//        while (input == "") {
////            println("Loop S")
//            leds.sparkle(0, 0, 0)
////            println("Loop E")
//        }
//    }
//    GlobalScope.launch {
//        //        println("SPK Run")
//        while (input == "") {
////            println("Loop S")
//            leds.sparkle(0, 0, 0)
////            println("Loop E")
//        }
//    }
    GlobalScope.launch {
        var n = 0
        while (n < 15) {
            println("R: $n")
            Thread.sleep((random()*1000000.0 % 1000).toInt().toLong())
            leds.wipe(80, 0, 0, Direction.BACKWARD)
            n++
        }
    }
    GlobalScope.launch {
        var n = 0
        while (n < 15) {
            println("G: $n")
            Thread.sleep((random()*1000000.0 % 1000).toInt().toLong())
            leds.wipe(0, 80, 0, Direction.FORWARD)
            n++
        }
    }

    while (readLine() != ""){}

}