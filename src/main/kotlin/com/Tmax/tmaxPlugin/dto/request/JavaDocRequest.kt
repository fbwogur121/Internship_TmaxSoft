package com.Tmax.tmaxPlugin.dto.request
import com.Tmax.tmaxPlugin.dto.message.JavaDocMessage

data class JavaDocRequest(
    val messages: List<JavaDocMessage>,
) {
    companion object {
        fun of(fileExtension: String, code: String): JavaDocRequest =
            JavaDocRequest(messages = listOf(makePrompt(fileExtension, code)))

        fun cleanUpResponse(response: String): String {
            // 응답 문자열에서 JavaDoc 주석과 코드 이후의 불필요한 문자열을 제거합니다.
            // 예시로, 마지막에 나타나는 특정 패턴("}") 이후의 내용을 제거합니다.
            val lastIndex = response.lastIndexOf("}")
            return if (lastIndex != -1) {
                response.substring(0, lastIndex + 1)
            } else {
                response // 마지막 "}"를 찾지 못한 경우, 응답을 그대로 반환
            }
        }

        private fun makePrompt(fileExtension: String, code: String): JavaDocMessage =
            JavaDocMessage(content = """
                [8]{"role": "system", "content": "What is my role?"},
                [7]{"role": "user", "content": "Your role is to generate JavaDoc comments for the given code without altering the code itself. Please provide detailed and comprehensive documentation that describes the purpose and functionality of the code. Include descriptions for any parameters, return values, and exceptions. After generating the JavaDoc comments, return the comments followed by the original code."},
                [6]{"role": "system", "content": "What language will I be dealing with?"},
                [5]{"role": "user", "content": "You are going to deal with $fileExtension."},
                [4]{"role": "system", "content": "Okey, what's my job?"},
                [3]{"role": "user", "content": "Please generate JavaDoc comments for the following code and return the comments followed by the original code:
                $code
                "},
                [2]{"role": "system", "content":"Your JavaDoc should start with /** and end with */. Remember to return the original code unmodified after the JavaDoc comments."},
                [1]{"role": "user", "content": "Remember to describe all aspects of the code, including its overall purpose, parameters, return values, and any thrown exceptions. Be as descriptive and precise as possible."}
                """.trimIndent())

    }
}
