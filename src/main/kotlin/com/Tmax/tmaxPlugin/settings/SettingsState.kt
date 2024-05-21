package com.Tmax.tmaxPlugin.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.Tmax.secret

@Service
@State(name = "SettingsState", storages = [Storage("chatGptSettings.xml")])
class SettingsState : PersistentStateComponent<SettingsState> {
    var apiKey: String = secret.API_KEY
    var responseTimeout: Int = 10

    override fun getState(): SettingsState {
        return this
    }

    override fun loadState(state: SettingsState) {
        apiKey = state.apiKey
        responseTimeout = state.responseTimeout
    }

    fun isApiKeyNotExists(): Boolean = apiKey.isBlank()

    companion object {
        val instance: SettingsState
            get() = service()
    }
}
