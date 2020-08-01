/* KeyTita
 *
 * Author: Thiago Rosa (http://www.thiagorosa.com)
 * Project: KeyTita (https://github.com/thiagolr/keytita)
 * License: CC BY-SA 4.0 (https://creativecommons.org/licenses/by-sa/4.0)
 *
 */

include <Piano/Piano.scad>;

$UNIT_THICKNESS = 1.2;
$UNIT_LOCK_SIZE = 3;
$UNIT_LOCK_OFFSET = 0.2;
$UNIT_DEPTH = 15;
$UNIT_OVERLAP = 14.0625;
$UNIT_FIRST_OCTAVE = 40.59;
$UNIT_EXTRA_PADDING = 0;

$LED_SMD_SIZE = 5;
$LED_SMD_OFFSET = 0.5;
$LED_STRIP_HEIGHT = 12;
$LED_STRIP_OFFSET = 0.5;
$LED_STRIP_THICKNESS = 1.5;

$SCREW_HOLE_DIAMETER = 2.9;
$SCREW_HOLE_DIAMETER_OFFSET = 0.4;
$SCREW_HEAD_DIAMETER = 5.4;
$SCREW_HEAD_DIAMETER_OFFSET = 0.6;
$SCREW_HEAD_HEIGHT = 2.0;
$SCREW_HEAD_HEIGHT_OFFSET = 0.9;
$SCREW_HOLDER_SIZE = 11.3;
$NUT_DIAMETER = 6.0;
$NUT_DIAMETER_OFFSET = 0.4;
$NUT_HEIGHT = 2.3;
$NUT_HEIGHT_OFFSET = 0.5;

$WHITE_LENGTH = 150;
$WHITE_WIDTH = 22.5;
$WHITE_HEIGHT = 17.8;
$BLACK_LENGTH = 95;
$BLACK_HEIGHT = $WHITE_HEIGHT + 12.5;
$SPACE_WIDTH = 1.068;

$COLOR_ALPHA = 1;

// ############################################################################################

mainThickness = $UNIT_THICKNESS;
mainLockSize = $UNIT_LOCK_SIZE;
mainLockOffset = $UNIT_LOCK_OFFSET;
mainExtraPadding = $UNIT_EXTRA_PADDING;

mainBlackHeight = ($BLACK_HEIGHT - $WHITE_HEIGHT);
mainWidth = $WHITE_WIDTH * 7 + $SPACE_WIDTH * 7 + mainExtraPadding;
mainHeight = mainThickness + mainBlackHeight + $LED_STRIP_OFFSET + $LED_STRIP_HEIGHT + $LED_STRIP_OFFSET + mainThickness;
mainDepth = $UNIT_DEPTH;
mainOverlap = $UNIT_OVERLAP;
mainFirstOctave = $UNIT_FIRST_OCTAVE;

ledStripHeight = $LED_STRIP_HEIGHT + $LED_STRIP_OFFSET * 2;
ledStripPosition = mainBlackHeight + mainThickness;
ledStripThickness = $LED_STRIP_THICKNESS;
ledHolePositionBottom = ledStripPosition + ($LED_STRIP_HEIGHT - $LED_SMD_SIZE) / 2 + $LED_STRIP_OFFSET - $LED_SMD_OFFSET;
ledHolePositionTop = ledHolePositionBottom + $LED_SMD_SIZE + $LED_SMD_OFFSET * 2;
ledHoleHeight = $LED_SMD_SIZE + $LED_SMD_OFFSET * 2;
ledHolderPosition = ledHolePositionBottom;
ledHolderTransitionSupportPosition = 17;
ledHolderCount = 9;

