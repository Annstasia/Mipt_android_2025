package task1

fun Array<Int>.oddCubesSum(): Int {
    return this.filter { it % 2 == 1 }.sumOf { it * it * it }
}

fun main(args: Array<String>) {
    val array = arrayOf(1, 2, 3, 4, 5, 6)
    println(array.oddCubesSum())
}