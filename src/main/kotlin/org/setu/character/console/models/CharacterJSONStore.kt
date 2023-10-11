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

val JSON_FILE = "placemarks.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<CharacterModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class CharacterJSONStore : CharacterStore {

    var placemarks = mutableListOf<CharacterModel>()

    init {
        if (exists(JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<CharacterModel> {
        return placemarks
    }

    override fun findOne(id: Long) : CharacterModel? {
        var foundPlacemark: CharacterModel? = placemarks.find { p -> p.id == id }
        return foundPlacemark
    }

    override fun create(placemark: CharacterModel) {
        placemark.id = generateRandomId()
        placemarks.add(placemark)
        serialize()
    }

    override fun update(placemark: CharacterModel) {
        var foundPlacemark = findOne(placemark.id!!)
        if (foundPlacemark != null) {
            foundPlacemark.title = placemark.title
            foundPlacemark.description = placemark.description
        }
        serialize()
    }

    override fun logAll() {
        placemarks.forEach { logger.info("${it}") }
    }

    override fun delete(placemark: CharacterModel) {
        placemarks.remove(placemark)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(placemarks, listType)
        write(JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(JSON_FILE)
        placemarks = Gson().fromJson(jsonString, listType)
    }
}