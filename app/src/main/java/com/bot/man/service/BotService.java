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
import com.bot.man.model.data.ResultItem;
import com.bot.man.utils.SharedPrefUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class BotService extends Service {

	private static final String CHANNEL_ID = "BotService";
	private static String mApiKey;
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
		mApiKey =
				SharedPrefUtils.getInstance().getStringValue("API_KEY") == null
				? "" : SharedPrefUtils.getInstance().getStringValue("API_KEY");
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
			Helper.getInstance(mApiKey).fetchGetUpdates()
			      .enqueue(new Callback<Response>() {
				      @Override
				      public void onResponse(
						      @NotNull Call<Response> call,
						      @NotNull retrofit2.Response<Response> response) {
					      if (response.body() != null) {

						      if (response.body().component1() == null) {
							      return;
						      } else {
							      if (response.body().component1().size() <=
							          0) {
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

						      Helper.getInstance(mApiKey).getUpdate(offset + 1)
						            .enqueue(new ChatHandler(
								            TelegramBotThread.this));
					      } else {
						      Log.e(TAG, "Response NULL from Thread.");
					      }
				      }

				      @Override
				      public void onFailure(
						      @NotNull Call<Response> call,
						      @NotNull Throwable t) {

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
				@NotNull retrofit2.Response<Response> response) {
			if (response.body() == null) {
				return;
			} else {
				if (response.body().getResult() == null) {
					return;
				} else {
					if (response.body().getResult().size() <= 0) {
						return;
					}
				}
			}

			List<ResultItem> items = response.body().component1();

			for (int i = 0; i < items.size(); i++) {
				String requestMessage = items.get(i).getMessage().getText();
				Integer chatId = items.get(i).getMessage().getChat().getId();

				Log.e(TAG, "" + requestMessage);

				Helper.getInstance(mApiKey)
				      .sendMessage(getReplyText(requestMessage), chatId)
				      .enqueue(new Callback<Response>() {
					      @Override
					      public void onResponse(
							      Call<Response> call,
							      retrofit2.Response<Response> response) {
						      if (response.body() == null) {
							      return;
						      }

						      Log.e(TAG, "SendMessage status: " +
						                 response.body().getOk());
					      }

					      @Override
					      public void onFailure(
							      Call<Response> call, Throwable t) {
						      Log.e(TAG, "Error while sending message: " +
						                 t.getLocalizedMessage());
					      }
				      });
			}

			mBot.run();
			Log.e(TAG, "ChatHandler status: " + response.body().component2());
		}

		private String getReplyText(String response) {
			if ("/start".equals(response)) {
				return "\\n\\nWelcome to the chat.\\n\\nI am SnapieBot, the " +
				       "virtual assistant of Prasanna.\\n\\nStart your conversation with /start or /profile";
			} else if ("/profile".equals(response)) {
				return "\\nYou can find Prasanna's profile here: https://www.linkedin.com/in/prasanna-srinivasan2905/";
			} else {
				return "Please select one of the following tags.\n" +
				       "    /start\n" + "    /profile\n" + "\n" +
				       "If you want to start a conversation with Mr. Prasanna, please click @prasansrini29";
			}

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
