package com.bot.man.model;

public class Repository {
	private static Repository mRepository;

	private Repository() {
	}

	public Repository getInstance() {
		if (mRepository == null) {
			mRepository = new Repository();
		}
		return mRepository;
	}
}
