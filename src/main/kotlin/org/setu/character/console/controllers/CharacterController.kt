package org.setu.character.console.controllers

import com.github.ajalt.mordant.rendering.TextAlign
import mu.KotlinLogging
import org.setu.character.console.helpers.calculateHP

import org.setu.character.console.models.CharacterJSONStore
import org.setu.character.console.models.CharacterModel
import org.setu.character.console.views.CharacterView

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextColors.Companion.rgb
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal       //https://github.com/ajalt/mordant

class CharacterController {

    val characters = CharacterJSONStore()
    val characterView = CharacterView()
    val logger = KotlinLogging.logger {}
    val t = Terminal()      //Mordant terminal
    var menuHeadingStyle = (rgb("#ff9393") + bold)

    init {
        logger.info { "Launching D&D Character Creator Console App" }
        t.info.width = 120      //setting max terminal width to be wider to allow for tables to print fully - https://github.com/ajalt/mordant/issues/13
        t.println(menuHeadingStyle("""
                     -+*#*+=                      
-+===--------:      ###-.:*## :+===--------:.     
 :*############*=  :=++:. +##: :*############*=.  
   +####*+++*#####: -+-+*+=++    =##*+++++*#####- 
   =####-    :####+:*##*##**.    :-====:   .*####-
   =####-     =#+::=#=:*#+-#*: :+*=++=-..   -####*
   =####-     =#*=#:+==.=--+=-:*+-####=     -####*
   =####-    -##:=####. :- -#*+= -=+++-    :#####:
   =####*++*####-*####     .=+##+==*.-*--*######: 
  .+###########+:=####+:.:=##*.#####=+##-####*-   
 :---------::.    ==########=  *+####*+-.::.      
 
        """.trimIndent()))
        t.println(menuHeadingStyle("=== Dungeons & Dragons Character Creator Kotlin App Version 1.1 ==="))

    }

    fun start() {
        do {
            val input: Int = menu()         //TODO: make menu print once per run of the menu
            when(input) {
                1 -> add()
                2 -> update()
                3 -> list()
                4 -> search()
                5 -> delete()
                -99 -> dummyData()
                -1 -> t.println(rgb("#ff9393")("Exiting Application..."))
                else -> t.println(red("Error: Invalid option entered."))
            }
            t.println()
        } while (input != -1)
        logger.info { "Shutting Down D&D Character Creator Console App" }
    }

    fun menu() :Int { return characterView.menu() }

    fun add(){
        var aCharacter = CharacterModel()
        var exitMenu = false        //boolean to check if menu has been exited, flipped to true upon exit confirmation
        t.println(menuHeadingStyle("==============    Create New Character    =============="))
        t.println(menuHeadingStyle("While entering values, enter '-1' to return to this menu"))
        t.println(menuHeadingStyle("========================================================"))

        do {
            t.println()
            t.println(green("============   Current character attributes   ============"))
            characterView.showCharacter(aCharacter)

            val input: Int = characterView.listAddOptions()
            when(input) {
                1 -> characterView.enterCharacterName(aCharacter)
                2 -> selectRace(aCharacter)
                3 -> selectClass(aCharacter)
                4 -> updateLevel(aCharacter)
                5 -> enterScores(aCharacter)
                6 -> selectBackground(aCharacter)
                7 -> {
                    //checking that all attributes have been filled out, and showing which ones haven't if needed
                    val nameEntered = aCharacter.name != ""
                    val raceEntered = aCharacter.race != ""
                    val classEntered = aCharacter.battleClass != ""
                    val bgEntered = aCharacter.background != ""

                    if(nameEntered && raceEntered && classEntered && bgEntered) {   //if all attributes have been filled out
                        characterView.showCharacter(aCharacter)
                        val exitConfirm = t.prompt(brightBlue("Create this character?"), choices = listOf("yes", "no"))
                        if (exitConfirm == "yes") {
                            createCharacter(aCharacter)
                            exitMenu = true
                            t.println(green("Character created!"))
                        }
                    }else{      //if not all attributes are filled out
                        t.println(red("Error: The following character attribute fields are empty:"))
                        if (!nameEntered) t.println(red("Name"))
                        if (!raceEntered) t.println(red("Race"))
                        if (!classEntered) t.println(red("Class"))
                        if (!bgEntered) t.println(red("Background"))
                        t.println(red("Please fill out these attributes and try again."))
                    }
                }
                -1 -> {
                    t.println(red("Are you sure you want to cancel character creation? All work done will be lost!"))
                    val exitConfirm = t.prompt(brightBlue("Cancel character creation?"), choices = listOf("yes", "no"))
                    if (exitConfirm == "yes"){
                        t.println(rgb("#ff9393")("Aborting character creation..."))
                        exitMenu = true
                    }
                }
                else -> t.println(red("Error: Invalid option entered."))
            }
        } while (!exitMenu)
    }

