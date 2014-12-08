package com.example.flightsofangels;

import android.content.Context;
import android.os.Bundle;

public class AppContext extends WiFiDirectTestAppActivity {
	private static Context context;

	public void onCreate(Bundle savedInstanceState) {
		AppContext.context = getApplicationContext();

	}

	public static Context getAppContext() {
		return AppContext.context;
	}
}
