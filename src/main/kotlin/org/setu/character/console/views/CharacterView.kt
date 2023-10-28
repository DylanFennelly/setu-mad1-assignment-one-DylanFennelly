package org.setu.character.console.views

import org.setu.character.console.models.CharacterModel
import org.setu.character.console.models.CharacterStore

class CharacterView {

    fun menu() : Int {

        var option : Int
        var input: String?

        println("MAIN MENU")
        println(" 1. Add Character")
        println(" 2. Update Character")
        println(" 3. List All Characters")
        println(" 4. Search Characters")
        println(" 5. Delete Character")
        println("-1. Exit")
        println()
        print("Enter Option : ")
        input = readln()!!
        option = if (input.toIntOrNull() != null && !input.isEmpty())
            input.toInt()
        else
            -9
        return option
    }

    fun listRaces(): Int {
        var option : Int
        var input: String?

        println("Choose a Race")
        println("===============")
        println(" 1. Dragonborn")
        println(" 2. Dwarf")
        println(" 3. Elf")
        println(" 4. Gnome")
        println(" 5. Half-Elf")
        println(" 6. Half-Orc")
        println(" 7. Halfling")
        println(" 8. Human")
        println(" 9. Tiefling")
        println()
        print("Enter Option : ")
        input = readln()!!
        option = if (input.toIntOrNull() != null && !input.isEmpty())
            input.toInt()
        else
            -9
        return option
    }

    fun listClasses(): Int {
        var option : Int
        var input: String?

        println("Choose a Class")
        println("===============")
        println(" 1. Barbarian")
        println(" 2. Bard")
        println(" 3. Cleric")
        println(" 4. Druid")
        println(" 5. Fighter")
        println(" 6. Monk")
        println(" 7. Paladin")
        println(" 8. Ranger")
        println(" 9. Rouge")
        println(" 10. Sorcerer")
        println(" 11. Warlock")
        println(" 12. Wizard")
        println()
        print("Enter Option : ")
        input = readln()!!
        option = if (input.toIntOrNull() != null && !input.isEmpty())
            input.toInt()
        else
            -9
        return option
    }

    fun listBackgrounds(): Int{
        var option : Int
        var input: String?

        println("Choose a Background")
        println("===============")
        println(" 1. Acolyte")
        println(" 2. Charlatan")
        println(" 3. Criminal")
        println(" 4. Entertainer")
        println(" 5. Folk Hero")
        println(" 6. Guild Artisan")
        println(" 7. Hermit")
        println(" 8. Noble")
        println(" 9. Outlander")
        println(" 10. Sage")
        println(" 11. Sailor")
        println(" 12. Soldier")
        println(" 13. Urchin")
        println()
        print("Enter Option : ")
        input = readln()!!
        option = if (input.toIntOrNull() != null && !input.isEmpty())
            input.toInt()
        else
            -9
        return option
    }

    fun listUpdateOptions() : Int{
        var option : Int
        var input: String?

        println("Select attribute to update")
        println("===============")
        println(" 1. Level")
        println(" 2. Name")
        println(" 3. Race")
        println(" 4. Class")
        println(" 5. Ability Scores")
        println(" 6. Background")
        println(" 7. Armour Class")
        println(" 8. Max HP Override")
        println()
        println(" -1. Return to main menu")
        println()
        print("Enter Option : ")
        input = readln()!!
        option = if (input.toIntOrNull() != null && !input.isEmpty())
            input.toInt()
        else
            -9
        return option
    }

    fun listAbilityScores() : Int{
        var option : Int
        var input: String?

        println("Select ability score")
        println("===============")
        println(" 1. Strength (STR)")
        println(" 2. Dexterity (DEX)")
        println(" 3. Constitution (CON)")
        println(" 4. Intelligence (INT)")
        println(" 5. Wisdom (WIS)")
        println(" 6. Charisma (CHA)")
        println()
        println(" -1. Return to update menu")
        println()
        print("Enter Option : ")
        input = readln()!!
        option = if (input.toIntOrNull() != null && !input.isEmpty())
            input.toInt()
        else
            -9
        return option
    }

