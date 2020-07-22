package com.bot.man.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResponseItemViewModel : ViewModel() {
    var mFromName = MutableLiveData<String>()
    var mMessage = MutableLiveData<String>()
    var mDate = MutableLiveData<String>()
    var mUpdateId = MutableLiveData<String>()
}