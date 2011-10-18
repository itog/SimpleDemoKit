package com.itog_lab.android.accessory;



public interface AccessoryListener {
	public static final int TYPE_SWITCH = 0x01;
	public static final int TYPE_TEMPERATURE = 0x04;
	public static final int TYPE_LIGHT = 0x05;
	public static final int TYPE_JOYSTICK = 0x06;
	
	void onAccessoryMessage(byte[] data);
}
