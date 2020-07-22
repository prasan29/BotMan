package com.bot.man.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bot.man.model.Repository;
import com.bot.man.model.data.Response;
import com.bot.man.utils.SharedPrefUtils;
import com.bot.man.view.adapter.MainAdapter;

public class MainViewModel extends ViewModel {
	public MutableLiveData<String> mApiKey = new MutableLiveData<>();
	public MutableLiveData<Boolean> mIsAPIKeyStored = new MutableLiveData<>();
	private String API_KEY;
	private MutableLiveData<MainAdapter> mAdapter = new MutableLiveData<>();
	private MutableLiveData<String> mErrorListener;
	public View.OnClickListener mOnResultClick = view -> initiateFetch();

	public void initialize(
			MutableLiveData<String> errorListener) {
		mErrorListener = errorListener;

		String localKey =
				SharedPrefUtils.getInstance().getStringValue("API_KEY");

		if (localKey == null) {
			mIsAPIKeyStored.setValue(false);
		} else {
			mIsAPIKeyStored.setValue(true);
			initiateFetch();
		}
	}

	public void initiateFetch() {
		MainAdapter mainAdapter = new MainAdapter();

		String localKey =
				SharedPrefUtils.getInstance().getStringValue("API_KEY");

		if (localKey == null) {
			API_KEY = mApiKey.getValue();
		} else {
			API_KEY = localKey;
		}

		Repository.getInstance().fetchAPI(new OnResultListener() {
			@Override
			public void onUpdateChanged(Response response) {
				mainAdapter.setResponse(response);
				if (localKey == null) {
					SharedPrefUtils.getInstance()
					               .setStringValue("API_KEY", API_KEY);
					mApiKey.setValue("");
					mIsAPIKeyStored.setValue(true);
				}
			}

			@Override
			public void onUpdateError(Throwable t) {
				mErrorListener.setValue("Error: " + t.getLocalizedMessage());
			}
		}, API_KEY);

		mAdapter.setValue(mainAdapter);
	}

	public MutableLiveData<MainAdapter> getAdapter() {
		return mAdapter;
	}

	public interface OnResultListener {
		void onUpdateChanged(Response response);

		void onUpdateError(Throwable t);
	}
}
