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
 
#include <FastLED.h>
#include <SPI.h>
#include <USB-MIDI.h>

#include <usbh_midi.h>
#include <usbhub.h>

//#define DEBUG

#ifdef DEBUG
    #define PRINT(x) Serial.print(x)
    #define PRINTLN(x) Serial.println(x)
    #define PRINTNOTE(x, y, z, w) printNote(x, y, z, w)
    #ifdef VERBOSE    
        #define PRINTNOTE_VERBOSE(x, y, z, w) printNote(x, y, z, w)
    #else
        #define PRINTNOTE_VERBOSE(x, y, z, w)
    #endif 
#else
    #define PRINT(x)
    #define PRINTLN(x)
    #define PRINTNOTE(x, y, z, w)
    #define PRINTNOTE_VERBOSE(x, y, z, w)
#endif

// ###################################################################################################################################
// ###################################################################################################################################

#define TYPE_CLEAR 0
#define TYPE_COLOR 1
#define TYPE_SPEED 2
#define TYPE_BRIGHTNESS 3
#define TYPE_DEMO 4

#define TYPE_RANDOM_COLOR 11
#define TYPE_RAINBOW_FULL_FIXED 12
#define TYPE_RAINBOW_FULL_MOVING 13
#define TYPE_RAINBOW_SINGLE_SHIFTING 14
#define TYPE_RAINBOW_GRADUAL_MOVING 15

#define DEFAULT_BRIGHTNESS 10
#define DEFAULT_SPEED 100

char btData[20];
byte btIndex = 0;

bool btDemo = false;
byte btType = 0;
int btVelocity = DEFAULT_SPEED;
byte btColorRed = 0;
byte btColorGreen = 0;
byte btColorBlue = 0;
int movingIndex = 0;

// ###################################################################################################################################
// ###################################################################################################################################

#define PIN_LED 2
#define NUM_LEDS 172
#define NUM_KEYS 88

const byte mapping[NUM_KEYS] = { 0,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,47,49,51,53,55,57,59,61,63,65,67,69,71,73,75,77,79,81,83,85,87,89,91,93,94,96,98,100,102,104,106,108,110,112,114,116,118,120,122,124,126,128,130,132,134,136,138,140,142,144,146,148,150,152,154,156,158,160,162,164,166,168,169,171 };
byte channels[NUM_KEYS];

CRGB leds[NUM_LEDS];

// ###################################################################################################################################
// ###################################################################################################################################

#define MIDI_THRU_CHANNEL 15
#define MIDI_LIGHT_CHANNEL 16

USB Usb;
USBMIDI_CREATE_DEFAULT_INSTANCE();

USBH_MIDI MIDI_Piano(&Usb);
#define MIDI_Synthesia MIDI

// ###################################################################################################################################
// ###################################################################################################################################

void setup() {       
    pinMode(LED_BUILTIN, OUTPUT);

    Serial.begin(115200);
    //while (!Serial) {
    //    digitalWrite(LED_BUILTIN, HIGH); 
    //    delay(500);
    //    digitalWrite(LED_BUILTIN, LOW); 
    //    delay(500);
    //}

    PRINTLN("LED");
    FastLED.addLeds<WS2812B, PIN_LED, GRB>(leds, NUM_LEDS);
    FastLED.setBrightness(DEFAULT_BRIGHTNESS);
    FastLED.show();

    PRINTLN("MIDI");
    MIDI_Synthesia.setHandleNoteOn(handleSynthesiaNoteOn);
    MIDI_Synthesia.setHandleNoteOff(handleSynthesiaNoteOff);
    MIDI_Synthesia.begin(MIDI_CHANNEL_OMNI);
    
    PRINTLN("USB");
    while (Usb.Init() == -1) {
        digitalWrite(LED_BUILTIN, HIGH); 
        delay(1000);
        digitalWrite(LED_BUILTIN, LOW);
        delay(500);
    }
    
    delay(200);
    digitalWrite(LED_BUILTIN, HIGH); 

    for (int i = 0; i < NUM_KEYS; i++) {
        channels[i] = 0;
    }

    btType = TYPE_DEMO;
    btDemo = true;

    PRINTLN("READY");
}

void loop() {
    MIDI_Synthesia.read();
    Usb.Task();
    
    if (Usb.getUsbTaskState() == USB_STATE_RUNNING) {
        handleUsb();
    }
    
    while (Serial.available() > 0) {
        char data = Serial.read();

        btData[btIndex] = data;
        btIndex++;
    }

    if (btIndex > 0 && btData[btIndex - 1] == '#') {
        btData[btIndex - 1] = '\0';
        btIndex = 0;
      
        char * data;
        data = strtok(btData, ",");
        int type = atoi(data);

        PRINTLN(type);

        switch (type) {
            case TYPE_CLEAR:
                btType = type;
                break;   
            case TYPE_COLOR:        
                btType = type;
                data = strtok(NULL, ",");
                btColorRed = atoi(data);
                data = strtok(NULL, ",");
                btColorGreen = atoi(data);
                data = strtok(NULL, ",");
                btColorBlue = atoi(data);
                break;     
            case TYPE_SPEED:     
                data = strtok(NULL, ",");
                btVelocity = atoi(data);    
                break;            
            case TYPE_BRIGHTNESS:
                data = strtok(NULL, ",");
                FastLED.setBrightness(atoi(data));
                break;
            default:
                btType = type;
                movingIndex = 0;
                break;          
        }

        btDemo = true;  
    }

    if (btDemo) {
        handleLedDemo();
    }
}

// ###################################################################################################################################
// ###################################################################################################################################

