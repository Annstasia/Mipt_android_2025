package zoo.animals

data class Horse(override val id: Long, override var height: Double ) : Animal(id, height), Soundable {
    override fun voice(): String = "Iiiii"
}
