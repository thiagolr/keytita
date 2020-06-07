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

package com.thiagorosa.keytita.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class PreferencesManager {

    private static final PreferencesManager INSTANCE = new PreferencesManager();

    private static final String PREFS_STRING_DEVICE = "device";
    private static final String PREFS_INTEGER_COLOR = "color";

    private SharedPreferences mSharedPreferences = null;

    /*******************************************************************************************
     *******************************************************************************************/

    private PreferencesManager() {
    }

    public static PreferencesManager getInstance() {
        return INSTANCE;
    }


    public void init(Context context) {
        mSharedPreferences = context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
    }

    /*******************************************************************************************
     *******************************************************************************************/

    private String getConfigString(String item, String defaultValue) {
        return mSharedPreferences.getString(item, defaultValue);
    }

    private void setConfigString(String item, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(item, value);
        editor.apply();
    }

    public String getDeviceMAC() {
        return getConfigString(PREFS_STRING_DEVICE, "");
    }

    public void setDeviceMAC(String value) {
        setConfigString(PREFS_STRING_DEVICE, value);
    }

    /*******************************************************************************************
     *******************************************************************************************/

    private int getConfigInteger(String item, int defaultValue) {
        return mSharedPreferences.getInt(item, defaultValue);
    }

    private void setConfigInteger(String item, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(item, value);
        editor.apply();
    }

    public int getColor(int index) {
        return getConfigInteger(PREFS_INTEGER_COLOR + index, Color.TRANSPARENT);
    }

    public void setColor(int index, int value) {
        setConfigInteger(PREFS_INTEGER_COLOR + index, value);
    }

}
