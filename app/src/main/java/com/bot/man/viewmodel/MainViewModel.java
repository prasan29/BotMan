package com.bot.man.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bot.man.model.Repository;
import com.bot.man.model.data.Response;
import com.bot.man.view.adapter.MainAdapter;

public class MainViewModel extends ViewModel {
	public MutableLiveData<String> mApiKey = new MutableLiveData<>();
	private MutableLiveData<MainAdapter> mAdapter = new MutableLiveData<>();
	public View.OnClickListener mOnResultClick = view -> initiateFetch();

	public void initiateFetch() {
		// TODO: Call API and when the response comes, set the Adapter.
		MainAdapter mainAdapter = new MainAdapter();

		String API_KEY = mApiKey.getValue();

		Repository.getInstance().fetchAPI(response -> {
			mainAdapter.setResponse(response);
			mApiKey.setValue("");
		}, API_KEY);

		mAdapter.setValue(mainAdapter);
	}

	public MutableLiveData<MainAdapter> getAdapter() {
		return mAdapter;
	}

	public interface OnResultListener {
		void onUpdateChanged(Response response);
	}
}
