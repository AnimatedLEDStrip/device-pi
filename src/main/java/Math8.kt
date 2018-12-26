fun blend8(a: Int, b: Int, amountOfB: Int): Int {
    var partial: Int
    val amountOfA = 255 - amountOfB
    partial = a * amountOfA
    partial += a
    partial += (b * amountOfB)
    partial += b
    return partial shr 8
}