    fun selectRace (character: CharacterModel) {
        var raceChosen = false
        do {
            val input: Int = characterView.listRaces()
            when(input){
                1 -> {character.race = "Dragonborn"; raceChosen = true}
                2 -> {character.race = "Dwarf"; raceChosen = true}
                3 -> {character.race = "Elf"; raceChosen = true}
                4 -> {character.race = "Gnome"; raceChosen = true}
                5 -> {character.race = "Half-Elf"; raceChosen = true}
                6 -> {character.race = "Half-Orc"; raceChosen = true}
                7 -> {character.race = "Halfling"; raceChosen = true}
                8 -> {character.race = "Human"; raceChosen = true}
                9 -> {character.race = "Tiefling"; raceChosen = true}
                -1 -> t.println(rgb("#ff9393")("Returning..."))
                else -> t.println(red("Error: Invalid option entered."))
            }
            t.println()
        } while (input != -1 && !raceChosen)    //loops menu until valid selection is made (race updated) or -1 is entered
    }

    fun selectClass (character: CharacterModel) {
        var classChosen = false
        do {
            val input: Int = characterView.listClasses()
            when(input){
                1 -> {character.battleClass = "Barbarian"; classChosen = true}
                2 -> {character.battleClass = "Bard"; classChosen = true}
                3 -> {character.battleClass = "Cleric"; classChosen = true}
                4 -> {character.battleClass = "Druid"; classChosen = true}
                5 -> {character.battleClass = "Fighter"; classChosen = true}
                6 -> {character.battleClass = "Monk"; classChosen = true}
                7 -> {character.battleClass = "Paladin"; classChosen = true}
                8 -> {character.battleClass = "Ranger"; classChosen = true}
                9 -> {character.battleClass = "Rouge"; classChosen = true}
                10 -> {character.battleClass = "Sorcerer"; classChosen = true}
                11 -> {character.battleClass = "Warlock"; classChosen = true}
                12 -> {character.battleClass = "Wizard"; classChosen = true}
                -1 -> t.println(rgb("#ff9393")("Returning..."))
                else -> t.println(red("Error: Invalid option entered."))
            }
        } while (input != -1 && !classChosen)

        if (classChosen){
            character.maxHP = calculateHP(character.level, character.battleClass, character.con)    //class update => base HP updated => recalculate HP
        }
    }

    fun enterScores (character: CharacterModel) {
        var conUpdated = false
        do {
            t.println()
            t.println(green("===========   Current ability scores   ==========="))
            t.println(table{            //creates table to display ability scores
                align = TextAlign.CENTER
                header {
                    style(bold = true)
                    row("STR", "DEX", "CON", "INT", "WIS", "CHA", "Stat Total")  }
                body {
                    style(green)
                    row(character.str, character.dex, character.con, character.int, character.wis, character.cha, character.str + character.dex + character.con + character.int + character.wis + character.cha) }
            })

            val input: Int = characterView.listAbilityScores()
            when(input){
                1 -> characterView.updateCharacterScores(character, "str")
                2 -> characterView.updateCharacterScores(character, "dex")
                3 -> {characterView.updateCharacterScores(character, "con"); conUpdated = true}
                4 -> characterView.updateCharacterScores(character, "int")
                5 -> characterView.updateCharacterScores(character, "wis")
                6 -> characterView.updateCharacterScores(character, "cha")
                -1 -> {}        //empty function to prevent error message printing upon -1 entry
                else -> t.println(red("Error: Invalid option entered."))
            }
            if (conUpdated) {
                character.maxHP = calculateHP(character.level, character.battleClass, character.con)    //con update => modifier changed => recalculate HP
            }
        } while (input != -1 )
    }

