include <Piano.scad>;

$LED_STRIP_HEIGHT = 13;
$THICKNESS = 2; // arrumar aqui

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

mainWidth = $WHITE_WIDTH * 7 + $SPACE_WIDTH * 7;
mainHeight = $BLACK_HEIGHT - $WHITE_HEIGHT + $LED_STRIP_HEIGHT + $THICKNESS * 2;
mainDepth = 15;
mainThickness = $THICKNESS;
mainColor = "white";

rotate([90, 0, 0]) translate([0, 0, -mainHeight])
difference(){
    MainUnit(color = mainColor);   
    MainUnit(color = mainColor, thickness = mainThickness);    
} 

module MainUnit(color = "black", thickness = 0) {
    difference(){
        translate([thickness, thickness, thickness]) 
            color(color, $COLOR_ALPHA) 
                cube([mainWidth - thickness * 2, mainDepth + thickness, mainHeight - thickness * 2]);
        
        translate([0, mainDepth - $WHITE_LENGTH + 10, -$WHITE_HEIGHT ]) 
            color(color, $COLOR_ALPHA) 
                Piano(thickness = thickness, includeSolidSpace = true);  
    }
}
