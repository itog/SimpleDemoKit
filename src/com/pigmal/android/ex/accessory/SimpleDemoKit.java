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

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pigmal.android.accessory.AccessoryBaseActivity;

public class SimpleDemoKit extends AccessoryBaseActivity implements OnClickListener {
	private static final String TAG = "SimpleDemokit";
	
	private ADKCommandReceiver mReceiver;
	
	private TextView inputLabel;
	private TextView outputLabel;
	private LinearLayout inputContainer;
	private LinearLayout outputContainer;
	@SuppressWarnings("unused")
	private OutputController mOutputController;
	private InputController mInputController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mReceiver = new ADKCommandReceiver();
		mOpenAccessory.setListener(mReceiver);
		
		if (mOpenAccessory.isConnected()) {
			showControls();
		} else {
			hideControls();
		} 
	}

	@Override
	public void onDestroy() {
		mOpenAccessory.removeListener();
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * Show controls on the display
	 */
	private void showControls() {
		setContentView(R.layout.main);
		inputLabel = (TextView) findViewById(R.id.inputLabel);
		outputLabel = (TextView) findViewById(R.id.outputLabel);
		inputContainer = (LinearLayout) findViewById(R.id.inputContainer);
		outputContainer = (LinearLayout) findViewById(R.id.outputContainer);
		inputLabel.setOnClickListener(this);
		outputLabel.setOnClickListener(this);
		
		showTabContents(true);
		
		mOutputController = new OutputController(this, mOpenAccessory);
		mInputController = new InputController(this);
		mReceiver.setInputController(mInputController);
	}

	/**
	 * Hide all controlls
	 */
	private void hideControls() {
		setContentView(R.layout.no_device);
		mReceiver.removeInputController();
		mInputController = null;
		mOutputController = null;		
	}

	/**
	 * Swich the display to show input or output
	 * @param showInput
	 */
	private void showTabContents(Boolean showInput) {
		if (showInput) {
			inputContainer.setVisibility(View.VISIBLE);
			inputLabel.setBackgroundColor(Color.DKGRAY);
			outputContainer.setVisibility(View.GONE);
			outputLabel.setBackgroundColor(Color.BLACK);
		} else {
			inputContainer.setVisibility(View.GONE);
			inputLabel.setBackgroundColor(Color.BLACK);
			outputContainer.setVisibility(View.VISIBLE);
			outputLabel.setBackgroundColor(Color.DKGRAY);
		}
	}
	
	public void onClick(View v) {
		int vId = v.getId();
		switch (vId) {
		case R.id.inputLabel:
			showTabContents(true);
			break;
		case R.id.outputLabel:
			showTabContents(false);
			break;
		}
	}
	
	@Override
	protected void onUsbAtached() {
		Log.v(TAG, "onUsbAtached");
		showControls();
	}
	
	@Override
	protected void onUsbDetached() {
		Log.v(TAG, "onUsbDetached");
		hideControls();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("Simulate")) {
			showControls();
		} else if (item.getTitle().equals("Quit")) {
			finish();
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Simulate");
		menu.add("Quit");
		return true;
	}
}