screwHoleRadius = $SCREW_HOLE_DIAMETER / 2 + $SCREW_HOLE_DIAMETER_OFFSET;
screwHeadRadius = $SCREW_HEAD_DIAMETER / 2 + $SCREW_HEAD_DIAMETER_OFFSET;
screwHeadHeight = $SCREW_HEAD_HEIGHT + $SCREW_HEAD_HEIGHT_OFFSET;
screwHolderSize = $SCREW_HOLDER_SIZE + mainThickness;
screwHolderDepth = 3.6;
screwHolePosition = ledStripPosition / 2;
nutHoleRadius = $NUT_DIAMETER / 2 + $NUT_DIAMETER_OFFSET;
nutHoleHeight = $NUT_HEIGHT + $NUT_HEIGHT_OFFSET;

leftSideHole = true;
rightSideHole = true;
screwMargin = false;

backConnectorOffset = 0.5;

insideThickness = mainThickness + mainLockOffset;
leftMarginThickness = mainThickness + mainLockOffset * (screwMargin ? 5 : 1);

// ############################################################################################

Main(explode = true);
*Main88(explode = false);
*Box();

*PrintFront(c1 = true);
*PrintFront(c2c3c4c5c6 = true);
*PrintFront(c7c8 = true);

*PrintBack(c1 = true);
*PrintBack(c2c3c4c5c6 = true);
*PrintBack(c7c8 = true);

*PrintConnector();

// ############################################################################################

module PrintFront(c1 = false, c2c3c4c5c6 = false, c7c8 = false) {   
    if (c1) {
        rotate([90, 0, 0]) translate([-mainWidth + mainFirstOctave, 0, 0])
        difference() {
            MainFront();
            
            cube([mainWidth - mainFirstOctave, mainDepth, mainHeight]);
        }
    }
    
    if (c2c3c4c5c6) {
        rotate([90, 0, 0])
        MainFront();
    }
    
    if (c7c8) {
        rotate([90, 0, 0])
        union() {
            MainFront();
            
            difference() {
                translate([mainWidth, 0, 0])    
                    MainFront();
                
                translate([mainWidth + mainOverlap, 0, 0])
                    cube([mainWidth, mainDepth, mainHeight]);
            }
        }
    }
}

module PrintBack(c1 = false, c2c3c4c5c6 = false, c7c8 = false) {
    if (c1) {
        rotate([180, 0, 0]) translate([-mainWidth + mainFirstOctave, -mainDepth, -mainHeight])
        union() {
            difference() {
                MainBack();         
                
                cube([mainWidth - mainFirstOctave, mainDepth, mainHeight]);
            }

            difference() {
                translate([mainWidth, 0, 0])    
                    MainBack();
                
                translate([mainWidth + mainOverlap, 0, 0])
                    cube([mainWidth, mainDepth, mainHeight]);
                
                
            }
            
            color("gray")
            translate([mainWidth - mainFirstOctave, mainThickness + ledStripThickness, mainBlackHeight + mainThickness])
                cube([mainThickness, mainDepth - mainThickness - ledStripThickness, mainHeight - mainBlackHeight - mainThickness]);
        }
    }
    
    if (c2c3c4c5c6) {        
        rotate([180, 0, 0]) translate([-mainOverlap, -mainDepth, -mainHeight])
        union() {
            difference() {
                MainBack();         
                cube([mainOverlap, mainDepth, mainHeight]);        
            }

            difference() {
                translate([mainWidth, 0, 0])    
                    MainBack();
                
                translate([mainWidth + mainOverlap, 0, 0])
                    cube([mainWidth, mainDepth, mainHeight]);
            }
        }
    }
    
    if (c7c8) {
        rotate([180, 0, 0]) translate([-mainOverlap, -mainDepth, -mainHeight])
        union() {
            difference() {
                MainBack();         
                cube([mainOverlap, mainDepth, mainHeight]);        
            }

            difference() {
                translate([mainWidth, 0, 0])    
                    MainBack(topHolder = false);
                
                translate([mainWidth + mainOverlap, 0, 0])
                    cube([mainWidth, mainDepth, mainHeight]);
            }
            
            translate([mainWidth + mainOverlap, 0, 0])
                cube([mainThickness, mainDepth, mainHeight]);
        }
    }
}

