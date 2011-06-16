package com.itog_lab.android.sample.SimpleDemoKit;

import java.text.DecimalFormat;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class InputController {
	protected OpenAccessoryBase mHostActivity;
	
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

	
	void accessoryAttached() {
		//TODO
		// initial value is fixed android:text="off"
		// should check the value
	}
	
	InputController(OpenAccessoryBase hostActivity) {
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
		/*
		 * Arduino board contains a 6 channel (8 channels on the Mini and Nano,
		 * 16 on the Mega), 10-bit analog to digital converter. This means that
		 * it will map input voltages between 0 and 5 volts into integer values
		 * between 0 and 1023. This yields a resolution between readings of: 5
		 * volts / 1024 units or, .0049 volts (4.9 mV) per unit.
		 */
		double voltagemv = temperatureFromArduino * 4.9;
		/*
		 * The change in voltage is scaled to a temperature coefficient of 10.0
		 * mV/degC (typical) for the MCP9700/9700A and 19.5 mV/degC (typical)
         * for the MCP9701/9701A. The out- put voltage at 0 degC is also scaled
         * to 500 mV (typical) and 400 mV (typical) for the MCP9700/9700A and
		 * MCP9701/9701A, respectively. VOUT = TC�TA+V0degC
		 */
		double kVoltageAtZeroCmv = 400;
		double kTemperatureCoefficientmvperC = 19.5;
		double ambientTemperatureC = ((double) voltagemv - kVoltageAtZeroCmv)
				/ kTemperatureCoefficientmvperC;
		double temperatureF = (9.0 / 5.0) * ambientTemperatureC + 32.0;
		mTemperature.setText(mTemperatureFormatter.format(temperatureF));
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
		//mJoystickView.setPosition(x, y);
		mJoystickView.setText(x + ", " + y);
	}

	public void onTemperature(int temperature) {
		setTemperature(temperature);
	}

	public void onLightChange(int lightValue) {
		setLightValue(lightValue);
	}

	public void onSwitchStateChange(int switchIndex, Boolean switchState) {
		switchStateChanged(switchIndex, switchState);
	}

	public void onButton(Boolean buttonState) {
		joystickButtonSwitchStateChanged(buttonState);
	}

	public void onStickMoved(int x, int y) {
		joystickMoved(x, y);
	}
}
