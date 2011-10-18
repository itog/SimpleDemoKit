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

public class DebugUtil {
	private static final String LOG_TAG = "AccessoryDemoDebug";
	private static final boolean TRACE = true;
	private static final boolean DEBUG = true;
	
	public static void trace() {
		if(TRACE) Log.d(LOG_TAG, Thread.currentThread().getStackTrace()[3].getMethodName());
	}
	
	public static void print(String msg) {
		if(DEBUG) Log.d(LOG_TAG, "[" + Thread.currentThread().getStackTrace()[2].getMethodName() + "]" + msg);
	}
}
