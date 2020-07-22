package com.bot.man;

import android.app.Application;
import android.content.Intent;

import com.bot.man.service.BotService;
import com.bot.man.utils.SharedPrefUtils;

public class BotApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		SharedPrefUtils.init(this);
		startForegroundService(new Intent(this, BotService.class));
	}
}
