package com.Tmax.refactorCodeGpt.dto.request
import com.Tmax.refactorCodeGpt.dto.Message.JavaDocMessage

data class JavaDocRequest(
    val messages: List<JavaDocMessage>,
) {
    companion object {
        fun of(fileExtension: String, code: String): JavaDocRequest =
            JavaDocRequest(messages = listOf(makePrompt(fileExtension, code)))


        private fun makePrompt(fileExtension: String, code: String): JavaDocMessage =
            JavaDocMessage(role = "user", content = """
                Refactor the following code and describe your changes in Korean only in comments within the code. Refactoring results in code that improves performance.
                This code's file extension: $fileExtension
                Here is the code:
                ```
                $code
                ```
                Respond start with the line 'Code:'
            """.trimIndent())

    }
}
