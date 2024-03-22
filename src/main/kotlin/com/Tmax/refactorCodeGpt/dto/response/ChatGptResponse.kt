package com.Tmax.refactorCodeGpt.dto.response

import com.Tmax.refactorCodeGpt.dto.Message
import com.Tmax.refactorCodeGpt.dto.Refactored

data class ChatGptResponse(
    val id: String,
    var choices: List<Choice>
) {

    data class Choice(
        val index: Int,
        val message: Message,
        val finishReason: String
    )

    fun toRefactored(): Refactored {
        return Refactored(
            code = choices.first().message.content.substringAfter("Code:").trim('`', '\n', ' ')
        )
    }
}
