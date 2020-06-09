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

// static single color
void colorSingle(CRGB color) {
    for (int i = 0; i < NUM_LEDS; i++) {
        colors[i] = color;
    }
}

// static random color
void colorRandom() {
    for (int i = 0; i < NUM_LEDS; i++) {
        colors[i] = pickRandom();
    }
}

// complete static rainbow
void rainbowCompleteStatic() {
    for (int i = 0; i < NUM_LEDS; i++) {
        colors[i] = colorWheel((i * 256 / NUM_LEDS) & 255);
    }
}

// complete moving rainbow colors
int j = 0;
void rainbowCompleteMoving() {
    for (int i = 0; i < NUM_LEDS; i++) {
        colors[i] = colorWheel(((i * 256 / NUM_LEDS) + j) & 255);
    }
    j++;
}

// single color shifting rainbow
int k = 0;
void rainbowSingleColorShifting() {
    for (int i = 0; i < NUM_LEDS; i++) {
        colors[i] = colorWheel(k & 255);
    }
    k++;
}

// gradual moving rainbow colors
int m = 0;
void rainbowGradualMoving() {
    for (int i = 0; i < NUM_LEDS; i++) {
        colors[i] = colorWheel((i + m) & 255);
    }
    m++;
}

// ###################################################################
// ###################################################################

