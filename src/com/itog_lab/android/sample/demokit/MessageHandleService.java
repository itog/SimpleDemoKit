package com.itog_lab.android.sample.demokit;

import com.itog_lab.android.accessory.OpenAccessory;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MessageHandleService extends Service {
	static final String TAG = "MessageHandleServie";

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy");
	}

	void sleep(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.v(TAG, "onStart");

		String cmd = intent.getStringExtra("cmd");
		OpenAccessory accessory = new OpenAccessory();
		accessory.open(this.getApplicationContext());
		
		ADKCommandSender adk = new ADKCommandSender(accessory);
		//adk.relaySequence();
		adk.servoSequence();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
