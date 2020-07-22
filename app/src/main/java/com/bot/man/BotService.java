package com.bot.man;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BotService extends Service {

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

		return START_NOT_STICKY;
	}
}
