import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val grid = input.map { line ->
            line.toCharArray().toTypedArray()
        }.toTypedArray()
        val solver = SearchSolver(grid)
        return solver.countOccurrences("XMAS")
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { line ->
            line.toCharArray().toTypedArray()
        }.toTypedArray()
        val solver = SearchSolver(grid)
        return solver.countOccurrences()
    }

    val testInput = readInput("day4")

    println("result => ${part1(testInput)}")
    print("result => ${part2(testInput)}")
}

class SearchSolver(private val grid: Array<Array<Char>>) {
    private val rows = grid.size
    private val cols = grid[0].size

    private val directions = arrayOf(
        0 to 1, 1 to 0, 1 to 1, -1 to 1,
        0 to -1, -1 to 0, -1 to -1, 1 to -1
    )

    fun countOccurrences(word: String): Int {
        if (word.isEmpty()) return 0

        var count = 0
        val wordChars = word.toCharArray()
        val lastIndex = wordChars.size - 1
        val firstChar = wordChars[0]

        val rowBounds = (-lastIndex).rangeTo(lastIndex)
        val colBounds = (-lastIndex).rangeTo(lastIndex)

        rowSearch@ for (row in 0 until rows) {
            colSearch@ for (col in 0 until cols) {
                if (grid[row][col] != firstChar) continue@colSearch

                dirSearch@ for ((dRow, dCol) in directions) {
                    // Quick bounds check using pre-calculated values
                    val endRow = row + dRow * lastIndex
                    val endCol = col + dCol * lastIndex
                    if (endRow !in 0 until rows || endCol !in 0 until cols ||
                        dRow !in rowBounds || dCol !in colBounds
                    ) {
                        continue@dirSearch
                    }

                    var currentRow = row
                    var currentCol = col
                    var charIndex = 1
                    while (charIndex <= lastIndex) {
                        currentRow += dRow
                        currentCol += dCol
                        if (grid[currentRow][currentCol] != wordChars[charIndex]) {
                            continue@dirSearch
                        }
                        charIndex++
                    }
                    count++
                }
            }
        }
        return count
    }

    fun countOccurrences(): Int {
        var count = 0
        val maxRow = rows - 3
        val maxCol = cols - 3

        if (maxRow < 0 || maxCol < 0) return 0

        var row = 0
        while (row <= maxRow) {
            var col = 0
            while (col <= maxCol) {
                if (grid[row + 1][col + 1] == 'A') {
                    val topLeft = grid[row][col]
                    val topRight = grid[row][col + 2]

                    if (topLeft != 'M' && topLeft != 'S') {
                        col++
                        continue
                    }

                    val bottomLeft = grid[row + 2][col]
                    val bottomRight = grid[row + 2][col + 2]

                    when {
                        topLeft == 'M' && bottomLeft == 'M' &&
                                topRight == 'S' && bottomRight == 'S' -> count++

                        topLeft == 'S' && bottomLeft == 'M' &&
                                topRight == 'S' && bottomRight == 'M' -> count++

                        topLeft == 'S' && bottomLeft == 'S' &&
                                topRight == 'M' && bottomRight == 'M' -> count++

                        topLeft == 'M' && bottomLeft == 'S' &&
                                topRight == 'M' && bottomRight == 'S' -> count++
                    }
                }
                col++
            }
            row++
        }
        return count
    }
}
