package com.smith.smith_rag.ui.screens.edit_api_key

import androidx.lifecycle.ViewModel
import com.smith.smith_rag.data.GeminiAPIKey
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class EditAPIKeyViewModel(
    private val geminiAPIKey: GeminiAPIKey,
) : ViewModel() {
    fun getAPIKey(): String? = geminiAPIKey.getAPIKey()

    fun saveAPIKey(apiKey: String) {
        geminiAPIKey.saveAPIKey(apiKey)
    }
}
