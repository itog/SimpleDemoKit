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

package com.pigmal.android.util;

import android.util.Log;

public class Logger {
	static final boolean ENABLE_LOG = true;
	static final String LOG_TAG = "AccessoryDemo";
	
	private Logger() {}
	public static void v(String msg) {
		if(ENABLE_LOG)
			Log.v(LOG_TAG, msg);
	}
	public static void w(String msg) {
		if(ENABLE_LOG)
			Log.w(LOG_TAG, msg);
	}
	public static void i(String msg) {
		if(ENABLE_LOG)
			Log.i(LOG_TAG, msg);
	}
	public static void d(String msg) {
		if(ENABLE_LOG)
			Log.d(LOG_TAG, msg);
	}
	public static void e(String msg) {
		Log.e(LOG_TAG, msg);
	}
}