// get a random color
CRGB pickRandom() {
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

// get a rainbow color
CRGB colorWheel(uint8_t pos) {
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
 
/*
// update the strip
void showStrip() {
  strip.show();
}

// set the brightness
void setBrightness(uint8_t value) {
  strip.setBrightness(value);
}

// set a single pixel
void setPixel(uint16_t pixel, uint8_t red, uint8_t green, uint8_t blue) {
  setPixel(pixel, strip.Color(red, green, blue));
}

// set a single pixel
void setPixel(uint16_t pixel, uint32_t color) {
  strip.setPixelColor(pixel, color);
}

// set all pixels
void setAll(uint8_t red, uint8_t green, uint8_t blue, uint16_t startIndex, uint16_t endIndex) {
  setAll(strip.Color(red, green, blue), startIndex, endIndex);
}

// set all pixels
void setAll(uint32_t color, uint16_t startIndex, uint16_t endIndex) {
  for (uint16_t i = startIndex; i < endIndex; i++) {
    setPixel(i, color);
  }
}

// red component
uint8_t colorRed(uint32_t color) {
  return (color >> 16) & 0xFF;
}

// green component
uint8_t colorGreen(uint32_t color) {
  return (color >> 8) & 0xFF;
}

// blue component
uint8_t colorBlue(uint32_t color) {
  return color & 0xFF;
}





// #########################################################################################################
// #########################################################################################################

// set the pixels one after the other with single color
void wipeColor(uint32_t color, uint16_t startIndex, uint16_t endIndex, uint8_t forward, uint16_t wait) {
#ifdef PRINT
  Serial.println("wipeColor");
#endif
  for (uint16_t i = startIndex; i < endIndex; i++) {
    setPixel(forward == 1 ? i : endIndex - (i - startIndex) - 1, color);
    showStrip();
    delay(wait);

    if (Bluetooth.available() >= 6)  {
      return;
    }
  }
}

// set the pixels one after the other with single color on all rows simultaneously
void wipeColorDouble(uint32_t color, uint16_t startIndex1, uint16_t endIndex1, uint16_t startIndex2, uint16_t endIndex2, uint16_t startIndex3, uint16_t endIndex3, uint8_t forward1, uint8_t forward2, uint8_t forward3, uint16_t wait) {
#ifdef PRINT
  Serial.println("wipeColorDouble");
#endif
  for (uint16_t i = startIndex1, j = startIndex2, k = startIndex3; i < endIndex1 && j < endIndex2 && k < endIndex3; i++, j++, k++) {
    setPixel(forward1 == 1 ? i : endIndex1 - (i - startIndex1) - 1, color);
    setPixel(forward2 == 1 ? j : endIndex2 - (j - startIndex2) - 1, color);
    setPixel(forward3 == 1 ? k : endIndex3 - (k - startIndex3) - 1, color);
    showStrip();
    delay(wait);

    if (Bluetooth.available() >= 6)  {
      return;
    }
  }
}

// theater-style crawling lights with single color
void theaterColor(uint32_t color, uint16_t startIndex, uint16_t endIndex, uint8_t forward, uint16_t wait, uint16_t length, uint8_t repeat) {
#ifdef PRINT
  Serial.println("theaterColor");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    for (uint16_t q = 0; q < length; q++) {
      for (uint16_t i = startIndex; i + q < endIndex; i = i + length) {
        setPixel((forward == 1 || forward == 3) ? i + q : endIndex - (i + q - startIndex) - 1, color);
      }

      showStrip();
      delay(wait);

      for (uint16_t i = startIndex; i + q < endIndex; i = i + length) {
        setPixel((forward == 1 || forward == 3) ? i + q : endIndex - (i + q - startIndex) - 1, 0);
      }

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
  }
}

// theater-style crawling lights with single color on all rows simultaneously
void theaterColorDouble(uint32_t color, uint16_t startIndex1, uint16_t endIndex1, uint16_t startIndex2, uint16_t endIndex2, uint16_t startIndex3, uint16_t endIndex3, uint8_t forward1, uint8_t forward2, uint8_t forward3, uint16_t wait, uint16_t length, uint8_t repeat) {
#ifdef PRINT
  Serial.println("theaterColorDouble");
#endif
  for (uint16_t j = 0; j < repeat; j++) {
    for (uint16_t q = 0; q < length; q++) {
      for (uint16_t i = startIndex1, j = startIndex2, m = startIndex3; i + q < endIndex1 && j + q < endIndex2 && m + q < endIndex3; i = i + length, j = j + length, m = m + length) {
        setPixel(forward1 == 1 ? i + q : endIndex1 - (i + q - startIndex1) - 1, color);
        setPixel(forward2 == 1 ? j + q : endIndex2 - (j + q - startIndex2) - 1, color);
        setPixel(forward3 == 1 ? m + q : endIndex3 - (m + q - startIndex3) - 1, color);
      }

      showStrip();
      delay(wait);

      for (uint16_t i = startIndex1, j = startIndex2, m = startIndex3; i + q < endIndex1 && j + q < endIndex2 && m + q < endIndex3; i = i + length, j = j + length, m = m + length) {
        setPixel(forward1 == 1 ? i + q : endIndex1 - (i + q - startIndex1) - 1, 0);
        setPixel(forward2 == 1 ? j + q : endIndex2 - (j + q - startIndex2) - 1, 0);
        setPixel(forward3 == 1 ? m + q : endIndex3 - (m + q - startIndex3) - 1, 0);
      }

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
  }
}

// theater-style crawling lights with rainbow colors
void theaterRainbow(uint16_t startIndex, uint16_t endIndex, uint8_t forward, uint16_t wait, uint16_t length) {
#ifdef PRINT
  Serial.println("theaterRainbow");
#endif
  for (uint16_t j = 0; j < 256; j++) {
    for (uint16_t q = 0; q < length; q++) {
      for (uint16_t i = startIndex; i + q < endIndex; i = i + length) {
        setPixel((forward == 1 || forward == 3) ? i + q : endIndex - (i + q - startIndex) - 1, colorWheel((i + j) % 255));
      }

      showStrip();
      delay(wait);

      for (uint16_t i = startIndex; i + q < endIndex; i = i + length) {
        setPixel((forward == 1 || forward == 3) ? i + q : endIndex - (i + q - startIndex) - 1, 0);
      }

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
  }
}






// linear rainbow colors
void rainbowLinear(uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint8_t repeat) {
#ifdef PRINT
  Serial.println("rainbowLinear");
#endif
  for (uint16_t k = 0; k <= repeat; k++) {
    for (uint16_t j = 0; j < 256; j++) {
      for (uint16_t i = startIndex; i < endIndex; i++) {
        setPixel(i, colorWheel((i + j) & 255));
      }
      showStrip();
      delay(wait);

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
  }
}

// cyclic rainbow colors
void rainbowCycle(uint16_t startIndex, uint16_t endIndex, uint16_t wait, uint8_t repeat) {
#ifdef PRINT
  Serial.println("rainbowCycle");
#endif
  for (uint16_t k = 0; k < repeat; k++) {
    for (uint16_t j = 0; j < 256; j++) {
      for (uint16_t i = startIndex; i < endIndex; i++) {
        setPixel(i, colorWheel(((i * 256 / (endIndex - startIndex)) + j) & 255));
      }
      showStrip();
      delay(wait);

      if (Bluetooth.available() >= 6)  {
        return;
      }
    }
  }
}
*/