module PrintConnector() {    
    translate([-leftMarginThickness - backConnectorOffset, -mainThickness - ledStripThickness - backConnectorOffset, -mainHeight + mainThickness + screwHolderDepth + mainThickness])
        BlockConnector();
}

// ############################################################################################

module Main(explode = false) {   
    if (!explode) {
        MainFront();
        MainBack();
    } else {
        translate([0, -5, 0]) MainFront();
        translate([0, 5, 0]) MainBack();
    }    
}

module Main88(explode = false) {
    space = explode ? 7 : 0.01;
    
    union() {
        translate([0, mainDepth + space, mainHeight]) rotate([-180, 0, 0]) {
            PrintBack(c1 = true);
        }

        translate([mainOverlap, mainDepth + space, mainHeight]) rotate([-180, 0, 0]) {
            translate([mainFirstOctave, 0, 0]) PrintBack(c2c3c4c5c6 = true);
            translate([mainFirstOctave + mainWidth, 0, 0]) PrintBack(c2c3c4c5c6 = true);
            translate([mainFirstOctave + mainWidth * 2, 0, 0]) PrintBack(c2c3c4c5c6 = true);
            translate([mainFirstOctave + mainWidth * 3, 0, 0]) PrintBack(c2c3c4c5c6 = true);
            translate([mainFirstOctave + mainWidth * 4, 0, 0]) PrintBack(c2c3c4c5c6 = true);
            translate([mainFirstOctave + mainWidth * 5, 0, 0]) PrintBack(c2c3c4c5c6 = true);
            translate([mainFirstOctave + mainWidth * 6, 0, 0]) PrintBack(c7c8 = true);
        }    
        
        translate([0, -space, 0]) rotate([-90, 0, 0]) {
            PrintFront(c1 = true);
            translate([mainFirstOctave, 0, 0]) PrintFront(c2c3c4c5c6 = true);
            translate([mainFirstOctave + mainWidth, 0, 0]) PrintFront(c2c3c4c5c6 = true);
            translate([mainFirstOctave + mainWidth * 2, 0, 0]) PrintFront(c2c3c4c5c6 = true);
            translate([mainFirstOctave + mainWidth * 3, 0, 0]) PrintFront(c2c3c4c5c6 = true);
            translate([mainFirstOctave + mainWidth * 4, 0, 0]) PrintFront(c2c3c4c5c6 = true);
            translate([mainFirstOctave + mainWidth * 5, 0, 0]) PrintFront(c2c3c4c5c6 = true);
            translate([mainFirstOctave + mainWidth * 6, 0, 0]) PrintFront(c7c8 = true);
        }    
    }    
}

module MainFront() {
    difference() {
        BlockMainFront();                
        BlockScrew();
    }
}

module MainBack(topHolder = true) {
    difference() {
        BlockMainBack(topHolder = topHolder);
        BlockNut();
        BlockScrew(back = true, topHolder = topHolder);
    }
} 

module Box() {
    import("KeyTita_Printer_BoxCase_Base.stl", convexity=3);
    
    rotate([180,0,0]) {
        translate([0, 0, -35]) import("KeyTita_Printer_BoxCase_LidTop.stl", convexity=3);
    
        translate([0, 0, -14]) import("KeyTita_Printer_BoxCase_LidMid.stl", convexity=3);
    }
}
    
// ############################################################################################

