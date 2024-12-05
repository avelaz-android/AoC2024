import kotlin.math.abs

fun main() {

    fun isSafe(numbers: List<Int>): Boolean {
        return numbers.zipWithNext().let { pairs ->
            if (pairs.any { (a, b) -> abs(a - b) > 3 }) {
                false
            } else {
                val firstDiff = pairs.first().let { (a, b) -> a - b }
                when {
                    firstDiff == 0 -> false
                    firstDiff > 0 -> pairs.all { (a, b) -> a - b > 0 }
                    else -> pairs.all { (a, b) -> a - b < 0 }
                }
            }
        }
    }

    fun canBeMadeSafe(numbers: List<Int>): Boolean {
        return numbers.indices.any { indexToRemove ->
            val numbersWithoutOne = numbers.filterIndexed { index, _ -> index != indexToRemove }
            isSafe(numbersWithoutOne)
        }
    }

    fun part1(input: List<String>): Int = input.count { line ->
        val numbers = line.split(" ").map { it.toInt() }
        isSafe(numbers)
    }

    fun part2(input: List<String>): Int = input.count { line ->
        val numbers = line.split(" ").map { it.toInt() }
        isSafe(numbers) || canBeMadeSafe(numbers)
    }

    val testInput = readInput("day2")

    val result = part1(testInput)
    println("result => $result")

    val result2 = part2(testInput)
    print("result => $result2")
}
