package com.Tmax.refactorCodeGpt.dto.response

import com.Tmax.refactorCodeGpt.dto.Refactored

data class ChatGptResponse(
    val id: String,
    var choices: List<Choice>
) {

    data class Choice(
        val index: Int,
        val text: String,
        val finishReason: String
    )

    fun toRefactored(): Refactored {
        return Refactored(
            code = choices.first().text.substringAfter("Code:").trim('`', '\n', ' ')
        )
    }
}
