package com.itog_lab.android.sample.demokit;

import com.itog_lab.android.accessory.AccessoryListener;
import com.itog_lab.android.accessory.OpenAccessory;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;


public class ADKCommandReceiver implements AccessoryListener {
	static final String TAG = "ADKCommandReceiver";
	
	static final int MESSAGE_SWITCH = 1;
	static final int MESSAGE_TEMPERATURE = 2;
	static final int MESSAGE_LIGHT = 3;
	static final int MESSAGE_JOY = 4;

	private InputController mInputController;
	
	public ADKCommandReceiver(OpenAccessory acc) {
		acc.setListener(this);
	}
	
	protected class SwitchMsg {
		private byte sw;
		private byte state;

		public SwitchMsg(byte sw, byte state) {
			this.sw = sw;
			this.state = state;
		}

		public byte getSw() {
			return sw;
		}

		public byte getState() {
			return state;
		}
	}

	protected class TemperatureMsg {
		private int temperature;

		public TemperatureMsg(int temperature) {
			this.temperature = temperature;
		}

		public int getTemperature() {
			return temperature;
		}
	}

	protected class LightMsg {
		private int light;

		public LightMsg(int light) {
			this.light = light;
		}

		public int getLight() {
			return light;
		}
	}

	protected class JoyMsg {
		private int x;
		private int y;

		public JoyMsg(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}
	
	private int composeInt(byte hi, byte lo) {
		int val = (int) hi & 0xff;
		val *= 256;
		val += (int) lo & 0xff;
		return val;
	}
	
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_SWITCH:
				SwitchMsg o = (SwitchMsg) msg.obj;
				handleSwitchMessage(o);
				break;

			case MESSAGE_TEMPERATURE:
				TemperatureMsg t = (TemperatureMsg) msg.obj;
				handleTemperatureMessage(t);
				break;

			case MESSAGE_LIGHT:
				LightMsg l = (LightMsg) msg.obj;
				handleLightMessage(l);
				break;

			case MESSAGE_JOY:
				JoyMsg j = (JoyMsg) msg.obj;
				handleJoyMessage(j);
				break;

			}
		}
	};

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
	}
	protected void handleJoyMessage(JoyMsg j) {
		if (mInputController != null) {
			mInputController.joystickMoved(j.getX(), j.getY());
		}
	}

	protected void handleLightMessage(LightMsg l) {
		if (mInputController != null) {
			mInputController.setLightValue(l.getLight());
		}
	}

	protected void handleTemperatureMessage(TemperatureMsg t) {
		if (mInputController != null) {
			mInputController.setTemperature(t.getTemperature());
		}
	}

	protected void handleSwitchMessage(SwitchMsg o) {
		if (mInputController != null) {
			byte sw = o.getSw();
			if (sw >= 0 && sw < 4) {
				mInputController.switchStateChanged(sw, o.getState() != 0);
			} else if (sw == 4) {
				mInputController
						.joystickButtonSwitchStateChanged(o.getState() != 0);
			}
		}
	}

	public void removeInputController() {
		mInputController = null;		
	}

	public void setInputController(InputController mInputController2) {
		mInputController = mInputController2;
	}

	@Override
	public void onAccessoryMessage(byte[] buffer) {
		int i = 0;
		int ret = buffer.length;
		while (i < ret) {
			int len = ret - i;

			switch (buffer[i]) {
			case 0x1:
				if (len >= 3) {
					Message m = Message.obtain(mHandler, MESSAGE_SWITCH);
					m.obj = new SwitchMsg(buffer[i + 1], buffer[i + 2]);
					mHandler.sendMessage(m);
				}
				i += 3;
				break;

			case 0x4:
				if (len >= 3) {
					Message m = Message.obtain(mHandler, MESSAGE_TEMPERATURE);
					m.obj = new TemperatureMsg(composeInt(buffer[i + 1],
							buffer[i + 2]));
					mHandler.sendMessage(m);
				}
				i += 3;
				break;

			case 0x5:
				if (len >= 3) {
					Message m = Message.obtain(mHandler, MESSAGE_LIGHT);
					m.obj = new LightMsg(composeInt(buffer[i + 1], buffer[i + 2]));
					mHandler.sendMessage(m);
				}
				i += 3;
				break;

			case 0x6:
				if (len >= 3) {
					Message m = Message.obtain(mHandler, MESSAGE_JOY);
					m.obj = new JoyMsg(buffer[i + 1], buffer[i + 2]);
					mHandler.sendMessage(m);
				}
				i += 3;
				break;

			default:
				Log.d(TAG, "unknown msg: " + buffer[i]);
				i = len;
				break;
			}
		}
	}
}
