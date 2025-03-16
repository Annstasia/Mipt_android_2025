package zoo.animals

data class Fish(override val id: Long, override var height: Double ) : Animal(id, height) {
}
