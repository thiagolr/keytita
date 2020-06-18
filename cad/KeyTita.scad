include <Piano.scad>;

$UNIT_THICKNESS = 1.2;
$UNIT_LOCK_SIZE = 2;
$UNIT_LOCK_OFFSET = 0.2;
$UNIT_DEPTH = 10;

$LED_SMD_SIZE = 5;
$LED_SMD_OFFSET = 0.5;
$LED_STRIP_HEIGHT = 12;
$LED_STRIP_OFFSET = 0.5;
$LED_STRIP_THICKNESS = 1.7;

$SCREW_HOLE_RADIUS = 3;
$SCREW_HEAD_RADIUS = 5;
$SCREW_HEAD_HEIGHT = 2;
$NUT_RADIUS = 4;
$NUT_HEIGHT = 3;

$WHITE_LENGTH = 125;
$WHITE_WIDTH = 21.9;
$WHITE_HEIGHT = 10;
$BLACK_LENGTH = 75;
$BLACK_HEIGHT = $WHITE_HEIGHT + 10;
$SPACE_WIDTH = 1.6;
$OCTAVES = 1;
$INCLUDE_A0_B0 = false;
$INCLUDE_END_C = false;

$COLOR_ALPHA = 1;

// ############################################################################################

mainThickness = $UNIT_THICKNESS;
mainLockSize = $UNIT_LOCK_SIZE;
mainLockOffset = $UNIT_LOCK_OFFSET;

mainBlackHeight = ($BLACK_HEIGHT - $WHITE_HEIGHT);
mainWidth = $WHITE_WIDTH * 7 + $SPACE_WIDTH * 7;
mainHeight = mainThickness + mainBlackHeight + $LED_STRIP_OFFSET + $LED_STRIP_HEIGHT + $LED_STRIP_OFFSET + mainThickness;
mainDepth = $UNIT_DEPTH;

ledStripHeight = $LED_STRIP_HEIGHT + $LED_STRIP_OFFSET * 2;
ledStripPosition = mainBlackHeight + mainThickness;
ledStripThickness = $LED_STRIP_THICKNESS;
ledHolePositionBottom = ledStripPosition + ($LED_STRIP_HEIGHT - $LED_SMD_SIZE) / 2 + $LED_STRIP_OFFSET - $LED_SMD_OFFSET;
ledHolePositionTop = ledHolePositionBottom + $LED_SMD_SIZE + $LED_SMD_OFFSET * 2;
ledHoleHeight = $LED_SMD_SIZE + $LED_SMD_OFFSET * 2;
ledHolderPosition = ledHolePositionBottom;
ledHolderTransitionSupportPosition = 5;
ledHolderCount = 12;

leftSideHole = true;
rightSideHole = true;
screwNut = true;

// ############################################################################################

*Main(explode = true);

//rotate([90, 0, 0]) mirror([0,0,1])
MainFront();

*rotate([-90, 0, 0]) translate([0, -mainDepth, 0])
MainBack();

/*translate([10, 50,0])
if (screwNut) {
    //cube([100,50,10]);
    
    cylinder(r=4,h=3,center=true,$fn=6);
}*/

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

module MainFront() {
    difference() {        
        BlockUnit();
        
        BlockUnitHole(thickness = mainThickness, offsetLeft = mainThickness, offsetRight = mainThickness, offsetTop = mainThickness, offsetBottom = mainThickness, offsetFront = mainThickness);
        
        color("magenta")
        translate([0, 0, ledHolePositionBottom])
            cube([mainWidth, mainDepth, mainHeight]);
        
        color("blue") 
        translate([leftSideHole ? 0 : mainThickness, mainThickness, ledStripPosition])
        cube([mainWidth + (leftSideHole ? -mainThickness : 0) + (rightSideHole ? mainThickness : 0), mainDepth - mainThickness, ledStripHeight]);
    }
    
    ScrewHolder();
}

module MainBack() {
    insideThickness = mainThickness + mainLockOffset;
    
    difference() {  
        BlockUnit();
    
        BlockUnitHole(thickness = mainThickness, offsetTop = mainThickness, offsetFront = mainThickness, offsetBack = mainThickness);
        
        color("magenta")
        cube([mainWidth, mainDepth - mainThickness, ledHolePositionTop]);
     }    
     
    color("yellow")
    BlockUnitHole(thickness = insideThickness, offsetLeft = insideThickness, offsetRight = insideThickness, offsetTop = mainThickness, offsetBottom = insideThickness, offsetBack = mainThickness, offsetFront = mainDepth - mainThickness - mainLockSize);

    translate([ledHolderTransitionSupportPosition, 0, 0]) LedHolder();
    for (j = [1 : ledHolderCount]) {
        translate([(mainWidth / (ledHolderCount + 1)) * j, 0, 0]) LedHolder();        
    }
    translate([mainWidth - ledHolderTransitionSupportPosition, 0, 0]) LedHolder();
    
    
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
        Piano(thickness = thickness, includeSolidSpace = true);
}

module LedHolder() {
    color("green")
    
    translate([0, mainThickness + ledStripThickness, ledHolderPosition])
        cube([mainThickness, mainDepth - mainThickness - ledStripThickness, mainHeight - ledHolderPosition]);
}

module ScrewHolder() {
    color("yellow")
    
    translate([0, mainThickness, mainThickness])
        cube([mainThickness, mainDepth - mainThickness - ledStripThickness, mainHeight - ledHolderPosition]);
}
