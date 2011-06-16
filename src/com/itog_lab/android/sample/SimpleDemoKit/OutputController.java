package com.itog_lab.android.sample.SimpleDemoKit;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class OutputController {
	private static final String TAG = "OutputController";
	private Activity mHostActivity;
	
	private ToggleButton relayButtons[];
	private SeekBar servoSeekBars[];
	private Spinner ledSpinner;
	private SeekBar ledSeekBars[];
	
	ADKCommandSender mAdkSender;
	
	OutputController(Activity activity, ADKCommandSender adk) {
		mHostActivity = activity;
		mAdkSender = adk;
		
		relayButtons = new ToggleButton[2];
		relayButtons[0] = (ToggleButton)findViewById(R.id.toggleButton1);
		relayButtons[1] = (ToggleButton)findViewById(R.id.toggleButton2);
		for (int i = 0; i < relayButtons.length; i++) {
			relayButtons[i].setOnCheckedChangeListener(relayListener);
			relayButtons[i].setTag(Integer.valueOf(i));
		}

		servoSeekBars = new SeekBar[3];
		servoSeekBars[0] = (SeekBar)findViewById(R.id.servoBar1);
		servoSeekBars[1] = (SeekBar)findViewById(R.id.servoBar2);
		servoSeekBars[2] = (SeekBar)findViewById(R.id.servoBar3);
		for (int i = 0; i < 3; i++) {
			servoSeekBars[i].setOnSeekBarChangeListener(servoListener);
			servoSeekBars[i].setMax(255);
			servoSeekBars[i].setTag(Integer.valueOf(i));
		}
		
		ledSeekBars = new SeekBar[3];
		ledSeekBars[0] = (SeekBar)findViewById(R.id.ledSeekBarRed);
		ledSeekBars[1] = (SeekBar)findViewById(R.id.ledSeekBarGreen);
		ledSeekBars[2] = (SeekBar)findViewById(R.id.ledSeekBarBlue);
		
		for (int i = 0 ; i < 3; i++) {
			ledSeekBars[i].setOnSeekBarChangeListener(ledListener);
			ledSeekBars[i].setMax(255);
			ledSeekBars[i].setTag(Integer.valueOf(i));
		}
		ledSpinner = (Spinner)findViewById(R.id.ledSpinner);
	}
	
	protected View findViewById(int id) {
		return mHostActivity.findViewById(id);
	}

	protected Resources getResources() {
		return mHostActivity.getResources();
	}

	void accessoryAttached() {
		onAccesssoryAttached();
	}

	protected void onAccesssoryAttached() {
		//TODO set initial values
	}

	private OnCheckedChangeListener relayListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton view, boolean isChecked) {
			mAdkSender.sendCommand(ADKCommandSender.RELAY_COMMAND, (byte)(((Integer)view.getTag()).intValue()), isChecked ? 1 : 0);
		}
	};
	
	private OnSeekBarChangeListener servoListener = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			int i = (Integer)seekBar.getTag();
			byte target = (byte) (i + 0x10);
			mAdkSender.sendCommand(ADKCommandSender.LED_SERVO_COMMAND, target, progress);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}
	};
	
	private OnSeekBarChangeListener ledListener = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			int i = (Integer)seekBar.getTag();
			if (mAdkSender != null) {
				byte commandTarget = (byte) ((Integer.valueOf((String)ledSpinner.getSelectedItem()) - 1) * 3 + i);
				mAdkSender.sendCommand(ADKCommandSender.LED_SERVO_COMMAND, commandTarget, (byte) progress);
			}
		}
	
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			Log.v(TAG, "onStartTrackingTouch");
		}
	
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			Log.v(TAG, "onStopTrackingTouch");
		}
	};
}
	
