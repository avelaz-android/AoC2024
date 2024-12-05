import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val (left, right) = input.map { line ->
            line.split("   ").let { (a, b) -> a.toInt() to b.toInt() }
        }.unzip().let { (leftNumbers, rightNumbers) ->
            SortedList().apply { leftNumbers.forEach { insert(it) } } to
                    SortedList().apply { rightNumbers.forEach { insert(it) } }
        }

        return left.elements.zip(right.elements) { a, b -> abs(a - b) }.sum()
    }

    fun part2(input: List<String>): Int {
        val data = mutableMapOf<Int, Pair<Int, Int>>()

        input.map { it.split("   ")[0].toInt() }
            .forEach { num ->
                data.merge(num, 1 to 0) { old, _ ->
                    (old.first + 1) to old.second
                }
            }

        input.map { it.split("   ")[1].toInt() }
            .forEach { num ->
                data.merge(num, 0 to 1) { old, _ ->
                    old.first to (old.second + 1)
                }
            }

        return data.entries.sumOf { (key, value) ->
            key * value.first * value.second
        }
    }

    val testInput = readInput("day1")

    val result = part1(testInput)
    println("result => $result")

    val result2 = part2(testInput)
    print("result => $result2")
}

class SortedList {
    val elements = mutableListOf<Int>()

    fun insert(value: Int) {
        val insertIndex = findInsertPosition(value)
        elements.add(insertIndex, value)
    }

    private fun findInsertPosition(value: Int): Int {
        if (elements.isEmpty() || value > elements.last()) return elements.size

        if (value <= elements.first()) return 0

        var left = 0
        var right = elements.size - 1

        while (left <= right) {
            val mid = (left + right) / 2
            when {
                elements[mid] == value -> {
                    var pos = mid
                    while (pos < elements.size && elements[pos] == value) {
                        pos++
                    }
                    return pos
                }

                elements[mid] < value -> left = mid + 1
                else -> right = mid - 1
            }
        }

        return left
    }
}