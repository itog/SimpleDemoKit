/*
 * Copyright (C) 2011 PIGMAL LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pigmal.android.ex.accessory;

import com.pigmal.android.accessory.Accessory;

import android.util.Log;

/**
 * Send command to RT-ADK & RT-ADS hardware
 * @author itog
 *
 */
public class ADKCommandSender {
	static final String TAG = "ADKCommandSender";
	
	public static final byte LED_SERVO_COMMAND = 2;
	public static final byte RELAY_COMMAND = 3;
	
	private Accessory openAccessory;
	
	private boolean isRelayRunning = false;
	private boolean isServoRunning = false;
	
	public ADKCommandSender(Accessory acc) {
		openAccessory = acc;
	}
	
	/**
	 * Send a command to ADK
	 * @param command
	 * @param target
	 * @param value
	 */
	private void sendCommand(byte command, byte target, int value) {
		if (value > 255)
			value = 255;

		if (target != -1) {
			openAccessory.write(command, target, (byte)value);
		}
	}

	
	public void sendServoCommand(int target, int value) {
		sendCommand(LED_SERVO_COMMAND, (byte) (target + 0x10), (byte)value);
	}
	
	public void sendLEDcommand(int target, int color_index, int value) {
		sendCommand(LED_SERVO_COMMAND, (byte) ((target - 1) * 3 + color_index), (byte)value);
	}
	
	public void relay(byte target, byte value) {
		sendCommand(RELAY_COMMAND, target, value);
	}

	/**
	 * sequence to on/off relay 10 times
	 */
	public void relaySequence() {
		if (isRelayRunning) return;
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.v(TAG, "relay thread started");
				isRelayRunning = true;

				byte onoff = 0;
				for (int i = 0; i < 10; i++) {
					Log.v(TAG, "relay thread is running " + onoff);
					onoff = (byte) (onoff == 0 ? 1 : 0);
					sendCommand((byte)3, (byte)0, onoff);
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
				Log.v(TAG, "servo sequence thread started");
				byte target = (byte) (0 + 0x10);
				isServoRunning = true;

				sendCommand(LED_SERVO_COMMAND, target, 100);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sendCommand(LED_SERVO_COMMAND, target, 127);
				byte progress = 0;
				for (int i = 0; i < 4; i++) {
					Log.v(TAG, "thread is running " + progress);
					progress = (byte) (progress == 30 ? 150 : 30);
					sendCommand(LED_SERVO_COMMAND, target, progress);
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
