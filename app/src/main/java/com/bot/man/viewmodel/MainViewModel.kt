package com.bot.man.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bot.man.model.Repository.Companion.instance
import com.bot.man.model.data.Response
import com.bot.man.utils.SharedPrefUtils.Companion.INSTANCE
import com.bot.man.view.adapter.MainAdapter

class MainViewModel : ViewModel() {
    @JvmField
    var mApiKey = MutableLiveData<String>()

    @JvmField
    var mIsAPIKeyStored = MutableLiveData<Boolean>()
    private var API_KEY: String? = null
    val adapter = MutableLiveData<MainAdapter>()
    private var mErrorListener: MutableLiveData<String>? = null

    @JvmField
    var mOnResultClick =
            View.OnClickListener { initiateFetch() }

    fun initialize(
            errorListener: MutableLiveData<String>?) {
        mErrorListener = errorListener
        val localKey = INSTANCE
                ?.getStringValue("API_KEY")
        if (localKey == null) {
            mIsAPIKeyStored.value = false
        } else {
            mIsAPIKeyStored.value = true
            initiateFetch()
        }
    }

    private fun initiateFetch() {
        val mainAdapter = MainAdapter()
        val localKey = INSTANCE
                ?.getStringValue("API_KEY")
        API_KEY = localKey ?: mApiKey.value
        instance!!.fetchAPI(object : OnResultListener {
            override fun onUpdateChanged(
                    response: Response?) {
                mainAdapter.setResponse(response)
                if (localKey == null) {
                    INSTANCE
                            ?.setStringValue("API_KEY",
                                    API_KEY)
                    mApiKey.value = ""
                    mIsAPIKeyStored.value = true
                }
            }

            override fun onUpdateError(t: Throwable) {
                mErrorListener!!.value = "Error: " + t.localizedMessage
            }
        }, API_KEY!!)
        adapter.value = mainAdapter
    }

    interface OnResultListener {
        fun onUpdateChanged(response: Response?)
        fun onUpdateError(t: Throwable)
    }
}