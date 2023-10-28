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

    fun showRaces(): Int {
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

    fun showClasses(): Int {
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

    fun showBackgrounds(): Int{
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
            val input: Int = showRaces()
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
            val input: Int = showClasses()
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
            val input: Int = showBackgrounds()
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

    fun updateCharacterData(character : CharacterModel) : Boolean {

//        var tempTitle: String?
//        var tempDescription: String?
//
//        if (character != null) {
//            print("Enter a new Title for [ " + character.title + " ] : ")
//            tempTitle = readln()!!
//            print("Enter a new Description for [ " + character.description + " ] : ")
//            tempDescription = readln()!!
//
//            if (!tempTitle.isNullOrEmpty() && !tempDescription.isNullOrEmpty()) {
//                character.title = tempTitle
//                character.description = tempDescription
//                return true
//            }
//        }
        return false
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