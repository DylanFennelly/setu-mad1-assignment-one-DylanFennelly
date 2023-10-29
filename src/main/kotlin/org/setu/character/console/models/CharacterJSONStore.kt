package org.setu.character.console.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import mu.KotlinLogging
import org.setu.character.console.helpers.exists
import org.setu.character.console.helpers.read
import org.setu.character.console.helpers.write

import java.util.*

private val logger = KotlinLogging.logger {}

val JSON_FILE = "characters.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<CharacterModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class CharacterJSONStore : CharacterStore {

    var characters = mutableListOf<CharacterModel>()

    init {
        if (exists(JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<CharacterModel> {
        return characters
    }

    override fun findOne(id: Long) : CharacterModel? {
        var foundCharacter: CharacterModel? = characters.find { p -> p.id == id }
        return foundCharacter
    }

    fun findOneByIndex(index: Long) : CharacterModel? {
        var foundCharacter: CharacterModel? = characters[index.toInt()]
        return foundCharacter
    }

    override fun create(character: CharacterModel) {
        character.id = generateRandomId()
        characters.add(character)
        serialize()
    }

    override fun update(character: CharacterModel) {
        val foundCharacter = findOne(character.id!!)
        if (foundCharacter != null) {
            foundCharacter.name = character.name
            foundCharacter.race = character.race
            foundCharacter.battleClass = character.battleClass
            foundCharacter.level = character.level
            foundCharacter.str = character.str
            foundCharacter.dex = character.dex
            foundCharacter.con = character.con
            foundCharacter.int = character.int
            foundCharacter.wis = character.wis
            foundCharacter.cha = character.cha
            foundCharacter.maxHP = character.maxHP
            foundCharacter.ac = character.ac
            foundCharacter.background = character.background
        }
        serialize()
    }

     fun update(character: CharacterModel, index: Long) {
        val foundCharacter = findOneByIndex(index!!)
        if (foundCharacter != null) {
            foundCharacter.name = character.name
            foundCharacter.race = character.race
            foundCharacter.battleClass = character.battleClass
            foundCharacter.level = character.level
            foundCharacter.str = character.str
            foundCharacter.dex = character.dex
            foundCharacter.con = character.con
            foundCharacter.int = character.int
            foundCharacter.wis = character.wis
            foundCharacter.cha = character.cha
            foundCharacter.maxHP = character.maxHP
            foundCharacter.ac = character.ac
            foundCharacter.background = character.background
        }
        serialize()
    }

    override fun logAll() {
        characters.forEach { logger.info("${it}") }
    }

    override fun delete(character: CharacterModel) {
        characters.remove(character)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(characters, listType)
        write(JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(JSON_FILE)
        characters = Gson().fromJson(jsonString, listType)
    }

    fun deleteAll(){        //deletes all data - USE CAREFULLY
        characters = mutableListOf()
        serialize()
    }


}