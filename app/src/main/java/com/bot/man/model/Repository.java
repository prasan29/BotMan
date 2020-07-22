package com.bot.man.model;

import com.bot.man.api.ApiHandler;
import com.bot.man.viewmodel.MainViewModel;

public class Repository {
	private static Repository mRepository;

	private Repository() {
	}

	public static Repository getInstance() {
		if (mRepository == null) {
			mRepository = new Repository();
		}
		return mRepository;
	}

	public void fetchAPI(
			MainViewModel.OnResultListener onResultListener, String API_KEY) {
		ApiHandler.getInstance().fetchData(onResultListener, API_KEY);
	}

}
