package com.bot.man.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Helper {
	private static APIWrapper INSTANCE;

	private Helper() {
	}

	public static APIWrapper getInstance(String API_KEY) {
		if (INSTANCE == null) {
			INSTANCE = new Retrofit.Builder()
					.baseUrl("https://api.telegram.org/bot" + API_KEY + "/")
					.addConverterFactory(GsonConverterFactory.create()).build()
					.create(APIWrapper.class);
		}

		return INSTANCE;
	}
}
