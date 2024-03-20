package com.Tmax.refactorCodeGpt.service

import com.Tmax.refactorCodeGpt.api.ChatGptApi
import com.Tmax.refactorCodeGpt.dto.Refactored
import com.Tmax.refactorCodeGpt.dto.request.ChatGptRequest
import com.Tmax.refactorCodeGpt.dto.response.ChatGptResponse
import com.Tmax.refactorCodeGpt.exception.ChatGptAuthenticationException
import com.Tmax.refactorCodeGpt.exception.ChatGptFetchFailureException
import retrofit2.Response
import java.net.SocketTimeoutException

class ChatGptService(
    private val chatGptApi: ChatGptApi = ChatGptApi.create()
) {

    fun refactorCode(fileExtension: String, code: String): Refactored =
        runCatching {
            ChatGptRequest.of(fileExtension, code)
                .let { chatGptApi.refactorCode(it).execute() }
        }.fold(
            onSuccess = { response -> onRefactorSuccess(response) },
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