void handleLedDemo() {  
    if (btType == TYPE_CLEAR) {
        btDemo = false;
    }
    
    for (int i = 0; i < NUM_LEDS; i++) {
        leds[i] = getLed(i);
    }

    FastLED.show();

    movingIndex++;
    delay(btVelocity);
}

void handleUsb() {
    uint8_t buffer[3];
    uint8_t size = 0;
  
    do {
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
    
        size = MIDI_Piano.RecvData(buffer);
        
        if (size > 0) {          
            if (buffer[0] >= 128 && buffer[0] <= 143) {
                handlePianoNoteOff(buffer[0] - 127, buffer[1], buffer[2]);
            } else if (buffer[0] >= 144 && buffer[0] <= 159) {
                handlePianoNoteOn(buffer[0] - 143, buffer[1], buffer[2]);             
            } else if (buffer[0] >= 176 && buffer[0] <= 191) {
                // control change
            } else if (buffer[0] >= 224 && buffer[0] <= 239) {
                // pitch wheel
            } else if (buffer[0] == 254) {
                // active sensing
            } else {
                PRINT("Unhandled status byte: ");
                PRINT(buffer[0]);
            }  
        }
    } while (size > 0);
}

void handleSynthesiaNoteOn(byte channel, byte note, byte velocity) {    
    if (channel == MIDI_THRU_CHANNEL) {
        return;      
    }
  
    PRINTNOTE("RCV SYN ON ", channel, note, velocity);

    if (channel == MIDI_LIGHT_CHANNEL) {       
        handleLed(true, channel, note);    
    } else {
        uint8_t buffer[3];
        buffer[0] = channel + 143;
        buffer[1] = note;
        buffer[2] = velocity;
        MIDI_Piano.SendData(buffer);
        
        PRINTNOTE_VERBOSE("SND PIA ON ", buffer[0] - 143, buffer[1], buffer[2]);
    }
}

void handleSynthesiaNoteOff(byte channel, byte note, byte velocity) {
    if (channel == MIDI_THRU_CHANNEL) {
        return;      
    }

    PRINTNOTE("RCV SYN OFF", channel, note, velocity);

    if (channel == MIDI_LIGHT_CHANNEL) {
        handleLed(false, channel, note);
    } else {
        uint8_t buffer[3];
        buffer[0] = channel + 127;
        buffer[1] = note;
        buffer[2] = 0;
        MIDI_Piano.SendData(buffer);

        PRINTNOTE_VERBOSE("SND PIA OFF", buffer[0] - 127, buffer[1], buffer[2]);
    }
}

void handlePianoNoteOn(byte channel, byte note, byte velocity) {
    handleLed(true, channel, note);  
    
    PRINTNOTE_VERBOSE("RCV PIA ON ", channel, note, velocity);                    
    PRINTNOTE("SND SYN ON ", MIDI_THRU_CHANNEL, note, velocity);     
    MIDI_Synthesia.sendNoteOn(note, velocity, MIDI_THRU_CHANNEL);
}

void handlePianoNoteOff(byte channel, byte note, byte velocity) {
    handleLed(false, channel, note);

    PRINTNOTE_VERBOSE("RCV PIA OFF", channel, note, 0);
    PRINTNOTE("SND SYN OFF", MIDI_THRU_CHANNEL, note, 0);
    MIDI_Synthesia.sendNoteOff(note, 0, MIDI_THRU_CHANNEL);
}

void handleLed(bool enable, byte channel, byte note) {
    if (btDemo) {
        btDemo = false;
        disableLeds();
    } 

    if (enable) {
        PRINTNOTE_VERBOSE("SND LED ON ", channel, note, 0);
    } else {
        PRINTNOTE_VERBOSE("SND LED OFF", channel, note, 0);
    }

    byte index = note - 21;
    byte led = mapping[index];
    if (enable) {
        if (channels[index] != MIDI_LIGHT_CHANNEL) {
            channels[index] = channel;
            leds[led] = getLed(led);
        }
    } else {
        if (channels[index] == channel) {
            channels[index] = 0;    
            leds[led] = CRGB::Black;      
        }
    }
    FastLED.show();
}

CRGB getLed(byte pos) {
    switch (btType) {
        case TYPE_CLEAR:
            return CRGB::Black;
        case TYPE_COLOR:
            return CRGB(btColorRed, btColorGreen, btColorBlue);  
        case TYPE_DEMO:
            if (btDemo) {
                return rainbowCompleteMoving(pos);    
            } else {
                return rainbowCompleteStatic(pos);
            }
        case TYPE_RANDOM_COLOR:
            return colorRandom();
        case TYPE_RAINBOW_FULL_FIXED:
            return rainbowCompleteStatic(pos);
        case TYPE_RAINBOW_FULL_MOVING:
            return rainbowCompleteMoving(pos);
        case TYPE_RAINBOW_SINGLE_SHIFTING:
            return rainbowSingleColorShifting();
        case TYPE_RAINBOW_GRADUAL_MOVING:
            return rainbowGradualMoving(pos);   
    }
    return CRGB::Black;
}

void disableLeds() {
    for (int i = 0; i < NUM_LEDS; i++) {
        leds[i] = CRGB::Black;
    }
    FastLED.show();
}

// ###################################################################################################################################
// ###################################################################################################################################

void printNote(String type, byte channel, byte note, byte velocity) {
    PRINT("[");
    PRINT(type);
    PRINT(channel < 10 ? "]  #0" : "]  #");
    PRINT(channel);
    PRINT("  n=");
    PRINT(note);
    if (velocity > 0) {
      PRINT("  v=");
      PRINT(velocity);
    }
    PRINTLN("");
}