module BlockMainFront() {
    difference() {
        union() {
            difference() {            
                BlockUnit();
                
                BlockUnitHole(thickness = mainThickness, offsetLeft = mainThickness, offsetRight = mainThickness, offsetTop = mainThickness, offsetBottom = mainThickness, offsetFront = mainThickness);        
            }
        
            *translate([mainThickness, mainThickness, ledStripPosition - mainThickness]) 
                cube([mainWidth - mainThickness * 2, mainDepth - mainThickness, mainThickness]);
                    
            translate([mainThickness / 2, 0, 0]) ScrewHolder();    
            translate([mainWidth / 2 - screwHolderSize - mainThickness / 2 + mainExtraPadding / 2, 0, 0]) ScrewHolder();    
            translate([mainWidth - screwHolderSize - mainThickness / 2, 0, 0]) ScrewHolder();        
        }
        
        color("magenta")
        translate([0, 0, ledHolePositionBottom])
            cube([mainWidth, mainDepth, mainHeight]);

        color("blue") 
        translate([leftSideHole ? 0 : mainThickness, mainThickness, ledStripPosition])
            cube([mainWidth + (leftSideHole ? -mainThickness : 0) + (rightSideHole ? mainThickness : 0), mainDepth - mainThickness, ledStripHeight]);
        
        color("greenyellow")
        translate([0, mainDepth - mainThickness, 0])
            cube([mainWidth, mainThickness, mainHeight]);
    }
}

module BlockMainBack(topHolder = true) {    
    difference() {  
        BlockUnit();
    
        BlockUnitHole(thickness = mainThickness, offsetTop = mainThickness, offsetFront = mainThickness, offsetBack = mainThickness);
        
        color("magenta")
        cube([mainWidth, mainDepth - mainThickness, ledHolePositionTop]);
     }    
     
    color("yellow")
    BlockUnitHole(thickness = insideThickness, offsetLeft = leftMarginThickness, offsetRight = insideThickness, offsetTop = mainThickness, offsetBottom = insideThickness, offsetBack = mainThickness, offsetFront = mainDepth - mainThickness - mainLockSize);
     
    if (topHolder) {
        color("purple")
        rotate([-90,0,0]) {
           translate([leftMarginThickness, -mainHeight + mainThickness, mainThickness + ledStripThickness])
                cube([screwHolderSize, screwHolderDepth, mainDepth - mainThickness * 2 - ledStripThickness]);
           translate([leftMarginThickness + screwHolderSize , -mainHeight + mainThickness, mainThickness + ledStripThickness]) 
                cube([screwHolderSize, screwHolderDepth, mainDepth - mainThickness * 2 - ledStripThickness]);
        }
    }

    for (j = [1 : ledHolderCount + 2]) {
        translate([ledHolderTransitionSupportPosition + ((mainWidth - ledHolderTransitionSupportPosition * 2) / (ledHolderCount + 1)) * j, 0, 0]) LedHolder();        
    }
}

// ############################################################################################

module BlockUnit() {
    color("white")
    
    difference(){
        cube([mainWidth, mainDepth, mainHeight]);
        BlockUnitPiano();            
    }
}

module BlockUnitHole(thickness = 0, offsetLeft = 0, offsetRight = 0, offsetTop = 0, offsetBottom = 0, offsetFront = 0, offsetBack = 0) {    
    color("white")
    
    difference() {
        translate([offsetLeft, offsetFront, offsetBottom]) 
            cube([mainWidth - offsetLeft - offsetRight, mainDepth - offsetFront - offsetBack, mainHeight - offsetTop - offsetBottom]);
        
        BlockUnitPiano(thickness = thickness);
    }
}

module BlockUnitPiano(thickness = 0) {
    color("white")
    
    translate([0, mainDepth - $WHITE_LENGTH / 2, -$WHITE_HEIGHT]) 
        Piano(thickness = thickness, extraPadding = mainExtraPadding, includeSolidSpace = true);
}

module BlockScrew(back = false, topHolder = true) {
    color("yellow") {
        if (screwMargin) {
            hull() {
                translate([screwHolderSize / 2 + mainThickness / 2 - screwHoleRadius / 2, 0, screwHolePosition]) ScrewHole(head = false);             
                translate([screwHolderSize / 2 + mainThickness / 2 + screwHoleRadius / 2, 0, screwHolePosition]) ScrewHole(head = false);             
             }
        } else {
            translate([screwHolderSize / 2 + mainThickness / 2, 0, screwHolePosition]) 
                ScrewHole();
        }

        translate([mainWidth / 2 - screwHolderSize / 2 - mainThickness / 2 + mainExtraPadding / 2, 0, screwHolePosition]) 
            ScrewHole();
        
        translate([mainWidth - screwHolderSize / 2 - mainThickness / 2, 0, screwHolePosition]) 
            ScrewHole();
    }
    
