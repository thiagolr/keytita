//  ----------------------------------------------------------------------- LICENSE
//  This "3D Printed Case for Arduino Uno, Leonardo" by Zygmunt Wojcik is licensed
//  under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
//  To view a copy of this license, visit
//  http://creativecommons.org/licenses/by-sa/3.0/
//  or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.


include <BoxCase_Params.scad>

$NUT_DIAMETER = 6.0;
$NUT_DIAMETER_OFFSET = 0.8;
$NUT_HEIGHT = 2.3;
$NUT_HEIGHT_OFFSET = 0.6;

nutHoleRadius = $NUT_DIAMETER / 2 + $NUT_DIAMETER_OFFSET;
nutHoleHeight = $NUT_HEIGHT + $NUT_HEIGHT_OFFSET;

module NutHole() {
    color("orangeRed")
    
    rotate([90, 0, 0])
        translate([0, 0, -nutHoleHeight / 2]) 
            cylinder(r = nutHoleRadius, h = nutHoleHeight, center = true, $fn = 6);   
}

//------------------------------------------------------------------------- MODULES
module pcbLeg() {		
	translate([0, 0, 0])
	difference() { 											
		cylinder(h = floorHeight + pcbPositionZ, r1=5.5/2, r2 = 4.5/2);
	}
}


//------------------------------------------------------------------------- MAIN BLOCK
difference()
{
																		// ADD
	union()
	{
		// Add Base
		linear_extrude(height = height/2, convexity = 10)
		minkowski()
		{									
			square([width, wide], center = true);
			circle(roundR);
		}
	}
																		// SUBSTRACT
	union()
	{
		// Lift floor height
		translate([0, 0, floorHeight])
		{
			// Cut Base hole
			linear_extrude(height = height/2, convexity = 10)
			minkowski()
			{
				square([width, wide], center = true);
				circle(roundR - pillarSize);
			}
			// Cut upper block lock
			difference() {
				translate([0, 0, height/2 - floorHeight - blockLockSize]) {
					cylinder(h = blockLockSize+gap, r=width);
				}
				translate([0, 0, height/2 - floorHeight - blockLockSize - gap*2]) {
					linear_extrude(height = blockLockSize+gap*4, convexity = 10)
					minkowski() {
						square([width, wide], center=true);
						circle(roundR - layerWidth*4);
					}
				}
			}
			// Cut x panels 
			for (i = [0 : 180 : 180])				
			rotate([0, 0, i])
			translate([width/2 + roundR - pillarSize/2 - layerWidth*7, 0, 0])
			{
				// Cut X panel hole
				translate([0, 0, height/2])
				cube([pillarSize, sidePanelXWidth, height], center=true);
                
				// Cut X, Y srew holes
				for (j = [wide/2, -wide/2])
				{
					translate([-(roundR - pillarSize/2 - layerWidth*7), j, - floorHeight])
					if (j>0) 
					{
						rotate([0, 0, 45])
						translate([screwHoleRoundR, 0, 0])
						{
							cylinder(h = height*2, r=screwExt/2, center=true);
							cylinder(h = 5,
                                    r1 = (screwHead + (screwHead - screwExt))/2,
                                    r2 = screwExt/2, center=true);
                            rotate([90, 0, 135]) NutHole();
						}
					}
					else
					{
						rotate([0, 0, -45])
						translate([screwHoleRoundR, 0, 0])
						{
							cylinder(h = height*2, r=screwExt/2, center=true);
							cylinder(h = 5,
                                    r1 = (screwHead + (screwHead - screwExt))/2,
                                    r2 = screwExt/2, center=true);
                            rotate([90, 0, -135]) NutHole();
						}
                        
             color("blue") rotate([0, 0, 90])  translate([-3.5,-j+(i == 0 ? 10 : -10),0])  {
              cylinder(h = height*2, r=screwExt/2, center=true);
							cylinder(h = 5,
                                    r1 = (screwHead + (screwHead - screwExt))/2,
                                    r2 = screwExt/2, center=true);
                            rotate([90, 0, -150]) NutHole();  
			}
                        
					}
				}
			}
			// Cut Y panels 
			for (i = [90 : 180 : 270])
			rotate([0, 0, i])
			translate([wide/2 + roundR - pillarSize/2 - layerWidth*7, 0, 0])
			{
				// Cut Y panel hole
				*translate([0, 0, height/2])
				cube([pillarSize, sidePanelYWidth, height], center=true);
                
                // Cut Y panel hole
				translate([0, i == 90 ? -10.6 : 10.6, height/2])
				cube([pillarSize, sidePanelYWidth-23.4, height], center=true);
                
                translate([0, (i == 90 ? 18.5 + 2.2 : -18.5 - 2.2), height/2])
				cube([pillarSize, 14-2.2, height], center=true);
			}
			
            // Cut USB and Power holes
			// translate to pcb position
			translate([-pcbPositionX, -pcbWide/2, pcbPositionZ + pcbHeight])
			{
				// cut power hole
				translate([0, powerJackPosition, (powerJackHeight-2)/2])
				cube([10, powerJackWide, powerJackHeight], center=true);
				// cut usb hole
				translate([0, usbHolePosition, 1.3])
				cube([10, usbWide, usbHeight], center=true);
			}
		}
	}
}
//------------------------------------------------------------------------- ADD PCB LEGS
// Translate to pcbPositionX	
translate([-pcbPositionX, -pcbWide/2, 0])

difference()
{
																		// ADD
	union()
	{
		// Add pcb legs
		for(i=[ [13.97, 2.54, 0], 				
     	    	[15.24, 50.8, 0],
          		[66.04, 35.56, 0],
          		[66.04, 7.62, 0] ])
		{
	    		translate(i)
	    		pcbLeg();
		}
		// Add pcb holders
		for(i=[ [13.97, 2.54, 0],
     	    	[15.24, 50.8, 0],
          		[66.04, 35.56, 0],
     	    	[66.04, 7.62, 0] ])
		{
			translate(i)
			cylinder(h=floorHeight+pcbPositionZ+1.5, r=1.2);
		}
	}
																		// SUBSTRACT
	union()
	{
		//
	}
}