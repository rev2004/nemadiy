/*
 * JetColourMap.java
 *
 * Created on 13 January 2006, 21:18
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.imirsel.m2k.vis;
import java.awt.Color;
/**
 * Jet Colour map implementation.
 * @author Kris West
 */
public class JetColourMap extends ColourMap{
    
    /** Creates a new instance of JetColourMap */
    public JetColourMap() {
        //initialise the map
        mapSize = 64;
        map = new Color[mapSize];
        map[0] = new Color(0f,0f,0.5625f);
        map[1] = new Color(0f,0f,0.625f);
        map[2] = new Color(0f,0f,0.6875f);
        map[3] = new Color(0f,0f,0.75f);
        map[4] = new Color(0f,0f,0.8125f);
        map[5] = new Color(0f,0f,0.875f);
        map[6] = new Color(0f,0f,0.9375f);
        map[7] = new Color(0f,0f,1f);
        map[8] = new Color(0f,0.0625f,1f);
        map[9] = new Color(0f,0.125f,1f);
        map[10] = new Color(0f,0.1875f,1f);
        map[11] = new Color(0f,0.25f,1f);
        map[12] = new Color(0f,0.3125f,1f);
        map[13] = new Color(0f,0.375f,1f);
        map[14] = new Color(0f,0.4375f,1f);
        map[15] = new Color(0f,0.5f,1f);
        map[16] = new Color(0f,0.5625f,1f);
        map[17] = new Color(0f,0.625f,1f);
        map[18] = new Color(0f,0.6875f,1f);
        map[19] = new Color(0f,0.75f,1f);
        map[20] = new Color(0f,0.8125f,1f);
        map[21] = new Color(0f,0.875f,1f);
        map[22] = new Color(0f,0.9375f,1f);
        map[23] = new Color(0f,1f,1f);
        map[24] = new Color(0.0625f,1f,0.9375f);
        map[25] = new Color(0.125f,1f,0.875f);
        map[26] = new Color(0.1875f,1f,0.8125f);
        map[27] = new Color(0.25f,1f,0.75f);
        map[28] = new Color(0.3125f,1f,0.6875f);
        map[29] = new Color(0.375f,1f,0.625f);
        map[30] = new Color(0.4375f,1f,0.5625f);
        map[31] = new Color(0.5f,1f,0.5f);
        map[32] = new Color(0.5625f,1f,0.4375f);
        map[33] = new Color(0.625f,1f,0.375f);
        map[34] = new Color(0.6875f,1f,0.3125f);
        map[35] = new Color(0.75f,1f,0.25f);
        map[36] = new Color(0.8125f,1f,0.1875f);
        map[37] = new Color(0.875f,1f,0.125f);
        map[38] = new Color(0.9375f,1f,0.0625f);
        map[39] = new Color(1f,1f,0f);
        map[40] = new Color(1f,0.9375f,0f);
        map[41] = new Color(1f,0.875f,0f);
        map[42] = new Color(1f,0.8125f,0f);
        map[43] = new Color(1f,0.75f,0f);
        map[44] = new Color(1f,0.6875f,0f);
        map[45] = new Color(1f,0.625f,0f);
        map[46] = new Color(1f,0.5625f,0f);
        map[47] = new Color(1f,0.5f,0f);
        map[48] = new Color(1f,0.4375f,0f);
        map[49] = new Color(1f,0.375f,0f);
        map[50] = new Color(1f,0.3125f,0f);
        map[51] = new Color(1f,0.25f,0f);
        map[52] = new Color(1f,0.1875f,0f);
        map[53] = new Color(1f,0.125f,0f);
        map[54] = new Color(1f,0.0625f,0f);
        map[55] = new Color(1f,0f,0f);
        map[56] = new Color(0.9375f,0f,0f);
        map[57] = new Color(0.875f,0f,0f);
        map[58] = new Color(0.8125f,0f,0f);
        map[59] = new Color(0.75f,0f,0f);
        map[60] = new Color(0.6875f,0f,0f);
        map[61] = new Color(0.625f,0f,0f);
        map[62] = new Color(0.5625f,0f,0f);
        map[63] = new Color(0.5f,0f,0f);
        
        interval = 1.0f / (float)mapSize;
    }
    
}
