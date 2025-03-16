package zoo.animals

open class Animal(open val id: Long, open var height: Double) {
    override fun equals(other: Any?): Boolean {
        return other != null && other is Animal && other.id == id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}