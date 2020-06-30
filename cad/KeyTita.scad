include <Piano.scad>;

// TESTAR acertar alinhamento das duas peças
// TESTAR se led holder está bom agora
// TESTAR fazer esquema de ter folga na última porca/parafuso
// fazer um esquema de ligar melhor na próxima oitava

$UNIT_THICKNESS = 1.2;
$UNIT_LOCK_SIZE = 3;
$UNIT_LOCK_OFFSET = 0.2;
$UNIT_DEPTH = 15;
$UNIT_OVERLAP = 14.0625;

$LED_SMD_SIZE = 5;
$LED_SMD_OFFSET = 0.5;
$LED_STRIP_HEIGHT = 12;
$LED_STRIP_OFFSET = 0.5;
$LED_STRIP_THICKNESS = 1.5;

$SCREW_HOLE_DIAMETER = 2.9;
$SCREW_HOLE_DIAMETER_OFFSET = 0.4;
$SCREW_HEAD_DIAMETER = 5.4;
$SCREW_HEAD_DIAMETER_OFFSET = 0.5;
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
mainOverlap = $UNIT_OVERLAP;

ledStripHeight = $LED_STRIP_HEIGHT + $LED_STRIP_OFFSET * 2;
ledStripPosition = mainBlackHeight + mainThickness;
ledStripThickness = $LED_STRIP_THICKNESS;
ledHolePositionBottom = ledStripPosition + ($LED_STRIP_HEIGHT - $LED_SMD_SIZE) / 2 + $LED_STRIP_OFFSET - $LED_SMD_OFFSET;
ledHolePositionTop = ledHolePositionBottom + $LED_SMD_SIZE + $LED_SMD_OFFSET * 2;
ledHoleHeight = $LED_SMD_SIZE + $LED_SMD_OFFSET * 2;
ledHolderPosition = ledHolePositionBottom;
ledHolderTransitionSupportPosition = 6.8;
ledHolderCount = 10;

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

overlapLeft = true;



// ############################################################################################

*Main(explode = false);

*rotate([90, 0, 0])
MainFront();

//rotate([180, 0, 0]) translate([-mainOverlap, -mainDepth, -mainHeight])
union() {
    difference() {
        MainBack();
        
        if (overlapLeft) {
            cube([mainOverlap, mainDepth, mainHeight]);        
        }
    }

    if (overlapLeft) {
        difference() {
            translate([mainWidth, 0, 0])    
                MainBack();
            
            translate([mainWidth + mainOverlap, 0, 0])
                cube([mainWidth, mainDepth, mainHeight]);
        }
    }
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

module MainFront() {
    difference() {
        BlockMainFront();                
        BlockScrew();
    }
}

module MainBack() {
    difference() {
        BlockMainBack();
        BlockNut();
        BlockScrew();
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
            translate([mainWidth / 2 - screwHolderSize - mainThickness / 2, 0, 0]) ScrewHolder();    
            *translate([mainWidth - screwHolderSize - mainThickness / 2, 0, 0]) ScrewHolder();        
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

module BlockMainBack() {
        insideThickness = mainThickness + mainLockOffset;
        leftMarginThickness = mainThickness + mainLockOffset * (screwMargin ? 5 : 1);
    
    difference() {  
        BlockUnit();
    
        BlockUnitHole(thickness = mainThickness, offsetTop = mainThickness, offsetFront = mainThickness, offsetBack = mainThickness);
        
        color("magenta")
        cube([mainWidth, mainDepth - mainThickness, ledHolePositionTop]);
     }    
     
    color("yellow")
    BlockUnitHole(thickness = insideThickness, offsetLeft = leftMarginThickness, offsetRight = insideThickness, offsetTop = mainThickness, offsetBottom = insideThickness, offsetBack = mainThickness, offsetFront = mainDepth - mainThickness - mainLockSize);

    translate([ledHolderTransitionSupportPosition, 0, 0]) LedHolder();
    for (j = [1 : ledHolderCount]) {
        translate([ledHolderTransitionSupportPosition + ((mainWidth - ledHolderTransitionSupportPosition * 2) / (ledHolderCount + 1)) * j, 0, 0]) LedHolder();        
    }
    
    echo (((mainWidth - ledHolderTransitionSupportPosition * 2) / (ledHolderCount + 1)));
    translate([mainWidth - ledHolderTransitionSupportPosition - mainThickness, 0, 0]) LedHolder();
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

module BlockScrew() {
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

        translate([mainWidth / 2 - screwHolderSize / 2 - mainThickness / 2, 0, screwHolePosition]) 
            ScrewHole();
        
        translate([mainWidth - screwHolderSize / 2 - mainThickness / 2, 0, screwHolePosition]) 
            ScrewHole();
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