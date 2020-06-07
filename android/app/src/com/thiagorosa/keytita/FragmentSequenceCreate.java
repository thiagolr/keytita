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

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.thiagorosa.keytita.common.CustomFragment;
import com.thiagorosa.keytita.manager.PreferencesManager;
import com.thiagorosa.keytita.model.Effect;

import java.util.ArrayList;

import static com.thiagorosa.keytita.model.EffectLogic.TYPE_BOUNCE;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_FADE;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_RAINBOW;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_RUNNING;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_SPARKLE;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_STATIC;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_STROBE;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_THEATER;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_TWINKLE;
import static com.thiagorosa.keytita.model.EffectLogic.TYPE_WIPE;

public class FragmentSequenceCreate extends CustomFragment {

    private ArrayList<Effect> mEffects = new ArrayList<>();

    private TextView mEffectName = null;
    private ViewLed mViewLed = null;

    private Button mButtonRed = null;
    private Button mButtonGreen = null;
    private Button mButtonBlue = null;
    private Button mButtonYellow = null;
    private Button mButtonMagenta = null;
    private Button mButtonCyan = null;
    private Button mButtonWhite = null;
    private Button mButtonBlack = null;

    private Button mButtonMore1 = null;
    private Button mButtonMore2 = null;
    private Button mButtonMore3 = null;
    private Button mButtonMore4 = null;
    private Button mButtonMore5 = null;
    private Button mButtonMore6 = null;
    private Button mButtonMore7 = null;
    private Button mButtonMore8 = null;

    private Button mButtonRainbowLinear = null;
    private Button mButtonRainbowCycle = null;
    private Button mButtonStaticColor = null;
    private Button mButtonStaticRandom = null;
    private Button mButtonRunningColor = null;
    private Button mButtonRunningRandom = null;
    private Button mButtonBounceColor = null;
    private Button mButtonWipeA1L = null;
    private Button mButtonTheaterA1L = null;
    private Button mButtonTheaterRainbowL = null;

    private SeekBar mSpeedValue = null;
    private TextView mSpeedText = null;
    private int mSpeed = 0;

    private SeekBar mLengthValue = null;
    private TextView mLengthText = null;
    private int mLength = 0;

    private SeekBar mBrightnessValue = null;
    private TextView mBrightnessText = null;
    private int mBrightness = 0;

