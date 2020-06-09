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

import java.util.ArrayList;
import java.util.Random;

public class EffectLogic {

    private static Random mRandom = new Random();

    private static boolean isEnabled = false;

    /*******************************************************************************************
     *******************************************************************************************/

    // static single color
    public static boolean colorSingle(ArrayList<Led> strip, Effect effect) {
        for (int i = 0; i < strip.size(); i++) {
            setPixel(strip, i, effect.getColor());
        }
        if (!showStrip()) {
            return false;
        }
        delay(effect.getSpeed());
        return true;
    }

    // static random colors
    public static boolean colorRandom(ArrayList<Led> strip, Effect effect) {
        for (int i = 0; i < strip.size(); i++) {
            setPixel(strip, i, colorRandom());
        }
        if (!showStrip()) {
            return false;
        }
        delay(effect.getSpeed());
        return true;
    }

    // complete static rainbow
    public static boolean rainbowCompleteStatic(ArrayList<Led> strip, Effect effect) {
        for (int i = 0; i < strip.size(); i++) {
            setPixel(strip, i, colorWheel((i * 256 / strip.size()) & 255));
        }
        if (!showStrip()) {
            return false;
        }
        delay(effect.getSpeed());
        return true;
    }

    // complete moving rainbow colors
    public static boolean rainbowCompleteMoving(ArrayList<Led> strip, Effect effect) {
        for (int j = 0; j < 256; j++) {
            for (int i = 0; i < strip.size(); i++) {
                setPixel(strip, i, colorWheel(((i * 256 / strip.size()) + j) & 255));
            }
            if (!showStrip()) {
                return false;
            }
            delay(effect.getSpeed());
        }
        return true;
    }

    // single color shifting rainbow
    public static boolean rainbowSingleColorShifting(ArrayList<Led> strip, Effect effect) {
        for (int j = 0; j < 256; j++) {
            setAll(strip, colorWheel(j & 255));
            if (!showStrip()) {
                return false;
            }
            delay(effect.getSpeed());
        }
        return true;
    }

    // gradual moving rainbow colors
    public static boolean rainbowGradualMoving(ArrayList<Led> strip, Effect effect) {
        for (int j = 0; j < 256; j++) {
            for (int i = 0; i < strip.size(); i++) {
                setPixel(strip, i, colorWheel((i + j) & 255));
            }
            if (!showStrip()) {
                return false;
            }
            delay(effect.getSpeed());
        }
        return true;
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public static void enable() {
        isEnabled = true;
    }

    public static void disable() {
        isEnabled = false;
    }

    private static boolean showStrip() {
        return isEnabled;
    }

    private static void setPixel(ArrayList<Led> strip, int pixel, int red, int green, int blue) {
        setPixel(strip, pixel, Color.rgb(red, green, blue));
    }

    private static void setPixel(ArrayList<Led> strip, int pixel, int color) {
        strip.get(pixel).setColor(color);
    }

    private static void setAll(ArrayList<Led> strip, int red, int green, int blue) {
        setAll(strip, Color.rgb(red, green, blue));
    }

    private static void setAll(ArrayList<Led> strip, int color) {
        for (int i = 0; i < strip.size(); i++) {
            setPixel(strip, i, color);
        }
    }

    private static int colorRandom() {
        switch (random(6)) {
            case 0:
                return 0xFFFF0000;
            case 1:
                return 0xFF00FF00;
            case 2:
                return 0xFF0000FF;
            case 3:
                return 0xFFFFFF00;
            case 4:
                return 0xFFFF00FF;
            case 5:
                return 0xFF00FFFF;
        }
        return 0xFFFFFFFF;
    }

    private static int colorWheel(int pos) {
        pos = 255 - pos;
        if (pos < 85) {
            return Color.rgb(255 - pos * 3, 0, pos * 3);
        }
        if (pos < 170) {
            pos -= 85;
            return Color.rgb(0, pos * 3, 255 - pos * 3);
        }
        pos -= 170;
        return Color.rgb(pos * 3, 255 - pos * 3, 0);
    }

    private static int random(int max) {
        return mRandom.nextInt(max);
    }

    private static void delay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
    }

}
