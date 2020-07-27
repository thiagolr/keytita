//  ----------------------------------------------------------------------- LICENSE
//  This "3D Printed Case for Arduino Uno, Leonardo" by Zygmunt Wojcik is licensed
//  under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
//  To view a copy of this license, visit
//  http://creativecommons.org/licenses/by-sa/3.0/
//  or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.


include <BoxCase_Params.scad>



frontX = 0;//100;
frontY = 0;//20;
height = 14;//14

backX = 100;
height = 56;//14

*color("blue") cube([10,10,9]);
*translate([0,0,21]) color("blue") cube([10,10,9]);
*translate([-33,20,21-8]) color("cyan") cube([3,3,17]);
*color("magenta",0.5) cube([5,5,30]);

difference() {
color("green", 0.3) translate([-20.5,0,21/2]) cube([1.2,width+ 2 * roundR,21], center = true);

color("yellow", 1) translate([-20.5, 4.45,21]) cube([2.54 + 1.27*2,11.5 + 1.27*2,24], center = true);
}



//------------------------------------------------------------------------- PARAMETERS
// lid holes dimensions
connectorRoundR = 2.54/2;
connectorWide = 2.54*3;

upperConnectorWidth = 44.704 + 2.54*2;
upperConnectorXPos = 18.796 - 2.54;
upperConnectorYPos = 50.8 - 2.54*2;
lowerConnectorWidth = 35.56 + 2.54*2;
lowerConnectorXPos = 27.94 - 2.54;
lowerConnectorYPos = 2.54 - 2.54;

icspConnectorWidth = 5.08 + 2.54*2;
icspConnectorWide = 2.54 + 2.54*2;
icspConnectorXPos = 63.64 - 2.54;
icspConnectorYPos = 30.43 + 2.54;

ledHoleWidth = 2.54 + 1.27*2;
ledHoleWide = 11.5 + 1.27*2;
ledHoleXPos = 4.45;
ledHoleYPos = 22 ;

buttonSize = 4;
buttonXPos = 6.15;
buttonYPos = 50;
buttonBaseHeight = 2;
buttonBaseR = buttonSize/2 + 0.25 + layerWidth*3;

$SCREW_HEAD_DIAMETER = 5.4;
$SCREW_HEAD_DIAMETER_OFFSET = 0.6;
$SCREW_HEAD_HEIGHT = 2.0;
$SCREW_HEAD_HEIGHT_OFFSET = 0.9;

screwHeadRadius = $SCREW_HEAD_DIAMETER / 2 + $SCREW_HEAD_DIAMETER_OFFSET;
screwHeadHeight = $SCREW_HEAD_HEIGHT + $SCREW_HEAD_HEIGHT_OFFSET;
 

//------------------------------------------------------------------------- MODULES
module pcbLeg() {		
	translate([0, 0, 0])
	difference() { 											
		cylinder(h = height - floorHeight - pcbPositionZ - pcbHeight, r = 5.7/2);
	}
}

module buttonFrame() {
	translate([0, 0, -0.05])
	cylinder(h=1.5, r1=buttonSize/2 + 0.5 + 1, r2=buttonSize/2 + 0.2);
}

module buttonFrame2() {
	translate([0, 0, -0.05])
	cylinder(h=1.5, r1=buttonSize/2 + 0.8 + 1, r2=buttonSize/2 + 0.2);
}


