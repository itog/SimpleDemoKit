package com.itog_lab.android.sample.SimpleDemoKit;

import java.io.IOException;
import java.io.OutputStream;

import android.util.Log;

public class ADKCommandSender {
	public static final byte LED_SERVO_COMMAND = 2;
	public static final byte RELAY_COMMAND = 3;
	
	private OutputStream mOutputStream;

	public void setOutputStream(OutputStream stream) {
		mOutputStream = stream;
	}
	
	public void sendCommand(byte command, byte target, int value) {
		byte[] buffer = new byte[3];
		if (value > 255)
			value = 255;

		buffer[0] = command;
		buffer[1] = target;
		buffer[2] = (byte) value;
		if (mOutputStream != null && buffer[1] != -1) {
			try {
				mOutputStream.write(buffer);
			} catch (IOException e) {
				Log.e(OpenAccessoryBase.TAG, "write failed", e);
			}
		}
	}
}
