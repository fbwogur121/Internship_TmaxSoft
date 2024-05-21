package com.Tmax.tmaxPlugin.service

import com.Tmax.tmaxPlugin.api.ChatGptApi
import com.Tmax.tmaxPlugin.dto.Refactored
import com.Tmax.tmaxPlugin.dto.request.ChatBotRequest
import com.Tmax.tmaxPlugin.dto.response.ChatGptResponse
import com.Tmax.tmaxPlugin.exception.ChatGptAuthenticationException
import com.Tmax.tmaxPlugin.exception.ChatGptFetchFailureException
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import retrofit2.Response

class ChatBotService {

    //private val client = OkHttpClient()
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.MINUTES)  // 연결 타임아웃을 10분으로 설정
        .readTimeout(10, TimeUnit.MINUTES)  // 읽기 타임아웃을 10분으로 설정
        .writeTimeout(10, TimeUnit.MINUTES)  // 쓰기 타임아웃을 10분으로 설정
        .build()
    private val chatGptApi: ChatGptApi = ChatGptApi.create()


    //chatbot
    fun chatWithGpt(userInput: String): String {
        val chatGptRequest = ChatBotRequest.chat(userInput)
        val response = chatGptApi.refactorCode(chatGptRequest).execute()

        if (response.isSuccessful) {
            return response.body()?.toRefactored()?.code ?: "No response from GPT"
        } else {
            val errorBody = response.errorBody()
            val errorMessage = if (errorBody != null) {
                errorBody.string()
            } else {
                "Unknown error"
            }
            throw ChatGptFetchFailureException("Failed to chat with GPT: $errorMessage")
        }
    }
    private fun onRefactorSuccess(response: Response<ChatGptResponse>): Refactored =
        takeUnless { response.code() == 401 }
            ?.let { response.body()?.toRefactored() }
            ?: throw ChatGptAuthenticationException()
}
