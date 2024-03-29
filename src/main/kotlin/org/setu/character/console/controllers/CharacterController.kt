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
import org.setu.character.console.helpers.validateByteToNum
import org.setu.character.console.helpers.validateShortToNum
import org.setu.character.console.models.ItemModel

class CharacterController {

    val characters = CharacterJSONStore()
    val characterView = CharacterView()
    val logger = KotlinLogging.logger {}
    val t = Terminal()      //Mordant terminal
    var menuHeadingStyle = (rgb("#ff9393") + bold)

    init {
        logger.info { "Launching D&D Character Creator Console App" }
        t.info.width = 150      //setting max terminal width to be wider to allow for tables to print fully - https://github.com/ajalt/mordant/issues/13
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
        t.println(menuHeadingStyle("=== Dungeons & Dragons Character Creator Kotlin App Version 2.0 ==="))

    }

    fun start() {
        do {
            val input: Int = menu()
            when(input) {
                1 -> add()
                2 -> update()
                3 -> list()
                4 -> search()
                5 -> delete()
                6 -> settings()
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
                1 -> characterView.updateCharacterName(aCharacter)
                2 -> updateRace(aCharacter)
                3 -> updateClass(aCharacter)
                4 -> updateLevel(aCharacter)
                5 -> updateAbilityScores(aCharacter)
                6 -> updateBackground(aCharacter)
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

    fun list() {
        characterView.listCharacters(characters)
    }

    fun update() {
        val charsExist = characterView.listCharacters(characters)
        if (charsExist) {
            var searchId = characterView.getId()
            val aCharacter = search(searchId)

            if (aCharacter != null) {
                t.println(menuHeadingStyle("================    Update Character    ================"))
                t.println(menuHeadingStyle("While entering values, enter '-1' to return to this menu"))
                t.println(menuHeadingStyle("========================================================"))
                do {
                    t.println()
                    t.println(green("============   Current character attributes   ============"))
                    characterView.showCharacter(aCharacter)
                    val input: Int = characterView.listUpdateOptions()
                    when (input) {
                        1 -> {
                            if (updateLevel(aCharacter)) {
                                updateCharacter(aCharacter, searchId)
                                t.println(green("Level updated!"))
                            }
                        }

                        2 -> {
                            if (characterView.updateCharacterName(aCharacter)) {
                                updateCharacter(aCharacter, searchId)
                                t.println(green("Name updated!"))
                            }
                        }

                        3 -> {
                            if (updateRace(aCharacter)) {
                                updateCharacter(aCharacter, searchId)
                                t.println(green("Race updated!"))
                            }
                        }

                        4 -> {
                            if (updateClass(aCharacter)) {
                                updateCharacter(aCharacter, searchId)
                                t.println(green("Class updated!"))
                            }
                        }

                        5 -> {
                            if (updateAbilityScores(aCharacter)) {
                                updateCharacter(aCharacter, searchId)
                                t.println(green("Ability scores updated!"))
                            }
                        }

                        6 -> {
                            if (updateBackground(aCharacter)) {
                                updateCharacter(aCharacter, searchId)
                                t.println(green("Background updated!"))
                            }
                        }

                        7 -> {
                            if (updateAC(aCharacter)) {
                                updateCharacter(aCharacter, searchId)
                                t.println(green("Armour class updated!"))
                            }
                        }

                        8 -> {
                            if (updateHP(aCharacter)) {
                                updateCharacter(aCharacter, searchId)
                                t.println(green("Max HP updated!"))
                            }
                        }

                        9 -> {
                            itemsMenu(aCharacter)
                        }

                        -1 -> {}        //empty function to prevent error message printing upon -1 entry
                        else -> t.println(red("Error: Invalid option entered."))
                    }
                } while (input != -1)
            } else
                t.println(red("Error: No Character with ID $searchId found"))
        }
    }

    fun delete(){
        val charsExist = characterView.listCharacters(characters)
        if (charsExist) {
            var searchId = characterView.getId()
            val aCharacter = search(searchId)

            if (aCharacter != null) {
                characterView.showCharacter(aCharacter)
                t.println(red("Are you sure you want to delete this character? Deletion cannot be undone, and this character's data will be lost forever!"))
                val exitConfirm = t.prompt(brightBlue("Delete character?"), choices = listOf("yes", "no"))
                if (exitConfirm == "yes") {      //delete confirmation: must type character name to confirm deletion
                    var matchingName: Boolean
                    var typedName: String?
                    do {
                        typedName =
                            t.prompt(brightBlue("Enter character name to confirm deletion (or type -1 to cancel deletion)"))
                        matchingName = typedName?.trim().equals(aCharacter.name)
                        if (!matchingName && !typedName.equals("-1")) {
                            t.println(red("Error: Entered name does not match character name"))
                        }
                    } while (typedName != "-1" && !matchingName)
                    if (matchingName) {
                        characters.delete(aCharacter)
                        t.println(rgb("#ff9393")("Character deleted."))
                        logger.info("Character Deleted : [ $aCharacter ]")
                    } else if (typedName.equals("-1")) {
                        t.println(rgb("#ff9393")("Aborting character deletion..."))
                        t.println(italic("${green(aCharacter.name)}: \"Phew, thought I was a goner!\""))
                    }
                } else {
                    t.println(rgb("#ff9393")("Aborting character deletion..."))
                    t.println(italic("${green(aCharacter.name)}: \"Phew, thought I was a goner!\""))
                }
            } else
                t.println(red("Error: No Character with ID $searchId found"))
        }
    }

    fun search() {
        if (characters.findAll().isNotEmpty()) {
            t.println()
            t.println(menuHeadingStyle("=================   Search Characters  ================="))
            t.println(menuHeadingStyle("While entering values, enter '-1' to return to this menu"))
            t.println(menuHeadingStyle("========================================================"))
            do {
                val input: Int = characterView.listSearchOptions()
                when (input) {
                    1 -> searchByLevel()
                    2 -> searchByName()
                    3 -> searchByRace()
                    4 -> searchByClass()
                    5 -> searchByAbility()
                    6 -> searchByBackground()
                    7 -> searchByAC()
                    8 -> searchByHP()
                    9 -> searchByID()
                    -1 -> {}
                    else -> t.println(red("Error: Invalid option entered."))
                }
                t.println()
            } while (input != -1)
        }else{
            t.println(red("Error: No characters have been created yet."))
        }
    }

    fun search(id: Long) : CharacterModel? {
        if (id.toInt() != -1 && id.toInt() > -2 && id.toInt() < characters.characters.size) {     //validating against index out of bounds errors
            var foundCharacter = characters.findOneByIndex(id)
            return foundCharacter
        }else if (id.toInt() == -1) {       //if user request to go back
            t.println(rgb("#ff9393")("Returning..."))
        }
        return null
    }

    fun settings(){
        do {
            val input: Int = characterView.listSettingsMenu()
            when(input) {
                1 -> deleteData()
                -1 -> {}
                else -> t.println(red("Error: Invalid option entered."))
            }
            t.println()
        } while (input != -1)
    }

    fun deleteData(){       //clear characters.json
        t.println(red("Are you sure you want to delete all character data? Deletion cannot be undone, and data will be lost forever!"))
        val deleteConfirm = t.prompt(brightBlue("Delete all data?"), choices = listOf("yes", "no"))
        if (deleteConfirm == "yes"){
            var matching: Boolean
            var typed: String?
            do {
                typed = t.prompt(brightBlue("Enter 'DELETE' to confirm deletion (or type -1 to cancel deletion)"))
                matching = typed.equals("DELETE")
                if (!matching && !typed.equals("-1")) {
                    t.println(red("Error: Input does not match 'DELETE'"))
                }
                if (matching) {
                    characters.deleteAll()
                    t.println(rgb("#ff9393")("Data deleted."))
                    logger.info("Character data deleted")
                } else if (typed.equals("-1")) {
                    t.println(rgb("#ff9393")("Aborting data deletion..."))
                }
            } while (typed != "-1" && !matching)
        }else {
            t.println(rgb("#ff9393")("Aborting data deletion..."))
        }
    }

    fun dummyData() {
        var character1 = CharacterModel(5377281911035930374, "Rylanor", "Human", "Barbarian", 1, 18, 14, 16, 7, 10, 8, 15, 10, "Outlander")
        character1.items.add(ItemModel("Greatsword", "Weapon", "Common", "Standard issue greatsword.", 50u, false, true))
        character1.items.add(ItemModel("Bag of Holding", "Wonderous item", "Uncommon", "This mysterious bag seems to have an infinitely large capacity", 200u, false, true))
        characters.create(character1)
        var character2 = CharacterModel(9218356089001388513, "Cyn", "Half-Elf", "Ranger", 2, 10, 16, 15, 8, 12, 10, 20, 13, "Criminal")
        character2.items.add(ItemModel("Crossbow +2", "Weapon", "Rare","You have a +2 bonus to attack and damage rolls made with this magic weapon.", 2000u, true, true ))
        characters.create(character2)
        characters.create(CharacterModel(747018093021143261, "Leona van der Vastenholt", "Human", "Sorcerer", 1, 8, 14, 14, 14, 12, 19, 8, 10, "Sage"))
    }

    fun updateRace (character: CharacterModel) : Boolean {
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
        return raceChosen
    }

    fun updateClass (character: CharacterModel): Boolean {
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

        return classChosen
    }

    fun updateAbilityScores (character: CharacterModel): Boolean {
        var scoreUpdated = false
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
                1 -> {characterView.updateCharacterScores(character, "str"); scoreUpdated = true}
                2 -> {characterView.updateCharacterScores(character, "dex"); scoreUpdated = true}
                3 -> {characterView.updateCharacterScores(character, "con"); scoreUpdated = true ; conUpdated = true}
                4 -> {characterView.updateCharacterScores(character, "int"); scoreUpdated = true}
                5 -> {characterView.updateCharacterScores(character, "wis"); scoreUpdated = true}
                6 -> {characterView.updateCharacterScores(character, "cha"); scoreUpdated = true}
                -1 -> {}        //empty function to prevent error message printing upon -1 entry
                else -> t.println(red("Error: Invalid option entered."))
            }
            if (conUpdated) {
                character.maxHP = calculateHP(character.level, character.battleClass, character.con)    //con update => modifier changed => recalculate HP
            }
        } while (input != -1 )

        return scoreUpdated
    }

    fun updateBackground (character: CharacterModel): Boolean {
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
        return bgUpdated
    }

    fun updateLevel (character: CharacterModel): Boolean {
        if (characterView.updateCharacterLevel(character)){
            character.maxHP = calculateHP(character.level, character.battleClass, character.con)    //level change => HP increase/decrease => recalculate HP
            return true
        }
        return false
    }
    fun updateAC (character: CharacterModel): Boolean {
        if(characterView.updateCharacterAC(character)) {
            return true
        }
        return false
    }

    fun updateHP (character: CharacterModel): Boolean {
        if(characterView.updateCharacterHP(character)) {
            return true
        }
        return false
    }

    fun createCharacter( character: CharacterModel){
        characters.create(character)
        logger.info("Character Added : [ $character ]")
    }

    fun updateCharacter( character: CharacterModel, index: Long){
        characters.update(character, index)
        logger.info("Character Updated : [ $character ]")
        Thread.sleep(100)       //wait to prevent logger from printing in the middle of table prints
    }

    fun searchByID(){
        var searchId = characterView.getId()
        val aCharacter = search(searchId)

        if (aCharacter != null) {
            t.println()
            characterView.showCharacter(aCharacter, true)
        } else
            t.println(red("Error: No Character with ID $searchId found"))
    }

    fun searchByName() {
        var inputValidated = false
        do {
            var searchName: String = t.prompt(brightBlue("Enter character name to search"))!!
            searchName = searchName.trim()
            if (searchName.isNotEmpty() && searchName != "-1") {
                inputValidated = true
                var matches: MutableList<CharacterModel> = mutableListOf()      //list of matches to build up search results
                characters.findAll().forEach { character ->
                    if (searchName.lowercase() in character.name.lowercase()) {               //if character name contains searched name
                        matches.add(character)
                    }
                }
                if (matches.isNotEmpty()) {
                    if (matches.size == 1)      //if only 1 match, display character
                        characterView.showCharacter(matches[0], true)
                    else                        //if multiple matches, display all
                        characterView.listCharacters(matches)
                } else {
                    t.println(red("Error: No characters found for input search term."))
                }
            } else if (searchName == "-1") {
                t.println(rgb("#ff9393")("Returning..."))
                inputValidated = true
            }else{
                t.println(red("Error: Input must not be empty."))
            }
        }while (!inputValidated)
    }

    fun searchByRace() {
        var exitSearch = false
        var raceChosen = false
        var searchRace: String = ""
        //select
        do {
            val input: Int = characterView.listRaces()
            when(input){
                1 -> {searchRace = "Dragonborn"; raceChosen = true}
                2 -> {searchRace = "Dwarf"; raceChosen = true}
                3 -> {searchRace = "Elf"; raceChosen = true}
                4 -> {searchRace = "Gnome"; raceChosen = true}
                5 -> {searchRace = "Half-Elf"; raceChosen = true}
                6 -> {searchRace = "Half-Orc"; raceChosen = true}
                7 -> {searchRace = "Halfling"; raceChosen = true}
                8 -> {searchRace = "Human"; raceChosen = true}
                9 -> {searchRace = "Tiefling"; raceChosen = true}
                -1 -> {t.println(rgb("#ff9393")("Returning...")); exitSearch = true}
                else -> t.println(red("Error: Invalid option entered."))
            }
            t.println()
        } while (input != -1 && !raceChosen)    //loops menu until valid selection is made (race updated) or -1 is entered

        //search
        if (!exitSearch){   //if -1 was not input
            var matches: MutableList<CharacterModel> = mutableListOf()      //list of matches to build up search results
            characters.findAll().forEach { character ->
                if (searchRace == character.race) {               //if character race is same as search race
                    matches.add(character)
                }
            }
            if (matches.isNotEmpty()) {
                if (matches.size == 1)      //if only 1 match, display character
                    characterView.showCharacter(matches[0], true)
                else
                    characterView.listCharacters(matches)
            } else {
                t.println(red("Error: No characters found for input search term."))
            }
        }
    }

    fun searchByClass() {
        var exitSearch = false
        var classChosen = false
        var searchClass: String = ""
        //select
        do {
            val input: Int = characterView.listClasses()
            when(input){
                1 -> {searchClass = "Barbarian"; classChosen = true}
                2 -> {searchClass = "Bard"; classChosen = true}
                3 -> {searchClass = "Cleric"; classChosen = true}
                4 -> {searchClass = "Druid"; classChosen = true}
                5 -> {searchClass = "Fighter"; classChosen = true}
                6 -> {searchClass = "Monk"; classChosen = true}
                7 -> {searchClass = "Paladin"; classChosen = true}
                8 -> {searchClass = "Ranger"; classChosen = true}
                9 -> {searchClass = "Rouge"; classChosen = true}
                10 -> {searchClass = "Sorcerer"; classChosen = true}
                11 -> {searchClass = "Warlock"; classChosen = true}
                12 -> {searchClass = "Wizard"; classChosen = true}
                -1 -> {t.println(rgb("#ff9393")("Returning...")); exitSearch = true}
                else -> t.println(red("Error: Invalid option entered."))
            }
            t.println()
        } while (input != -1 && !classChosen)    //loops menu until valid selection is made (race updated) or -1 is entered

        //search
        if (!exitSearch){   //if -1 was not input
            var matches: MutableList<CharacterModel> = mutableListOf()      //list of matches to build up search results
            characters.findAll().forEach { character ->
                if (searchClass == character.battleClass) {
                    matches.add(character)
                }
            }
            if (matches.isNotEmpty()) {
                if (matches.size == 1)      //if only 1 match, display character
                    characterView.showCharacter(matches[0], true)
                else
                    characterView.listCharacters(matches)
            } else {
                t.println(red("Error: No characters found for input search term."))
            }
        }
    }

    fun searchByBackground() {
        var exitSearch = false
        var bgChosen = false
        var searchBG: String = ""
        //select
        do {
            val input: Int = characterView.listBackgrounds()
            when(input){
                1 -> {searchBG = "Acolyte"; bgChosen = true}
                2 -> {searchBG = "Charlatan"; bgChosen = true}
                3 -> {searchBG = "Criminal"; bgChosen = true}
                4 -> {searchBG = "Entertainer"; bgChosen = true}
                5 -> {searchBG = "Folk Hero"; bgChosen = true}
                6 -> {searchBG = "Guild Artisan"; bgChosen = true}
                7 -> {searchBG = "Hermit"; bgChosen = true}
                8 -> {searchBG = "Noble"; bgChosen = true}
                9 -> {searchBG = "Outlander"; bgChosen = true}
                10 -> {searchBG = "Sage"; bgChosen = true}
                11 -> {searchBG = "Sailor"; bgChosen = true}
                12 -> {searchBG = "Solider"; bgChosen = true}
                13 -> {searchBG = "Urchin"; bgChosen = true}
                -1 -> {t.println(rgb("#ff9393")("Returning...")); exitSearch = true}
                else -> t.println(red("Error: Invalid option entered."))
            }
            t.println()
        } while (input != -1 && !bgChosen)    //loops menu until valid selection is made (race updated) or -1 is entered

        //search
        if (!exitSearch){   //if -1 was not input
            var matches: MutableList<CharacterModel> = mutableListOf()      //list of matches to build up search results
            characters.findAll().forEach { character ->
                if (searchBG == character.background) {
                    matches.add(character)
                }
            }
            if (matches.isNotEmpty()) {
                if (matches.size == 1)      //if only 1 match, display character
                    characterView.showCharacter(matches[0], true)
                else
                    characterView.listCharacters(matches)
            } else {
                t.println(red("Error: No characters found for input search term."))
            }
        }
    }

    fun searchByLevel() {
        var inputValidated = false
        do {
            var searchLevel: String = t.prompt(brightBlue("Enter character level to search"))!!
            searchLevel = searchLevel.trim()
            if (validateByteToNum(searchLevel, 1, 20)) {
                val response = t.prompt(brightBlue("Do you want to search for same level, above level, or below level characters?"), choices = listOf("same", "above", "below"))
                inputValidated = true
                var matches: MutableList<CharacterModel> = mutableListOf()      //list of matches to build up search results
                if (response == "same") {            //if looking for characters of same level
                    characters.findAll().forEach { character ->
                        if (searchLevel.toByte() == character.level) {
                            matches.add(character)
                        }
                    }
                }else if (response == "above"){     //if looking for above input
                    characters.findAll().forEach { character ->
                        if (searchLevel.toByte() < character.level) {
                            matches.add(character)
                        }
                    }
                }else if (response == "below"){     //if looking for below input
                    characters.findAll().forEach { character ->
                        if (searchLevel.toByte() > character.level) {
                            matches.add(character)
                        }
                    }
                }
                if (matches.isNotEmpty()) {
                    if (matches.size == 1)      //if only 1 match, display character
                        characterView.showCharacter(matches[0], true)
                    else
                        characterView.listCharacters(matches)
                } else {
                    t.println(red("Error: No characters found for input search term."))
                }
            } else if (searchLevel == "-1") {
                t.println(rgb("#ff9393")("Returning..."))
                inputValidated = true
            }else{
                t.println(red("Invalid input: Character level must be a valid number between 1 and 20."))
            }
        }while (!inputValidated)
    }

    fun searchByAC() {
        var inputValidated = false
        do {
            var searchAC: String = t.prompt(brightBlue("Enter armour class to search"))!!
            searchAC = searchAC.trim()
            if (validateByteToNum(searchAC, 1, 20)) {
                val response = t.prompt(brightBlue("Do you want to search characters with same, above, or below armour class?"), choices = listOf("same", "above", "below"))
                inputValidated = true
                var matches: MutableList<CharacterModel> = mutableListOf()      //list of matches to build up search results
                if (response == "same") {            //if looking for characters of same level
                    characters.findAll().forEach { character ->
                        if (searchAC.toByte() == character.ac) {
                            matches.add(character)
                        }
                    }
                }else if (response == "above"){     //if looking for above input
                    characters.findAll().forEach { character ->
                        if (searchAC.toByte() < character.ac) {
                            matches.add(character)
                        }
                    }
                }else if (response == "below"){     //if looking for below input
                    characters.findAll().forEach { character ->
                        if (searchAC.toByte() > character.ac) {
                            matches.add(character)
                        }
                    }
                }
                if (matches.isNotEmpty()) {
                    if (matches.size == 1)      //if only 1 match, display character
                        characterView.showCharacter(matches[0], true)
                    else
                        characterView.listCharacters(matches)
                } else {
                    t.println(red("Error: No characters found for input search term."))
                }
            } else if (searchAC == "-1") {
                t.println(rgb("#ff9393")("Returning..."))
                inputValidated = true
            }else{
                t.println(red("Invalid input: Character level must be a valid number between 1 and 20."))
            }
        }while (!inputValidated)
    }

    fun searchByHP() {
        var inputValidated = false
        do {
            var searchHP: String = t.prompt(brightBlue("Enter Max HP to search"))!!
            searchHP = searchHP.trim()
            if (validateShortToNum(searchHP, 1, 20)) {
                val response = t.prompt(brightBlue("Do you want to search characters with same, above, or below HP?"), choices = listOf("same", "above", "below"))
                inputValidated = true
                var matches: MutableList<CharacterModel> = mutableListOf()      //list of matches to build up search results
                if (response == "same") {            //if looking for characters of same level
                    characters.findAll().forEach { character ->
                        if (searchHP.toShort() == character.maxHP) {
                            matches.add(character)
                        }
                    }
                }else if (response == "above"){     //if looking for above input
                    characters.findAll().forEach { character ->
                        if (searchHP.toShort() < character.maxHP) {
                            matches.add(character)
                        }
                    }
                }else if (response == "below"){     //if looking for below input
                    characters.findAll().forEach { character ->
                        if (searchHP.toShort() > character.maxHP) {
                            matches.add(character)
                        }
                    }
                }
                if (matches.isNotEmpty()) {
                    if (matches.size == 1)      //if only 1 match, display character
                        characterView.showCharacter(matches[0], true)
                    else
                        characterView.listCharacters(matches)
                } else {
                    t.println(red("Error: No characters found for input search term."))
                }
            } else if (searchHP == "-1") {
                t.println(rgb("#ff9393")("Returning..."))
                inputValidated = true
            }else{
                t.println(red("Invalid input: Character level must be a valid number between 1 and 20."))
            }
        }while (!inputValidated)
    }

    fun searchByAbility() {     //186 line method :)
        var exitSearch = false
        var abilityChosen = false
        var searchScore = ""
        var inputValidated = false
        //select
        do {
            val input: Int = characterView.listAbilityScores()
            when(input){
                1 -> {searchScore = "str";abilityChosen = true}
                2 -> {searchScore = "dex";abilityChosen = true}
                3 -> {searchScore = "con";abilityChosen = true}
                4 -> {searchScore = "int";abilityChosen = true}
                5 -> {searchScore = "wis";abilityChosen = true}
                6 -> {searchScore = "cha";abilityChosen = true}
                -1 -> {t.println(rgb("#ff9393")("Returning...")); exitSearch = true}
                else -> t.println(red("Error: Invalid option entered."))
            }
            t.println()
        } while (input != -1 && !abilityChosen)    //loops menu until valid selection is made (race updated) or -1 is entered

        //search
        if (!exitSearch) {
            do {
                var searchValue: String = t.prompt(brightBlue("Enter ability score value to search"))!!
                searchValue = searchValue.trim()
                if (validateByteToNum(searchValue, 1, 30)) {
                    val response = t.prompt(
                        brightBlue("Do you want to search characters with same, above, or below ability score?"),
                        choices = listOf("same", "above", "below")
                    )
                    inputValidated = true
                    var matches: MutableList<CharacterModel> =
                        mutableListOf()      //list of matches to build up search results
                    if (response == "same") {            //if looking for characters of same level
                        when(searchScore){
                            "str" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() == character.str) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "dex" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() == character.dex) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "con" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() == character.con) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "int" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() == character.int) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "wis" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() == character.wis) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "cha" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() == character.cha) {
                                        matches.add(character)
                                    }
                                }
                            }
                        }
                    } else if (response == "above") {     //if looking for above input
                        when(searchScore){
                            "str" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() < character.str) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "dex" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() < character.dex) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "con" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() < character.con) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "int" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() < character.int) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "wis" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() < character.wis) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "cha" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() < character.cha) {
                                        matches.add(character)
                                    }
                                }
                            }
                        }
                    } else if (response == "below") {     //if looking for below input
                        when(searchScore){
                            "str" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() > character.str) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "dex" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() > character.dex) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "con" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() > character.con) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "int" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() > character.int) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "wis" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() > character.wis) {
                                        matches.add(character)
                                    }
                                }
                            }
                            "cha" -> {
                                characters.findAll().forEach { character ->
                                    if (searchValue.toByte() > character.cha) {
                                        matches.add(character)
                                    }
                                }
                            }
                        }
                    }
                    if (matches.isNotEmpty()) {
                        if (matches.size == 1)      //if only 1 match, display character
                            characterView.showCharacter(matches[0], true)
                        else
                            characterView.listCharacters(matches)
                    } else {
                        t.println(red("Error: No characters found for input search term."))
                    }
                } else if (searchValue == "-1") {
                    t.println(rgb("#ff9393")("Returning..."))
                    inputValidated = true
                } else {
                    t.println(red("Invalid input: Character level must be a valid number between 1 and 20."))
                }
            } while (!inputValidated)
        }
    }

