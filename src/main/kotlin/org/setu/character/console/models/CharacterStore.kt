package org.setu.character.console.models

interface CharacterStore {
    fun findAll(): List<CharacterModel>
    fun findOne(id: Long): CharacterModel?
    fun create(placemark: CharacterModel)
    fun update(placemark: CharacterModel)
    fun logAll()
    fun delete(placemark: CharacterModel)
}