    private int mColor = Color.WHITE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setup, null);

        mEffectName = view.findViewById(R.id.sequence_name);

        mViewLed = view.findViewById(R.id.led_view);
        mViewLed.load(mEffects);

        mButtonRed = view.findViewById(R.id.red);
        mButtonRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.RED);
            }
        });

        mButtonGreen = view.findViewById(R.id.green);
        mButtonGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.GREEN);
            }
        });

        mButtonBlue = view.findViewById(R.id.blue);
        mButtonBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.BLUE);
            }
        });

        mButtonYellow = view.findViewById(R.id.yellow);
        mButtonYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.YELLOW);
            }
        });

        mButtonMagenta = view.findViewById(R.id.magenta);
        mButtonMagenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.MAGENTA);
            }
        });

        mButtonCyan = view.findViewById(R.id.cyan);
        mButtonCyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.CYAN);
            }
        });

        mButtonWhite = view.findViewById(R.id.white);
        mButtonWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.WHITE);
            }
        });

        mButtonBlack = view.findViewById(R.id.black);
        mButtonBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateColors(Color.BLACK);
            }
        });

        mButtonMore1 = view.findViewById(R.id.more1);
        mButtonMore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(1);
            }
        });

        mButtonMore2 = view.findViewById(R.id.more2);
        mButtonMore2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(2);
            }
        });

        mButtonMore3 = view.findViewById(R.id.more3);
        mButtonMore3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(3);
            }
        });

        mButtonMore4 = view.findViewById(R.id.more4);
        mButtonMore4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(4);
            }
        });

        mButtonMore5 = view.findViewById(R.id.more5);
        mButtonMore5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(5);
            }
        });

        mButtonMore6 = view.findViewById(R.id.more6);
        mButtonMore6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(6);
            }
        });

        mButtonMore7 = view.findViewById(R.id.more7);
        mButtonMore7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(7);
            }
        });

        mButtonMore8 = view.findViewById(R.id.more8);
        mButtonMore8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(8);
            }
        });


        mButtonRainbowLinear = view.findViewById(R.id.rainbow_linear);
        mButtonRainbowLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEffectName.setText(R.string.rainbow_linear);
                rainbow(4, 1);
            }
        });

        mButtonRainbowCycle = view.findViewById(R.id.rainbow_cycle);
        mButtonRainbowCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEffectName.setText(R.string.rainbow_cycle);
                rainbow(4, 2);
            }
        });

        mButtonStaticColor = view.findViewById(R.id.static_color);
        mButtonStaticColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEffectName.setText(R.string.static_color);
                static2(4, 1);
            }
        });

        mButtonStaticRandom = view.findViewById(R.id.static_random);
        mButtonStaticRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEffectName.setText(R.string.static_random);
                static2(4, 2);
            }
        });

        mButtonRunningColor = view.findViewById(R.id.running_color);
        mButtonRunningColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEffectName.setText(R.string.running_color);
                running(4, 1);
            }
        });

        mButtonRunningRandom = view.findViewById(R.id.running_random);
        mButtonRunningRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEffectName.setText(R.string.running_random);
                running(4, 2);
            }
        });

        mButtonBounceColor = view.findViewById(R.id.bounce_color);
        mButtonBounceColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEffectName.setText(R.string.bounce_color);
                bounce(4, 1);
            }
        });

        mButtonWipeA1L = view.findViewById(R.id.wipe_a1l);
        mButtonWipeA1L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEffectName.setText(R.string.wipe_color);
                wipe(1, 0);
            }
        });

        mButtonTheaterA1L = view.findViewById(R.id.theater_a1l);
        mButtonTheaterA1L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEffectName.setText(R.string.theater_color);
                theater(1, 0);
            }
        });

        mButtonTheaterRainbowL = view.findViewById(R.id.theater_rainbowl);
        mButtonTheaterRainbowL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEffectName.setText(R.string.theater_rainbow);
                theater(4, 2);
            }
        });

        mSpeedValue = view.findViewById(R.id.speed_value);
        mSpeedValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSpeedText.setText(progress + getText(R.string.create_milliseconds).toString());
                mSpeed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mSpeedText = view.findViewById(R.id.speed_text);
        mSpeedText.setText(mSpeedValue.getProgress() + getText(R.string.create_milliseconds).toString());
        mSpeed = mSpeedValue.getProgress();

        mLengthValue = view.findViewById(R.id.length_value);
        mLengthValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress += 1;
                mLengthText.setText(progress + " " + getText(R.string.create_leds).toString());
                mLength = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mLengthText = view.findViewById(R.id.length_text);
        mLength = mLengthValue.getProgress() + 1;
        mLengthText.setText(mLength + " " + getText(R.string.create_leds).toString());

        mBrightnessValue = view.findViewById(R.id.brightness_value);
        mBrightnessValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBrightnessText.setText(progress + getText(R.string.create_percentage).toString());
                mBrightness = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Effect.brightness(mBrightness);
            }
        });

        mBrightnessText = view.findViewById(R.id.brightness_text);
        mBrightness = mBrightnessValue.getProgress();
        mBrightnessText.setText(mBrightness + getText(R.string.create_percentage).toString());

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(null);

        update(R.string.setup_title, true);

        updateColors(mColor);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.create, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear) {
            Effect.clear();
            mEffects.clear();
            mEffectName.setText("");
            updateSequence();
            return true;
        } else if (item.getItemId() == R.id.reset) {
            PreferencesManager.getInstance().setColor(1, Color.TRANSPARENT);
            PreferencesManager.getInstance().setColor(2, Color.TRANSPARENT);
            PreferencesManager.getInstance().setColor(3, Color.TRANSPARENT);
            PreferencesManager.getInstance().setColor(4, Color.TRANSPARENT);
            PreferencesManager.getInstance().setColor(5, Color.TRANSPARENT);
            PreferencesManager.getInstance().setColor(6, Color.TRANSPARENT);
            PreferencesManager.getInstance().setColor(7, Color.TRANSPARENT);
            PreferencesManager.getInstance().setColor(8, Color.TRANSPARENT);
            updateColors(Color.WHITE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateColors(int color) {
        mColor = color;

        mButtonMore1.setBackgroundColor(PreferencesManager.getInstance().getColor(1));
        mButtonMore2.setBackgroundColor(PreferencesManager.getInstance().getColor(2));
        mButtonMore3.setBackgroundColor(PreferencesManager.getInstance().getColor(3));
        mButtonMore4.setBackgroundColor(PreferencesManager.getInstance().getColor(4));
        mButtonMore5.setBackgroundColor(PreferencesManager.getInstance().getColor(5));
        mButtonMore6.setBackgroundColor(PreferencesManager.getInstance().getColor(6));
        mButtonMore7.setBackgroundColor(PreferencesManager.getInstance().getColor(7));
        mButtonMore8.setBackgroundColor(PreferencesManager.getInstance().getColor(8));

        mButtonMore1.setText(PreferencesManager.getInstance().getColor(1) == 0 ? "+" : "");
        mButtonMore2.setText(PreferencesManager.getInstance().getColor(2) == 0 ? "+" : "");
        mButtonMore3.setText(PreferencesManager.getInstance().getColor(3) == 0 ? "+" : "");
        mButtonMore4.setText(PreferencesManager.getInstance().getColor(4) == 0 ? "+" : "");
        mButtonMore5.setText(PreferencesManager.getInstance().getColor(5) == 0 ? "+" : "");
        mButtonMore6.setText(PreferencesManager.getInstance().getColor(6) == 0 ? "+" : "");
        mButtonMore7.setText(PreferencesManager.getInstance().getColor(7) == 0 ? "+" : "");
        mButtonMore8.setText(PreferencesManager.getInstance().getColor(8) == 0 ? "+" : "");

        mButtonStaticColor.setTextColor(mColor);
        mButtonRunningColor.setTextColor(mColor);

        mButtonBounceColor.setTextColor(mColor);

        mButtonWipeA1L.setTextColor(mColor);
        mButtonTheaterA1L.setTextColor(mColor);
    }

    private void updateSequence() {
        // mListEffect.removeAllViews();

        for (Effect effect : mEffects) {
            TextView tv = new TextView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = 10;
            lp.rightMargin = 10;
            tv.setLayoutParams(lp);
            tv.setText(effect.toString());
            tv.setTextColor(effect.getColor());

            // mListEffect.addView(tv);
        }

        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    private void selectColor(final int index) {
        if (PreferencesManager.getInstance().getColor(index) == 0) {
            final ColorPicker cp = new ColorPicker(getActivity(), Color.red(PreferencesManager.getInstance().getColor(index)), Color.green(PreferencesManager.getInstance().getColor(index)), Color.blue(PreferencesManager.getInstance().getColor(index)));
            cp.show();

            Button ok = (Button) cp.findViewById(R.id.okColorButton);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int color = Color.rgb(cp.getRed(), cp.getGreen(), cp.getBlue());
                    PreferencesManager.getInstance().setColor(index, color);
                    cp.dismiss();

                    updateColors(color);
                }
            });
        } else {
            updateColors(PreferencesManager.getInstance().getColor(index));
        }
    }

    private void wipe(int index, int direction) {
        mEffects.add(new Effect(mColor, TYPE_WIPE, index, direction, mSpeed, 0));
        updateSequence();
    }

    private void theater(int index, int direction) {
        mEffects.add(new Effect(mColor, TYPE_THEATER, index, direction, mSpeed, mLength));
        updateSequence();
    }

    private void rainbow(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_RAINBOW, index, type, mSpeed, 0));
        updateSequence();
    }

    private void static2(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_STATIC, index, type, mSpeed, 0));
        updateSequence();
    }

    private void running(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_RUNNING, index, type, mSpeed, 0));
        updateSequence();
    }

    private void fade(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_FADE, index, type, mSpeed, 0));
        updateSequence();
    }

    private void twinkle(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_TWINKLE, index, type, mSpeed, mLength));
        updateSequence();
    }

    private void strobe(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_STROBE, index, type, mSpeed, 0));
        updateSequence();
    }

    private void sparkle(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_SPARKLE, index, type, mSpeed, mLength));
        updateSequence();
    }

    private void bounce(int index, int type) {
        mEffects.add(new Effect(mColor, TYPE_BOUNCE, index, type, mSpeed, mLength));
        updateSequence();
    }

}