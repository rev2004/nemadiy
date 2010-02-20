package org.imirsel.m2k.io.weka;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import weka.core.*;

/**
 * Translate Signal object into a Weka Instance object.
 * @author  Kris West
 */
public class Signal2Instance {
    
    //public static final String[] metadata = { Signal.PROP_FILE_LOCATION };
    
    /** Creates a new instance of SignalArray2Instances */
    public Signal2Instance() {
    }
    
    /**
     * Change each row of Signal data in the form of Instance that can be processed in
     * WEKA modules. That is, each Signal is translated into Instances,
     * and Signal array into Instances array.
     * @throws java.lang.Exception if an error occurs.
     */
    public static Instances convert(Signal aSignal, Attribute classMetadata)// throws java.lang.Exception
    {
        
        String classValue =  null;
        if (classMetadata == null){
            classMetadata = new Attribute("class");
        }
        try {
            classValue = aSignal.getStringMetadata(classMetadata.name());
            if (classValue.trim().equals("")){
                classValue = null;
            }
        } catch (noMetadataException ex) {
            classValue =  null;
            
        }
        weka.core.FastVector fv = new weka.core.FastVector();
        String[] columnLabels = null;
        //FastVector dummy = null;
        //Attribute classAttribute = new Attribute(classMetadata, dummy);
        
        //classAttribute.addStringValue(classValue);
        
        Instances inst;
        try {
            columnLabels = aSignal.getStringArrayMetadata(Signal.PROP_COLUMN_LABELS);
            for (int column=0; column<columnLabels.length; column++){
                fv.addElement(new Attribute(columnLabels[column]));
            }
            if (classMetadata != null){
                fv.addElement(classMetadata);
            }
            
            inst = new Instances(aSignal.getStringMetadata(Signal.PROP_FILE_LOCATION), fv, aSignal.getNumRows());
            if (classMetadata != null){
                inst.setClass(classMetadata);
            }
        } catch (noMetadataException ex) {
            throw new RuntimeException("Signal Object conversion failed!",ex);
        }
        
        for (int row=0; row<aSignal.getNumRows(); row++){
            Instance anInst = new Instance(1,aSignal.getDataRow(row));
            anInst.insertAttributeAt(aSignal.getNumCols());
            anInst.setDataset(inst);
            //anInst.setValue(aSignal.getNumCols(),classValue);
            if (classMetadata != null){
                if (classValue != null){
                    try{
                        anInst.setClassValue(classValue);
                    }catch(Exception iae)
                    {
                        try {
                            System.out.println("WARNING: Signal2Instance unable to set class value from attribute '" + classMetadata.name() + "' to '" + classValue + "' for Signal representing: " + aSignal.getStringMetadata(Signal.PROP_FILE_LOCATION));
                        } catch (noMetadataException ex) {
                            Logger.getLogger(Signal2Instance.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        iae.printStackTrace();
                        System.out.println("Signal Object that failed:");
                        System.out.println(aSignal.toString());
                        
                        anInst.setClassMissing();
                    }
                }else
                {
                    anInst.setClassMissing();
                }
            }
            inst.add(anInst);
        }
        try {
            
            inst.setRelationName(aSignal.getStringMetadata(Signal.PROP_FILE_LOCATION));
        } catch (noMetadataException ex) {
            throw new RuntimeException("Signal Object conversion failed!",ex);
        }
        
        //String className = aSignal.getStringMetadata(Signal.PROP_CLASS);
        /*if (classMetadata != null){
         
            inst.setClass(new Attribute(classValue));
        }*/
        
        return inst;
    }
    
    protected static Signal convert(Instances inst) throws java.lang.Exception {
        
        String[] columnLabels = new String[inst.numAttributes()];
        double[][] theData = new double[columnLabels.length][inst.numInstances()];
        
        for (int i = 0; i < inst.numInstances(); i++) {
            Instance anInstance = inst.instance(i);
            for (int j = 0; j < columnLabels.length; j++) {
                theData[j][i] = anInstance.value(j);
            }
            
        }
        
        Signal theSig = new Signal(inst.relationName());
        
        String className = inst.classAttribute().value(0);
        if ((!className.equals(""))||(className != null)) {
            theSig.setMetadata(Signal.PROP_CLASS,className);
        }
        
        return theSig;
    }
    
    
}
