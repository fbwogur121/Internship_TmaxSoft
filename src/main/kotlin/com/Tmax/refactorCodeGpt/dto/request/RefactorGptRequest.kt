package com.Tmax.refactorCodeGpt.dto.request
import com.Tmax.refactorCodeGpt.dto.message.RefactorGptMessage

data class RefactorGptRequest(
    val messages: List<RefactorGptMessage>,
) {
    companion object {
        fun of(fileExtension: String, code: String): RefactorGptRequest =
            RefactorGptRequest(messages = listOf(makePrompt(fileExtension, code)))

        fun applyCommentToResponse(response: String): String {
            // 예시 응답에서 "refactored code" 부분을 식별하는 패턴
            val refactoredCodeStartPattern = ":"
            // refactored code의 끝을 나타내는 패턴을: 가장 마지막 '}'의 위치
            val endIndex = response.lastIndexOf("}") + 1 // '}' 포함

            // 시작 패턴을 기준으로 refactored code의 시작 위치 찾기
            val startIndex = response.indexOf(refactoredCodeStartPattern)

            if (startIndex == -1 || endIndex == -1 || endIndex <= startIndex) {
                // refactored code 패턴을 찾지 못한 경우, 전체를 주석 처리
                return response.lines().joinToString("\n") { "// $it" }
            }

            // refactored code 이전 부분 주석 처리
            val beforeRefactored = response.substring(0, startIndex).lines().joinToString("\n") { "// $it" }
            // refactored code 부분
            val refactoredCode = response.substring(startIndex, endIndex)
            // refactored code 이후 부분 주석 처리
            val afterRefactored = if (endIndex < response.length) response.substring(endIndex).lines().joinToString("\n") { "// $it" } else ""

            return beforeRefactored + refactoredCode + afterRefactored
        }


        private fun makePrompt(fileExtension: String, code: String): RefactorGptMessage =
            RefactorGptMessage(content = """
                [8]{"role": "system", "content": "What is my role?"},
                [7]{"role": "user", "content": "Your role is to refactor the code efficiently and comment out all words except refactored code with the code you received. Do not create any role and content. Please just summarize the key points and answer me. Just print out the necessary information briefly. The questions are based on the programming language. When you reply, you can't print natural languages . Don't repeat the requested question in the answer. You must change the code. Never print the same code, Answer only once. Once you finish applying the information of last content, do nothing."},
                [6]{"role": "system", "content": "What language will I be dealing with?"},
                [5]{"role": "user", "content": "You are going to deal with $fileExtension"},
                [4]{"role": "system", "content": "Okey what's my job?"},
                [3]{"role": "user", "content": "please refactor this code efficiently and Comment out all words except refactored code.
                public class Calculator {
                    public int calculateTotal(int[] numbers) {
                        int total = 0;
                        for (int i = 0; i < numbers.length; i++) {
                            total += numbers[i];
                        }
                        return total;
                        }
                    }
                "},
                [2]{"role": "system", "content":"
                Here is the refactored code
                after :
                public class Calculator {
                    public int calculateTotal(int[] numbers) {
                        return Arrays.stream(numbers).sum();
                    }
                "},
                [1]{"role": "user", "content": "please refactor this code efficiently :
                $code
                "},
                [0]{"role": "system", "content":"
                Here is the refactored code
                after:
                """.trimIndent())
    }
}
