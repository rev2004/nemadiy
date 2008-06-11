
package org.imirsel.m2k.vis;
import java.awt.Color;
/**
 * An abstract class represneting a ColorMap that linearly maps a finite set of 
 * Colors to float values in the range 0:1
 * @author kris
 */
public abstract class ColourMap {
    /** Array of colours */
    Color[] map;
    /** Number of Colors in map */
    int mapSize;
    /** 1.0f divided by mapSize */
    float interval;
    /** Creates a new instance of ColourMap */
    public ColourMap() {
    }
    
    /**
     * Maps a floating point value to a colour from the colour map.
     * @param val a floating point value
     * @return a colour from the colour map.
     */
    public Color mapToColor(float val){
        if ((val<0.0f)||(val > 1.0f))
        {
            throw new RuntimeException("ColourMap: Can only map values in the range 0:1 to colours!");
        }
        int idx = Math.round(val / interval);
        if (idx == mapSize){
            idx = mapSize-1;
        }
        return map[idx];
    }
    
}
