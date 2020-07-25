package com.bot.man.api

import com.bot.man.model.data.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIWrapper {
    @GET("getUpdates")
    fun fetchGetUpdates(): Call<Response?>?

    @GET("getUpdates?timeout=100")
    fun getUpdate(@Query("offset")
                  offset: Int?): Call<Response?>?

    @GET("sendMessage")
    fun sendMessage(
            @Query("text") text: String?, @Query("chat_id")
            chat_id: Int?): Call<Response?>?
}