package com.kahin.espressodemo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class GlobalApp : Application() {

    companion object {
        /**
         * Global application context.
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}