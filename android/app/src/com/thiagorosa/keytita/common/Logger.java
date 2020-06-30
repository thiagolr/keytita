/*
  Copyright (c) 2020 Thiago Lopes Rosa

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.thiagorosa.keytita.common;

import android.util.Log;

public class Logger {

    private static final String TAG = "[KEYTITA]";

    public static void BT(String text) {
        if (Constants.DEBUG_LOG_BLUETOOTH) {
            Log.d(TAG, "[BT] " + text);
        }
    }

    public static void EFFECT(String text) {
        if (Constants.DEBUG_LOG_EFFECT) {
            Log.d(TAG, "[EF] " + text);
        }
    }

    public static void SERIAL(String text) {
        if (Constants.DEBUG_LOG_SERIAL) {
            Log.d(TAG, text);
        }
    }

    public static void USB(String text) {
        if (Constants.DEBUG_LOG_USB) {
            Log.d(TAG, "[USB] " + text);
        }
    }

}
