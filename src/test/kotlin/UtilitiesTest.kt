import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.setu.character.console.helpers.calculateHP
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

    @Test
    fun testCalculateHP() {
        assertEquals(10, calculateHP(1, "Bard", 14), "Incorrect max HP")             //level 1 bard, 14 con: 8 + 2 = 10     (simple case)
        assertEquals(17, calculateHP(3, "Wizard", 12), "Incorrect max HP")           //level 3 wizard, 12 con: 6 + 1 + (4+1) * 2 = 17   (level up case)
        assertEquals(345, calculateHP(20, "Barbarian", 30), "Incorrect max HP")      //level 20 barbarian, 30 con: 12 + 10 + (7+10)*19 = 345    (upper bound)
        assertEquals(3, calculateHP(3, "Sorcerer", 1), "Incorrect max HP")           //level 3 sorcerer, 1 con: 6 - 5 + [(4-5) -1 < 1? 1] * 2 = 3   (negative HP per level case - defaults to 1 hp per level)
    }
}