package com.bot.man.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bot.man.R
import com.bot.man.databinding.ActivityMainBinding
import com.bot.man.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val mErrorListener = MutableLiveData<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding: ActivityMainBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this).get(
                MainViewModel::class.java)
        viewModel.initialize(mErrorListener)
        mBinding.viewModel = viewModel
        mBinding.lifecycleOwner = this
        mErrorListener.observe(this,
                Observer { error: String ->
                    Toast
                            .makeText(this@MainActivity, "Error: $error",
                                    Toast.LENGTH_SHORT).show()
                })
    }
}