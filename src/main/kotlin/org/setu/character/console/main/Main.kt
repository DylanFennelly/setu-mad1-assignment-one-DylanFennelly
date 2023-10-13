package org.setu.character.console.main

import mu.KotlinLogging
import org.setu.character.console.controllers.CharacterController

private val logger = KotlinLogging.logger {}
var characterController = CharacterController()

fun main(args: Array<String>){
    characterController.start()
}