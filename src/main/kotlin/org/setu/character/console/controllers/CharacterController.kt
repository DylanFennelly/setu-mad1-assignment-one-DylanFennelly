package org.setu.character.console.controllers

import mu.KotlinLogging
import org.setu.character.console.models.CharacterJSONStore
import org.setu.character.console.models.CharacterModel
import org.setu.character.console.views.CharacterView

class CharacterController {

    //val placemarks = CharacterMemStore()

    val placemarks = CharacterJSONStore()
    val characterView = CharacterView()
    val logger = KotlinLogging.logger {}

    init {
        logger.info { "Launching Placemark Console App" }
        println("Placemark Kotlin App Version 1.0")
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
        logger.info { "Shutting Down Placemark Console App" }
    }

    fun menu() :Int { return characterView.menu() }

    fun add(){
        var aPlacemark = CharacterModel()

        if (characterView.addPlacemarkData(aPlacemark))
            placemarks.create(aPlacemark)
        else
            logger.info("Placemark Not Added: Title and/or Description was empty")
    }

    fun list() {
        characterView.listPlacemarks(placemarks)
    }

    fun update() {

        characterView.listPlacemarks(placemarks)
        var searchId = characterView.getId()
        val aPlacemark = search(searchId)

        if(aPlacemark != null) {
            if(characterView.updatePlacemarkData(aPlacemark)) {
                placemarks.update(aPlacemark)
                characterView.showPlacemark(aPlacemark)
                logger.info("Placemark Updated : [ $aPlacemark ]")
            }
            else
                logger.info("Placemark Not Updated: Title and/or Description was empty")
        }
        else
            println("No Placemark with ID $searchId found")
    }

    fun delete(){
        characterView.listPlacemarks(placemarks)
        var searchId = characterView.getId()
        val aPlacemark = search(searchId)

        if(aPlacemark != null) {
                placemarks.delete(aPlacemark)
                logger.info("Placemark Deleted : [ $aPlacemark ]")
        }
        else
            println("No Placemark with ID $searchId found")
    }

    fun search() {
        characterView.listPlacemarks(placemarks)
        val aPlacemark = search(characterView.getId())
        characterView.showPlacemark(aPlacemark)
    }


    fun search(id: Long) : CharacterModel? {
        var foundPlacemark = placemarks.findOne(id)
        return foundPlacemark
    }

    fun dummyData() {
        placemarks.create(CharacterModel(title = "New York New York", description = "So Good They Named It Twice"))
        placemarks.create(CharacterModel(title= "Ring of Kerry", description = "Some place in the Kingdom"))
        placemarks.create(CharacterModel(title = "Waterford City", description = "You get great Blaas Here!!"))
    }
}