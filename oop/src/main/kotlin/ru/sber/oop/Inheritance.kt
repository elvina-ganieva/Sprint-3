package ru.sber.oop

open class Room(val name: String, val size: Int) {

    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    val goblin: Monster = Goblin()

    fun description() = "Room: $name"

    open fun load() = goblin.getSalutation()
}

class TownSquare(name: String = "Town Square", size: Int = 1000) : Room(name, size) {

    override val dangerLevel: Int
        get() = super.dangerLevel - 3

    final override fun load(): String {
        return super.load() + " from Town Square"
    }
}
