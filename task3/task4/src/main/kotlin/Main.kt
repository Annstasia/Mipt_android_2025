fun main(args: Array<String>) {
    val stack = Stack<Int>()
    stack.push(1);
    stack.push(3)
    stack.push(2)
    assert(stack.max() == 3)
    assert(stack.pop() == 2)
    assert(stack.max() == 3)
    assert(stack.pop() == 3)
    assert(stack.max() == 1)
    assert(stack.pop() == 1)
    assert(stack.max() == null)
    assert(stack.pop() == null)
}