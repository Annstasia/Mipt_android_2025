package zoo.animals

data class Cat(override val id: Long, override var height: Double ) : Animal(id, height), Soundable {
    override fun voice(): String = "Meow"
}