//items
    fun itemsMenu(character: CharacterModel){
        t.println()
        t.println(menuHeadingStyle("===================   Manage Items   ==================="))
        t.println(menuHeadingStyle("While entering values, enter '-1' to return to this menu"))
        t.println(menuHeadingStyle("========================================================"))
        do {
            t.println(green("============   Current Inventory   ============"))
            characterView.listCharactersItems(character)
            val input: Int = characterView.listItemOptions()
            when(input) {
                1 -> addItem(character)
                2 -> updateItemMenu(character)
                3 -> deleteItem(character)
                -1 -> {}
                else -> t.println(red("Error: Invalid option entered."))
            }
            t.println()
        } while (input != -1)
    }

    fun addItem(character: CharacterModel){
        var anItem = ItemModel()
        var exitMenu = false        //boolean to check if menu has been exited, flipped to true upon exit confirmation
        t.println(menuHeadingStyle("================    Create New Item     ================"))
        t.println(menuHeadingStyle("While entering values, enter '-1' to return to this menu"))
        t.println(menuHeadingStyle("========================================================"))

        do {
            t.println()
            t.println(green("============   Current item attributes   ============"))
            characterView.showItem(anItem)

            val input: Int = characterView.listItemAddOptions(false)
            when(input) {
                1 -> characterView.updateItemName(anItem)
                2 -> characterView.updateItemType(anItem)
                3 -> updateItemRarity(anItem)
                4 -> characterView.updateItemDescription(anItem)
                5 -> characterView.updateItemCost(anItem)
                6 -> characterView.updateItemAttunement(anItem)
                7 -> characterView.updateItemEquip(anItem)
                8 -> {
                    //checking that all attributes have been filled out, and showing which ones haven't if needed
                    val nameEntered = anItem.name != ""
                    val typeEntered = anItem.itemType != ""
                    val rarityEntered = anItem.rarity != ""

                    if(nameEntered && typeEntered && rarityEntered) {   //if all attributes have been filled out
                        characterView.showItem(anItem)
                        val exitConfirm = t.prompt(brightBlue("Create this item?"), choices = listOf("yes", "no"))
                        if (exitConfirm == "yes") {
                            createItem(character, anItem)
                            exitMenu = true
                            t.println(green("Item created!"))
                        }
                    }else{      //if not all attributes are filled out
                        t.println(red("Error: The following character attribute fields are empty:"))
                        if (!nameEntered) t.println(red("Name"))
                        if (!typeEntered) t.println(red("Type"))
                        if (!rarityEntered) t.println(red("Rarity"))
                        t.println(red("Please fill out these attributes and try again."))
                    }
                }
                -1 -> {
                    t.println(red("Are you sure you want to cancel item creation? All work done will be lost!"))
                    val exitConfirm = t.prompt(brightBlue("Cancel item creation?"), choices = listOf("yes", "no"))
                    if (exitConfirm == "yes"){
                        t.println(rgb("#ff9393")("Aborting item creation..."))
                        exitMenu = true
                    }
                }
                else -> t.println(red("Error: Invalid option entered."))
            }
        } while (!exitMenu)
    }

    fun updateItemMenu(character: CharacterModel) {
        val itemsExist = character.items.isNotEmpty()
        if (itemsExist) {
            var searchId = characterView.getItemId()
            val aItem = searchItems(searchId, character)

            if (aItem != null) {
                t.println(menuHeadingStyle("==================    Update Item     =================="))
                t.println(menuHeadingStyle("While entering values, enter '-1' to return to this menu"))
                t.println(menuHeadingStyle("========================================================"))
                do {
                    t.println()
                    t.println(green("============   Current item attributes   ============"))
                    characterView.showItem(aItem)
                    val input: Int = characterView.listItemAddOptions(true)
                    when (input) {
                        1 -> {
                            if (characterView.updateItemName(aItem)) {
                                updateItem(character, aItem, searchId)
                                t.println(green("Name updated!"))
                            }
                        }

                        2 -> {
                            if (characterView.updateItemType(aItem)) {
                                updateItem(character, aItem, searchId)
                                t.println(green("Type updated!"))
                            }
                        }

                        3 -> {
                            if (updateItemRarity(aItem)) {
                                updateItem(character, aItem, searchId)
                                t.println(green("Rarity updated!"))
                            }
                        }

                        4 -> {
                            if (characterView.updateItemDescription(aItem)) {
                                updateItem(character, aItem, searchId)
                                t.println(green("Description updated!"))
                            }
                        }

                        5 -> {
                            if (characterView.updateItemCost(aItem)) {
                                updateItem(character, aItem, searchId)
                                t.println(green("Cost updated!"))
                            }
                        }

                        6 -> {
                            characterView.updateItemAttunement(aItem)
                            updateItem(character, aItem, searchId)
                            t.println(green("Attunement updated!"))

                        }

                        7 -> {
                            val equip = characterView.updateItemEquip(aItem)
                            updateItem(character, aItem, searchId)
                            if (equip)
                                t.println(green("Item equipped!"))
                            else
                                t.println(green("Item unequipped!"))
                        }

                        -1 -> {}        //empty function to prevent error message printing upon -1 entry
                        else -> t.println(red("Error: Invalid option entered."))
                    }
                } while (input != -1)
            } else
                t.println(red("Error: No Item with ID $searchId found"))
        }
    }

    fun deleteItem(character: CharacterModel){
        val itemsExist = character.items.isNotEmpty()
        if (itemsExist) {
            var searchId = characterView.getItemId()
            val aItem = searchItems(searchId, character)

            if (aItem != null) {
                characterView.showItem(aItem)
                t.println(red("Are you sure you want to delete this item? Deletion cannot be undone, and this item's data will be lost forever!"))
                val exitConfirm = t.prompt(brightBlue("Delete item?"), choices = listOf("yes", "no"))
                if (exitConfirm == "yes") {      //delete confirmation: must type character name to confirm deletion
                    characters.deleteItem(character, aItem)
                    t.println(rgb("#ff9393")("Item deleted."))
                    logger.info("Character Deleted : [ $aItem ]")
                }else {
                    t.println(rgb("#ff9393")("Aborting Item deletion..."))
                }
            } else {
                t.println(red("Error: No Character with ID $searchId found"))
            }
        }
    }

    fun updateItemRarity (item: ItemModel) : Boolean {
        var rarityChosen = false
        do {
            val input: Int = characterView.listItemRarites()
            when(input){
                1 -> {item.rarity = "Common"; rarityChosen = true}
                2 -> {item.rarity = "Uncommon"; rarityChosen = true}
                3 -> {item.rarity = "Rare"; rarityChosen = true}
                4 -> {item.rarity = "Very Rare"; rarityChosen = true}
                5 -> {item.rarity = "Legendary"; rarityChosen = true}
                -1 -> t.println(rgb("#ff9393")("Returning..."))
                else -> t.println(red("Error: Invalid option entered."))
            }
            t.println()
        } while (input != -1 && !rarityChosen)    //loops menu until valid selection is made (race updated) or -1 is entered
        return rarityChosen
    }

    fun createItem(character: CharacterModel, item: ItemModel){
        characters.addItemToCharacter(character, item)
        logger.info("Item Added to ${character.name} : [ $item ]")
    }

    fun updateItem( character: CharacterModel, item: ItemModel, index: Int){
        characters.updateItem(character, item, index)
        logger.info("Character Updated : [ $character ]")
        Thread.sleep(100)       //wait to prevent logger from printing in the middle of table prints
    }

    fun searchItems(id: Int, character: CharacterModel) : ItemModel? {
        if (id != -1 && id> -2 && id< character.items.size) {     //validating against index out of bounds errors
            return characters.findOneItem(character, id)
        }else if (id == -1) {       //if user request to go back
            t.println(rgb("#ff9393")("Returning..."))
        }
        return null
    }
}