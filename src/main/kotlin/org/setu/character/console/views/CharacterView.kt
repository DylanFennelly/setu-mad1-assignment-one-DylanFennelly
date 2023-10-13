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
        print("Enter a Title : ")
        character.title = readln()!!
        print("Enter a Description : ")
        character.description = readln()!!

        return character.title.isNotEmpty() && character.description.isNotEmpty()
    }

    fun updateCharacterData(character : CharacterModel) : Boolean {

        var tempTitle: String?
        var tempDescription: String?

        if (character != null) {
            print("Enter a new Title for [ " + character.title + " ] : ")
            tempTitle = readln()!!
            print("Enter a new Description for [ " + character.description + " ] : ")
            tempDescription = readln()!!

            if (!tempTitle.isNullOrEmpty() && !tempDescription.isNullOrEmpty()) {
                character.title = tempTitle
                character.description = tempDescription
                return true
            }
        }
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