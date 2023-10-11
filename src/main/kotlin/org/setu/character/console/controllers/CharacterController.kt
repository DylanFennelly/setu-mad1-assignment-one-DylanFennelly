package org.setu.character.console.controllers

import mu.KotlinLogging
import org.setu.character.console.models.CharacterJSONStore
import org.setu.character.console.models.CharacterModel
import org.setu.character.console.views.CharacterView

class CharacterController {

    //val placemarks = CharacterMemStore()

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

        if (characterView.addPlacemarkData(aCharacter))
            characters.create(aCharacter)
        else
            logger.info("Character Not Added: Title and/or Description was empty")
    }

    fun list() {
        characterView.listPlacemarks(characters)
    }

    fun update() {

        characterView.listPlacemarks(characters)
        var searchId = characterView.getId()
        val aCharacter = search(searchId)

        if(aCharacter != null) {
            if(characterView.updatePlacemarkData(aCharacter)) {
                characters.update(aCharacter)
                characterView.showPlacemark(aCharacter)
                logger.info("Character Updated : [ $aCharacter ]")
            }
            else
                logger.info("Character Not Updated: Title and/or Description was empty")
        }
        else
            println("No Character with ID $searchId found")
    }

    fun delete(){
        characterView.listPlacemarks(characters)
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
        characterView.listPlacemarks(characters)
        val aCharacter = search(characterView.getId())
        characterView.showPlacemark(aCharacter)
    }


    fun search(id: Long) : CharacterModel? {
        var foundPlacemark = characters.findOne(id)
        return foundPlacemark
    }

    fun dummyData() {
        characters.create(CharacterModel(title = "New York New York", description = "So Good They Named It Twice"))
        characters.create(CharacterModel(title= "Ring of Kerry", description = "Some place in the Kingdom"))
        characters.create(CharacterModel(title = "Waterford City", description = "You get great Blaas Here!!"))
    }
}