class BinaryTree<T: Comparable<T>>{
    private var root: TreeNode<T>? = null
    data class TreeNode<T>(val value: T, var left: TreeNode<T>? = null, var right: TreeNode<T>? = null)
    fun insert(value: T) {
        if (root == null) {
            root = TreeNode(value)
        } else {
            insert(root, value)
        }
    }

    fun buildTreeString(): String {
        val builder = StringBuilder()
        buildTreeString(root, builder)
        return builder.toString()
    }

    private fun buildTreeString(node: TreeNode<T>?, builder: StringBuilder) {
        node?.let {
            buildTreeString(it.left, builder)
            builder.append(it.value).append(" ")
            buildTreeString(it.right, builder)
        }
    }
    private fun insert(node: TreeNode<T>?, value: T): TreeNode<T> {
        if (node == null) {
            return TreeNode(value)
        } else if (value < node.value) {
            node.left = insert(node.left, value)
        } else {
            node.right = insert(node.right, value)
        }
        return node
    }
}
