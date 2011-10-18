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

import java.text.DecimalFormat;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

/**
 * Control the view for sensor input display
 * @author itog
 *
 */
public class InputController {
	protected Activity mHostActivity;
	
	private TextView mTemperature;
	private TextView mLightView;
	private TextView mLightRawView;
	private TextView mButtons[];
	private TextView mJoystickView;
	
	private final DecimalFormat mLightValueFormatter = new DecimalFormat("##.#");
	private final DecimalFormat mTemperatureFormatter = new DecimalFormat("###" + (char)0x00B0);

	protected View findViewById(int id) {
		return mHostActivity.findViewById(id);
	}

	protected Resources getResources() {
		return mHostActivity.getResources();
	}
	
	InputController(Activity hostActivity) {
		mHostActivity = hostActivity;
		mTemperature = (TextView) findViewById(R.id.tempValue);
		mLightView = (TextView) findViewById(R.id.lightPercentValue);
		mLightRawView = (TextView) findViewById(R.id.lightRawValue);
		mJoystickView = (TextView) findViewById(R.id.joystickValue);
		
		mButtons = new TextView[4];
		mButtons[0] = (TextView)findViewById(R.id.Button1);
		mButtons[1] = (TextView)findViewById(R.id.Button2);
		mButtons[2] = (TextView)findViewById(R.id.Button3);
		mButtons[3] = (TextView)findViewById(R.id.Button4);
	}

	
	public void setTemperature(int temperatureFromArduino) {
		mTemperature.setText(mTemperatureFormatter.format(Util.getTemperatureByRawdata(temperatureFromArduino)));
	}

	public void setLightValue(int lightValueFromArduino) {
		mLightRawView.setText(String.valueOf(lightValueFromArduino));
		mLightView.setText(mLightValueFormatter
				.format((100.0 * (double) lightValueFromArduino / 1024.0)));
	}

	public void switchStateChanged(int switchIndex, boolean switchState) {
		if (switchIndex >= 0 && switchIndex < mButtons.length) {
			if (switchState) {
				mButtons[switchIndex].setText("ON");
			} else {
				mButtons[switchIndex].setText("OFF");
			}
			
		}
	}

	public void joystickButtonSwitchStateChanged(boolean buttonState) {
		if (buttonState) {
			mJoystickView.setBackgroundColor(Color.RED);
		} else {
			mJoystickView.setBackgroundColor(Color.DKGRAY);
		}
	}

	public void joystickMoved(int x, int y) {
		mJoystickView.setText(x + ", " + y);
	}
}
