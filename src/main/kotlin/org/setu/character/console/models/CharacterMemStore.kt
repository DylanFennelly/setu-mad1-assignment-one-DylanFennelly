package org.setu.character.console.models

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
private var lastId = 0L

private fun getId(): Long {
    return lastId++
}

class CharacterMemStore : CharacterStore {

    val placemarks = ArrayList<CharacterModel>()

    override fun findAll(): List<CharacterModel> {
        return placemarks
    }

    override fun findOne(id: Long) : CharacterModel? {
        var foundPlacemark: CharacterModel? = placemarks.find { p -> p.id == id }
        return foundPlacemark
    }

    override fun create(placemark: CharacterModel) {
        placemark.id = getId()
        placemarks.add(placemark)
        logAll()
    }

    override fun update(placemark: CharacterModel) {
        var foundPlacemark = findOne(placemark.id!!)
        if (foundPlacemark != null) {
            foundPlacemark.title = placemark.title
            foundPlacemark.description = placemark.description
        }
    }

    override fun logAll() {
        placemarks.forEach { logger.info("${it}") }
    }

    override fun delete(placemark: CharacterModel) {
        placemarks.remove(placemark)
    }
}