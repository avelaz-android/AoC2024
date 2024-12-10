data class Position(val row: Int, val col: Int)

class Guard(val startPos: Position, val startDir: Char) {
    var pos: Position = startPos
    var direction: Char = startDir
}

class LabMap(private val rows: Int, private val cols: Int) {
    private val obstacles = HashSet<Position>()
    private val visited = HashSet<Position>()
    private lateinit var guard: Guard

    private val moves = mapOf(
        '^' to { pos: Position -> Position(pos.row - 1, pos.col) },
        '>' to { pos: Position -> Position(pos.row, pos.col + 1) },
        'v' to { pos: Position -> Position(pos.row + 1, pos.col) },
        '<' to { pos: Position -> Position(pos.row, pos.col - 1) }
    )

    private val turns = mapOf('^' to '>', '>' to 'v', 'v' to '<', '<' to '^')

    fun addObstacle(pos: Position) = obstacles.add(pos)

    fun removeObstacle(pos: Position) = obstacles.remove(pos)

    fun setGuard(pos: Position, dir: Char) {
        guard = Guard(pos, dir)
    }

    private fun isOutOfMap(pos: Position): Boolean =
        pos.row !in 0..<rows || pos.col !in 0..<cols

    fun calculateGuardPath(): Int {
        visited.add(guard.pos)

        while (true) {
            val nextPos = moves[guard.direction]!!(guard.pos)

            if (isOutOfMap(nextPos) && !obstacles.contains(nextPos)) break

            if (isOutOfMap(nextPos) || obstacles.contains(nextPos)) {
                guard.direction = turns[guard.direction]!!
            } else {
                guard.pos = nextPos
                visited.add(nextPos)
            }
        }

        return visited.size
    }
}


fun main() {

    fun part1(input: List<String>): Int {
        val labMap = LabMap(input.size, input[0].length)

        input.forEachIndexed { row, line ->
            line.forEachIndexed { col, char ->
                val pos = Position(row, col)
                when (char) {
                    '#' -> labMap.addObstacle(pos)
                    '^', '>', 'v', '<' -> labMap.setGuard(pos, char)
                }
            }
        }

        return labMap.calculateGuardPath()
    }

//    fun part2(input: List<String>): Int {
//        return 0
//    }

    val testInput = readInput("day6")

    println("result => ${part1(testInput)}")
    // print("result => ${part2(testInput)}")
}