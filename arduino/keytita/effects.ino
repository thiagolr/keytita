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

// static random color
CRGB colorRandom() {
    return pickRandomColor();
}

// complete static rainbow
CRGB rainbowCompleteStatic(unsigned int i) {
    return pickWheelColor((i * 256 / NUM_LEDS) & 255);
}

// complete moving rainbow colors
CRGB rainbowCompleteMoving(unsigned int i) {
    return pickWheelColor(((i * 256 / NUM_LEDS) + movingIndex) & 255);
}

// single color shifting rainbow
CRGB rainbowSingleColorShifting() {
    return pickWheelColor(movingIndex & 255);
}

// gradual moving rainbow colors
CRGB rainbowGradualMoving(unsigned int i) {
    return pickWheelColor((i + movingIndex) & 255);    
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
CRGB pickWheelColor(long pos) {
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

// pick a note color
CRGB pickRainbowColor(byte index) {
    switch (index) {
    case 0:
        return CRGB(0xFF, 0, 0);
    case 1:
        return CRGB(0xFF, 0x80, 0);
    case 2:
        return CRGB(0xFF, 0xFF, 0);
    case 3:
        return CRGB(0, 0x80, 0);
    case 4:
        return CRGB(0, 0, 0xFF);
    case 5:
        return CRGB(0xFF, 0, 0xFF);
    case 6:
        return CRGB(0xFF, 0x69, 0xB4);        
    }
    return CRGB(255, 255, 255);
}
