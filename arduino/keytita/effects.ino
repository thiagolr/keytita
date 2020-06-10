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

// NEW IDEA: color by note
// NEW IDEA: color by octave
// NEW IDEA: rainbow by octave

// static single color
void colorSingle(CRGB color) {
    for (int i = 0; i < NUM_LEDS; i++) {
        colors[i] = color;
    }
}

// static random color
void colorRandom() {
    for (int i = 0; i < NUM_LEDS; i++) {
        colors[i] = pickRandomColor();
    }
}

// complete static rainbow
void rainbowCompleteStatic() {
    for (int i = 0; i < NUM_LEDS; i++) {
        colors[i] = pickWheelColor((i * 256 / NUM_LEDS) & 255);
    }
}

// complete moving rainbow colors
int rainbowCompleteMovingIndex = 0;
void rainbowCompleteMoving() {
    for (int i = 0; i < NUM_LEDS; i++) {
        colors[i] = pickWheelColor(((i * 256 / NUM_LEDS) + rainbowCompleteMovingIndex) & 255);
    }
    rainbowCompleteMovingIndex++;
}

// single color shifting rainbow
int rainbowSingleColorShiftingIndex = 0;
void rainbowSingleColorShifting() {
    for (int i = 0; i < NUM_LEDS; i++) {
        colors[i] = pickWheelColor(rainbowSingleColorShiftingIndex & 255);
    }
    rainbowSingleColorShiftingIndex++;
}

// gradual moving rainbow colors
int rainbowGradualMovingIndex = 0;
void rainbowGradualMoving() {
    for (int i = 0; i < NUM_LEDS; i++) {
        colors[i] = pickWheelColor((i + rainbowGradualMovingIndex) & 255);
    }
    rainbowGradualMovingIndex++;
}

// ###################################################################
// ###################################################################

// get a random color
CRGB pickRandomColor() {
    switch (random(6)) {
    case 0:
        return CRGB(255, 0, 0);
    case 1:
        return CRGB(0, 255, 0);
    case 2:
        return CRGB(0, 0, 255);
    case 3:
        return CRGB(255, 255, 0);
    case 4:
        return CRGB(255, 0, 255);
    case 5:
        return CRGB(0, 255, 255);
    }
    return CRGB(255, 255, 255);
}

// pick a rainbow color
CRGB pickWheelColor(uint8_t pos) {
    pos = 255 - pos;
    if (pos < 85) {
        return CRGB(255 - pos * 3, 0, pos * 3);
    }
    if (pos < 170) {
        pos -= 85;
        return CRGB(0, pos * 3, 255 - pos * 3);
    }
    pos -= 170;
    return CRGB(pos * 3, 255 - pos * 3, 0);
}
