package com.bot.man.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bot.man.R;
import com.bot.man.api.Helper;
import com.bot.man.model.data.Response;
import com.bot.man.utils.SharedPrefUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;

public class BotService extends Service {

	private static final String CHANNEL_ID = "BotService";
	private TelegramBotThread mTelegramBotThread;

	public BotService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mTelegramBotThread = new TelegramBotThread(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		createNotificationChannel();

		Notification notification =
				new NotificationCompat.Builder(this, CHANNEL_ID)
						.setContentTitle("Bot Manager")
						.setContentText("Telegram API service running")
						.setSmallIcon(R.drawable.ic_launcher_background)
						.build();

		startForeground(1, notification);

		//		ScheduledExecutorService scheduledExecutorService =
		//				Executors.newScheduledThreadPool(4);
		//		scheduledExecutorService
		//				.scheduleAtFixedRate(this::scheduleRunner, 100, 100,
		//				                     TimeUnit.MILLISECONDS);

		scheduleRunner();

		return START_NOT_STICKY;
	}

	private void createNotificationChannel() {
		NotificationChannel channel =
				new NotificationChannel(CHANNEL_ID, "Bot Manager",
				                        NotificationManager.IMPORTANCE_DEFAULT);

		NotificationManager manager =
				getSystemService(NotificationManager.class);
		manager.createNotificationChannel(channel);
	}

	private void scheduleRunner() {
		mTelegramBotThread.run();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private static class TelegramBotThread implements Runnable {
		private static final String TAG = "TelegramBotThread";
		private Context mContext;

		TelegramBotThread(Context context) {
			mContext = context;
		}

		@Override
		public void run() {
			Helper.getInstance(
					SharedPrefUtils.getInstance().getStringValue("API_KEY"))
			      .fetchGetUpdates().enqueue(new Callback<Response>() {
				@Override
				public void onResponse(
						@NotNull Call<Response> call,
						@NotNull retrofit2.Response<Response> response) {
					if (response.body() != null) {

						if (response.body().component1() == null) {
							return;
						} else {
							if (response.body().component1().size() <= 0) {
								run();
								return;
							}
						}

						Integer offset = response.body().component1()
						                         .get(response.body()
						                                      .component1()
						                                      .size() - 1)
						                         .getUpdateId();

						Log.e(TAG, "Last offset: " + offset);

						Helper.getInstance(SharedPrefUtils.getInstance()
						                                  .getStringValue(
								                                  "API_KEY"))
						      .getUpdate(offset + 1)
						      .enqueue(new ChatHandler(TelegramBotThread.this));
					} else {
						Log.e(TAG, "Response NULL from Thread.");
					}
				}

				@Override
				public void onFailure(
						@NotNull Call<Response> call, @NotNull Throwable t) {

					Log.e(TAG, "" + t.getLocalizedMessage());
				}
			});
		}
	}

	private static class ChatHandler implements Callback<Response> {
		private static final String TAG = "ChatHandler";
		private final TelegramBotThread mBot;

		public ChatHandler(TelegramBotThread telegramBotThread) {
			mBot = telegramBotThread;
		}

		@Override
		public void onResponse(
				@NotNull Call<Response> call,
				retrofit2.Response<Response> response) {
			mBot.run();
			Log.e(TAG, "ChatHandler status: " + response.body().component2());
		}

		@Override
		public void onFailure(
				@NotNull Call<Response> call, @NotNull Throwable error) {
			if (error instanceof SocketTimeoutException) {
				Log.e(TAG, "Connection timeout!");
				mBot.run();
			} else if (error instanceof IOException) {
				Log.e(TAG, "Timeout!");
			} else {
				if (call.isCanceled()) {
					Log.e(TAG, "Call cancelled by user!");
				} else {
					Log.e(TAG, "ChatHandler error status: " +
					           error.getLocalizedMessage());
				}
			}
		}
	}
}
