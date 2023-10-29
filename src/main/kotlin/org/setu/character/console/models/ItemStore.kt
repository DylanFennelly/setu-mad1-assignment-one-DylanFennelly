package org.setu.character.console.models

interface ItemStore {
    fun listAllItems(character: CharacterModel): List<ItemModel>
    fun findOneItem(character: CharacterModel, index: Int): ItemModel?
    fun addItemToCharacter(character: CharacterModel, item: ItemModel)
    fun updateItem(character: CharacterModel, item: ItemModel, index: Int)
    fun deleteItem(character: CharacterModel, item: ItemModel)
}