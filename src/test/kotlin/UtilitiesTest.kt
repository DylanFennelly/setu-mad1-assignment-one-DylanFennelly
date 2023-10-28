import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.setu.character.console.helpers.calculateMod
import org.setu.character.console.models.CharacterModel

class UtilitiesTest {

    private var characters: MutableList<CharacterModel>? = arrayListOf()

    @BeforeEach
    fun setUp() {
        characters = mutableListOf()
        characters!!.add(CharacterModel(1, "Rylanor", "Human", "Barbarian", 1, 18, 14, 16, 8, 11, 7, 15, 10, "Outlander"))

    }

    @AfterEach
    fun tearDown() {
        characters = null
    }

    @Test
    fun testCalculateMod() {
        assertEquals(3, calculateMod(16), "Incorrect ability score mod")        //standard case
        assertEquals(2, calculateMod(15), "Incorrect ability score mod")        //case with decimal
        assertEquals(-1, calculateMod(8), "Incorrect ability score mod")        //case with minus
        assertEquals(-2, calculateMod(7), "Incorrect ability score mod")        //case with minus and decimal
        assertEquals(-5, calculateMod(1), "Incorrect ability score mod")        //lower edge case
        assertEquals(10, calculateMod(30), "Incorrect ability score mod")        //upper edge case
    }
}