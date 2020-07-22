package com.bot.man.api;

import com.bot.man.model.data.Response;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIWrapper {
	@GET("getUpdates")
	Call<Response> fetchGetUpdates();
}