    fun listCharacters(characters : CharacterStore) {
        println("List All Characters")
        println()
        characters.logAll()
        println()
    }

    fun showCharacter(character : CharacterModel?) {
        if(character != null)
            println("Character Details [ $character ]")
        else
            println("Character Not Found...")
    }

    //TODO: refactor to better adhere to MVC model
    fun addCharacterData(character : CharacterModel) : Boolean {

        println()
        var nameValidated = false
        do{
            print("Enter Character Name: ")
            character.name = readln()!!
            if (character.name.isNotEmpty()){
                nameValidated = true
            }else{
                println("Character name must not be empty")
            }
        } while (!nameValidated)

        do {
            val input: Int = listRaces()
            when(input) {
                1 -> character.race = "Dragonborn"
                2 -> character.race = "Dwarf"
                3 -> character.race = "Elf"
                4 -> character.race = "Gnome"
                5 -> character.race = "Half-Elf"
                6 -> character.race = "Half-Orc"
                7 -> character.race = "Halfling"
                8 -> character.race = "Human"
                9 -> character.race = "Tiefling"
                else -> println("Invalid Option")
            }
            println()
        } while (character.race.isEmpty())

        do {
            val input: Int = listClasses()
            when(input) {
                1 -> character.battleClass = "Barbarian"
                2 -> character.battleClass = "Bard"
                3 -> character.battleClass = "Cleric"
                4 -> character.battleClass = "Druid"
                5 -> character.battleClass = "Fighter"
                6 -> character.battleClass = "Monk"
                7 -> character.battleClass = "Paladin"
                8 -> character.battleClass = "Ranger"
                9 -> character.battleClass = "Rouge"
                10 -> character.battleClass = "Sorcerer"
                11 -> character.battleClass = "Warlock"
                12 -> character.battleClass = "Wizard"
                else -> println("Invalid Option")
            }
            println()
        } while (character.battleClass.isEmpty())

        print("Enter Strength Score: ")
        character.str = readln()!!.toByte()     //TODO: input validation on toByte - util class?
        print("Enter Dexterity Score: ")
        character.dex = readln()!!.toByte()
        print("Enter Constitution Score: ")
        character.con = readln()!!.toByte()
        print("Enter Intelligence Score: ")
        character.int = readln()!!.toByte()
        print("Enter Wisdom Score: ")
        character.wis = readln()!!.toByte()
        print("Enter Charisma Score: ")
        character.cha = readln()!!.toByte()

        do {
            val input: Int = listBackgrounds()
            when(input) {
                1 -> character.background = "Acolyte"
                2 -> character.background = "Charlatan"
                3 -> character.background = "Criminal"
                4 -> character.background = "Entertainer"
                5 -> character.background = "Folk Hero"
                6 -> character.background = "Guild Artisan"
                7 -> character.background = "Hermit"
                8 -> character.background = "Noble"
                9 -> character.background = "Outlander"
                10 -> character.background = "Sage"
                11 -> character.background = "Sailor"
                12 -> character.background = "Solider"
                13 -> character.background = "Urchin"
                else -> println("Invalid Option")
            }
            println()
        } while (character.background.isEmpty())


        return true //TODO: change return type (all inputs should be validated by the time method gets here)
    }

    //level up/down character
    fun updateCharacterLevel(character: CharacterModel) : Boolean {
        var tempLevel : String?
        var levelValidated = false
        println("--- Update character level (enter '-1' to return) ---")
        do{
            print("Enter new character level: ")
            tempLevel = readln()!!
            if (tempLevel.toByteOrNull() != null
                && !tempLevel.isEmpty()
                && tempLevel != "-1"
                && tempLevel.toByteOrNull()!! > 0
                && tempLevel.toByteOrNull()!! <= 20)    //character level cannot be less than 1 or greater than 20
            {
                levelValidated = true
                character.level = tempLevel.toByte()
            }else if (tempLevel == "-1"){
                return false
            }else{
                println("Invalid input: Character level must be a valid number between 1 and 20.")
            }
        } while (!levelValidated)

        return true
    }

