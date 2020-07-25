package com.bot.man.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Helper {
    private var INSTANCE: APIWrapper? = null

    @JvmStatic
    fun getInstance(API_KEY: String): APIWrapper? {
        if (INSTANCE == null) {
            INSTANCE = Retrofit.Builder()
                    .baseUrl("https://api.telegram.org/bot$API_KEY/")
                    .addConverterFactory(GsonConverterFactory.create()).build()
                    .create(APIWrapper::class.java)
        }
        return INSTANCE
    }
}