fun <T> MutableList<T>.swap(i: Int, j: Int) {
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
}

data class State(
    val rules: MutableMap<Int, MutableList<Int>> = mutableMapOf(),
    val safeUpdates: MutableList<List<Int>> = mutableListOf(),
    val unsafeData: MutableList<MutableList<Int>> = mutableListOf()
)

fun main() {

    fun isValidSequence(numbers: List<Int>, restrictions: Map<Int, List<Int>>): Boolean {
        val seen = HashSet<Int>(numbers.size)

        for (number in numbers) {
            val forbidden = restrictions[number] ?: continue
            if (seen.any { it in forbidden }) {
                return false
            }
            seen.add(number)
        }
        return true
    }

    fun reorderUntilValid(numbers: MutableList<Int>, restrictions: Map<Int, List<Int>>): List<Int> {
        for (i in numbers.indices) {
            var currentIndex = i

            while (true) {
                val current = numbers[currentIndex]
                val forbidden = restrictions[current] ?: break

                val conflictIndex = (0..<currentIndex).find { j ->
                    numbers[j] in forbidden
                } ?: break

                for (j in currentIndex downTo (conflictIndex + 1)) {
                    numbers.swap(j, j - 1)
                }

                currentIndex = conflictIndex
            }
        }

        return numbers
    }

    fun processInput(input: List<String>): State {
        val state = State()
        var isRules = true

        input.forEach { line ->
            when {
                line.isEmpty() -> isRules = false
                isRules -> {
                    val (key, value) = line.split("|").map(String::toInt)
                    if (state.rules.containsKey(key)) {
                        state.rules[key]!!.add(value)
                    } else {
                        state.rules[key] = mutableListOf(value)
                    }
                }

                else -> {
                    val update = line.split(",").map(String::toInt)
                    if (update.zipWithNext().all { (current, next) ->
                            !state.rules.containsKey(current) ||
                                    (next in (state.rules[current] ?: emptyList()))
                        } && isValidSequence(update, state.rules)) {
                        state.safeUpdates.add(update)
                    } else {
                        state.unsafeData.add(update.toMutableList())
                    }
                }
            }
        }
        return state
    }

    fun sumCentralElements(lists: List<List<Int>>): Int =
        lists.sumOf { it.getOrElse(it.size / 2) { 0 } }

    fun part1(input: List<String>): Int =
        sumCentralElements(processInput(input).safeUpdates)

    fun part2(state: State): Int =
        sumCentralElements(state.unsafeData.map { data ->
            reorderUntilValid(data, state.rules)
        })

    val testInput = readInput("day5")
    val state = processInput(testInput)

    println("result => ${part1(testInput)}")
    print("result => ${part2(state)}")
}
