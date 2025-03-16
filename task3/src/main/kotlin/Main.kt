fun task3(str: String, allowed: Set<Int>): String {
    return str.filter { it.digitToIntOrNull() in allowed }
}

fun main(args: Array<String>) {
    while (true)    {
        val input = readln()
        val allowed = readln().map { it.digitToIntOrNull() }.filterNotNull().toSet()
        println(task3(input, allowed))
    }
}