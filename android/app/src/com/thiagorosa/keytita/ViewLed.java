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

package com.thiagorosa.keytita;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.thiagorosa.keytita.common.Constants;
import com.thiagorosa.keytita.common.Logger;
import com.thiagorosa.keytita.model.Effect;
import com.thiagorosa.keytita.model.EffectLogic;
import com.thiagorosa.keytita.model.Led;

import java.util.ArrayList;

public class ViewLed extends View {

    private static final int NUM_LEDS = Constants.TOTAL_NOTES;

    private static final int COLOR_BACKGROUND = 0xFFCCCCCC;

    private ArrayList<Led> mLeds = new ArrayList<>();
    private Effect mEffect = null;
    private Paint mPaintLed = null;

    private boolean isRunning = false;

    private int mCellWidth = 0;
    private int mCellHeight = 0;
    private int mCellMarginLeft = 0;
    private int mCellMarginTop = 0;
    private int mLedWidth = 0;
    private int mLedHeight = 0;
    private int mLedSpacing = 0;

    /*******************************************************************************************
     *******************************************************************************************/

    public ViewLed(Context context) {
        super(context);
        init();
    }

    public ViewLed(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewLed(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ViewLed(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        for (int i = 0; i < NUM_LEDS; i++) {
            mLeds.add(new Led(Color.WHITE));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isRunning = false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isRunning = true;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.EFFECT("Thread started!");
                while (isRunning) {
                    if (!show()) {
                        try {
                            reset();
                            Thread.sleep(500);
                        } catch (Exception ignored) {
                        }
                    }
                }
                Logger.EFFECT("Thread ended!");
            }
        });
        thread.start();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mCellWidth = 0;
        mCellHeight = 0;
        mCellMarginLeft = 0;
        mCellMarginTop = 0;
        mLedWidth = 0;
        mLedHeight = 0;
        mLedSpacing = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(COLOR_BACKGROUND);

        if (getWidth() == 0 || getHeight() == 0) {
            invalidate();
            return;
        }

        if (mCellWidth == 0 || mCellHeight == 0 || mLedWidth == 0 || mLedHeight == 0) {
            mCellWidth = getWidth();
            mCellHeight = getHeight();

            mLedWidth = 12;
            mLedHeight = 100;
            mLedSpacing = 9;

            mCellMarginLeft = (mCellWidth - (mLedWidth * NUM_LEDS + mLedSpacing * (NUM_LEDS - 1))) / 2;
            mCellMarginTop = (mCellHeight - mLedHeight) / 2;
        }

        for (int l = 0; l < NUM_LEDS; l++) {
            mPaintLed.setColor(Color.WHITE);
            canvas.drawRect(mCellMarginLeft + l * (mLedWidth + mLedSpacing), mCellMarginTop, mCellMarginLeft + l * (mLedWidth + mLedSpacing) + mLedWidth, mCellMarginTop + mLedHeight, mPaintLed);
            mPaintLed.setColor(mLeds.get(l).getColor());
            canvas.drawRect(mCellMarginLeft + l * (mLedWidth + mLedSpacing), mCellMarginTop, mCellMarginLeft + l * (mLedWidth + mLedSpacing) + mLedWidth, mCellMarginTop + mLedHeight, mPaintLed);
        }

        invalidate();
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public void reset() {
        for (int i = 0; i < NUM_LEDS; i++) {
            mLeds.get(i).setColor(Color.WHITE);
        }

        mPaintLed = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLed.setColor(Color.WHITE);
        invalidate();
    }

    public void clear() {
        mEffect = null;
        EffectLogic.disable();
    }

    public void load(Effect effect) {
        clear();
        reset();

        mEffect = effect;
        EffectLogic.disable();

        try {
            Thread.sleep(500);
        } catch (Exception ignored) {
        }

        EffectLogic.enable();

        invalidate();
    }

    private boolean show() {
        try {
            if (mEffect == null) {
                return false;
            }

            switch (mEffect.getType()) {
                case Effect.TYPE_COLOR:
                    if (!EffectLogic.colorSingle(mLeds, mEffect.getColor())) {
                        return false;
                    }
                    break;
                case Effect.TYPE_COLOR_RANDOM:
                    if (!EffectLogic.colorRandom(mLeds)) {
                        return false;
                    }
                    break;
                case Effect.TYPE_RAINBOW_FULL_FIXED:
                    if (!EffectLogic.rainbowCompleteStatic(mLeds)) {
                        return false;
                    }
                    break;
                case Effect.TYPE_RAINBOW_FULL_MOVING:
                    if (!EffectLogic.rainbowCompleteMoving(mLeds)) {
                        return false;
                    }
                    break;
                case Effect.TYPE_RAINBOW_SINGLE_SHIFTING:
                    if (!EffectLogic.rainbowSingleColorShifting(mLeds)) {
                        return false;
                    }
                    break;
                case Effect.TYPE_RAINBOW_GRADUAL_MOVING:
                    if (!EffectLogic.rainbowGradualMoving(mLeds)) {
                        return false;
                    }
                    break;
                case Effect.TYPE_RAINBOW_NOTE:
                    if (!EffectLogic.rainbowNote(mLeds)) {
                        return false;
                    }
                    break;
                case Effect.TYPE_RAINBOW_OCTAVE:
                    if (!EffectLogic.rainbowOctave(mLeds)) {
                        return false;
                    }
                    break;
            }
        } catch (Exception ignored) {
            return false;
        }

        try {
            Thread.sleep(10);
        } catch (Exception ignored) {
        }

        return true;
    }

}