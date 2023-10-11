package org.setu.character.console.controllers

import mu.KotlinLogging
import org.setu.character.console.models.PlacemarkJSONStore
import org.setu.character.console.models.PlacemarkModel
import org.setu.character.console.views.PlacemarkView

class PlacemarkController {

    //val placemarks = PlacemarkMemStore()

    val placemarks = PlacemarkJSONStore()
    val placemarkView = PlacemarkView()
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

    fun menu() :Int { return placemarkView.menu() }

    fun add(){
        var aPlacemark = PlacemarkModel()

        if (placemarkView.addPlacemarkData(aPlacemark))
            placemarks.create(aPlacemark)
        else
            logger.info("Placemark Not Added: Title and/or Description was empty")
    }

    fun list() {
        placemarkView.listPlacemarks(placemarks)
    }

    fun update() {

        placemarkView.listPlacemarks(placemarks)
        var searchId = placemarkView.getId()
        val aPlacemark = search(searchId)

        if(aPlacemark != null) {
            if(placemarkView.updatePlacemarkData(aPlacemark)) {
                placemarks.update(aPlacemark)
                placemarkView.showPlacemark(aPlacemark)
                logger.info("Placemark Updated : [ $aPlacemark ]")
            }
            else
                logger.info("Placemark Not Updated: Title and/or Description was empty")
        }
        else
            println("No Placemark with ID $searchId found")
    }

    fun delete(){
        placemarkView.listPlacemarks(placemarks)
        var searchId = placemarkView.getId()
        val aPlacemark = search(searchId)

        if(aPlacemark != null) {
                placemarks.delete(aPlacemark)
                logger.info("Placemark Deleted : [ $aPlacemark ]")
        }
        else
            println("No Placemark with ID $searchId found")
    }

    fun search() {
        placemarkView.listPlacemarks(placemarks)
        val aPlacemark = search(placemarkView.getId())
        placemarkView.showPlacemark(aPlacemark)
    }


    fun search(id: Long) : PlacemarkModel? {
        var foundPlacemark = placemarks.findOne(id)
        return foundPlacemark
    }

    fun dummyData() {
        placemarks.create(PlacemarkModel(title = "New York New York", description = "So Good They Named It Twice"))
        placemarks.create(PlacemarkModel(title= "Ring of Kerry", description = "Some place in the Kingdom"))
        placemarks.create(PlacemarkModel(title = "Waterford City", description = "You get great Blaas Here!!"))
    }
}