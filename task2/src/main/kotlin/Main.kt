fun main(args: Array<String>) {
    val tree = BinaryTree<Int>()
    val array = arrayOf(1, 7, 2, 6, 3, 6, -1, 0, 3234)
    for (element in array) {
        tree.insert(element)
    }
    println(tree.buildTreeString())
}