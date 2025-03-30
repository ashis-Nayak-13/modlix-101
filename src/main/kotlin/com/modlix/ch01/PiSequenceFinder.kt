package org.example.com.modlix.ch01

import java.io.File

object PiSequenceFinder {
    private const val MILLION_PI_FILE = "pi_1million.txt"
    private const val BILLION_PI_FILE = "pi_1billion.txt"

    fun findInPi(sequence: String, useBillion: Boolean = false): Int {
        val fileName = if (useBillion) BILLION_PI_FILE else MILLION_PI_FILE
        val piDigits = File(fileName).readText().replace(".", "") // Remove decimal point if present

        return piDigits.indexOf(sequence)
    }
}

fun main() {
    val queries = listOf("14159", "31415926", "123456789", "999999")
    for (query in queries) {
        val indexMillion = PiSequenceFinder.findInPi(query)
        val indexBillion = PiSequenceFinder.findInPi(query, useBillion = true)
        println("Query: $query")
        println("In 1 Million digits: $indexMillion")
        println("In 1 Billion digits: $indexBillion")
        println("---------------------")
    }
}
