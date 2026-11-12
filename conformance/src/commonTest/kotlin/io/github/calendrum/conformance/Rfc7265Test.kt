package io.github.calendrum.conformance

import io.github.calendrum.jcal.JCal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json as KotlinxJson

// The two examples of RFC 7265 Appendix B as printed in the RFC. Example 2 contradicts its own iCalendar input in seven
// places (VERSION/PRODID order, BYDAY, the second event's DTSTART and SUMMARY); the test pins exactly those
// differences, so any other one fails. Both printed examples also read back to the same jCal.
class Rfc7265Test {
    private fun ours(case: String): Json {
        val embedded = Suite.cases.single { it.directory == case }
        val meta = readJson(embedded.file(Suite.META)!!.bytes).value as JsonObject
        return (jcalOf(embedded.file(Suite.INPUT)!!.bytes, meta) as JsonArray).items.single()
    }

    @Test
    fun example1MatchesTheRfc() {
        assertEquals(emptyList(), differences(parseJson(EXAMPLE_1), ours("rfc7265-example-1"), "$"))
    }

    @Test
    fun example2DiffersFromTheRfcOnlyWhereTheRfcContradictsItsOwnInput() {
        assertEquals(
            listOf(
                "$[1][0][0]: \"prodid\" in the RFC, \"version\" here",
                "$[1][0][3]: \"-//Example Corp.//Example Client//EN\" in the RFC, \"2.0\" here",
                "$[1][1][0]: \"version\" in the RFC, \"prodid\" here",
                "$[1][1][3]: \"2.0\" in the RFC, \"-//Example Corp.//Example Client//EN\" here",
                "$[2][0][2][1][1][1][3].byday: \"1SU\" in the RFC, \"-1SU\" here",
                "$[2][2][1][1][3]: \"2006-01-02T14:00:00\" in the RFC, \"2006-01-04T14:00:00\" here",
                "$[2][2][1][4][3]: \"Event #2\" in the RFC, \"Event #2 bis\" here",
            ),
            differences(parseJson(EXAMPLE_2.replace("@DESCRIPTION@", DESCRIPTION)), ours("rfc7265-example-2"), "$"),
        )
    }

    @Test
    fun theExamplesOfTheRfcReadBackToTheSameJcal() {
        for (example in listOf(EXAMPLE_1, EXAMPLE_2.replace("@DESCRIPTION@", DESCRIPTION))) {
            val read = JCal.decodeFromString(example)
            assertEquals(emptyList(), read.problems)
            val written = JCal.encodeToJsonElement(read.components)
            assertEquals(KotlinxJson.parseToJsonElement("[$example]"), written)
        }
    }

    private fun differences(rfc: Json, ours: Json, path: String): List<String> = when {
        rfc is JsonArray && ours is JsonArray -> if (rfc.items.size != ours.items.size) {
            listOf("$path: ${rfc.items.size} items in the RFC, ${ours.items.size} here")
        } else {
            rfc.items.indices.flatMap { differences(rfc.items[it], ours.items[it], "$path[$it]") }
        }

        rfc is JsonObject && ours is JsonObject -> if (rfc.keys.sorted() != ours.keys.sorted()) {
            listOf("$path: keys ${rfc.keys.sorted()} in the RFC, ${ours.keys.sorted()} here")
        } else {
            rfc.keys.flatMap { differences(rfc[it]!!, ours[it]!!, "$path.$it") }
        }

        rfc == ours -> emptyList()

        else -> listOf("$path: ${rfc.toCompactJson()} in the RFC, ${ours.toCompactJson()} here")
    }

    private companion object {
        const val DESCRIPTION = "We are having a meeting all this week at 12 pm for one hour, with an additional " +
            "meeting on the first day 2 hours long.\\nPlease bring your own lunch for the 12 pm meetings."

        const val EXAMPLE_1 = """
            ["vcalendar",
              [
                ["calscale", {}, "text", "GREGORIAN"],
                ["prodid", {}, "text", "-//Example Inc.//Example Calendar//EN"],
                ["version", {}, "text", "2.0"]
              ],
              [
                ["vevent",
                  [
                    ["dtstamp", {}, "date-time", "2008-02-05T19:12:24Z"],
                    ["dtstart", {}, "date", "2008-10-06"],
                    ["summary", {}, "text", "Planning meeting"],
                    ["uid", {}, "text", "4088E990AD89CB3DBB484909"]
                  ],
                  []
                ]
              ]
            ]
        """

        const val EXAMPLE_2 = """
            ["vcalendar",
              [
                ["prodid", {}, "text", "-//Example Corp.//Example Client//EN"],
                ["version", {}, "text", "2.0"]
              ],
              [
                ["vtimezone",
                  [
                    ["last-modified", {}, "date-time", "2004-01-10T03:28:45Z"],
                    ["tzid", {}, "text", "US/Eastern"]
                  ],
                  [
                    ["daylight",
                      [
                        ["dtstart", {}, "date-time", "2000-04-04T02:00:00"],
                        ["rrule",
                          {},
                          "recur",
                          {
                            "freq": "YEARLY",
                            "byday": "1SU",
                            "bymonth": 4
                          }
                        ],
                        ["tzname", {}, "text", "EDT"],
                        ["tzoffsetfrom", {}, "utc-offset", "-05:00"],
                        ["tzoffsetto", {}, "utc-offset", "-04:00"]
                      ],
                      []
                    ],
                    ["standard",
                      [
                        ["dtstart", {}, "date-time", "2000-10-26T02:00:00"],
                        ["rrule",
                          {},
                          "recur",
                          {
                            "freq": "YEARLY",
                            "byday": "1SU",
                            "bymonth": 10
                          }
                        ],
                        ["tzname", {}, "text", "EST"],
                        ["tzoffsetfrom", {}, "utc-offset", "-04:00"],
                        ["tzoffsetto", {}, "utc-offset", "-05:00"]
                      ],
                      []
                    ]
                  ]
                ],
                ["vevent",
                  [
                    ["dtstamp", {}, "date-time", "2006-02-06T00:11:21Z"],
                    ["dtstart",
                      { "tzid": "US/Eastern" },
                      "date-time",
                      "2006-01-02T12:00:00"
                    ],
                    ["duration", {}, "duration", "PT1H"],
                    ["rrule", {}, "recur", { "freq": "DAILY", "count": 5 } ],
                    ["rdate",
                      { "tzid": "US/Eastern" },
                      "period",
                      "2006-01-02T15:00:00/PT2H"
                    ],
                    ["summary", {}, "text", "Event #2"],
                    ["description",
                     {},
                     "text",
                     "@DESCRIPTION@"
                    ],
                    ["uid", {}, "text", "00959BC664CA650E933C892C@example.com"]
                  ],
                  []
                ],
                ["vevent",
                  [
                    ["dtstamp", {}, "date-time", "2006-02-06T00:11:21Z"],
                    ["dtstart",
                      { "tzid": "US/Eastern" },
                      "date-time",
                      "2006-01-02T14:00:00"
                    ],
                    ["duration", {}, "duration", "PT1H"],
                    ["recurrence-id",
                      { "tzid": "US/Eastern" },
                      "date-time",
                      "2006-01-04T12:00:00"
                    ],
                    ["summary", {}, "text", "Event #2"],
                    ["uid", {}, "text", "00959BC664CA650E933C892C@example.com"]
                  ],
                  []
                ]
              ]
            ]
        """
    }
}
