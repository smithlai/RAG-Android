package com.smith.smith_rag.ui.screens.docs

import androidx.lifecycle.ViewModel
import com.smith.smith_rag.api.RagApi

import org.koin.android.annotation.KoinViewModel
import org.koin.core.context.GlobalContext

@KoinViewModel
class DocsViewModel(

) : ViewModel() {
    val ragApi: RagApi = GlobalContext.get().get()
}
