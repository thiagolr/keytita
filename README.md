KeyTita
========

<img src="/media/keytita.png?raw=true">

This project allows you to learn a song note by note on the piano, by lighting up appropriate LED lights that correspond to each individual key on the piano.

### Features

- <b>Learn</b>: the piano will show which note should be played and then it will wait for you
- <b>Listen</b>: the piano will play the song and you will watch which notes it is playing
- <b>Play</b>: play any song and the piano will light up the notes played

### How It Works

The main unit is composed by an Arduino Leonardo and an USB-Host Shield. The piano is connected to the USB-Host Shield and a PC or mobile running [Synthesia](https://synthesiagame.com/) is connected to the Arduino Leonardo.

Synthesia shows any MIDI song as falling notes, then the Arduino Leonardo board receive this information and light up the appropriate LED. When you press a piano key, this information is sent back to Synthesia.

An android application is available to setup the key colors and effects.

### Materials and Tools

- Arduino Leonardo
- Arduino USB-Host Board
- WS2812B Led Strip (144 led/m)
- 5V Power Supply
- 1000 uF capacitor
- 470 ohm resistor
- 3D Printer

### Electronics

A 1000 uF capacitor was adde across the + and – terminals from the power supply to buffer sudden changes in the current drawn by the LED strip. A 470 ohm resistor was added to the LED strip data line to reduce the noise.

### LED Support

A [LED support](https://www.thingiverse.com/thing:4548263) was designed using OpenSCAD in order to hold the LED strip, it is fully parametric and can be easily adjusted to other pianos or keyboards.

### Box Case

The [box case](https://github.com/zygmuntw/3D-Printed-Case-for-Arduino) holding the Arduino Leonardo and the USB-Host Shield was based on the work of [Zygmunt Wojcik](https://github.com/zygmuntw). It was modified to fit the USB-Host Shield connected above the Arduino Leonardo board.

### Screenshots

- TBD

<img src="/media/screenshot1.png?raw=true" width="200">&nbsp;&nbsp;&nbsp;&nbsp;<img src="/media/screenshot2.png?raw=true" width="200">
