package com.Tmax.refactorCodeGpt.api

import com.Tmax.refactorCodeGpt.dto.request.ChatGptRequest
import com.Tmax.refactorCodeGpt.dto.response.ChatGptResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

fun interface ChatGptApi {

    @POST("v1/chat/completions")
    fun refactorCode(@Body request: ChatGptRequest): Call<ChatGptResponse>

    companion object {

        fun create(): ChatGptApi =
            ChatGptApiClient
                .getClient()
                .create(ChatGptApi::class.java)
    }
}
