package com.itog_lab.android.sample.demokit;

import com.itog_lab.android.accessory.OpenAccessory;
import android.util.Log;

public class ADKCommandSender {
	static final String TAG = "ADKCommandSender";
	
	public static final byte LED_SERVO_COMMAND = 2;
	public static final byte RELAY_COMMAND = 3;
	
	private OpenAccessory openAccessory;
	
	private boolean isRelayRunning = false;
	private boolean isServoRunning = false;
	
	public ADKCommandSender(OpenAccessory acc) {
		openAccessory = acc;
	}
	
	public void sendLEDcommand(byte target, byte value) {
		openAccessory.sendCommand(LED_SERVO_COMMAND, target, value);
	}
	
	public void relay(byte target, byte value) {
		openAccessory.sendCommand(RELAY_COMMAND, target, value);
	}

	/**
	 * sequence to on/off relay 10 times
	 */
	public void relaySequence() {
		if (isRelayRunning) return;
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.v(TAG, "thread started");
				isRelayRunning = true;

				byte onoff = 0;
				for (int i = 0; i < 10; i++) {
					Log.v(TAG, "thread is running " + onoff);
					onoff = (byte) (onoff == 0 ? 1 : 0);
					openAccessory.sendCommand((byte)3, (byte)0, onoff);
					try {
						Thread.sleep(1 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				isRelayRunning = false;
			}
		}).start();
	}
	
	public void servoSequence() {
		if (isServoRunning) return;
	
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.v(TAG, "thread started");
				byte target = (byte) (0 + 0x10);
				isServoRunning = true;

				byte progress = 0;
				for (int i = 0; i < 4; i++) {
					Log.v(TAG, "thread is running " + progress);
					progress = (byte) (progress == 30 ? 150 : 30);
					openAccessory.sendCommand(LED_SERVO_COMMAND, target, progress);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				isServoRunning = false;
			}
		}).start();
	}	
}