import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.Math.random

fun main(args: Array<String>) {

    val leds = AnimatedLEDStrip(20, 12)
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

//    for (n in 1..10) {
//        GlobalScope.launch {
//            var n = 0
//            when ((random() * 10000 % 2).toInt()) {
//                0 -> while (n < 15) {
////                println("R: $n")
//                    Thread.sleep((random() * 1000000.0 % 1000).toInt().toLong())
//                    leds.wipe(80, 0, 0, Direction.BACKWARD)
//                    n++
//                }
//                1 -> while (n < 15) {
//                    //                println("G: $n")
//                    Thread.sleep((random() * 1000000.0 % 1000).toInt().toLong())
//                    leds.wipe(0, 80, 0, Direction.FORWARD)
//                    n++
//                }
//            }
//        }
//    }

//    GlobalScope.launch {
//        var n = 0
//        while (n < 15) {
//            println("R: $n")
//            Thread.sleep((random() * 1000000.0 % 1000).toInt().toLong())
//            leds.wipe(80, 0, 0, Direction.BACKWARD)
//            n++
//        }
//    }
//    runBlocking {
//        var n = 0
//        while (n < 15) {
//            println("G: $n")
//            Thread.sleep((random() * 1000000.0 % 1000).toInt().toLong())
//            leds.wipe(0, 80, 0, Direction.FORWARD)
//            n++
//        }
//    }

//    while (readLine() != ""){}

//    leds.multiPixelRun(5, Direction.FORWARD, ColorContainer(0xFFFF))
//    leds.setStripColor(0xFFFF00)
//    leds.sparkleToColor(ColorContainer(0xFFFF00))
//    Thread.sleep(10000)
//    leds.sparkleToColor(ColorContainer(0xFFFF))
//    leds.setStripColor(0x0)
//    println("test")
    leds.setPixelColor(5, 0xFF0000)

}
