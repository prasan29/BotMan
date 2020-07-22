package com.bot.man.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bot.man.R;
import com.bot.man.databinding.ActivityMainBinding;
import com.bot.man.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

	ActivityMainBinding mBinding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		MainViewModel viewModel =
				ViewModelProviders.of(this).get(MainViewModel.class);

		mBinding.setViewModel(viewModel);
		mBinding.setLifecycleOwner(this);
	}
}