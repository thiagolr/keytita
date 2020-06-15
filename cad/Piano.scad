/* Piano Keyboard
 *
 * Author: Thiago Rosa (http://www.thiagorosa.com)
 * Project: KeyTita (https://github.com/thiagolr/keytita)
 * License: CC BY-SA 4.0 (https://creativecommons.org/licenses/by-sa/4.0)
 * 
 * Design: http://datagenetics.com/blog/may32016/index.html
 * 525|490|490|490|525|455|490|490|490|490|490|455
 *
 */

$WHITE_LENGTH = 135;
$WHITE_WIDTH = 22;
$WHITE_HEIGHT = 10;
$BLACK_LENGTH = 85;
$BLACK_HEIGHT = 15;
$SPACE_WIDTH = 1;
$THICKNESS = 0;
$OCTAVES = 1;
$INCLUDE_A0_B0 = false;
$INCLUDE_END_C = false;

$INCLUDE_SOLID_SPACE = false;
$KEY_COLOR_ALPHA = 1.0;

//Piano();
//Piano61();
//Piano88();
   
module Piano(whiteLength = $WHITE_LENGTH, whiteWidth = $WHITE_WIDTH, whiteHeight = $WHITE_HEIGHT, blackLength = $BLACK_LENGTH, blackHeight = $BLACK_HEIGHT, spaceWidth = $SPACE_WIDTH, thickness = $THICKNESS, octaves = $OCTAVES, includeA0B0 = $INCLUDE_A0_B0, includeEndC = $INCLUDE_END_C, includeSolidSpace = $INCLUDE_SOLID_SPACE) {
   
    baseWhiteWidth = 840;   
    baseSizeA = 525;
    baseSizeB = 490;
    baseSizeC = 455;
    baseFactor = whiteWidth / baseWhiteWidth;
    
    sizeA = baseSizeA * baseFactor;
    sizeB = baseSizeB * baseFactor;
    sizeC = baseSizeC * baseFactor;
    sizeBSpaced = sizeB + spaceWidth;
    
    holeSizeOffset = 0;
    holeSizeX = sizeBSpaced;
    holeSizeY = blackLength + spaceWidth * 2;
    holeSizeZ = blackHeight + holeSizeOffset;     
    
    a0b0Width = includeA0B0 ? (2 * whiteWidth) + (2 * spaceWidth) : 0;
    octaveWidth = (7 * whiteWidth) + (7 * spaceWidth);
    blackWidth = sizeBSpaced - spaceWidth * 2;

    blackPositions = [
        sizeA * 1,
        sizeA * 1 + sizeB * 1 + sizeBSpaced * 1,
        sizeA * 2 + sizeB * 1 + sizeBSpaced * 2 + sizeC * 1 + spaceWidth,
        sizeA * 2 + sizeB * 2 + sizeBSpaced * 3 + sizeC * 1 + spaceWidth,
        sizeA * 2 + sizeB * 3 + sizeBSpaced * 4 + sizeC * 1 + spaceWidth
    ];
    
    difference() {
        union() {
            for (i = [0 : octaves - 1]) {
                for (j = [0 : 6]) {
                    posX = a0b0Width + i * octaveWidth + j * (whiteWidth + spaceWidth);
                    Key("white", posX, 0, 0, whiteWidth, whiteLength, whiteHeight);
                    if (includeSolidSpace) {
                        Key("blue", posX + whiteWidth, 0, 0, spaceWidth, whiteLength, whiteHeight); 
                    }
                }            
            }      
        }
     
        for (i = [0 : octaves - 1]) {
            for (j = [0 : 4]) {
                posX = a0b0Width + i * octaveWidth + blackPositions[j];         
                posY = whiteLength - blackLength - spaceWidth;
                posZ = -holeSizeOffset / 2;
                Key("white", posX, posY, posZ, holeSizeX, holeSizeY, holeSizeZ);
            }
        }
    }
    
