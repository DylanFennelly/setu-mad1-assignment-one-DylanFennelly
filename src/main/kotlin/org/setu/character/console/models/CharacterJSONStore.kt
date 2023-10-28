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

    override fun create(character: CharacterModel) {
        character.id = generateRandomId()
        characters.add(character)
        serialize()
    }

    override fun update(character: CharacterModel) {
        //TODO: Fix
//        var foundCharacter = findOne(character.id!!)
//        if (foundCharacter != null) {
//            foundCharacter.title = character.title
//            foundCharacter.description = character.description
//        }
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
}