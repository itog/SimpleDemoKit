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

public class Util {
	public static double getTemperatureByRawdata(int temperatureFromArduino) {
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
		//double temperatureF = (9.0 / 5.0) * ambientTemperatureC + 32.0;
		//mTemperature.setText(mTemperatureFormatter.format(temperatureF));
		return ambientTemperatureC;
	}
}
