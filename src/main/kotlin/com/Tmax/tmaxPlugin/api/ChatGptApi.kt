package com.Tmax.tmaxPlugin.api

import com.Tmax.tmaxPlugin.dto.request.ChatBotRequest
import com.Tmax.tmaxPlugin.dto.response.ChatGptResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

fun interface ChatGptApi {

    @POST("v1/chat/completions")
    fun refactorCode(@Body request: ChatBotRequest): Call<ChatGptResponse>

    companion object {

        fun create(): ChatGptApi =
            ChatGptApiClient
                .getClient()
                .create(ChatGptApi::class.java)
    }
}
