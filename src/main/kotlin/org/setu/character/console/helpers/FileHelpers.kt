package org.setu.character.console.helpers

import mu.KotlinLogging
import java.io.*

val logger = KotlinLogging.logger {}

fun write( fileName: String, data: String) {
    try {
        File(fileName).writeText(data)
    } catch (e: Exception) {
        logger.error { "Cannot read file: " + e.toString() }
    }
}

fun read(path: String): String {
    val file = File(path)
    try {
        val lines = mutableListOf<String>()
        file.forEachLine { lines.add(it) }
        return lines.joinToString(separator = "\n")
    } catch (e: FileNotFoundException) {
        logger.error { "Cannot Find file: " + e.toString() }
    } catch (e: IOException) {
        logger.error { "Cannot Read file: " + e.toString() }
    }
    return "{}"
}

fun exists(fileName: String): Boolean {
    val file = File(fileName)
    return file.exists()
}