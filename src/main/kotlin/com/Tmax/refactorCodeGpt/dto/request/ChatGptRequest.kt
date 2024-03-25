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
                Refactoring has the effect of improving the readability of the code and reducing its complexity, and these benefits are derived from a simpler, cleaner, or more expressive internal architecture or object model to improve the maintainability of the source code and improve scalability. allows you to create
                Refactor the following code for better readability and maintainability.
                However, you must restructure the code without changing the results.
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
