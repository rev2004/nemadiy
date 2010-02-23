/*
 * SignalTrainedTransform.java
 *
 * Created on July 20, 2005, 3:36 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.imirsel.m2k.transforms;

import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * This interface defines the methods of a trainable M2K transform that operates 
 * directly on an <code>org.imirsel.m2k.util.Signal</code> Object.
 *
 * @author Kris West
 */
public interface SignalTrainedTransform extends Serializable{
    
    /**
     * Returns a flag indicating whether the transform has been trained or not.
     * @return a flag indicating whether the transform has been trained or not.
     */
    public boolean isTrained();
    
    /**
     * Sets a flag indicating whether the transform has been trained or not.
     * @param val a flag indicating whether the transform has been trained or not.
     */
    public void setIsTrained(boolean val);
    
    /**
     * Returns the name of the tranform.
     * @return the name of the tranform.
     */
    public String getTransformName();
    
    /**
     * Trains the transform on the array of Signal objects.
     * @param signals The array of Signal Objects to train the transform on.
     * @throws noMetadataException Thrown if there is no class metadata to train 
     * the transform with.
     */
    public void train(org.imirsel.m2k.util.Signal[] signals) throws noMetadataException;
    
    /**
     * Transform a data matrix (<code>Signal[]</code>) using the trained transform.
     * A RuntimeException should be thrown if the transform is not trained.
     * @param input Signal[] to transform.
     * @param inPlace A flag indicating whether the transform should be performed 
     * 'in place' or on a clone of the array.
     * @return Transformed array of Signal objects.
     */
    public Signal[] transform(Signal[] input, boolean inPlace);
    
    /**
     * Tranforms a single <code>Signal</code> object using the trained transform.
     * @param input Signal to be transformed
     * @param inPlace A flag indicating whether the transform should be performed 
     * 'in place' or on a clone of the <code>Signal</code> Object.
     * @return Transformed Signal Object
     */
    public Signal transform(Signal input, boolean inPlace);
    
    /**
     * Transforms a single vector of data using the trained transform.
     * @param input Vector to transform.
     * @return Transformed vector.
     */
    public double[] transform(double[] input);
}
