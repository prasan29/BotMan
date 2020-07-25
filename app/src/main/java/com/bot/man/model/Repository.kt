package com.bot.man.model

import com.bot.man.api.ApiHandler
import com.bot.man.viewmodel.MainViewModel.OnResultListener

class Repository private constructor() {
    fun fetchAPI(
            onResultListener: OnResultListener, API_KEY: String) {
        ApiHandler.instance?.fetchData(onResultListener, API_KEY)
    }

    companion object {
        private var mRepository: Repository? = null

        @JvmStatic
        val instance: Repository?
            get() {
                if (mRepository == null) {
                    mRepository = Repository()
                }
                return mRepository
            }
    }
}