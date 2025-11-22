package xyz.malefic.doppelganger

/**
 * Generates an infinite Fibonacci sequence starting from the `firstElement` and `secondElement`.
 * The sequence is lazily evaluated, meaning elements are computed as they are requested.
 *
 * @return A sequence of Fibonacci numbers.
 */
fun generateFibi() =
    sequence {
        var a = 1
        yield(a)
        var b = 2
        yield(b)
        while (true) {
            val c = a + b
            yield(c)
            a = b
            b = c
        }
    }
