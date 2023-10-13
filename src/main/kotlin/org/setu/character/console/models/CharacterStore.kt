package org.setu.character.console.models

interface CharacterStore {
    fun findAll(): List<CharacterModel>
    fun findOne(id: Long): CharacterModel?
    fun create(character: CharacterModel)
    fun update(character: CharacterModel)
    fun logAll()
    fun delete(character: CharacterModel)
}