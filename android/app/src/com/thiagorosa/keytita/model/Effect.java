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

package com.thiagorosa.keytita.model;

import android.graphics.Color;

import com.thiagorosa.keytita.manager.BluetoothManager;

public class Effect {

    private static final int TYPE_CLEAR = 0;
    public static final int TYPE_COLOR = 1;
    private static final int TYPE_SPEED = 2;
    private static final int TYPE_BRIGHTNESS = 3;

    public static final int TYPE_COLOR_RANDOM = 11;
    public static final int TYPE_RAINBOW_FULL_FIXED = 12;
    public static final int TYPE_RAINBOW_FULL_MOVING = 13;
    public static final int TYPE_RAINBOW_SINGLE_SHIFTING = 14;
    public static final int TYPE_RAINBOW_GRADUAL_MOVING = 15;
    public static final int TYPE_RAINBOW_NOTE = 16;
    public static final int TYPE_RAINBOW_OCTAVE = 17;

    private int mType;
    private int mColor;

    public Effect(int type) {
        mType = type;
        type();
    }

    public Effect(int type, int color) {
        mType = type;
        mColor = color;
        color();
    }

    public int getType() {
        return mType;
    }

    public int getColor() {
        return mColor;
    }

    private void type() {
        BluetoothManager.getInstance().write(mType);
    }

    public void color() {
        BluetoothManager.getInstance().write(mType, Color.red(mColor), Color.green(mColor), Color.blue(mColor));
    }

    public static void clear() {
        BluetoothManager.getInstance().write(TYPE_CLEAR);
    }

    public static void speed(int value) {
        BluetoothManager.getInstance().write(TYPE_SPEED, value);
    }

    public static void brightness(int value) {
        double a = 9.7758463166360387E-01;
        double b = 5.5498961535023345E-02;
        double result = Math.floor((a * Math.exp(b * value) + .5)) - 1;
        BluetoothManager.getInstance().write(TYPE_BRIGHTNESS, (int) result);
    }

}
