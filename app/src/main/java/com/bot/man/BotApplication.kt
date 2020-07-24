package com.bot.man

import android.app.Application
import android.content.Intent
import com.bot.man.service.BotService
import com.bot.man.utils.SharedPrefUtils

class BotApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPrefUtils.init(this)
        startForegroundService(Intent(this, BotService::class.java))
    }
}