package org.setu.character.console.models

data class CharacterModel(
    var id: Long = 0,
    var name: String = "",          //name of character
    var race: String = "",          //race
    var battleClass: String = "",   //class of character -> class is reserved word
    var level: Byte = 1,            //character level, all characters start at lvl 1 and max 20
    var str: Byte = 10,             //strength score, min 1 and max 30
    var dex: Byte = 10,             //dexterity score
    var con: Byte = 10,             //constitution score
    var int: Byte = 10,             //intelligence
    var wis: Byte = 10,             //wisdom
    var cha: Byte = 10,             //charisma
    var maxHP: Short = 0,           //max hit points, calculated from battleClass and constitution score
    var ac: Byte = 10,              //armour class
    var background: String = ""     //character background
)