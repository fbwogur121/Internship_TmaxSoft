package com.Tmax.refactorCodeGpt.dto.request

data class ChatGptRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: Array<Message>,
    val maxTokens: Int = 500,
    val temperature: Int = 0
) {
    data class Message(
        val role: String = "user",
        val content: String
    )

    companion object {
        fun of(fileExtension: String, code: String): ChatGptRequest =
            ChatGptRequest(messages = arrayOf(makePrompt(fileExtension, code)))

        private fun makePrompt(fileExtension: String, code: String): Message =
            Message(content = """
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
