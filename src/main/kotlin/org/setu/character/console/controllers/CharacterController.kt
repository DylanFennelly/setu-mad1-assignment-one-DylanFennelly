package org.setu.character.console.controllers

import mu.KotlinLogging
import org.setu.character.console.helpers.calculateHP

import org.setu.character.console.models.CharacterJSONStore
import org.setu.character.console.models.CharacterModel
import org.setu.character.console.views.CharacterView

class CharacterController {

    val characters = CharacterJSONStore()
    val characterView = CharacterView()
    val logger = KotlinLogging.logger {}

    init {
        logger.info { "Launching D&D Character Creator Console App" }
        println("D&D Character Creator Kotlin App Version Alpha 0.3")
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
                -99 -> dummyData()
                -1 -> println("Exiting App")
                else -> println("Invalid Option")
            }
            println()
        } while (input != -1)
        logger.info { "Shutting Down D&D Character Creator Console App" }
    }

    fun menu() :Int { return characterView.menu() }

    fun add(){
        var aCharacter = CharacterModel()

        if (characterView.addCharacterData(aCharacter)) {
            aCharacter.maxHP = calculateHP(aCharacter.level, aCharacter.battleClass, aCharacter.con)
            characters.create(aCharacter)
        }else
            logger.info("Character Not Added: Title and/or Description was empty")
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
        if(characterView.updateCharacterLevel(character)) {
            character.maxHP = calculateHP(character.level, character.battleClass, character.con)    //level change => HP increase/decrease => recalculate HP
            characters.update(character)
            characterView.showCharacter(character)
            logger.info("Character Updated : [ $character ]")
        }
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
        var scoreUpdated = false
        var conUpdated = false
        do {
            val input: Int = characterView.listAbilityScores()
            when(input){
                1 -> scoreUpdated = characterView.updateCharacterScores(character, "str")
                2 -> scoreUpdated = characterView.updateCharacterScores(character, "dex")
                3 -> {scoreUpdated = characterView.updateCharacterScores(character, "con"); conUpdated = true}
                4 -> scoreUpdated = characterView.updateCharacterScores(character, "int")
                5 -> scoreUpdated = characterView.updateCharacterScores(character, "wis")
                6 -> scoreUpdated = characterView.updateCharacterScores(character, "cha")
                else -> println("Invalid Option")
            }
        } while (input != -1 && !scoreUpdated)

        if (scoreUpdated){
            if (conUpdated) {
                character.maxHP = calculateHP(character.level, character.battleClass, character.con)    //con update => modifier changed => recalculate HP
            }
            characters.update(character)
            characterView.showCharacter(character)
            logger.info("Character Updated : [ $character ]")
        }
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