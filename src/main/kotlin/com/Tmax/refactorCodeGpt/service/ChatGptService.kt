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
import java.util.concurrent.TimeUnit

class ChatGptService {

    //private val client = OkHttpClient()
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.MINUTES)  // 연결 타임아웃을 10분으로 설정
        .readTimeout(10, TimeUnit.MINUTES)  // 읽기 타임아웃을 10분으로 설정
        .writeTimeout(10, TimeUnit.MINUTES)  // 쓰기 타임아웃을 10분으로 설정
        .build()

    fun refactorCode(fileExtension: String, code: String): Refactored =
        runCatching {
            val json = JSONObject()
            json.put("model", "mistralai/Mixtral-8x7B-Instruct-v0.1")
            json.put("load_in_4bit", "True")
            json.put("prompt", ChatGptRequest.of(fileExtension, code).messages.first().content)
            json.put("pad_token_id", 0)
            json.put("eos_token_id", 1)
            json.put("max_length", 1)
            val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url("http://192.168.115.38:5000/generate-text")
                .post(body)
                .addHeader("Authorization", Credentials.basic("username", "password123!@#"))
                .build()
            println("body:"+body)
            println("request:"+request)
            println("selected code:"+code)
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                response.body?.string()?.let { responseBody ->
                    println(responseBody)
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