//------------------------------------------------------------------------- MAIN BLOCK
difference()
{
																		// ADD
	union()
	{
		// Add Base
		linear_extrude(height = height/2 + blockLockSize, convexity = 10)
		minkowski()
		{									
			square([width, wide], center = true);
			circle(roundR);
		}
	}
																		// SUBSTRACT
	union()
	{
		// lift floor height
		translate([0, 0, floorHeight])
		{
			// Cut base inner hole
			linear_extrude(height = height, convexity = 10)
			minkowski()
			{
				square([width, wide], center = true);
				circle(roundR - pillarSize);
			}
			// Cut block lock
			translate([0, 0, height/2 - blockLockSize])
			linear_extrude(height = height, convexity = 10)
			minkowski()
			{
				square([width, wide], center = true);
				circle(roundR - layerWidth*3);
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
                  
					translate([-(roundR - pillarSize/2 - layerWidth*7), j,
                    -(floorHeight-layerHeight*3 + height)]) {
					if (j>0) 
					{
						rotate([0, 0, 45])
						translate([screwHoleRoundR, 0, 0]) {
						cylinder(h=height * 2, r=screwExt/2);
                        translate([0, 0, -layerHeight*3 + height]) cylinder(r = screwHeadRadius, h = screwHeadHeight + 10.2, center = false, $fn = 100); 
                        }
					}
					else
					{
						rotate([0, 0, -45])
						translate([screwHoleRoundR, 0, 0]) {
						cylinder(h=height * 2, r=screwExt/2);
                        translate([0, 0, -layerHeight*3 + height]) cylinder(r = screwHeadRadius, h = screwHeadHeight+ 10.2, center = false, $fn = 100);                        
                        
                        
                        
                        }
                                             
        color("blue") rotate([0, 0, 90])  translate([-3.5,-j+(i == 0 ? 10 : -10),0])  {
                cylinder(h=height * 2, r=screwExt/2);
                        translate([0, 0, -layerHeight*3 + height]) cylinder(r = screwHeadRadius, h = screwHeadHeight + 10.2, center = false, $fn = 100); 
  
			}
            
                  
       
        
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
				translate([0, i == 90 ? -10.5 : 10.5, height/2])
				cube([pillarSize, sidePanelYWidth-21, height], center=true);
                
                translate([0, i == 90 ? 41 : -41, height/2])
				cube([pillarSize, sidePanelYWidth, height], center=true);
			}
            
            // Cut USB and Power holes
			// Rotate due to panel upside down
			mirror([0, 1 , 0])
            
			// translate to pcb position
			translate([-pcbPositionX, -pcbWide/2, height - floorHeight*2 -pcbPositionZ-pcbHeight])
			{
				// Cut power hole
				translate([0, powerJackPosition, -(powerJackHeight-2)/2])
				cube([25, powerJackWide, powerJackHeight], center=true);
				// Cut usb hole
				translate([0, usbHolePosition, -1.3])
				cube([10, usbWide, usbHeight], center=true);
                
				// Cut connectors holes
				// upper
                // thiago
                if (backX == 0)
				translate([upperConnectorXPos + upperConnectorWidth/2 + frontY / 2,
						upperConnectorYPos + connectorWide/2 - upperConnectorYPos / 2,
						-25])
				linear_extrude(height = 50, convexity = 10)
				minkowski()
				{									
					square([upperConnectorWidth - connectorRoundR*2 + frontY,
					connectorWide - connectorRoundR*2 + upperConnectorYPos + frontX], center = true);
					circle(connectorRoundR);
				}
				// lower
				*translate([lowerConnectorXPos + lowerConnectorWidth/2,
						lowerConnectorYPos + connectorWide/2,
						-25])
				linear_extrude(height = 50, convexity = 10)
				minkowski()
				{									
					square([lowerConnectorWidth - connectorRoundR*2,
					connectorWide - connectorRoundR*2], center = true);
					circle(connectorRoundR);
				}
				// icsp
                if (backX == 0)
				translate([icspConnectorXPos + icspConnectorWide/2,
						icspConnectorYPos - icspConnectorWidth/2,
						-25])
				linear_extrude(height = 50, convexity = 10)
				minkowski()
				{									
					square([icspConnectorWide - connectorRoundR*2,
					icspConnectorWidth - connectorRoundR*2], center = true);
					circle(connectorRoundR);
				}
				// led hole
				translate([ledHoleXPos ,
						ledHoleYPos ,
						-25])
				linear_extrude(height = 50, convexity = 10)
				minkowski()
				{									
					square([ledHoleWidth - connectorRoundR*2,
					ledHoleWide - connectorRoundR*2], center = true);
					circle(connectorRoundR);
				}
				// button
				translate([buttonXPos, buttonYPos, -25])
				cylinder(h = 50, r = buttonSize/2 + 0.5);
				// buttonFrame
				translate([buttonXPos, buttonYPos, -(height - floorHeight -pcbPositionZ-pcbHeight)])
				buttonFrame();
                
                // wires
                translate([buttonXPos, buttonYPos - 10, -25])
				cylinder(h = 50, r = buttonSize/2 + 0.8);
				// buttonFrame
				translate([buttonXPos, buttonYPos - 10, -(height - floorHeight -pcbPositionZ-pcbHeight)])
				buttonFrame2();
			}
		}
	}
    
    if (backX > 0) rotate([0,180,0]) {
    translate([20,-backX/2,-backX/2]) cube([backX,backX,backX]);
    translate([20-3.2,-backX/2,-backX-21]) cube([backX,backX,backX]);        
    }
}


//------------------------------------------------------------------------- ADD PCB LEGS
// Rotate due to panel upside down
mirror([0, 1 , 0])

// Translate to pcbPositionX	
translate([-pcbPositionX, -pcbWide/2, 0])


if (backX == 0)
difference()
{
																		// ADD
	union()
	{
		// Add pcb legs
		for(i=[ [15.24, 50.8, 0],
          		[66.04, 35.56, 0],
          		[66.04, 7.62, 0] ])
		{
            if (i[0] != 66.04)
	    		translate(i)
	    		pcbLeg();
		}
		translate([13.97, 2.54, 0])
		cylinder(h = height - floorHeight - pcbPositionZ - pcbHeight, r = 4.5/2);

		// Add connectors walls
		// upper
        // thiago
		translate([upperConnectorXPos + upperConnectorWidth/2,
				upperConnectorYPos + connectorWide/2 - upperConnectorYPos / 2,
				0])
		linear_extrude(height = floorHeight+2, convexity = 10)
		minkowski()
		{									
			square([upperConnectorWidth - connectorRoundR*2 - frontY,
			connectorWide - connectorRoundR*2 + upperConnectorYPos - frontX], center = true);
			circle(connectorRoundR + layerWidth*3);
		}
		// lower
		*translate([lowerConnectorXPos + lowerConnectorWidth/2,
				lowerConnectorYPos + connectorWide/2,
				0])
		linear_extrude(height = floorHeight+2, convexity = 10)
		minkowski()
		{									
			square([lowerConnectorWidth - connectorRoundR*2,
			connectorWide - connectorRoundR*2], center = true);
			circle(connectorRoundR+ layerWidth*3);
		}
		// icsp
        if (frontX == 0)
		translate([icspConnectorXPos + icspConnectorWide/2,
				icspConnectorYPos - icspConnectorWidth/2,
				0])
		linear_extrude(height = floorHeight+2, convexity = 10)
		minkowski()
		{									
			square([icspConnectorWide - connectorRoundR*2,
			icspConnectorWidth - connectorRoundR*2], center = true);
			circle(connectorRoundR+ layerWidth*3);
		}
		// ledHole
		translate([ledHoleXPos,
				ledHoleYPos,
				0])
		linear_extrude(height = floorHeight+2, convexity = 10)
		minkowski()
		{									
			square([ledHoleWidth - connectorRoundR*2,
			ledHoleWide - connectorRoundR*2], center = true);
			circle(connectorRoundR + layerWidth*3);	
		}      
		// button
		translate([buttonXPos, buttonYPos, 0])

		cylinder(h = height - floorHeight - pcbPositionZ - pcbHeight - 3 - buttonBaseHeight - layerHeight*2,
				r = buttonBaseR);
        
        // wires
		translate([buttonXPos, buttonYPos - 10, 0])

		cylinder(h = height - floorHeight - pcbPositionZ - pcbHeight - 3 - buttonBaseHeight - layerHeight*2,
				r = buttonBaseR);

	}
																		// SUBSTRACT
	union()
	{
		// Cut connectors holes
		// upper
		translate([0, 0, height - floorHeight -pcbPositionZ-pcbHeight])
		{
			translate([upperConnectorXPos + upperConnectorWidth/2,
					upperConnectorYPos + connectorWide/2 - upperConnectorYPos / 2,
					-17])
			linear_extrude(height = 25, convexity = 10)
			minkowski()
			{									
				square([upperConnectorWidth - connectorRoundR*2,
				connectorWide - connectorRoundR*2 + upperConnectorYPos], center = true);
				circle(connectorRoundR);
			}
			// lower
			*translate([lowerConnectorXPos + lowerConnectorWidth/2,
					lowerConnectorYPos + connectorWide/2,
					-17])
			linear_extrude(height = 25, convexity = 10)
			minkowski()
			{									
				square([lowerConnectorWidth - connectorRoundR*2,
				connectorWide - connectorRoundR*2], center = true);
				circle(connectorRoundR);
			}
			// icsp
			translate([icspConnectorXPos + icspConnectorWide/2,
					icspConnectorYPos - icspConnectorWidth/2,
					-17])
			linear_extrude(height = 25, convexity = 10)
			minkowski()
			{									
				square([icspConnectorWide - connectorRoundR*2,
				icspConnectorWidth - connectorRoundR*2], center = true);
				circle(connectorRoundR);
			}
			// ledHole
			translate([ledHoleXPos ,
					ledHoleYPos,
					-17])
			linear_extrude(height = 25, convexity = 10)
			minkowski()
			{									
				square([ledHoleWidth - connectorRoundR*2,
				ledHoleWide - connectorRoundR*2], center = true);
				circle(connectorRoundR);	
			}
			// button
			translate([buttonXPos, buttonYPos, -17])
			cylinder(h = 15, r = buttonSize/2 + 0.25);
            
            // wires
            translate([buttonXPos, buttonYPos - 10, -17])
			cylinder(h = 15, r = buttonSize/2 + 0.6);

            // Cut power hole
            translate([0, powerJackPosition, -(powerJackHeight-2)/2])
                     cube([25, powerJackWide, powerJackHeight], center=true);
            }
		// buttonFrame
		translate([buttonXPos, buttonYPos, 0])
		buttonFrame();
            
        // wires
		translate([buttonXPos, buttonYPos - 10, 0])
		buttonFrame2();
	}
    
    if (backX > 0) rotate([0,180,0])
    translate([-14,-backX/4,-backX/2]) cube([backX,backX,backX]);
}