    if (back && topHolder) {
        rotate([-90,0,0])
        color("pink") {
            translate([screwHolderSize / 2 + mainThickness / 2, -mainHeight, mainDepth / 2]) 
                ScrewHole();
        
            translate([mainOverlap + screwHolderSize / 2, -mainHeight, mainDepth / 2]) 
                ScrewHole();        
        }
    }
}

module BlockNut() {
    color("red") {
        if (screwMargin) {
            hull() {
                translate([screwHolderSize / 2 + mainThickness / 2 - screwHoleRadius / 2, mainDepth - nutHoleHeight, screwHolePosition]) NutHole();
                translate([screwHolderSize / 2 + mainThickness / 2 + screwHoleRadius / 2, mainDepth - nutHoleHeight, screwHolePosition]) NutHole();
            }
        } else {
            translate([screwHolderSize / 2 + mainThickness / 2, mainDepth - nutHoleHeight, screwHolePosition]) NutHole();
        }
        
        translate([mainWidth / 2 - screwHolderSize / 2 - mainThickness / 2, mainDepth - nutHoleHeight, screwHolePosition]) NutHole();
        
        translate([mainWidth - screwHolderSize / 2 - mainThickness / 2, mainDepth - nutHoleHeight, screwHolePosition]) NutHole();
    }
}

module BlockConnector() {
    color("blue")
    difference() {
        translate([leftMarginThickness + backConnectorOffset, mainThickness + ledStripThickness + backConnectorOffset, mainHeight - mainThickness - screwHolderDepth - mainThickness]) {
            cube([screwHolderSize * 2 - backConnectorOffset * 2, mainDepth - mainThickness - ledStripThickness - mainLockSize - mainThickness - backConnectorOffset * 2, mainThickness * 3]);

            translate([(screwHolderSize * 2 - backConnectorOffset * 2) / 2 - 0.2,  mainDepth - mainThickness - ledStripThickness - mainLockSize - mainThickness - backConnectorOffset * 2 - 1, mainThickness * 3]) cube([7,mainThickness,4], center = true);
        }        
        
        rotate([-90,0,0]) {            
            translate([screwHolderSize / 2 + mainThickness / 2, -mainHeight, mainDepth / 2]) {
                    ScrewHole(head = false);
                    translate([0, nutHoleHeight - 0.4, 0]) NutHole();
            }
            
            translate([mainOverlap + screwHolderSize / 2, -mainHeight, mainDepth / 2]) {
                    ScrewHole(head = false);    
                translate([0, nutHoleHeight - 0.4, 0]) NutHole();
            }       
        }
    }
}

// ############################################################################################

module LedHolder() {
    color("green")
    
    translate([0, mainThickness + ledStripThickness, ledHolderPosition])
        cube([mainThickness, mainDepth - mainThickness - ledStripThickness, mainHeight - ledHolderPosition]);
}

// ############################################################################################

module ScrewHolder() {
    color("cyan")
        
    translate([0, mainThickness, mainThickness])
        cube([screwHolderSize, screwHolderDepth, ledStripPosition]);
}

module ScrewHole(head = true) {
    color("orange")
    
    rotate([90, 0, 0]) {
        translate([0, 0, -mainDepth / 2]) 
            cylinder(r = screwHoleRadius, h = mainDepth, center = true, $fn = 100);   

        if (head) {
            translate([0, 0, -screwHeadHeight / 2]) 
                cylinder(r = screwHeadRadius, h = screwHeadHeight, center = true, $fn = 100);   
        }
    }
}

module NutHole() {
    color("orangeRed")
    
    rotate([90, 0, 0])
        translate([0, 0, -nutHoleHeight / 2]) 
            cylinder(r = nutHoleRadius, h = nutHoleHeight, center = true, $fn = 6);   
}