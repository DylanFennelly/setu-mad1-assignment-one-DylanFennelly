package org.setu.character.console.helpers

import kotlin.math.floor

// Takes in character level and con score to calculate max HP
fun calculateHP(charLevel : Byte, conScore : Byte): Short{
    return 0
}

// Calculates the ability score modifier from an input score. Used for HP calculation
fun calculateMod(abilityScore: Byte) : Byte{
    var mod : Float = abilityScore.toFloat()        //mod value can end up with a decimal due to division
    mod = floor((mod - 10) / 2)                  //10 is taken away, then divided by 2, then rounded down

    return mod.toInt().toByte()                     //actual recommended way for converting float to byte...
}