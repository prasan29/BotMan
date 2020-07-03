package com.bot.man.api;

public class ApiHandler {
	private ApiHandler INSTANCE;

	private ApiHandler() {
	}

	public ApiHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ApiHandler();
		}
		return INSTANCE;
	}
}
