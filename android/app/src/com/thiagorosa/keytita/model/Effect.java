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

import com.thiagorosa.keytita.common.Logger;
import com.thiagorosa.keytita.manager.BluetoothManager;

import static com.thiagorosa.keytita.model.EffectLogic.TYPE_BOUNCE;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_BRIGHTNESS;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_CLEAR;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_COLOR;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_FADE;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_RAINBOW;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_RUNNING;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_SPARKLE;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_STATIC;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_STROBE;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_THEATER;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_TWINKLE;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_UNDO;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_WIPE;

public class Effect {

    private int mColor = Color.WHITE;
    private int mEffect = 0;
    private int mIndex = 0;
    private int mType = 0;
    private int mSpeed = 0;
    private int mLength = 0;
    private int mRepeat = 0;

    public Effect(int color, int effect, int index, int type, int speed, int length) {
        mColor = color;
        mEffect = effect;
        mIndex = index;
        mType = type;
        mSpeed = speed;
        mLength = length;
        mRepeat = 1;//repeat;

        show();

        Logger.EFFECT("new: " + toString());
    }

    public Effect(String info) {
        try {
            String values[] = info.split("#");
            if (values.length == 7) {
                mEffect = Integer.parseInt(values[0]);
                mIndex = Integer.parseInt(values[1]);
                mType = Integer.parseInt(values[2]);
                mSpeed = Integer.parseInt(values[3]);
                mLength = Integer.parseInt(values[4]);
                mRepeat = Integer.parseInt(values[5]);
                mColor = Integer.parseInt(values[6]);
            }
        } catch (Exception ignored) {
        }
        Logger.EFFECT("invalid: " + info);
    }

    public void show() {
        BluetoothManager.getInstance().write(TYPE_COLOR, Color.red(mColor), Color.green(mColor), Color.blue(mColor), 0, 0);
        BluetoothManager.getInstance().write(mEffect, mIndex, mType, mSpeed, mLength, mRepeat);
    }

    public int getEffect() {
        return mEffect;
    }

    public int getIndex() {
        return mIndex;
    }

    public int getType() {
        return mType;
    }

    public int getSpeed() {
        return mSpeed;
    }

    public int getLength() {
        return mLength;
    }

    public int getRepeat() {
        return mRepeat;
    }

    public int getColor() {
        return mColor;
    }

    public String getInfo() {
        return mEffect + "#" + mIndex + "#" + mType + "#" + mSpeed + "#" + mLength + "#" + mRepeat + "#" + mColor;
    }

    public static void clear() {
        BluetoothManager.getInstance().write(TYPE_CLEAR, 0, 0, 0, 0, 0);
    }

    public static void brightness(int value) {
        double a = 9.7758463166360387E-01;
        double b = 5.5498961535023345E-02;
        double result = Math.floor((a * Math.exp(b * value) + .5)) - 1;

        BluetoothManager.getInstance().write(TYPE_BRIGHTNESS, (int) result, 0, 0, 0, 0);
    }

    public static void undo() {
        BluetoothManager.getInstance().write(TYPE_UNDO, 0, 0, 0, 0, 0);
    }

    @Override
    public String toString() {
        String type = "";
        switch (mEffect) {
            case TYPE_WIPE:
                type = "wp";
                break;
            case TYPE_THEATER:
                type = "th";
                break;
            case TYPE_RAINBOW:
                type = "rb";
                break;
            case TYPE_STATIC:
                type = "s";
                break;
            case TYPE_TWINKLE:
                type = "tw";
                break;
            case TYPE_FADE:
                type = "w";
                break;
            case TYPE_STROBE:
                type = "st";
                break;
            case TYPE_SPARKLE:
                type = "sp";
                break;
            case TYPE_BOUNCE:
                type = "bo";
                break;
            case TYPE_RUNNING:
                type = "ru";
                break;
        }

        String direction = "";
        if (mType == 1 || mType == 3) {
            direction = "R";
        } else if (mType == 0 || mType == 2) {
            direction = "L";
        }

        String index = "";
        switch (mIndex) {
            case 1:
                index = "A";
                break;
            case 2:
                index = "B";
                break;
            case 3:
                index = "C";
                break;
            case 4:
                index = "*";
                break;
        }

        return index + type + direction;
    }

}
