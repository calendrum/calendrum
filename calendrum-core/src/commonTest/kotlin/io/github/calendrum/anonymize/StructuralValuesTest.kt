package io.github.calendrum.anonymize

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// What passes as structural is read from explicit lists, and a word that only has the right shape is text.
class StructuralValuesTest {
    @Test
    fun languageTagsWithoutVariants() {
        for (tag in listOf("de", "de-CH", "zh-Hant-TW", "sr-Latn", "es-419", "ru")) {
            assertTrue(StructuralValues.parameter("LANGUAGE", tag), tag)
        }
        for (tag in listOf("de-zorgwin", "en-US-x-clinic", "Anna", "de-Zorg", "de-CH-1996", "")) {
            assertFalse(StructuralValues.parameter("LANGUAGE", tag), tag)
        }
    }

    @Test
    fun mediaTypesOfTheList() {
        for (type in listOf("text/html", "IMAGE/PNG", "application/pdf", "application/vnd.ms-excel")) {
            assertTrue(StructuralValues.parameter("FMTTYPE", type), type)
        }
        for (type in listOf("application/vnd.zorgwin-clinic", "text/anna", "image")) {
            assertFalse(StructuralValues.parameter("FMTTYPE", type), type)
        }
    }

    @Test
    fun vendorValuesOfTheList() {
        for (value in listOf("BUSY", "OOF", "TRUE", "0", "PT1H", "#FF2968", "20260907T090000Z", "automatic")) {
            assertTrue(StructuralValues.extension(value), value)
        }
        for (value in listOf("ZORGWIN", "IVANOVA-ANNA", "Busy room 4")) {
            assertFalse(StructuralValues.extension(value), value)
        }
    }

    @Test
    fun zoneAbbreviationsInUpperCase() {
        for (value in listOf("CET", "CEST", "MSK", "ACWST", "+03", "-0330", "GMT+3", "UTC-05:00")) {
            assertTrue(StructuralValues.property("TZNAME", value), value)
        }
        for (value in listOf("Anna", "Clinic", "ZORGWIN", "cet", "Moscow Standard Time")) {
            assertFalse(StructuralValues.property("TZNAME", value), value)
        }
    }

    @Test
    fun producersWithTheWordsOfAProduct() {
        for (value in listOf("-//Google Inc//Google Calendar 70.9054//EN", "Microsoft Exchange Server 2010")) {
            assertTrue(StructuralValues.property("PRODID", value), value)
        }
        for (value in listOf(
            "-//Steamship Lines//EN",
            "-//Google//Zorgwin//EN",
            "-//Calendar//EN",
            "-//Yandex\u0007//EN",
        )) {
            assertFalse(StructuralValues.property("PRODID", value), value)
        }
    }
}
