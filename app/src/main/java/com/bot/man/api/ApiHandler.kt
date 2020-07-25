package com.bot.man.api

import android.util.Log
import com.bot.man.model.data.Response
import com.bot.man.viewmodel.MainViewModel.OnResultListener
import retrofit2.Call
import retrofit2.Callback

class ApiHandler private constructor() {
    fun fetchData(
            mOnResultListener: OnResultListener, API_KEY: String) {
        HandlerThread(mOnResultListener, API_KEY).start()
    }

    private class HandlerThread internal constructor(
            private val mOnResultListener: OnResultListener,
            private val API_KEY: String) : Thread() {
        override fun run() {
            Helper.getInstance(API_KEY)?.fetchGetUpdates()
                    ?.enqueue(object : Callback<Response?> {
                        override fun onResponse(
                                call: Call<Response?>,
                                response: retrofit2.Response<Response?>) {
                            if (response.body() != null) {
                                Log.e(
                                        TAG,
                                        "Response status: " +
                                                response.body()!!.ok)
                                mOnResultListener
                                        .onUpdateChanged(response.body())
                            }
                            interrupt()
                        }

                        override fun onFailure(
                                call: Call<Response?>,
                                t: Throwable) {
                            Log.e(
                                    TAG,
                                    "onFailure " + t.localizedMessage)
                            mOnResultListener.onUpdateError(t)
                        }
                    })
        }

        companion object {
            private const val TAG = "BotHandlerThread"
        }

    }

    companion object {
        private var INSTANCE: ApiHandler? = null

        @JvmStatic
        val instance: ApiHandler?
            get() {
                if (INSTANCE == null) {
                    INSTANCE = ApiHandler()
                }
                return INSTANCE
            }
    }
}