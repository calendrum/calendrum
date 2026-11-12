package io.github.calendrum.tooling.ops

internal fun explain(request: Request, run: Run) {
    val code = checkToken(request.requiredText("code"), "code").uppercase()
    val row = PROBLEM_CODES.firstOrNull { it[0] == code } ?: throw OpsFailure(UNKNOWN_CODE, "There is no such code")
    val out = run.out
    out.beginObject()
    out.field("code", row[0])
    out.field("severity", row[1].lowercase())
    out.field("constant", row[2])
    out.field("meaning", row[3])
    out.endObject()
}

/** The fixed sentence for a problem code, from the public guide. */
internal fun meaningOf(code: String): String = PROBLEM_CODES.firstOrNull { it[0] == code }?.get(3) ?: code
