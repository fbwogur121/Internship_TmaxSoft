//package com.Tmax.refactorCodeGpt.service
//
//import com.Tmax.refactorCodeGpt.api.ChatGptApi
//import com.Tmax.refactorCodeGpt.dto.Refactored
//import com.Tmax.refactorCodeGpt.dto.request.ChatGptRequest
//import com.Tmax.refactorCodeGpt.dto.response.ChatGptResponse
//import com.Tmax.refactorCodeGpt.exception.ChatGptAuthenticationException
//import com.Tmax.refactorCodeGpt.exception.ChatGptFetchFailureException
//import retrofit2.Response
//import java.net.SocketTimeoutException
//
//class ChatGptService(
//    private val chatGptApi: ChatGptApi = ChatGptApi.create()
//) {
//
//    fun refactorCode(fileExtension: String, code: String): Refactored =
//        runCatching {
//            ChatGptRequest.of(fileExtension, code)
//                .let { chatGptApi.refactorCode(it).execute() }
//        }.fold(
//            onSuccess = { response -> onRefactorSuccess(response) },
//            onFailure = { exception ->
//                if (exception is SocketTimeoutException) {
//                    throw ChatGptFetchFailureException("timeout error.\nPlease check your network or set longer timeout in settings.")
//                }
//                throw ChatGptFetchFailureException(exception.message)
//            }
//        )
//
//    private fun onRefactorSuccess(response: Response<ChatGptResponse>): Refactored =
//        takeUnless { response.code() == 401 }
//            ?.let { response.body()?.toRefactored() }
//            ?: throw ChatGptAuthenticationException()
//}

package com.Tmax.refactorCodeGpt.service

import com.Tmax.refactorCodeGpt.dto.Refactored
import com.Tmax.refactorCodeGpt.dto.request.ChatGptRequest
import com.Tmax.refactorCodeGpt.dto.response.ChatGptResponse
import com.Tmax.refactorCodeGpt.exception.ChatGptAuthenticationException
import com.Tmax.refactorCodeGpt.exception.ChatGptFetchFailureException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Credentials
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.net.SocketTimeoutException
import java.io.IOException

class ChatGptService {

    private val client = OkHttpClient()

    fun refactorCode(fileExtension: String, code: String): Refactored =
        runCatching {
            val json = JSONObject()
            json.put("model", "codellama/CodeLlama-7b-Python-hf")
            json.put("prompt", ChatGptRequest.makePrompt(fileExtension, code).content)
            json.put("pad_token_id", 0)
            json.put("eos_token_id", 1)
            json.put("max_length", 500)

            val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url("http://192.168.115.38:5000/generate-code")
                .post(body)
                .addHeader("Authorization", Credentials.basic("username", "password123!@#"))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                response.body?.string()?.let { responseBody ->
                    JSONObject(responseBody).getString("generated_text")
                }
            }
        }.fold(
            onSuccess = { refactoredCode -> Refactored(refactoredCode ?: "") },
            onFailure = { exception ->
                if (exception is SocketTimeoutException) {
                    throw ChatGptFetchFailureException("timeout error.\nPlease check your network or set longer timeout in settings.")
                }
                throw ChatGptFetchFailureException(exception.message)
            }
        )
}
