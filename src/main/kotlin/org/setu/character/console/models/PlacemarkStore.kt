package org.setu.character.console.models

import org.setu.character.console.models.PlacemarkModel

interface PlacemarkStore {
    fun findAll(): List<PlacemarkModel>
    fun findOne(id: Long): PlacemarkModel?
    fun create(placemark: PlacemarkModel)
    fun update(placemark: PlacemarkModel)
    fun logAll()
    fun delete(placemark: PlacemarkModel)
}