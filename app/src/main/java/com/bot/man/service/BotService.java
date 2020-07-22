package com.bot.man.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.bot.man.R;
import com.bot.man.api.Helper;
import com.bot.man.model.data.Response;
import com.bot.man.utils.SharedPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;

public class BotService extends Service {

	private static final String CHANNEL_ID = "BotService";

	public BotService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
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

	static class TelegramBotThread extends Thread {
		private Context mContext;

		TelegramBotThread(Context context) {
			mContext = context;
		}

		@Override
		public void run() {
			while (true) {
				Helper.getInstance(
						SharedPrefUtils.getInstance().getStringValue("API_KEY"))
				      .fetchGetUpdates().enqueue(new Callback<Response>() {
					@Override
					public void onResponse(
							Call<Response> call,
							retrofit2.Response<Response> response) {

					}

					@Override
					public void onFailure(Call<Response> call, Throwable t) {
						Toast.makeText(mContext, "" + t.getLocalizedMessage(),
						               Toast.LENGTH_SHORT).show();
						interrupt();
					}
				});
			}
		}
	}
}
