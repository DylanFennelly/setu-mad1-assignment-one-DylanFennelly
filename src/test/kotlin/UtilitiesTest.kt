import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.setu.character.console.helpers.*
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
        assertEquals(0, calculateHP(1, "Invalid", 10), "Incorrect max HP")           //Level 1 no class, 10 con = 0     (for if calculation is done before class assigned)
        assertEquals(19, calculateHP(20, "Invalid", 10), "Incorrect max HP")         //Level 20 no class, 10 con = 0 + 1*19 = 19     (if level up would give 0 hp, defaults to 1)
    }

    @Test
    fun testValidateByteToNum(){
        assertTrue(validateByteToNum("12", 0, 127))     //standard case
        assertTrue(validateByteToNum("0",0, 127))       //input same as lower - true
        assertTrue(validateByteToNum("10", 0, 10))      //input same as upper - true
        assertFalse(validateByteToNum("", 0 ,127))      //empty string - false
        assertFalse(validateByteToNum("d12",0,127))     //invalid byte - false
        assertFalse(validateByteToNum("-10", 0, 127))   //under lower - false
        assertFalse(validateByteToNum("50", 0, 30))     //over upper - false

        assertTrue(validateByteToNum("15"))                               //standard case
        assertFalse(validateByteToNum(""))                                 //empty string - false
        assertFalse(validateByteToNum("d12"))
    }

    @Test
    fun testValidateShortToNum(){
        assertTrue(validateShortToNum("12", 0, 127))     //standard case
        assertTrue(validateShortToNum("0",0, 127))       //input same as lower - true
        assertTrue(validateShortToNum("10", 0, 10))      //input same as upper - true
        assertFalse(validateShortToNum("", 0 ,127))      //empty string - false
        assertFalse(validateShortToNum("d12",0,127))     //invalid byte - false
        assertFalse(validateShortToNum("-10", 0, 127))   //under lower - false
        assertFalse(validateShortToNum("50", 0, 30))     //over upper - false

        assertTrue(validateShortToNum("15"))                               //standard case
        assertFalse(validateShortToNum(""))                                 //empty string - false
        assertFalse(validateShortToNum("d12"))
    }

    @Test
    fun testValidateUIntToNum(){
        assertTrue(validateUIntToNum("12", 0, 127))     //standard case
        assertTrue(validateUIntToNum("0",0, 127))       //input same as lower - true
        assertTrue(validateUIntToNum("10", 0, 10))      //input same as upper - true
        assertFalse(validateUIntToNum("", 0 ,127))      //empty string - false
        assertFalse(validateUIntToNum("d12",0,127))     //invalid uint - false
        assertFalse(validateUIntToNum("2", 5, 127))   //under lower - false
        assertFalse(validateUIntToNum("50", 0, 30))     //over upper - false
        assertFalse(validateUIntToNum("-2", -10, 127))     //signed int - false

        assertTrue(validateUIntToNum("15"))                               //standard case
        assertFalse(validateUIntToNum(""))                                 //empty string - false
        assertFalse(validateUIntToNum("d12"))                               //invalid uint - false
        assertFalse(validateUIntToNum("-2"))                                //signed int - false
    }
}