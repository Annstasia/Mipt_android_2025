package zoo.animals

data class Dog(override val id: Long, override var height: Double) : Animal(id, height), Soundable {
    override fun voice() = "Woof"
}
