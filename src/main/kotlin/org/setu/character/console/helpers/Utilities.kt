package org.setu.character.console.helpers

import kotlin.math.floor

// Takes in character level, class, and con score to calculate max HP
fun calculateHP(charLevel : Byte, charClass : String, conScore : Byte): Short{
    var maxHP : Int = 0

    //Base HP (level 1): Class HP + con mod
    when(charClass){
        "Barbarian" -> maxHP += (12 + calculateMod(conScore))
        "Bard" -> maxHP += (8 + calculateMod(conScore))
        "Cleric" -> maxHP += (8 + calculateMod(conScore))
        "Druid"  ->maxHP += (8 + calculateMod(conScore))
        "Fighter"  ->maxHP += (10 + calculateMod(conScore))
        "Monk"  -> maxHP += (8 + calculateMod(conScore))
        "Paladin"  -> maxHP += (10 + calculateMod(conScore))
        "Ranger"  -> maxHP += (10 + calculateMod(conScore))
        "Rouge"  -> maxHP += (8 + calculateMod(conScore))
        "Sorcerer"  -> maxHP += (6 + calculateMod(conScore))
        "Warlock"  -> maxHP += (8 + calculateMod(conScore))
        "Wizard"  -> maxHP += (6 + calculateMod(conScore))
        else -> maxHP = 0   //for if during character creation con score is changed before class is selected
    }

    //HP above level 1: (Class HP + con mod) x levels above 1
    if (charLevel >= 2){
        var hpPerLevel: Int = 0
        when(charClass){
            "Barbarian" -> hpPerLevel += (7 + calculateMod(conScore))
            "Bard" -> hpPerLevel += (5 + calculateMod(conScore))
            "Cleric" -> hpPerLevel += (5 + calculateMod(conScore))
            "Druid"  ->hpPerLevel += (5 + calculateMod(conScore))
            "Fighter"  ->hpPerLevel += (6 + calculateMod(conScore))
            "Monk"  -> hpPerLevel += (5 + calculateMod(conScore))
            "Paladin"  -> hpPerLevel += (6 + calculateMod(conScore))
            "Ranger"  -> hpPerLevel += (6 + calculateMod(conScore))
            "Rouge"  -> hpPerLevel += (5 + calculateMod(conScore))
            "Sorcerer"  -> hpPerLevel += (4 + calculateMod(conScore))
            "Warlock"  -> hpPerLevel += (5 + calculateMod(conScore))
            "Wizard"  -> hpPerLevel += (4 + calculateMod(conScore))
            else -> hpPerLevel = 1
        }

        if (hpPerLevel < 1){    //if character would gain no HP/lose hp on level up, guarantee 1 HP gain
            hpPerLevel = 1
        }
        maxHP += hpPerLevel*(charLevel - 1) //multiply by levels above 1 (i.e. level 5 character has base HP + hpPerLevel * 4)
    }

    return maxHP.toShort();
}

// Calculates the ability score modifier from an input score. Used for HP calculation
fun calculateMod(abilityScore: Byte) : Byte{
    var mod : Float = abilityScore.toFloat()        //mod value can end up with a decimal due to division
    mod = floor((mod - 10) / 2)                  //10 is taken away, then divided by 2, then rounded down

    return mod.toInt().toByte()                     //actual recommended way for converting float to byte...
}

// Validates if input from string is valid to be transformed into Byte, with a lower and upper range (inclusive)
fun validateByteToNum(score: String, lower: Byte, upper: Byte): Boolean{
    return score.isNotEmpty() &&                //string is not empty
            score.toByteOrNull() != null &&     //string can be converted to byte
            score.toByteOrNull()!! >= lower &&  //byte is greater than/equal to lower
            score.toByteOrNull()!! <= upper     //byte is less than/equal to upper
}

// Version of above method without upper and lower bounds
fun validateByteToNum(score: String): Boolean{
    return score.isNotEmpty() &&                //string is not empty
            score.toByteOrNull() != null        //string can be converted to byte
}


fun validateShortToNum(score: String, lower: Short, upper: Short): Boolean{
    return score.isNotEmpty() &&                //string is not empty
            score.toShortOrNull() != null &&     //string can be converted to byte
            score.toShortOrNull()!! >= lower &&  //byte is greater than/equal to lower
            score.toShortOrNull()!! <= upper     //byte is less than/equal to upper
}

fun validateShortToNum(score: String): Boolean{
    return score.isNotEmpty() &&                //string is not empty
            score.toShortOrNull() != null    //string can be converted to byte
}
