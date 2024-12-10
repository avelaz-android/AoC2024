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

    private fun removeObstacle(pos: Position) = obstacles.remove(pos)

    fun setGuard(pos: Position, dir: Char) {
        guard = Guard(pos, dir)
    }

    private fun isOutOfMap(pos: Position): Boolean =
        pos.row !in 0..<rows || pos.col !in 0..<cols

    private fun pathUntilExit(): Set<Position> {
        val pathPositions = HashSet<Position>()
        val testGuard = Guard(guard.startPos, guard.startDir)
        pathPositions.add(testGuard.pos)

        while (true) {
            val nextPos = moves[testGuard.direction]!!(testGuard.pos)

            if (isOutOfMap(nextPos) && !obstacles.contains(nextPos)) break

            if (isOutOfMap(nextPos) || obstacles.contains(nextPos)) {
                testGuard.direction = turns[testGuard.direction]!!
            } else {
                testGuard.pos = nextPos
                pathPositions.add(nextPos)
            }
        }
        return pathPositions
    }

    private fun willGuardBeFree(): Boolean {
        val statesSeen = mutableSetOf<Pair<Position, Char>>()
        val testGuard = Guard(guard.startPos, guard.startDir)

        while (true) {
            val state = testGuard.pos to testGuard.direction
            if (!statesSeen.add(state)) return false

            val nextPos = moves[testGuard.direction]!!(testGuard.pos)

            if (isOutOfMap(nextPos) && !obstacles.contains(nextPos)) return true

            if (isOutOfMap(nextPos) || obstacles.contains(nextPos)) {
                testGuard.direction = turns[testGuard.direction]!!
            } else {
                testGuard.pos = nextPos
            }
        }
    }

    fun findLoopPositions(): List<Position> {
        val loopPositions = mutableListOf<Position>()
        val guardPath = pathUntilExit()

        for (pos in guardPath) {
            if (pos == guard.startPos) continue

            addObstacle(pos)
            if (!willGuardBeFree()) loopPositions.add(pos)
            removeObstacle(pos)
        }

        return loopPositions
    }

    fun getVisitedPositionsCount(): Int {
        visited.clear()
        pathUntilExit().let { visited.addAll(it) }
        return visited.size
    }
}


fun main() {

    fun parseInput(input: List<String>): LabMap {
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

        return labMap
    }

    fun part1(input: List<String>): Int {
        val labMap = parseInput(input)
        return labMap.getVisitedPositionsCount()
    }

    fun part2(input: List<String>): Int {
        val labMap = parseInput(input)
        return labMap.findLoopPositions().size
    }

    val testInput = readInput("day6")

    println("result => ${part1(testInput)}")
    print("result => ${part2(testInput)}")
}