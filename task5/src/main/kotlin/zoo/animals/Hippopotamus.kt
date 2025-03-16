package zoo.animals

data class Hippopotamus(override val id: Long, override var height: Double) : Animal(id, height), Soundable {
    override fun voice(): String = "Waa... I have no idea how to write his sound"
}