package io.github.calendrum.tooling.cli

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LossTest {
    private fun answer(
        codes: List<String?> = emptyList(),
        problemCount: Int? = codes.size,
        truncated: Boolean = false,
        extra: String = "",
    ): Answer {
        val problems = codes.joinToString(",") { code ->
            if (code ==
                null
            ) {
                "{}"
            } else {
                """{"code":"$code","severity":"warning","message":"m","line":1,"column":1,"offset":0}"""
            }
        }
        val count = if (problemCount == null) "" else ""","problem_count":$problemCount"""
        val raw = """{"ops_version":1,"ok":true,"result":{"problems":[$problems]$count$extra},"truncated":$truncated}"""
        return Answer(raw, Json.parseToJsonElement(raw).jsonObject)
    }

    @Test
    fun theCodesThatLoseNothingAreTheOnesOnTheList() {
        val harmless = listOf(
            "ICAL-S001", "ICAL-S002", "ICAL-S003", "ICAL-S004", "ICAL-S015", "ICAL-S016", "ICAL-S017", "ICAL-S018",
            "ICAL-S024",
        )
        for (code in harmless) assertFalse(losesData(answer(listOf(code))), code)
        assertFalse(losesData(answer(harmless)))
        assertFalse(losesData(answer()))
    }

    @Test
    fun everyOtherCodeOfReadingIsALoss() {
        val others = listOf(
            "ICAL-S005", "ICAL-S006", "ICAL-S007", "ICAL-S008", "ICAL-S009", "ICAL-S010", "ICAL-S011", "ICAL-S012",
            "ICAL-S013", "ICAL-S014", "ICAL-S019", "ICAL-S020", "ICAL-S021", "ICAL-S022", "ICAL-S023", "ICAL-S025",
            "ICAL-S026", "ICAL-S027", "ICAL-S028", "ICAL-S029", "ICAL-S030", "ICAL-V001", "ICAL-V006", "ICAL-J001",
        )
        for (code in others) {
            for (command in LOSSY_COMMANDS) assertTrue(losesData(answer(listOf(code))), "$command $code")
        }
    }

    @Test
    fun aCodeThatNobodyHasLookedAtIsALoss() {
        assertTrue(losesData(answer(listOf("ICAL-S999"))))
        assertTrue(losesData(answer(listOf("OPS-NEW"))))
        assertTrue(losesData(answer(listOf("ICAL-S003", "ICAL-S999"))))
        assertTrue(losesData(answer(listOf(null))))
        assertTrue(losesData(answer(listOf("ical-s003"))))
    }

    @Test
    fun aProblemBeyondTheListIsALossWhateverTheListHolds() {
        assertTrue(losesData(answer(listOf("ICAL-S003"), problemCount = 2)))
        assertTrue(losesData(answer(emptyList(), problemCount = 1000)))
        assertFalse(losesData(answer(listOf("ICAL-S003"), problemCount = 1)))
    }

    @Test
    fun aTruncatedAnswerIsALoss() {
        assertTrue(losesData(answer(truncated = true)))
        assertTrue(losesData(answer(truncated = true, problemCount = null)))
        assertFalse(losesData(answer(problemCount = null)))
    }

    @Test
    fun theOccurrencesOfARuleAreLostWhenTheRuleEndsOrIsNotExpanded() {
        for (code in listOf("ICAL-R003", "ICAL-R005", "ICAL-R006", "ICAL-R007", "ICAL-R008", "ICAL-R009")) {
            assertFalse(losesData(answer(listOf(code))), code)
        }
        for (code in listOf("ICAL-R001", "ICAL-R002", "ICAL-R004", "ICAL-R010")) {
            assertTrue(losesData(answer(listOf(code))), code)
        }
    }

    @Test
    fun anAnswerWithNoCalendarIsTold() {
        fun fixed(ics: String) = Answer("{}", Json.parseToJsonElement("""{"result":{"ics":"$ics"}}""").jsonObject)
        assertTrue(hasNoCalendar("fix", fixed("")))
        assertTrue(hasNoCalendar("format", fixed("")))
        assertTrue(hasNoCalendar("anonymize", fixed("")))
        assertFalse(hasNoCalendar("fix", fixed("BEGIN:VCALENDAR\\r\\nEND:VCALENDAR\\r\\n")))
        fun converted(body: String) = Answer("{}", Json.parseToJsonElement("""{"result":{$body}}""").jsonObject)
        assertTrue(hasNoCalendar("convert", converted(""""jcal":[]""")))
        assertTrue(hasNoCalendar("convert", converted(""""ics":"" """)))
        assertFalse(hasNoCalendar("convert", converted(""""jcal":[["vcalendar",[],[]]]""")))
        assertFalse(hasNoCalendar("convert", converted(""""ics":"BEGIN:VCALENDAR\\r\\nEND:VCALENDAR\\r\\n"""")))
        assertFalse(hasNoCalendar("lint", fixed("")))
        assertEquals(JsonObject(emptyMap()), JsonObject(emptyMap()))
    }
}
