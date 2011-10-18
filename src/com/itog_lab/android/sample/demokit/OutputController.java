package com.itog_lab.android.sample.demokit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class OutputController {
	private static final String TAG = "OutputController";
	
	private ToggleButton relayButtons[];
	private SeekBar servoSeekBars[];
	private Spinner ledSpinner;
	private SeekBar ledSeekBars[];
	private Button relaySequenceButton;
	private Button servoSequenceButton;

	private Button serviceButton;
	
	private Activity hostActivity;
	private ADKCommandSender adkSender;
	
	OutputController(Activity activity, ADKCommandSender adk) {
		hostActivity = activity;
		adkSender = adk;
		
		relayButtons = new ToggleButton[2];
		relayButtons[0] = (ToggleButton)findViewById(R.id.toggleButton1);
		relayButtons[1] = (ToggleButton)findViewById(R.id.toggleButton2);
		for (int i = 0; i < relayButtons.length; i++) {
			Log.v(TAG, "relay toggle i = " + i);
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
		
		relaySequenceButton = (Button)findViewById(R.id.relayButton);
		relaySequenceButton.setOnClickListener(buttonListener);
		servoSequenceButton = (Button)findViewById(R.id.servoButton);
		servoSequenceButton.setOnClickListener(buttonListener);
		
		serviceButton = (Button)findViewById(R.id.serviceButton);
		serviceButton.setOnClickListener(buttonListener);
	}
	
	OnClickListener buttonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.relayButton:
				adkSender.relaySequence();
				break;
			case R.id.servoButton:
				adkSender.servoSequence();
				break;
			case R.id.serviceButton:
				Log.v(TAG, "service button clicked");
		        Intent service = new Intent(hostActivity, MessageHandleService.class);
		        //service.putExtra("cmd", "");
		        hostActivity.startService(service);
		        break;
			default:
				break;
			}
		}
		
	};
	
	private View findViewById(int id) {
		return hostActivity.findViewById(id);
	}

	private Resources getResources() {
		return hostActivity.getResources();
	}

	private OnCheckedChangeListener relayListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton view, boolean isChecked) {
			adkSender.relay((byte)(((Integer)view.getTag()).intValue()), (byte) (isChecked ? 1 : 0));
		}
	};
	
	private OnSeekBarChangeListener servoListener = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			adkSender.sendServoCommand((Integer)seekBar.getTag(), progress);
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
			if (adkSender != null) {
				adkSender.sendLEDcommand(Integer.valueOf((String)ledSpinner.getSelectedItem()), i, progress);
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

