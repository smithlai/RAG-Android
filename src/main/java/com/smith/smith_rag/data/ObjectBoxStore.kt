package com.smith.smith_rag.data

import android.content.Context
import com.smith.smith_rag.BuildConfig
import io.objectbox.BoxStore

object ObjectBoxStore {
    lateinit var store: BoxStore
        private set

    fun init(context: Context) {
        store = MyObjectBox.builder()
            .androidContext(context)
            // https://stackoverflow.com/questions/53546614/how-to-use-objectbox-in-gradle-multi-module-project
            .name(BuildConfig.MODULE_NAME)
            .build()
    }
}
