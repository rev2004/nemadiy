/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.meandre.m2k;

import java.io.Serializable;

/**
 *
 * @author dtcheng
 */
public class StreamTerminator implements Serializable{

    public static boolean isStreamTerminator(Object anObj){
        if (anObj instanceof StreamTerminator){
            return true;
        }else{
            return false;
        }
    }
}