    for (i = [0 : octaves - 1]) {
        for (j = [0 : 4]) {
            posX = a0b0Width + i * octaveWidth + blackPositions[j] + (sizeBSpaced - blackWidth) / 2;
            posY = whiteLength - blackLength;
            posZ = 0;             
            Key("black", posX, posY, posZ, blackWidth, blackLength, blackHeight);
            if (includeSolidSpace || thickness > 0) {
                posX = a0b0Width + i * octaveWidth + blackPositions[j];
                posY = whiteLength - blackLength - spaceWidth;
                posZ = -holeSizeOffset / 2;                
                if (includeSolidSpace) {
                    Key("blue", posX, posY, posZ, holeSizeX, holeSizeY, holeSizeZ);
                }                
                if (thickness > 0) {           
                    Key("red", posX - thickness, posY - thickness, posZ, holeSizeX + thickness * 2, holeSizeY + thickness, holeSizeZ + thickness); 
                }
            }
        }    
    }
    
    if (includeA0B0) {
        blackPosition = whiteWidth * 2 - sizeC - sizeBSpaced;
        
        difference() {
            union() {
                for (j = [0 : 1]) {
                    posX = j * (whiteWidth + spaceWidth);                    
                    Key("white", posX, 0, 0, whiteWidth, whiteLength, whiteHeight);
                    if (includeSolidSpace) {
                        Key("blue", posX + whiteWidth, 0, 0, spaceWidth, whiteLength, whiteHeight); 
                    }
                }            
            } 
         
            posX = blackPosition;         
            posY = whiteLength - blackLength - spaceWidth;
            posZ = -holeSizeOffset / 2;
            Key("white", posX, posY, posZ, holeSizeX, holeSizeY, holeSizeZ);              
        }   
      
        posX = blackPosition + (sizeBSpaced - blackWidth) / 2;
        posY = whiteLength - blackLength;
        posZ = 0;
        Key("black", posX, posY, posZ, blackWidth, blackLength, blackHeight);

        if (includeSolidSpace || thickness > 0) {
            posX = blackPosition;         
            posY = whiteLength - blackLength - spaceWidth;
            posZ = -holeSizeOffset / 2;            
            if (includeSolidSpace) {
                Key("blue", posX, posY, posZ, holeSizeX, holeSizeY, holeSizeZ);
            }            
            if (thickness > 0) {           
                Key("red", posX - thickness, posY - thickness, posZ, holeSizeX + thickness * 2, holeSizeY + thickness, holeSizeZ + thickness); 
            }
        }
    }
    
    if (includeEndC) {
        posX = a0b0Width + octaves * octaveWidth; 
        Key("white", posX, 0, 0, whiteWidth, whiteLength, whiteHeight);        
        if (includeSolidSpace) {
            Key("blue", posX + whiteWidth, 0, 0, spaceWidth, whiteLength, whiteHeight); 
        }
    }
}

module Key(colorKey, posX, posY, posZ, sizeX, sizeY, sizeZ) {
    translate([posX, posY, posZ])          
        color(colorKey, $KEY_COLOR_ALPHA)
            cube([sizeX, sizeY, sizeZ]);
}

module Piano61(whiteLength = $WHITE_LENGTH, whiteWidth = $WHITE_WIDTH, whiteHeight = $WHITE_HEIGHT, blackLength = $BLACK_LENGTH, blackHeight = $BLACK_HEIGHT, spaceWidth = $SPACE_WIDTH) {  
    
    Piano(whiteLength, whiteWidth, whiteHeight, blackLength, blackHeight, spaceWidth, octaves = 5, includeA0B0 = false, includeEndC = true);
    
}

module Piano88(whiteLength = $WHITE_LENGTH, whiteWidth = $WHITE_WIDTH, whiteHeight = $WHITE_HEIGHT, blackLength = $BLACK_LENGTH, blackHeight = $BLACK_HEIGHT, spaceWidth = $SPACE_WIDTH) {
    
    Piano(whiteLength, whiteWidth, whiteHeight, blackLength, blackHeight, spaceWidth, octaves = 7, includeA0B0 = true, includeEndC = true);
    
}