package org.setu.character.console.views

import com.github.ajalt.mordant.rendering.TextAlign
import org.setu.character.console.helpers.validateByteToNum
import org.setu.character.console.helpers.validateShortToNum
import org.setu.character.console.models.CharacterModel
import org.setu.character.console.models.CharacterStore

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextColors.Companion.rgb
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal

class CharacterView {

    val t = Terminal()
    val titleStyle = (bold + underline)     //style for menu titles

    init {
        t.info.width = 150      //setting max terminal width to be wider to allow for tables to print fully
    }
    fun menu() : Int {

        var option : Int
        var input: String?

        t.println()
        t.println(titleStyle("Main Menu"))
        t.println(" ${green("1.")} Add Character")
        t.println(" ${green("2.")} Update Character")
        t.println(" ${green("3.")} List All Characters")
        t.println(" ${green("4.")} Search Characters")
        t.println(" ${green("5.")} Delete Character")
        t.println()
        t.println("${green(" -1.")} Exit Application")
        t.println()
        input = t.prompt(brightBlue("Enter Option"))!!

        option = if (input.toIntOrNull() != null && input.isNotEmpty())
            input.toInt()
        else
            -9
        return option
    }

    fun listAddOptions() : Int{     //add menu
        var option : Int
        var input: String?

        t.println(titleStyle("Select attribute"))
        t.println(" ${green("1.")} Name")
        t.println(" ${green("2.")} Race")
        t.println(" ${green("3.")} Class")
        t.println(" ${green("4.")} Level")
        t.println(" ${green("5.")} Ability Scores")
        t.println(" ${green("6.")} Background")
        t.println()
        t.println(" ${green("7.")} Create character")
        t.println(" ${green("-1.")} Cancel character creation")
        t.println()
        input = t.prompt(brightBlue("Enter Option"))!!
        option = if (input.toIntOrNull() != null && !input.isEmpty())
            input.toInt()
        else
            -9
        return option
    }

    fun listRaces(): Int {
        var option : Int
        var input: String?

        t.println(titleStyle("Choose a Race"))
        t.println(" ${green("1.")} Dragonborn       ${green("6.")} Half-Orc")
        t.println(" ${green("2.")} Dwarf            ${green("7.")} Halfling")
        t.println(" ${green("3.")} Elf              ${green("8.")} Human")
        t.println(" ${green("4.")} Gnome            ${green("9.")} Tiefling")
        t.println(" ${green("5.")} Half-Elf")
        t.println()
        input = t.prompt(brightBlue("Enter Option"))!!
        option = if (input.toIntOrNull() != null && !input.isEmpty())
            input.toInt()
        else
            -9
        return option
    }

    fun listClasses(): Int {
        var option : Int
        var input: String?

        t.println(titleStyle("Choose a Class"))
        t.println(" ${green("1.")} Barbarian        ${green("7.")} Paladin")
        t.println(" ${green("2.")} Bard             ${green("8.")} Ranger")
        t.println(" ${green("3.")} Cleric           ${green("9.")} Rouge")
        t.println(" ${green("4.")} Druid            ${green("10.")} Sorcerer")
        t.println(" ${green("5.")} Fighter          ${green("11.")} Warlock")
        t.println(" ${green("6.")} Monk             ${green("12.")} Wizard")
        t.println()
        input = t.prompt(brightBlue("Enter Option"))!!
        option = if (input.toIntOrNull() != null && !input.isEmpty())
            input.toInt()
        else
            -9
        return option
    }

    fun listBackgrounds(): Int{
        var option : Int
        var input: String?

        t.println(titleStyle("Choose a Background"))
        t.println(" ${green("1.")} Acolyte          ${green("8.")} Noble")
        t.println(" ${green("2.")} Charlatan        ${green("9.")} Outlander")
        t.println(" ${green("3.")} Criminal         ${green("10.")} Sage")
        t.println(" ${green("4.")} Entertainer      ${green("11.")} Sailor")
        t.println(" ${green("5.")} Folk Hero        ${green("12.")} Soldier")
        t.println(" ${green("6.")} Guild Artisan    ${green("13.")} Urchin")
        t.println(" ${green("7.")} Hermit")
        t.println()
        input = t.prompt(brightBlue("Enter Option"))!!
        option = if (input.toIntOrNull() != null && !input.isEmpty())
            input.toInt()
        else
            -9
        return option
    }

    fun listUpdateOptions() : Int{
        var option : Int
        var input: String?

        t.println(titleStyle("Select attribute to update"))
        t.println(" ${green("1.")} Level        ${green("5.")} Ability Scores")
        t.println(" ${green("2.")} Name         ${green("6.")} Background")
        t.println(" ${green("3.")} Race         ${green("7.")} Armour Class")
        t.println(" ${green("4.")} Class        ${green("8.")} Max HP Override")
        t.println()
        t.println(" ${green("-1.")} Return to main menu")
        t.println()
        input = t.prompt(brightBlue("Enter Option"))!!
        option = if (input.toIntOrNull() != null && !input.isEmpty())
            input.toInt()
        else
            -9
        return option
    }