    fun updateCharacterName(character: CharacterModel) : Boolean {
        var tempName : String?
        var nameValidated = false
        println("--- Update character name (enter '-1' to return) ---")
        do{
            print("Enter New Character Name: ")
            tempName = readln()!!
            if (tempName.isNotEmpty() && tempName != "-1") {
                nameValidated = true
                character.name = tempName
            }else if (tempName == "-1"){
                return false
            }else{
                println("Character name must not be empty.")
            }
        } while (!nameValidated)

        return true
    }


    fun updateCharacterScores(character: CharacterModel, score: String) : Boolean {
        var tempScore : String?
        var scoreValidated = false
        println("--- Update ability score (enter '-1' to return) ---")
        do{
            print("Enter new score value: ")
            tempScore = readln()!!

            if (tempScore.toByteOrNull() != null
                && !tempScore.isEmpty()
                && tempScore != "-1"
                && tempScore.toByteOrNull()!! > 0
                && tempScore.toByteOrNull()!! <= 30) //Ability score cannot be below 1 or above 30
            {
                scoreValidated = true
                when (score){
                    "str" -> character.str = tempScore.toByte()
                    "dex" -> character.dex = tempScore.toByte()
                    "con" -> character.con = tempScore.toByte()
                    "int" -> character.int = tempScore.toByte()
                    "wis" -> character.wis = tempScore.toByte()
                    "cha" -> character.cha = tempScore.toByte()
                }
            }else if (tempScore == "-1"){
                return false
            }else{
                println("Invalid input: Ability score must be a valid number between 1 and 30.")
            }
        } while (!scoreValidated)

        return true
    }

    fun updateCharacterAC(character: CharacterModel) : Boolean {
        var tempAC : String?
        var acValidated = false
        println("--- Update character armour class (enter '-1' to return) ---")
        do{
            print("Enter Armour Class value: ")
            tempAC = readln()!!

            if (tempAC.toByteOrNull() != null
                && !tempAC.isEmpty()
                && tempAC != "-1"
                && tempAC.toByteOrNull()!! > 0) //Armour class cannot be below 1
            {
                acValidated = true
                character.ac = tempAC.toByte()
            }else if (tempAC == "-1"){
                return false
            }else{
                println("Invalid input: Armour class must be a valid number between 1 and 127.")
            }
        } while (!acValidated)

        return true
    }

    fun updateCharacterHP(character: CharacterModel) : Boolean {
        var tempHP : String?
        var hpValidated = false
        println("--- Override character max HP (enter '-1' to return) ---")
        println("### WARNING: Maximum HP override value will be overwritten if character's level, class, or CON score is changed. ###")
        do{
            print("Enter new max HP value: ")
            tempHP = readln()!!

            if (tempHP.toShortOrNull() != null
                && !tempHP.isEmpty()
                && tempHP != "-1"
                && tempHP.toShortOrNull()!! > 0) //Armour class cannot be below 1
            {
                hpValidated = true
                character.maxHP = tempHP.toShort()
            }else if (tempHP == "-1"){
                return false
            }else{
                println("Invalid input: Max HP must be a valid number between 1 and 32767.")
            }
        } while (!hpValidated)

        return true
    }


    fun getId() : Long {
        var strId : String? // String to hold user input
        var searchId : Long // Long to hold converted id
        print("Enter id to Search/Update/Delete : ")
        strId = readln()!!
        searchId = if (strId.toLongOrNull() != null && !strId.isEmpty())
            strId.toLong()
        else
            -9
        return searchId
    }
}