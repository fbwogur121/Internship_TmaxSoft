package com.Tmax.tmaxPlugin.dto.response

import com.Tmax.tmaxPlugin.dto.message.ChatBotMessage
import com.Tmax.tmaxPlugin.dto.Refactored

data class ChatGptResponse(
    val id: String,
    var choices: List<Choice>
) {

    data class Choice(
        val index: Int,
        val message: ChatBotMessage,
        val finishReason: String
    )

    fun toRefactored(): Refactored {
        return Refactored(
            code = choices.first().message.content.substringAfter("Code:").trim('`', '\n', ' ')
        )
    }
}