    fun selectBackground (character: CharacterModel) {
        var bgUpdated = false
        do {
            val input: Int = characterView.listBackgrounds()
            when(input){
                1 -> {character.background = "Acolyte"; bgUpdated = true}
                2 -> {character.background = "Charlatan"; bgUpdated = true}
                3 -> {character.background = "Criminal"; bgUpdated = true}
                4 -> {character.background = "Entertainer"; bgUpdated = true}
                5 -> {character.background = "Folk Hero"; bgUpdated = true}
                6 -> {character.background = "Guild Artisan"; bgUpdated = true}
                7 -> {character.background = "Hermit"; bgUpdated = true}
                8 -> {character.background = "Noble"; bgUpdated = true}
                9 -> {character.background = "Outlander"; bgUpdated = true}
                10 -> {character.background = "Sage"; bgUpdated = true}
                11 -> {character.background = "Sailor"; bgUpdated = true}
                12 -> {character.background = "Solider"; bgUpdated = true}
                13 -> {character.background = "Urchin"; bgUpdated = true}
                -1 -> t.println(rgb("#ff9393")("Returning..."))
                else -> t.println(red("Error: Invalid option entered."))
            }
        } while (input != -1 && !bgUpdated)
    }

    fun createCharacter( character: CharacterModel){
        characters.create(character)
        logger.info("Character Added : [ $character ]")
    }


    fun list() {
        characterView.listCharacters(characters)
    }

    fun update() {

        characterView.listCharacters(characters)
        var searchId = characterView.getId()
        val aCharacter = search(searchId)

        if(aCharacter != null) {
            do {
                val input: Int = characterView.listUpdateOptions()
                when(input) {
                    1 -> updateLevel(aCharacter)
                    2 -> updateName(aCharacter)
                    3 -> updateRace(aCharacter)
                    4 -> updateClass(aCharacter)
                    5 -> updateScores(aCharacter)
                    6 -> updateBackground(aCharacter)
                    7 -> updateAC(aCharacter)
                    8 -> updateHP(aCharacter)
                    else -> println("Invalid Option")
                }
                println()
            } while (input != -1)
        }
        else
            println("No Character with ID $searchId found")
    }

    fun updateLevel (character: CharacterModel) {
        characterView.updateCharacterLevel(character)
        character.maxHP = calculateHP(character.level, character.battleClass, character.con)    //level change => HP increase/decrease => recalculate HP
    }

    fun updateName (character: CharacterModel) {
        if(characterView.updateCharacterName(character)) {
            characters.update(character)
            characterView.showCharacter(character)
            logger.info("Character Updated : [ $character ]")
        }
    }

    fun updateRace (character: CharacterModel) {
        println("--- Update character race (enter '-1' to return) ---")
        var raceUpdated = false
        do {
            val input: Int = characterView.listRaces()
            when(input){
                1 -> {character.race = "Dragonborn"; raceUpdated = true}
                2 -> {character.race = "Dwarf"; raceUpdated = true}
                3 -> {character.race = "Elf"; raceUpdated = true}
                4 -> {character.race = "Gnome"; raceUpdated = true}
                5 -> {character.race = "Half-Elf"; raceUpdated = true}
                6 -> {character.race = "Half-Orc"; raceUpdated = true}
                7 -> {character.race = "Halfling"; raceUpdated = true}
                8 -> {character.race = "Human"; raceUpdated = true}
                9 -> {character.race = "Tiefling"; raceUpdated = true}
                else -> println("Invalid Option")
            }
        } while (input != -1 && !raceUpdated)    //loops menu until valid selection is made (race updated) or -1 is entered

        if (raceUpdated){
            characters.update(character)
            characterView.showCharacter(character)
            logger.info("Character Updated : [ $character ]")
        }
    }

    fun updateClass (character: CharacterModel) {
        println("--- Update character class (enter '-1' to return) ---")
        var classUpdated = false
        do {
            val input: Int = characterView.listClasses()
            when(input){
                1 -> {character.battleClass = "Barbarian"; classUpdated = true}
                2 -> {character.battleClass = "Bard"; classUpdated = true}
                3 -> {character.battleClass = "Cleric"; classUpdated = true}
                4 -> {character.battleClass = "Druid"; classUpdated = true}
                5 -> {character.battleClass = "Fighter"; classUpdated = true}
                6 -> {character.battleClass = "Monk"; classUpdated = true}
                7 -> {character.battleClass = "Paladin"; classUpdated = true}
                8 -> {character.battleClass = "Ranger"; classUpdated = true}
                9 -> {character.battleClass = "Rouge"; classUpdated = true}
                10 -> {character.battleClass = "Sorcerer"; classUpdated = true}
                11 -> {character.battleClass = "Warlock"; classUpdated = true}
                12 -> {character.battleClass = "Wizard"; classUpdated = true}
                else -> println("Invalid Option")
            }
        } while (input != -1 && !classUpdated)

        if (classUpdated){
            character.maxHP = calculateHP(character.level, character.battleClass, character.con)    //class update => base HP updated => recalculate HP
            characters.update(character)
            characterView.showCharacter(character)
            logger.info("Character Updated : [ $character ]")
        }
    }

