package zoo

import zoo.animals.Animal
import zoo.animals.Soundable
import zoo.keepers.Person
import java.util.TreeSet
import kotlin.reflect.KClass

class Zoo() {
    val animalsByKeeper: MutableMap<Long, MutableSet<Animal>> = HashMap()
    val animalById: MutableMap<Long, Animal> = HashMap()
    val personsByName: MutableMap<String, MutableSet<Person>> = HashMap()
    val personById: MutableMap<Long, Person> = HashMap()
    val keeperByAnimal: MutableMap<Long, Person> = HashMap()
    val animalByType: MutableMap<KClass<out Animal>, MutableSet<Animal>> = HashMap()
    val animalsSortedByHeight: TreeSet<Animal> = TreeSet { a1, a2 ->
        when {
            a1.height < a2.height -> -1
            a1.height > a2.height -> 1
            else -> a1.id.compareTo(a2.id)
        }
    }

    constructor(animals: Collection<Animal>): this(){
        animals.forEach {
            register(it)
        }
    }

    fun register(animal: Animal) {
        animalById[animal.id] = animal
        animalByType.computeIfAbsent(animal.javaClass.kotlin) { mutableSetOf() }
            .add(animal)
        animalsSortedByHeight.add(animal)
    }

    fun register(person: Person) {
        personById[person.id] = person
        personsByName.computeIfAbsent(person.name) { mutableSetOf() }
            .add(person)
    }



    fun findAnimal(id: Long): Animal? {
        return animalById[id];
    }

    fun removeAnimal(id: Long): Animal? {
        val targetAnimal = animalById.remove(id) ?: return null
        animalByType.get(targetAnimal.javaClass.kotlin)?.remove(targetAnimal)
        animalsSortedByHeight.remove(targetAnimal)
        val animalKeeper = keeperByAnimal.remove(targetAnimal.id)
        if (animalKeeper != null) {
            animalsByKeeper.get(animalKeeper.id)?.remove(targetAnimal)
        }
        return targetAnimal
    }

    fun assignAnimalKeeper(person: Person, animal: Animal) {
        if (animalById.get(animal.id) == null) {
            register(animal)
        }
        if (personById.get(person.id) == null) {
            register(person)
        }
        val oldAssignedKeeper = keeperByAnimal.get(animal.id)
        if (oldAssignedKeeper != null) {
            animalsByKeeper.get(oldAssignedKeeper.id)?.remove(animal)
        }
        keeperByAnimal[animal.id] = person
        animalsByKeeper.computeIfAbsent(person.id){ mutableSetOf() }.add(animal)
    }

    fun getAllAssignedToPerson(id: Long): Set<Animal> {
        return animalsByKeeper.getOrDefault(id, HashSet())
    }

//    Можно было бы для этого метода тоже завести отдельную переменную
//    для оптимизации, но если может быть очень много надзирателей
//    с одним именем, то кажется сомнительным пытаться использовать этот метод
    fun getAllAssignedToPersons(name: String): Set<Animal> {
        return personsByName.get(name).orEmpty().asSequence()
            .mapNotNull { animalsByKeeper.get(it.id) }
            .flatMap{ it }.toSet()
    }

    fun getAllHigherThen(height: Double): Set<Animal> {
        return animalsSortedByHeight.tailSet(Animal(-1, height))
    }

    fun getAnimalsByType(type: KClass<out Animal>): Set<Animal> {
        return animalByType[type].orEmpty()
    }

    fun getAllAnimalsWithVoice(): Set<Animal> {
        return animalByType.keys.asSequence().filter {
            Soundable::class.java.isAssignableFrom(it.java) }
            .map { getAnimalsByType(it) }.flatMap { it }.toSet()
    }
}