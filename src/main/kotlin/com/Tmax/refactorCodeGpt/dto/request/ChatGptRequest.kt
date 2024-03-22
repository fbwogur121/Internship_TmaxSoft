package com.Tmax.refactorCodeGpt.dto.request

import com.Tmax.refactorCodeGpt.dto.Message

data class ChatGptRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>,
) {
    companion object {
        fun of(fileExtension: String, code: String): ChatGptRequest =
            ChatGptRequest(messages = listOf(makePrompt(fileExtension, code)))

        private fun makePrompt(fileExtension: String, code: String): Message =
            Message(role = "user", content = """
                You role is perfect code refactoring prompt.
                Refactor the following code for better readability and maintainability.
                Don't say ANY explain. Just response the code strictly.
                This code's file extension: $fileExtension
                Here is the code:
                ```
                $code
                ```

                Respond start with the line 'Code:'
            """.trimIndent())
    }
}
