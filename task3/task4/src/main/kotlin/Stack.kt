class Stack<T: Comparable<T>> {
    data class Pair<T>(val value: T, val maxUnderTheValueInclusive: T)
    val stack: MutableList<Pair<T>> = ArrayList()

    fun push(value: T) {
        when (stack.size) {
            0 -> stack.add(Pair(value, value))
            else -> stack.add(Pair(value, maxOf(value, stack.last().maxUnderTheValueInclusive)))
        }
    }

    fun pop(): T? {
        return stack.removeLastOrNull()?.value
    }

    fun max(): T? {
        return stack.lastOrNull()?.maxUnderTheValueInclusive
    }
}