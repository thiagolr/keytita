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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.thiagorosa.keytita.common.Logger;
import com.thiagorosa.keytita.model.Effect;
import com.thiagorosa.keytita.model.EffectLogic;
import com.thiagorosa.keytita.model.Led;

import java.util.ArrayList;

public class ViewLed extends View {

    private static final int NUM_LEDS = 52;

    private static final int COLOR_BACKGROUND = 0xFFCCCCCC;

    private ArrayList<Led> mLeds = new ArrayList<>();
    private Effect mEffect = null;
    private Paint mPaintLed = null;

    private boolean isRunning = false;

    private int mCellWidth = 0;
    private int mCellHeight = 0;
    private int mCellMargin = 0;
    private int mLedWidth = 0;

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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(COLOR_BACKGROUND);

        if (getWidth() == 0 || getHeight() == 0) {
            invalidate();
            return;
        }

        if (mCellWidth == 0 || mCellHeight == 0 || mLedWidth == 0) {
            mCellWidth = getWidth();
            mCellHeight = getHeight();
            mLedWidth = mCellWidth / (NUM_LEDS + 2);
            mCellMargin = mLedWidth;
        }

        for (int l = 0; l < NUM_LEDS; l++) {
            mPaintLed.setColor(Color.WHITE);
            canvas.drawRect(+mCellMargin + l * mLedWidth, mCellHeight / 2 - mLedWidth * 2, +mCellMargin + l * mLedWidth + mLedWidth / 2, mCellHeight / 2 + mLedWidth * 2, mPaintLed);
            mPaintLed.setColor(mLeds.get(l).getColor());
            canvas.drawRect(mCellMargin + l * mLedWidth, mCellHeight / 2 - mLedWidth * 2, mCellMargin + l * mLedWidth + mLedWidth / 2, mCellHeight / 2 + mLedWidth * 2, mPaintLed);
        }

        invalidate();
    }

    /*******************************************************************************************
     *******************************************************************************************/

    public void reset() {
        Logger.EFFECT("reset()");
        for (int i = 0; i < NUM_LEDS; i++) {
            mLeds.get(i).setColor(Color.WHITE);
        }

        mPaintLed = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLed.setColor(Color.WHITE);
        invalidate();
    }

    public void clear() {
        Logger.EFFECT("clear()");
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
                Logger.EFFECT("show() effect is null");
                return false;
            }

            switch (mEffect.getType()) {
                case Effect.TYPE_COLOR:
                    if (!EffectLogic.colorSingle(mLeds, mEffect)) {
                        return false;
                    }
                    break;
                case Effect.TYPE_COLOR_RANDOM:
                    if (!EffectLogic.colorRandom(mLeds, mEffect)) {
                        return false;
                    }
                    break;
                case Effect.TYPE_RAINBOW_FULL_FIXED:
                    if (!EffectLogic.rainbowCompleteStatic(mLeds, mEffect)) {
                        return false;
                    }
                    break;
                case Effect.TYPE_RAINBOW_FULL_MOVING:
                    if (!EffectLogic.rainbowCompleteMoving(mLeds, mEffect)) {
                        return false;
                    }
                    break;
                case Effect.TYPE_RAINBOW_SINGLE_SHIFTING:
                    if (!EffectLogic.rainbowSingleColorShifting(mLeds, mEffect)) {
                        return false;
                    }
                    break;
                case Effect.TYPE_RAINBOW_GRADUAL_MOVING:
                    if (!EffectLogic.rainbowGradualMoving(mLeds, mEffect)) {
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