package com.Tmax.tmaxPlugin.dto.request
import com.Tmax.tmaxPlugin.dto.message.CodeSummaryMessage

data class CodeSummaryRequest(
    val messages: List<CodeSummaryMessage>,
) {
    companion object {
        fun of(fileExtension: String, code: String): CodeSummaryRequest =
            CodeSummaryRequest(messages = listOf(makePrompt(fileExtension, code)))

//        fun cleanUpResponse(response: String): String {
//            // 응답 문자열에서 JavaDoc 주석과 코드 이후의 불필요한 문자열을 제거합니다.
//            // 예시로, 마지막에 나타나는 특정 패턴("}") 이후의 내용을 제거합니다.
//            val lastIndex = response.lastIndexOf("}")
//            return if (lastIndex != -1) {
//                response.substring(0, lastIndex + 1)
//            } else {
//                response // 마지막 "}"를 찾지 못한 경우, 응답을 그대로 반환
//            }
//        }

        private fun makePrompt(fileExtension: String, code: String): CodeSummaryMessage =
            CodeSummaryMessage(content = """
                [8]{"role": "system", "content": "What is my role?"},
                [7]{"role": "user", "content": "Your role is to generate code summary comments for the given code. Please provide detailed and comprehensive documentation that describes the purpose and functionality of the code. Include descriptions for any parameters, return values, and exceptions. After generating the comments, return the comments followed by the original code."},
                [6]{"role": "system", "content": "What language will I be dealing with?"},
                [5]{"role": "user", "content": "You are going to deal with Korean and English"},
                [4]{"role": "system", "content": "Okey, what's my job?"},

                [3]{"role": "user", "content": "Please generate codeSummary comments for the following code:
                public List<Integer> findPrimeNumbers(int limit) {
                    List<Integer> primeNumbers = new ArrayList<>();

                    for (int i = 2; i <= limit; i++) {
                        boolean isPrime = true;
                        for (int j = 2; j < i; j++) {
                            if (i % j == 0) {
                                isPrime = false;
                                break;
                            }
                        }

                        if (isPrime) {
                            primeNumbers.add(i);
                        }
                    }

                    return primeNumbers;
                }
                "},
                [2]{"role": "system", "content":"
                Here is the generated code summary
                after :
                <English Explanation>
                The findPrimeNumbers method is designed to find and return a list of prime numbers up to a specified limit. The steps of the method are as follows:
                1. Initialization: A List<Integer> named primeNumbers is initialized to store the prime numbers.
                2. Outer Loop: A for loop runs from 2 to the given limit (inclusive). This loop checks each number to see if it is prime.
                3. Prime Check: For each number i, assume it is prime by setting isPrime to true.
                4. Inner Loop: Another for loop runs from 2 to i-1. This loop checks if i is divisible by any number between 2 and i-1.
                - If i is divisible by any j, set isPrime to false and break out of the inner loop.
                5. Adding Prime: If isPrime remains true after the inner loop, i is a prime number and is added to the primeNumbers list.
                6. Return: After the loops complete, the list primeNumbers containing all prime numbers up to the limit is returned.
                "},

                [1]{"role": "user", "content": "
                Please generate codeSummary comments for the following code:
                $code
                "},
                [0]{"role": "system", "content":"
                Here is the generated codeSummary comments
                after:
                """.trimIndent())

    }
}
