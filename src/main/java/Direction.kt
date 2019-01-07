package animatedledstrip.leds

/**
 * Helper enum that determines if an animation should appear to move 'Forward'
 * or 'Backward'.
 *
 * The start of the LED strip is the end where the signal is connected (small
 * arrows on strip pointing away from it).
 * The end of the LED strip is the opposite end from the start (small arrows on
 * strip point towards it).
 */
enum class Direction {
    /**
     * Animation appears to move from the start to the end of the strip.
     */
    FORWARD,
    /**
     * Animation appears to move from the end to the start of the strip.
     */
    BACKWARD
}