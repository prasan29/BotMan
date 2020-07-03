package com.bot.man.viewmodel;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bot.man.model.data.Response;
import com.bot.man.model.data.Result;
import com.bot.man.view.adapter.MainAdapter;

import java.util.ArrayList;

public class MainViewModel
		extends ViewModel {
	public MutableLiveData<Boolean> mIsResultOkay = new MutableLiveData<>();
	public View.OnClickListener mOnResultClick = new View.OnClickListener() {
		@Override public void onClick(View view) {
			Toast.makeText(view.getContext(), "" + !mIsResultOkay.getValue(),
			               Toast.LENGTH_SHORT).show();
			mIsResultOkay.setValue(!mIsResultOkay.getValue());
		}
	};
	private MutableLiveData<MainAdapter> mAdapter = new MutableLiveData<>();

	public void initiate() {
		// Call API and when the response comes, set the Adapter.
		MainAdapter mainAdapter = new MainAdapter();
		ArrayList<Result> dummyList = new ArrayList<>();
		mainAdapter.setResponse(new Response(true, dummyList));
		mIsResultOkay.setValue(true);
		mAdapter.setValue(mainAdapter);
	}

	public MutableLiveData<MainAdapter> getAdapter() {
		return mAdapter;
	}
}
