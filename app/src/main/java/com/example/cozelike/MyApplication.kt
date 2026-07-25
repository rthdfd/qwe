package com.example.cozelike

import android.app.Application
import com.example.cozelike.data.AppRepository

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppRepository.initialize(this)
    }
}