    fun updateScores (character: CharacterModel) {
//        var scoreUpdated = false
//        var conUpdated = false
//        do {
//            val input: Int = characterView.listAbilityScores()
//            when(input){
//                1 -> scoreUpdated = characterView.updateCharacterScores(character, "str")
//                2 -> scoreUpdated = characterView.updateCharacterScores(character, "dex")
//                3 -> {scoreUpdated = characterView.updateCharacterScores(character, "con"); conUpdated = true}
//                4 -> scoreUpdated = characterView.updateCharacterScores(character, "int")
//                5 -> scoreUpdated = characterView.updateCharacterScores(character, "wis")
//                6 -> scoreUpdated = characterView.updateCharacterScores(character, "cha")
//                else -> println("Invalid Option")
//            }
//        } while (input != -1 && !scoreUpdated)
//
//        if (scoreUpdated){
//            if (conUpdated) {
//                character.maxHP = calculateHP(character.level, character.battleClass, character.con)    //con update => modifier changed => recalculate HP
//            }
//            characters.update(character)
//            characterView.showCharacter(character)
//            logger.info("Character Updated : [ $character ]")
//        }
    }

    fun updateBackground (character: CharacterModel) {
        println("--- Update character background (enter '-1' to return) ---")
        var bgUpdated = false
        do {
            val input: Int = characterView.listBackgrounds()
            when(input){
                1 -> {character.background = "Acolyte"; bgUpdated = true}
                2 -> {character.background = "Charlatan"; bgUpdated = true}
                3 -> {character.background = "Criminal"; bgUpdated = true}
                4 -> {character.background = "Entertainer"; bgUpdated = true}
                5 -> {character.background = "Folk Hero"; bgUpdated = true}
                6 -> {character.background = "Guild Artisan"; bgUpdated = true}
                7 -> {character.background = "Hermit"; bgUpdated = true}
                8 -> {character.background = "Noble"; bgUpdated = true}
                9 -> {character.background = "Outlander"; bgUpdated = true}
                10 -> {character.background = "Sage"; bgUpdated = true}
                11 -> {character.background = "Sailor"; bgUpdated = true}
                12 -> {character.background = "Solider"; bgUpdated = true}
                13 -> {character.background = "Urchin"; bgUpdated = true}
                else -> println("Invalid Option")
            }
        } while (input != -1 && !bgUpdated)

        if (bgUpdated){
            characters.update(character)
            characterView.showCharacter(character)
            logger.info("Character Updated : [ $character ]")
        }
    }

    fun updateAC (character: CharacterModel) {
        if(characterView.updateCharacterAC(character)) {
            characters.update(character)
            characterView.showCharacter(character)
            logger.info("Character Updated : [ $character ]")
        }
    }

    fun updateHP (character: CharacterModel) {
        if(characterView.updateCharacterHP(character)) {
            characters.update(character)
            characterView.showCharacter(character)
            logger.info("Character Updated : [ $character ]")
        }
    }

    fun delete(){
        characterView.listCharacters(characters)
        var searchId = characterView.getId()
        val aCharacter = search(searchId)

        if(aCharacter != null) {
                characters.delete(aCharacter)
                logger.info("Character Deleted : [ $aCharacter ]")
        }
        else
            println("No Character with ID $searchId found")
    }

    fun search() {
        characterView.listCharacters(characters)
        val aCharacter = search(characterView.getId())
        characterView.showCharacter(aCharacter)
    }


    fun search(id: Long) : CharacterModel? {
        var foundCharacter = characters.findOne(id)
        return foundCharacter
    }

    fun dummyData() {
        characters.create(CharacterModel(5377281911035930374, "Rylanor", "Half-Orc", "Barbarian", 1, 18, 14, 16, 7, 10, 8, 15, 10, "Outlander"))
        characters.create(CharacterModel(9218356089001388513, "Cyn", "Half-Elf", "Ranger", 1, 10, 16, 15, 8, 12, 10, 12, 10, "Criminal"))
        characters.create(CharacterModel(747018093021143261, "Leona van der Vastenholt", "Human", "Sorcerer", 1, 8, 14, 14, 14, 12, 19, 8, 10, "Sage"))
    }
}