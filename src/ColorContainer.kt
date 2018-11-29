open class ColorContainer(var r: Int, var g: Int, var b: Int) {
    constructor(hexIn: Long): this((hexIn and 0xFF0000 shr 16).toInt(), (hexIn and 0x00FF00 shr 8).toInt(), (hexIn and 0x0000FF).toInt())

    fun setRGB(rIn: Int, gIn: Int, bIn: Int) {
        r = rIn
        g = gIn
        b = bIn
    }

    fun setRGBFromHex(hexIn: Long) {setRGB((hexIn and 0xFF0000 shr 16).toInt(), (hexIn and 0x00FF00 shr 8).toInt(), (hexIn and 0x0000FF).toInt())}

    fun getColorHex(): Long {
        var temp: Long = (r shr 16).toLong()
        temp = temp or (g shr 8).toLong()
        return (temp or b.toLong())
    }

}
