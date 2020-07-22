package com.bot.man.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.bot.man.R;

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
}