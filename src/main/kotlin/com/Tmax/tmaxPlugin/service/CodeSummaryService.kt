package com.Tmax.tmaxPlugin.service

import com.Tmax.tmaxPlugin.dto.Refactored
import com.Tmax.tmaxPlugin.dto.request.CodeSummaryRequest
import com.Tmax.tmaxPlugin.dto.response.ChatGptResponse
import com.Tmax.tmaxPlugin.exception.ChatGptAuthenticationException
import com.Tmax.tmaxPlugin.exception.ChatGptFetchFailureException
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException


class CodeSummaryService {

    //private val client = OkHttpClient()
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.MINUTES)  // 연결 타임아웃을 10분으로 설정
        .readTimeout(10, TimeUnit.MINUTES)  // 읽기 타임아웃을 10분으로 설정
        .writeTimeout(10, TimeUnit.MINUTES)  // 쓰기 타임아웃을 10분으로 설정
        .build()

    //javadoc
    fun generateSummary(fileExtension: String, code: String): Refactored =
        runCatching {
            val json = JSONObject()
            json.put("model", "mistralai/Mixtral-8x7B-Instruct-v0.1")
            json.put("prompt", CodeSummaryRequest.of(fileExtension, code).messages.first().content)
            json.put("max_new_token", 200)
            val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url("http://192.168.115.38:5009/generate-javadoc")
                .post(body)
                .addHeader("Authorization", Credentials.basic("username", "password123!@#"))
                .build()
            println("body:"+body)
            println("request:"+request)
            println("selected code:"+code)
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                response.body?.string()?.let { responseBody ->
                    val jsonResponse = JSONObject(responseBody)
                    val isSuccess = jsonResponse.getBoolean("isSuccess")
                    if (isSuccess) {
                        // 성공 응답 처리
                        val resultObject = jsonResponse.getJSONObject("result")
                        val outputText = resultObject.getString("output_text") // 예시로 'output_text'를 추출한다고 가정
                        outputText
                    } else {
                        // 실패 응답 처리
                        throw ChatGptFetchFailureException("Server returned failure response: ${jsonResponse.getString("result")}")
                    }
                } ?: throw IOException("Response body is null")
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
    private fun onRefactorSuccess(response: Response<ChatGptResponse>): Refactored =
        takeUnless { response.code() == 401 }
            ?.let { response.body()?.toRefactored() }
            ?: throw ChatGptAuthenticationException()
}
