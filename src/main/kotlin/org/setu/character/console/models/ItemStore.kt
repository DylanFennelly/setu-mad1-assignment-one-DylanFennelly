package org.setu.character.console.models

interface ItemStore {
    fun listAllItems(character: CharacterModel): List<ItemModel>
    fun findOneItem(id: Long): ItemModel?
    fun addItemToCharacter(character: CharacterModel, itemModel: ItemModel)
    fun updateItem(character: CharacterModel, itemModel: ItemModel)
    fun deleteItem(character: CharacterModel, itemModel: ItemModel)
}