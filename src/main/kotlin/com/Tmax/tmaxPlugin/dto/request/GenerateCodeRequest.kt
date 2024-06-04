package com.Tmax.tmaxPlugin.dto.request
import com.Tmax.tmaxPlugin.dto.message.JavaDocMessage

data class GenerateCodeRequest(
    val messages: List<JavaDocMessage>,
) {
    companion object {
        fun of(fileExtension: String, code: String): JavaDocRequest =
            JavaDocRequest(messages = listOf(makePrompt(fileExtension, code)))

        fun cleanUpResponse(response: String): String {
            val refactoredCodeStartPattern = ":"
            val endIndex = response.lastIndexOf("}") + 1 // '}' 포함

            val startIndex = response.indexOf(refactoredCodeStartPattern)

            if (startIndex == -1 || endIndex == -1 || endIndex <= startIndex) {
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

        private fun makePrompt(fileExtension: String, code: String): JavaDocMessage =
            JavaDocMessage(content = """
                [8]{"role": "system", "content": "What is my role?"},
                [7]{"role": "user", "content": "Your role is to take a sentence with comments and generate code based on the description of the sentence. However, when answering, please do not provide any explanation other than the code. Do not create any role and content. Please just summarize the key points and answer me. Just print out the necessary information briefly. The questions are based on the programming language. When you reply, you can't print natural languages . Don't repeat the requested question in the answer. You must change the code. Never print the same code, Answer only once. Once you finish applying the information of last content, do nothing."},
                [6]{"role": "system", "content": "What language will I be dealing with?"},
                [5]{"role": "user", "content": "You are going to deal with java"},
                [4]{"role": "system", "content": "Okey what's my job?"},
                [3]{"role": "user", "content": "please generate a java code to find the number of prime numbers when given numbers from 1 to n."},
                [2]{"role": "system", "content":"
                Here is the generated code
                after :
                public class PrimeCounter {
                    public static int countPrimes(int n) {
                        if (n < 2) return 0;

                        boolean[] isPrime = new boolean[n + 1];
                        for (int i = 2; i <= n; i++) {
                            isPrime[i] = true;
                        }

                        for (int i = 2; i * i <= n; i++) {
                            if (isPrime[i]) {
                                for (int j = i * i; j <= n; j += i) {
                                    isPrime[j] = false;
                                }
                            }
                        }

                        int count = 0;
                        for (int i = 2; i <= n; i++) {
                            if (isPrime[i]) {
                                count++;
                            }
                        }

                        return count;
                    }

                    public static void main(String[] args) {
                        int n = 100;
                        System.out.println(countPrimes(n));
                    }
                }
                "},
                [1]{"role": "user", "content": "please generate Java code efficiently based on the given sentence.:
                $code
                "},
                [0]{"role": "system", "content":"
                Here is the generated code
                after:
                """.trimIndent())

    }
}
