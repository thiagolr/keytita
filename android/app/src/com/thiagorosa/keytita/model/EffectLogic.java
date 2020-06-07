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

    public static final int TYPE_CLEAR = 0;
    public static final int TYPE_UNDO = 1;
    public static final int TYPE_COLOR = 2;
    public static final int TYPE_WIPE = 3;
    public static final int TYPE_THEATER = 4;
    public static final int TYPE_RAINBOW = 5;
    public static final int TYPE_STATIC = 6;
    public static final int TYPE_TWINKLE = 7;
    public static final int TYPE_FADE = 8;
    public static final int TYPE_STROBE = 9;
    public static final int TYPE_SPARKLE = 10;
    public static final int TYPE_BOUNCE = 11;
    public static final int TYPE_RUNNING = 12;
    public static final int TYPE_BRIGHTNESS = 13;

    private ArrayList<Led> strip = null;
    private Random mRandom = new Random();

    public EffectLogic(ArrayList<Led> effect) {
        strip = effect;
    }

    /*******************************************************************************************
     *******************************************************************************************/

    // set the pixels one after the other with single color
    public void wipeColor(int color, int startIndex, int endIndex, int forward, int wait) {
        for (int i = startIndex; i < endIndex; i++) {
            setPixel(forward == 1 ? i : endIndex - (i - startIndex) - 1, color);
            showStrip();
            delay(wait);
        }
    }

    // set the pixels one after the other with single color on all rows simultaneously
    public void wipeColorDouble(int color, int startIndex1, int endIndex1, int startIndex2, int endIndex2, int startIndex3, int endIndex3, int forward1, int forward2, int forward3, int wait) {
        for (int i = startIndex1, j = startIndex2, k = startIndex3; i < endIndex1 && j < endIndex2 && k < endIndex3; i++, j++, k++) {
            setPixel(forward1 == 1 ? i : endIndex1 - (i - startIndex1) - 1, color);
            setPixel(forward2 == 1 ? j : endIndex2 - (j - startIndex2) - 1, color);
            setPixel(forward3 == 1 ? k : endIndex3 - (k - startIndex3) - 1, color);
            showStrip();
            delay(wait);
        }
    }

    // theater-style crawling lights with single color
    public void theaterColor(int color, int startIndex, int endIndex, int forward, int wait, int length, int repeat) {
        for (int j = 0; j < repeat; j++) {
            for (int q = 0; q < length; q++) {
                for (int i = startIndex; i + q < endIndex; i = i + length) {
                    setPixel((forward == 1 || forward == 3) ? i + q : endIndex - (i + q - startIndex) - 1, color);
                }

                showStrip();
                delay(wait);

                for (int i = startIndex; i + q < endIndex; i = i + length) {
                    setPixel((forward == 1 || forward == 3) ? i + q : endIndex - (i + q - startIndex) - 1, 0);
                }
            }
        }
    }

    // theater-style crawling lights with single color on all rows simultaneously
    public void theaterColorDouble(int color, int startIndex1, int endIndex1, int startIndex2, int endIndex2, int startIndex3, int endIndex3, int forward1, int forward2, int forward3, int wait, int length, int repeat) {
        for (int k = 0; k < repeat; k++) {
            for (int q = 0; q < length; q++) {
                for (int i = startIndex1, j = startIndex2, m = startIndex3; i + q < endIndex1 && j + q < endIndex2 && m + q < endIndex3; i = i + length, j = j + length, m = m + length) {
                    setPixel(forward1 == 1 ? i + q : endIndex1 - (i + q - startIndex1) - 1, color);
                    setPixel(forward2 == 1 ? j + q : endIndex2 - (j + q - startIndex2) - 1, color);
                    setPixel(forward3 == 1 ? m + q : endIndex3 - (m + q - startIndex3) - 1, color);
                }

                showStrip();
                delay(wait);

                for (int i = startIndex1, j = startIndex2, m = startIndex3; i + q < endIndex1 && j + q < endIndex2 && m + q < endIndex3; i = i + length, j = j + length, m = m + length) {
                    setPixel(forward1 == 1 ? i + q : endIndex1 - (i + q - startIndex1) - 1, 0);
                    setPixel(forward2 == 1 ? j + q : endIndex2 - (j + q - startIndex2) - 1, 0);
                    setPixel(forward3 == 1 ? m + q : endIndex3 - (m + q - startIndex3) - 1, 0);
                }
            }
        }
    }

    // theater-style crawling lights with rainbow colors
    public void theaterRainbow(int startIndex, int endIndex, int forward, int wait, int length) {
        for (int j = 0; j < 256; j++) {
            for (int q = 0; q < length; q++) {
                for (int i = startIndex; i + q < endIndex; i = i + length) {
                    setPixel(forward > 0 ? i + q : endIndex - (i + q - startIndex) - 1, colorWheel((i + j) % 255));
                }

                showStrip();
                delay(wait);

                for (int i = startIndex; i + q < endIndex; i = i + length) {
                    setPixel(forward > 0 ? i + q : endIndex - (i + q - startIndex) - 1, 0);
                }
            }
        }
    }

    // linear rainbow colors
    public void rainbowLinear(int startIndex, int endIndex, int wait, int repeat) {
        for (int k = 0; k <= repeat; k++) {
            for (int j = 0; j < 256; j++) {
                for (int i = startIndex; i < endIndex; i++) {
                    setPixel(i, colorWheel((i + j) & 255));
                }
                showStrip();
                delay(wait);
            }
        }
    }

    // cyclic rainbow colors
    public void rainbowCycle(int startIndex, int endIndex, int wait, int repeat) {
        for (int k = 0; k < repeat; k++) {
            for (int j = 0; j < 256; j++) {
                for (int i = startIndex; i < endIndex; i++) {
                    setPixel(i, colorWheel(((i * 256 / (endIndex - startIndex)) + j) & 255));
                }
                showStrip();
                delay(wait);
            }
        }
    }

    // static single color
    public void staticColor(int color, int startIndex, int endIndex, int wait, int repeat) {
        for (int j = 0; j < repeat; j++) {
            for (int i = startIndex; i < endIndex; i++) {
                setPixel(i, color);
            }
            showStrip();
            delay(wait);
        }
    }

    // static random color
    public void staticRandom(int startIndex, int endIndex, int wait, int repeat) {
        for (int j = 0; j < repeat; j++) {
            for (int i = startIndex; i < endIndex; i++) {
                setPixel(i, colorRandom());
            }
            showStrip();
            delay(wait);
        }
    }

    // twinkle effect with single color
    public void twinkleColor(int color, int startIndex, int endIndex, int wait, int count, int single, int repeat) {
        for (int j = 0; j < repeat; j++) {
            setAll(Color.BLACK, startIndex, endIndex);
            for (int i = 0; i < count; i++) {
                setPixel(startIndex + random(endIndex - startIndex), color);
                showStrip();
                delay(wait);
                if (single == 1) {
                    setAll(Color.BLACK, startIndex, endIndex);
                }
            }
        }
    }

    // twinkle effect with random color
    public void twinkleRandom(int startIndex, int endIndex, int wait, int count, int single, int repeat) {
        for (int j = 0; j < repeat; j++) {
            setAll(Color.BLACK, startIndex, endIndex);
            for (int i = 0; i < count; i++) {
                setPixel(startIndex + random(endIndex - startIndex), colorRandom());
                showStrip();
                delay(wait);
                if (single == 1) {
                    setAll(Color.BLACK, startIndex, endIndex);
                }
            }
        }
    }

    // single color fade in
    public void fadeInColor(int color, int startIndex, int endIndex, int wait, int repeat) {
        for (int j = 0; j < repeat; j++) {
            for (int k = 0; k < 256; k++) {
                for (int i = startIndex; i < endIndex; i++) {
                    setAll(Color.argb(k, Color.red(color), Color.green(color), Color.blue(color)), startIndex, endIndex);
                }
                showStrip();
                delay(wait);
            }
        }
    }

    // single color fade out
    public void fadeOutColor(int color, int startIndex, int endIndex, int wait, int repeat) {
        for (int j = 0; j < repeat; j++) {
            for (int k = 255; k >= 0; k--) {
                for (int i = startIndex; i < endIndex; i++) {
                    setAll(Color.argb(k, Color.red(color), Color.green(color), Color.blue(color)), startIndex, endIndex);
                }
                showStrip();
                delay(wait);
            }
        }
    }

    // strobe effect with single color and constant delay
    public void strobeColorConstant(int color, int startIndex, int endIndex, int wait, int repeat) {
        for (int j = 0; j < repeat; j++) {
            setAll(color, startIndex, endIndex);
            showStrip();
            delay(wait);
            setAll(Color.BLACK, startIndex, endIndex);
            showStrip();
            delay(wait);
        }
        delay(wait * 4);
    }

    // strobe effect with random color and constant delay
    public void strobeRandomConstant(int startIndex, int endIndex, int wait, int repeat) {
        for (int j = 0; j < repeat; j++) {
            setAll(colorRandom(), startIndex, endIndex);
            showStrip();
            delay(wait);
            setAll(Color.BLACK, startIndex, endIndex);
            showStrip();
            delay(wait);
        }
        delay(wait * 4);
    }

    // strobe effect with single color and random delay
    public void strobeColorCrazy(int color, int startIndex, int endIndex, int wait, int repeat) {
        for (int j = 0; j < repeat; j++) {
            setAll(color, startIndex, endIndex);
            showStrip();
            delay(10 + random(wait));
            setAll(Color.BLACK, startIndex, endIndex);
            showStrip();
            delay(10 + random(wait));
        }
        delay(wait * 4);
    }

    // strobe effect with random color and random delay
    public void strobeRandomCrazy(int startIndex, int endIndex, int wait, int repeat) {
        for (int j = 0; j < repeat; j++) {
            setAll(colorRandom(), startIndex, endIndex);
            showStrip();
            delay(10 + random(wait));
            setAll(Color.BLACK, startIndex, endIndex);
            showStrip();
            delay(10 + random(wait));
        }
        delay(wait * 4);
    }

    // sparkle effect with dark background and single color
    public void sparkleColorDark(int color, int startIndex, int endIndex, int wait, int length, int repeat) {
        for (int j = 0; j < repeat; j++) {
            setAll(Color.rgb(0, 0, 15), startIndex, endIndex);
            showStrip();

            for (int k = 0; k < length; k++) {
                int pixel = random(endIndex - startIndex);

                setPixel(startIndex + pixel, color);
                showStrip();
                delay(wait);
            }

            setAll(Color.rgb(0, 0, 15), startIndex, endIndex);
            showStrip();
        }
    }

    // sparkle effect with light background and single color
    public void sparkleColorLight(int color, int startIndex, int endIndex, int wait, int length, int repeat) {
        for (int j = 0; j < repeat; j++) {
            setAll(Color.rgb(200, 200, 200), startIndex, endIndex);
            showStrip();

            for (int k = 0; k < length; k++) {
                int pixel = random(endIndex - startIndex);

                setPixel(startIndex + pixel, color);
                showStrip();
                delay(wait);
            }

            setAll(Color.rgb(200, 200, 200), startIndex, endIndex);
            showStrip();
        }
    }

    // bounce effect with single color
    public void bounceColor(int color, int startIndex, int endIndex, int wait, int length, int repeat) {
        for (int k = 0; k < repeat; k++) {
            for (int i = startIndex; i < endIndex - length - 2; i++) {
                setAll(Color.BLACK, startIndex, endIndex);
                setPixel(i, Color.red(color) / 2, Color.green(color) / 2, Color.blue(color) / 2);
                for (int j = 1; j <= length; j++) {
                    setPixel(i + j, color);
                }
                setPixel(i + length + 1, Color.red(color) / 2, Color.green(color) / 2, Color.blue(color) / 2);
                showStrip();
                delay(wait);
            }
            delay(wait);

            for (int i = endIndex - length - 2; i > 0; i--) {
                setAll(Color.BLACK, startIndex, endIndex);
                setPixel(i, Color.red(color) / 2, Color.green(color) / 2, Color.blue(color) / 2);
                for (int j = 1; j <= length; j++) {
                    setPixel(i + j, color);
                }
                setPixel(i + length + 1, Color.red(color) / 2, Color.green(color) / 2, Color.blue(color) / 2);
                showStrip();
                delay(wait);
            }
            delay(wait);
        }
    }

    // running effect with single color
    public void runningColor(int color, int startIndex, int endIndex, int wait, int repeat) {
        for (int j = 0; j < repeat; j++) {
            int pos = 0;
            for (int i = startIndex; i < endIndex * 2; i++) {
                pos++;
                for (i = 0; i < endIndex; i++) {
                    setPixel(i, (int) ((Math.sin(i + pos) * 127 + 128) / 255) * Color.red(color), (int) ((Math.sin(i + pos) * 127 + 128) / 255) * Color.green(color), (int) ((Math.sin(i + pos) * 127 + 128) / 255) * Color.blue(color));
                }
                showStrip();
                delay(wait);
            }
        }
    }

    // running effect with random color
    public void runningRandom(int startIndex, int endIndex, int wait, int repeat) {
        for (int j = 0; j < repeat; j++) {
            int pos = 0;
            for (int i = startIndex; i < endIndex * 2; i++) {
                pos++;
                for (int k = 0; k < endIndex; k++) {
                    setPixel(k, (int) ((Math.sin(k + pos) * 127 + 128) / 255) * random(256), (int) ((Math.sin(k + pos) * 127 + 128) / 255) * random(256), (int) ((Math.sin(k + pos) * 127 + 128) / 255) * random(256));
                }
                showStrip();
                delay(wait);
            }
        }
    }

    /*******************************************************************************************
     *******************************************************************************************/

    private void showStrip() {
    }

    private void setPixel(int pixel, int red, int green, int blue) {
        setPixel(pixel, Color.rgb(red, green, blue));
    }

    private void setPixel(int pixel, int color) {
        strip.get(pixel).setColor(color);
    }

    private void setAll(int red, int green, int blue, int startIndex, int endIndex) {
        setAll(Color.rgb(red, green, blue), startIndex, endIndex);
    }

    private void setAll(int color, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            setPixel(i, color);
        }
    }

    private int colorRandom() {
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

    private int colorWheel(int pos) {
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

    private int random(int max) {
        return mRandom.nextInt(max);
    }

    private void delay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
    }

}