    fun listAbilityScores() : Int{
        var option : Int
        var input: String?

        t.println(titleStyle("Select ability score"))
        t.println(" ${green("1.")} Strength (STR)       ${green("4.")} Intelligence (INT)")
        t.println(" ${green("2.")} Dexterity (DEX)      ${green("5.")} Wisdom (WIS)")
        t.println(" ${green("3.")} Constitution (CON)   ${green("6.")} Charisma (CHA)")
        t.println()
        t.println(" ${green("-1.")} Return to previous menu")
        t.println()
        input = t.prompt(brightBlue("Enter Option"))!!
        option = if (input.toIntOrNull() != null && !input.isEmpty())
            input.toInt()
        else
            -9
        return option
    }

    fun listCharacters(characters : CharacterStore) {
        t.println(titleStyle("List All Character"))
        t.println(table{            //creates table to display character attributes
            align = TextAlign.CENTER
            column(0){
                style = green
            }
            header {
                style(green, bold = true)
                row("ID","Name", "Race", "Class", "Level", "STR", "DEX", "CON", "INT", "WIS", "CHA", "Background", "Max HP", "AC")  }
            body {
                characters.findAll().forEachIndexed { index, character ->
                    row(index, character.name, character.race, character.battleClass, character.level, character.str, character.dex, character.con, character.int, character.wis, character.cha, character.background, character.maxHP, character.ac)
                }

            }
        })
    }

    fun showCharacter(character : CharacterModel?) {
        if(character != null)
            t.println(table{            //creates table to display character attributes
                align = TextAlign.CENTER
                header {
                    style(bold = true)
                    row("Name", "Race", "Class", "Level", "STR", "DEX", "CON", "INT", "WIS", "CHA", "Background", "Max HP", "AC")  }
                body {
                    style(green)
                    row(character.name, character.race, character.battleClass, character.level, character.str, character.dex, character.con, character.int, character.wis, character.cha, character.background, character.maxHP, character.ac) }
            })
        else
            t.println(red("Error: Character Not Found..."))
    }

    //level up/down character
    fun updateCharacterLevel(character: CharacterModel):Boolean {
        var tempLevel : String?
        var levelValidated = false
        do{
            tempLevel = t.prompt(brightBlue("Enter character level"))!!
            if (validateByteToNum(tempLevel, 1, 20)){    //character level cannot be less than 1 or greater than 20{
                levelValidated = true
                character.level = tempLevel.toByte()
            }else if (tempLevel == "-1"){
                t.println(rgb("#ff9393")("Returning..."))
                return false
            }else{
                t.println(red("Invalid input: Character level must be a valid number between 1 and 20."))
            }
        } while (!levelValidated)
        return true
    }

    fun updateCharacterName(character: CharacterModel): Boolean {
        var tempName : String?
        var nameValidated = false
        do{
            tempName = t.prompt(brightBlue("Enter Character Name"))!!
            if (tempName.isNotEmpty() && tempName != "-1") {
                nameValidated = true
                character.name = tempName
            }else if (tempName == "-1"){
                t.println(rgb("#ff9393")("Returning..."))
                return false
            }else{
                t.println(red("Error: Character name must not be empty."))
            }
        } while (!nameValidated)
        return true
    }

    fun updateCharacterScores(character: CharacterModel, score: String) {
        var tempScore : String?
        var scoreValidated = false
        do{
            tempScore = t.prompt(brightBlue("Enter score value"))!!

            if (validateByteToNum(tempScore, 1, 30)){ //Ability score cannot be below 1 or above 30
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
                t.println(rgb("#ff9393")("Returning..."))
               break
            }else{
                t.println(red("Invalid input: Ability score must be a valid number between 1 and 30."))
            }
        } while (!scoreValidated)
    }

    fun updateCharacterAC(character: CharacterModel) : Boolean {
        var tempAC : String?
        var acValidated = false
        do{
            tempAC = t.prompt(brightBlue("Enter armour class value"))!!

            if (validateByteToNum(tempAC, 1, 127)){ //Armour class cannot be below 1
                acValidated = true
                character.ac = tempAC.toByte()
            }else if (tempAC == "-1"){
                t.println(rgb("#ff9393")("Returning..."))
                return false
            }else{
                t.println(red("Invalid input: Armour class must be a valid number between 1 and 127."))
            }
        } while (!acValidated)

        return true
    }

    fun updateCharacterHP(character: CharacterModel) : Boolean {
        var tempHP : String?
        var hpValidated = false
        t.println(red("### WARNING: Maximum HP override value will be overwritten if character's level, class, or CON score is changed. ###"))
        do{
            tempHP = t.prompt(brightBlue("Enter max HP value"))!!

            if (validateShortToNum(tempHP, 1, 32767)){ //Armour class cannot be below 1
                hpValidated = true
                character.maxHP = tempHP.toShort()
            }else if (tempHP == "-1"){
                t.println(rgb("#ff9393")("Returning..."))
                return false
            }else{
                t.println(red("Invalid input: Max HP must be a valid number between 1 and 32767."))
            }
        } while (!hpValidated)

        return true
    }


    fun getId() : Long {
        var strId : String? // String to hold user input
        var searchId : Long // Long to hold converted id
        strId = t.prompt(brightBlue("Enter character ID"))!!
        searchId = if (strId.toLongOrNull() != null && !strId.isEmpty())
            strId.toLong()
        else
            -9
        return searchId
    }
}