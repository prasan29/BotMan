package com.bot.man.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bot.man.R
import com.bot.man.api.Helper.getInstance
import com.bot.man.model.data.Response
import com.bot.man.utils.SharedPrefUtils.Companion.INSTANCE
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException
import java.net.SocketTimeoutException

class BotService : Service() {
    private var mTelegramBotThread: TelegramBotThread? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mTelegramBotThread = TelegramBotThread()
        mApiKey = if (INSTANCE
                        ?.getStringValue(
                                "API_KEY") == null) "" else INSTANCE!!
                .getStringValue("API_KEY")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification =
                NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Bot Manager")
                        .setContentText("Telegram API service running")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .build()
        startForeground(1, notification)
        scheduleRunner()
        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        val channel =
                NotificationChannel(CHANNEL_ID, "Bot Manager",
                        NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(
                NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun scheduleRunner() {
        mTelegramBotThread!!.run()
    }

    private class TelegramBotThread : Runnable {
        override fun run() {
            getInstance(mApiKey!!)!!.fetchGetUpdates()
                    ?.enqueue(object : Callback<Response?> {
                        override fun onResponse(
                                call: Call<Response?>,
                                response: retrofit2.Response<Response?>) {
                            if (response.body() != null) {
                                if (response.body()!!.result == null) {
                                    return
                                } else {
                                    if (response.body()!!.result!!.isEmpty()) {
                                        run()
                                        return
                                    }
                                }
                                val offset = response.body()!!.result!![response.body()!!
                                        .result!!
                                        .size - 1]
                                        ?.updateId
                                Log.e(TAG,
                                        "Last offset: $offset")
                                getInstance(mApiKey!!)
                                        ?.getUpdate(offset!! + 1)
                                        ?.enqueue(ChatHandler(
                                                this@TelegramBotThread))
                            } else {
                                Log.e(TAG,
                                        "Response NULL from Thread.")
                            }
                        }

                        override fun onFailure(
                                call: Call<Response?>,
                                t: Throwable) {
                            Log.e(TAG,
                                    "" + t.localizedMessage)
                        }
                    })
        }

        companion object {
            private const val TAG = "TelegramBotThread"
        }
    }

    private class ChatHandler(private val mBot: TelegramBotThread) :
            Callback<Response?> {
        override fun onResponse(
                call: Call<Response?>,
                response: retrofit2.Response<Response?>) {
            if (response.body() == null) {
                return
            } else {
                if (response.body()!!.result == null) {
                    return
                } else {
                    if (response.body()!!.result!!.isEmpty()) {
                        return
                    }
                }
            }
            val items = response.body()!!.result
            for (i in items!!.indices) {
                val requestMessage = items[i]!!.message!!.text
                val chatId = items[i]!!.message!!.chat!!.id
                Log.e(TAG, "" + requestMessage)
                getInstance(mApiKey!!)
                        ?.sendMessage(getReplyText(requestMessage), chatId)
                        ?.enqueue(object : Callback<Response?> {
                            override fun onResponse(
                                    call: Call<Response?>,
                                    response: retrofit2.Response<Response?>) {
                                if (response.body() == null) {
                                    return
                                }
                                Log.e(TAG,
                                        "SendMessage status: " +
                                                response.body()!!.ok)
                            }

                            override fun onFailure(
                                    call: Call<Response?>,
                                    t: Throwable) {
                                Log.e(TAG,
                                        "Error while sending message: " +
                                                t.localizedMessage)
                            }
                        })
            }
            mBot.run()
            Log.e(TAG,
                    "ChatHandler status: " + response.body()!!.ok)
        }

        private fun getReplyText(response: String?): String {
            return when (response) {
                "/start" -> {
                    "\\n\\nWelcome to the chat.\\n\\nI am SnapieBot, the " +
                            "virtual assistant of Prasanna.\\n\\nStart your conversation with /start or /profile"
                }
                "/profile" -> {
                    "\\nYou can find Prasanna's profile here: https://www.linkedin.com/in/prasanna-srinivasan2905/"
                }
                else -> {
                    """Please select one of the following tags.
        /start
        /profile
    
    If you want to start a conversation with Mr. Prasanna, please click @prasansrini29"""
                }
            }
        }

        override fun onFailure(
                call: Call<Response?>,
                error: Throwable) {
            if (error is SocketTimeoutException) {
                Log.e(TAG, "Connection timeout!")
                mBot.run()
            } else if (error is IOException) {
                Log.e(TAG, "Timeout!")
            } else {
                if (call.isCanceled) {
                    Log.e(TAG, "Call cancelled by user!")
                } else {
                    Log.e(TAG, "ChatHandler error status: " +
                            error.localizedMessage)
                }
            }
        }

        companion object {
            private const val TAG = "ChatHandler"
        }

    }

    companion object {
        private const val CHANNEL_ID = "BotService"
        private var mApiKey: String? = null
    }
}