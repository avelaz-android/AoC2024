import java.io.File

fun readInput(name: String) = File("tests", "$name.txt").readLines()