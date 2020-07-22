package com.bot.man.view.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.bot.man.R;
import com.bot.man.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

	private MutableLiveData<String> mErrorListener = new MutableLiveData<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		com.bot.man.databinding.ActivityMainBinding mBinding =
				DataBindingUtil.setContentView(this, R.layout.activity_main);
		MainViewModel viewModel =
				ViewModelProviders.of(this).get(MainViewModel.class);

		viewModel.initialize(mErrorListener);

		mBinding.setViewModel(viewModel);
		mBinding.setLifecycleOwner(this);

		mErrorListener.observe(this, error -> Toast
				.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT)
				.show());
	}

}
