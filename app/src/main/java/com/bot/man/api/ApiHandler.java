package com.bot.man.api;

import android.util.Log;

import com.bot.man.model.data.Response;
import com.bot.man.viewmodel.MainViewModel;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;

public class ApiHandler {
	private static ApiHandler INSTANCE;

	private ApiHandler() {
	}

	public static ApiHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ApiHandler();
		}
		return INSTANCE;
	}

	public void fetchData(
			MainViewModel.OnResultListener mOnResultListener, String API_KEY) {
		new HandlerThread(mOnResultListener, API_KEY).start();
	}

	private static class HandlerThread extends Thread {
		private static final String TAG = "BotHandlerThread";
		private String API_KEY;
		private MainViewModel.OnResultListener mOnResultListener;

		HandlerThread(
				MainViewModel.OnResultListener onResultListener,
				String API_KEY) {
			mOnResultListener = onResultListener;
			this.API_KEY = API_KEY;
		}

		@Override
		public void run() {
			//TODO: Fetch results and call the onResultListener
			// .onUpdateChanged(Response response).

			Helper.getInstance(API_KEY).fetchGetUpdates()
			      .enqueue(new Callback<Response>() {
				      @Override
				      public void onResponse(
						      @NotNull Call<Response> call,
						      @NotNull retrofit2.Response<Response> response) {
					      if (response.body() != null) {
						      Log.e(TAG,
						            "onResponse " + response.body().toString());
						      mOnResultListener
								      .onUpdateChanged(response.body());
					      }
					      interrupt();
				      }

				      @Override
				      public void onFailure(
						      @NotNull Call<Response> call,
						      @NotNull Throwable t) {
					      Log.e(TAG, "onFailure " + t.getLocalizedMessage());
					      mOnResultListener.onUpdateError(t);
				      }
			      });
		}
	}
}
