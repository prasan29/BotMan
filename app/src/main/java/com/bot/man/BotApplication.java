package com.bot.man;

import android.app.Application;

import com.bot.man.utils.SharedPrefUtils;

public class BotApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		SharedPrefUtils.init(this);
	}
}
