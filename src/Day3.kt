enum class PatternType {
    MUL, DONT, DO
}

val mulRegex = """mul\((\d+),(\d+)\)""".toRegex()
val dontRegex = """don't\(\)""".toRegex()
val doRegex = """do\(\)""".toRegex()

data class Pattern(
    val type: PatternType,
    val position: Int,
    val value: MatchResult
)

fun main() {

    fun findAllPatterns(input: String): List<Pattern> = buildList {
        mulRegex.findAll(input).forEach {
            add(Pattern(PatternType.MUL, it.range.first, it))
        }
        dontRegex.findAll(input).forEach {
            add(Pattern(PatternType.DONT, it.range.first, it))
        }
        doRegex.findAll(input).forEach {
            add(Pattern(PatternType.DO, it.range.first, it))
        }
    }.sortedBy { it.position }

    fun sumAllMultiplications(input: String): Int =
        mulRegex.findAll(input)
            .sumOf { matchResult ->
                matchResult.destructured.let { (num1, num2) ->
                    num1.toInt() * num2.toInt()
                }
            }

    fun part1(input: List<String>): Int =
        input.sumOf(::sumAllMultiplications)

    fun part2(input: List<String>): Int {
        val patterns = findAllPatterns(input.joinToString(""))
        var sum = 0
        var sumOn = true

        patterns.forEach { pattern ->
            when (pattern.type) {
                PatternType.MUL -> if (sumOn) {
                    pattern.value.destructured.let { (num1, num2) ->
                        sum += num1.toInt() * num2.toInt()
                    }
                }

                PatternType.DONT -> sumOn = false
                PatternType.DO -> sumOn = true
            }
        }
        return sum
    }

    val testInput = readInput("day3")

    val result = part1(testInput)
    println("result => $result")

    val result2 = part2(testInput)
    print("result => $result2")
}

