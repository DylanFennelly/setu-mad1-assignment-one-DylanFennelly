package org.setu.character.console.main

import mu.KotlinLogging
import org.setu.character.console.controllers.PlacemarkController

private val logger = KotlinLogging.logger {}
var placemarkController = PlacemarkController()

fun main(args: Array<String>){
    placemarkController.start()
}