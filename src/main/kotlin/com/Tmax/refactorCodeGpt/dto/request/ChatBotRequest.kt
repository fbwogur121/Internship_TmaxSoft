package com.Tmax.refactorCodeGpt.dto.request
import com.Tmax.refactorCodeGpt.dto.Message.ChatBotMessage

data class ChatBotRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<ChatBotMessage>,
) {
    companion object {
        fun chat(text: String): ChatBotRequest =
            ChatBotRequest(messages = listOf(makeChat(text)))

        private fun makeChat(text: String): ChatBotMessage =
            ChatBotMessage(role = "user", content = """
                Your role is coding teacher.
                Here is my question: $text
                please talk to me kindly.
            """.trimIndent())

    }
}
