package zoo

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import zoo.animals.*
import zoo.keepers.Person

// Тесты нв 99% сгенерированы (в тз тестов нет). Код в src/main мой
class ZooTest {
    private lateinit var zoo: Zoo
    private val cat = Cat(1, 0.3)
    private val dog = Dog(2, 0.6)
    private val fish = Fish(3, 0.1)
    private val horse = Horse(4, 1.5)
    private val hippo = Hippopotamus(5, 1.2)
    private val keeper = Person("John", 100)

    @BeforeEach
    fun setup() {
        zoo = Zoo()
    }

    @Test
    fun `should register and retrieve animals`() {
        zoo = Zoo(setOf(cat, dog))
        assertEquals(cat, zoo.findAnimal(1))
        assertEquals(dog, zoo.findAnimal(2))
    }

    @Test
    fun `should register and retrieve persons`() {
        zoo.register(keeper)
        assertEquals(keeper, zoo.personById[100])
    }

    @Test
    fun `should assign keeper to animal`() {
        zoo.assignAnimalKeeper(keeper, cat)
        assertEquals(setOf(cat), zoo.getAllAssignedToPerson(keeper.id))
    }

    @Test
    fun `should remove animal and clean up references`() {
        zoo.register(cat)
        zoo.register(dog)
        zoo.assignAnimalKeeper(keeper, cat)

        val removed = zoo.removeAnimal(1)
        assertEquals(cat, removed)
        assertNull(zoo.findAnimal(1))
        assertTrue(zoo.getAllAssignedToPerson(keeper.id).isEmpty())
    }

    @Test
    fun `should get all animals of a specific type`() {
        zoo.register(cat)
        zoo.register(dog)
        zoo.register(fish)

        assertEquals(setOf(cat), zoo.getAnimalsByType(Cat::class))
        assertEquals(setOf(dog), zoo.getAnimalsByType(Dog::class))
        assertEquals(setOf(fish), zoo.getAnimalsByType(Fish::class))
    }

    @Test
    fun `should retrieve animals with a voice`() {
        zoo.register(cat)
        zoo.register(dog)
        zoo.register(fish)
        zoo.register(horse)
        zoo.register(hippo)

        val soundableAnimals = zoo.getAllAnimalsWithVoice()
        assertEquals(setOf(cat, dog, horse, hippo), soundableAnimals)
    }

    @Test
    fun `should return all animals assigned to persons by name`() {
        val keeper2 = Person("John", 101)
        zoo.assignAnimalKeeper(keeper, cat)
        zoo.assignAnimalKeeper(keeper2, dog)

        val animals = zoo.getAllAssignedToPersons("John")
        assertEquals(setOf(cat, dog), animals)
    }

    @Test
    fun `should return animals taller than given height`() {
        zoo.register(cat)
        zoo.register(dog)
        zoo.register(fish)
        zoo.register(horse)
        zoo.register(hippo)

        val tallAnimals = zoo.getAllHigherThen(1.0)
        assertEquals(setOf(horse, hippo), tallAnimals)
    }
}
