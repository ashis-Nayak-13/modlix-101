package org.example.com.modlix.ch01

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object PiGenerator {
    private val MC = MathContext(1000050, RoundingMode.HALF_EVEN) // Increased precision for 1 million digits

    fun computePi(digits: Int): String {
        require(digits > 0) { "Number of digits must be positive." }

        val C = BigDecimal("426880") * sqrt(BigDecimal("10005"))
        var K = BigDecimal.ZERO
        var M = BigDecimal.ONE
        var X = BigDecimal.ONE
        var L = BigDecimal("13591409")
        var S = L

        for (k in 1 until (digits / 14) + 1) {
            K += BigDecimal("6")
            M = (M * (K.pow(3) - (K * BigDecimal(16)))) / (BigDecimal(k).pow(3))
            X *= BigDecimal("-262537412640768000")
            L += BigDecimal("545140134")
            S += M * L / X
        }

        val pi = C / S
        return pi.toString().substring(0, digits + 2) // Include "3." in output
    }

    private fun sqrt(value: BigDecimal): BigDecimal {
        var x = BigDecimal(Math.sqrt(value.toDouble()), MC)
        var prev: BigDecimal
        do {
            prev = x
            x = value.divide(x, MC).add(x).divide(BigDecimal("2"), MC)
        } while (x != prev)
        return x
    }
}

fun main() {
    println("klklk")
    val pi1000 = PiGenerator.computePi(1000)
    println("Pi to 1000 digits:\n$pi1000")

    val pi10000 = PiGenerator.computePi(10000)
    println("Pi to 10,000 digits:\n${pi10000}...") // Print first 100 for preview

    val pi50000 = PiGenerator.computePi(50000)
    println("Pi to 50,000 digits:\n${pi50000}...") // Print first 100 for preview

    val pi1000000 = PiGenerator.computePi(1000000)
    println("Pi to 1,000,000 digits:\n${pi1000000}...") // Print first 100 for preview
}
