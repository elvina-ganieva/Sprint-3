package ru.sber.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt()

    fun attack(opponent: Fightable): Int
}

class Player : Fightable {
    override val powerType: String =  "player"
    override var healthPoints: Int = 100
    val name: String = "player name"
    var isBlessed: Boolean = false

    override fun attack(opponent: Fightable): Int {
        val modifiedDamageRoll = if (isBlessed) damageRoll * 2 else damageRoll
        opponent.healthPoints -= modifiedDamageRoll
        return modifiedDamageRoll
    }
}

abstract class Monster : Fightable {
    abstract val name: String
    abstract val description: String

    override fun attack(opponent: Fightable): Int {
        opponent.healthPoints -= damageRoll
        return damageRoll
    }
}

class Goblin : Monster() {
    override val name: String = "Goblin"
    override val description: String = "bad"
    override val powerType: String = "claws"
    override var healthPoints: Int = 100

    override val damageRoll: Int
        get() = super.damageRoll / 2
}

fun Monster.getSalutation() = "Hi! I am $name, a monster"


