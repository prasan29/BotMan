package com.bot.man.api;

import com.bot.man.model.data.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIWrapper {
	@GET("getUpdates")
	Call<Response> fetchGetUpdates();

	@GET("getUpdates?timeout=100")
	Call<Response> getUpdate(@Query("offset") Integer offset);
}
