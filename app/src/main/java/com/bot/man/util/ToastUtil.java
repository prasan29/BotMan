package com.bot.man.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	private static ToastUtil INSTANCE;
	private static Toast mToast;

	private ToastUtil() {

	}

	public static ToastUtil getInstance(Context context, String message) {
		if (INSTANCE == null) {
			mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			INSTANCE = new ToastUtil();
		}
		return INSTANCE;
	}

	public void showToast() {
		if (mToast != null) {
			mToast.show();
		}
	}
}
