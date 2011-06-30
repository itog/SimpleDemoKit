package com.itog_lab.android.accessory;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;

public class OpenAccessory implements Runnable {
	static final String TAG = "OpenAccessory";

	//static private UsbAccessory mAccessory;
	static private ParcelFileDescriptor mFileDescriptor;
	static private FileInputStream mInputStream;
	static private FileOutputStream mOutputStream;

	private UsbManager mUsbManager;
	private AccessoryListener listener;
	private boolean isThreadRunning;
	
	public void open(Context context) {
		if (isConnected()) {
			return ;
		}

		mUsbManager = UsbManager.getInstance(context); //2.3.4
//		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE); //3.1

		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (mUsbManager.hasPermission(accessory)) {
				openAccessory(accessory);
			} else {
				//TODO request permission explicitly
			}
		} else {
			Log.d(TAG, "mAccessory is null");
		}
	}
		
	private void openAccessory(UsbAccessory accessory) {
		mFileDescriptor = mUsbManager.openAccessory(accessory);
		
		if (mFileDescriptor != null) {
			//mAccessory = accessory;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();
			mInputStream = new FileInputStream(fd);
			mOutputStream = new FileOutputStream(fd);
		} else {
			Log.d(TAG, "accessory open fail");
		}
	}

	public void close() {
		Log.v(TAG, "closeAccessory");
		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			mFileDescriptor = null;
			//mAccessory = null;
			mInputStream = null;
			mOutputStream = null;
		}
	}
	
	public boolean isConnected() {
		return mFileDescriptor != null;
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
				Log.e(TAG, "write failed", e);
			}
		}
	}
	
	public void setListener(AccessoryListener l) {
		listener = l;
		Thread thread = new Thread(null, this, "DemoKit");
		thread.start();
	}

	public void removeListener() {
		stopRunningThread();
	}
	
	private void stopRunningThread() {
		isThreadRunning = false;
	}
	
	public void run() {
		int ret = 0;
		byte[] buffer = new byte[16384];
		byte[] pass;

		isThreadRunning = true;
		while (ret >= 0 && isThreadRunning) {
			try {
				ret = mInputStream.read(buffer);
			} catch (IOException e) {
				break;
			}

			pass = new byte[ret];
			System.arraycopy(buffer, 0, pass, 0, ret);
			listener.onAccessoryMessage(pass);
		}
		listener = null;
		isThreadRunning = false;
	}
}
