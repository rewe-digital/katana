package org.rewedigital.katana.comparison

import org.nield.kotlinstatistics.median

const val ITERATIONS = 1_000_000

data class Timings(val setup: Long,
                   val execution: Long)

data class Results(val setupAverage: Double,
                   val setupMedian: Double,
                   val executionAverage: Double,
                   val executionMedian: Double)

val katanaTimings = mutableListOf<Timings>()
val koinTimings = mutableListOf<Timings>()
val kodeinTimings = mutableListOf<Timings>()

fun main(args: Array<String>) {
    println("Running $ITERATIONS iterations. Please stand by...")

    repeat(ITERATIONS) {
        val katana = KatanaSubject()
        katanaTimings.add(measure(katana))

        val koin = KoinSubject()
        koinTimings.add(measure(koin))

        val kodein = KodeinSubject()
        kodeinTimings.add(measure(kodein))
    }

    println()

    println("=== Katana ===")
    katanaTimings.results().print()
    println()

    println("=== Koin ===")
    koinTimings.results().print()
    println()

    println("=== Kodein ===")
    kodeinTimings.results().print()
    println()
}

fun measure(subject: Subject): Timings {
    val setup = measureCall { subject.setup() }
    val execution = measureCall { subject.execute() }
    subject.shutdown()
    return Timings(setup, execution)
}

fun measureCall(body: () -> Unit): Long {
    val before = System.nanoTime()
    body()
    val after = System.nanoTime()
    return after - before
}

fun Iterable<Timings>.results() =
    Results(setupAverage = map { it.setup }.average(),
            setupMedian = map { it.setup }.median(),
            executionAverage = map { it.execution }.average(),
            executionMedian = map { it.execution }.median())

fun Results.print() {
    println("setup (average):     $setupAverage ns")
    println("setup (median):      $setupMedian ns")
    println("execution (average): $executionAverage ns")
    println("execution (median):  $executionMedian ns")
}

fun Double.toMillis() = this / 1_000_000
