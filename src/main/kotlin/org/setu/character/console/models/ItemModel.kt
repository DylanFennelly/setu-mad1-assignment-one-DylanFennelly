package org.setu.character.console.models

data class ItemModel(
    var name: String = "",              //name of item
    var itemType: String = "",          //item type (weapon, armour, etc.)
    var rarity: String = "",            //item rarity (Common, Uncommon, Rare, Very rare, Legendary)
    var desc: String = "",              //item description
    var cost: UInt = 0u,                //item cost, unsigned integer (item cant cost negative) - https://kotlinlang.org/docs/unsigned-integer-types.html
    var attunement: Boolean = false,    //if item requires attunement or not
    var equipped: Boolean = false       //if item is equipped

)