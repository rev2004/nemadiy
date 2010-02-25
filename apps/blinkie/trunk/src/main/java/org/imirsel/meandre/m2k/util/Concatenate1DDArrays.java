package org.imirsel.meandre.m2k.util;

import org.meandre.annotations.Component;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentInput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;
//import ncsa.d2k.core.modules.DataPrepModule;

import org.meandre.core.ComponentContextProperties;
import org.meandre.annotations.ComponentProperty;

import java.util.Vector;
import org.imirsel.meandre.m2k.StreamTerminator;

/**
 * Concatenates two double arrays into one double array.  A D2K/M2K module that reads in two
 * double arrays of real numbers, and returns an array concatenating them.
 *
 * @author David Tcheng
 * Modified by Lily Dong
 */
@Component(creator = "David Tcheng", description = "concatenate two 1-D double arrays", name = "Concatenate1DDArrays", tags = "concatenation" /*, firingPolicy = Component.FiringPolicy.all */)
public class Concatenate1DDArrays implements ExecutableComponent {//extends DataPrepModule {
    @ComponentInput(description = "the first input(1-D double array)", name = "input_1")
    final static String DATA_INPUT_1 = "input_1";
    @ComponentInput(description = "the second input(1-D double array)", name = "input_2")
    final static String DATA_INPUT_2 = "input_2";
    @ComponentOutput(description = "the concatenated output(1-D double array)", name = "output")
    final static String DATA_OUTPUT = "output";

  
    /*    public boolean isReady(){
    if ((this.getFlags()[0] > 0)&&(this.getFlags()[1] > 0))
    {
    return true;
    }
    return false;
    }*/
    /**
     * Concatenates two double arrays into one double array.
     */
    public void execute(ComponentContext cc)
            throws ComponentExecutionException, ComponentContextException {

        //System.out.println("\nConcatenate1DDArrays");

        Object object1 = (cc.getDataComponentFromInput(DATA_INPUT_1));
        Object object2 = (cc.getDataComponentFromInput(DATA_INPUT_2));

        if ((object1 instanceof StreamTerminator) && (object2 instanceof StreamTerminator)) {
            cc.pushDataComponentToOutput(DATA_OUTPUT, object1);
        } else if ((object1 instanceof StreamTerminator) || (object2 instanceof StreamTerminator)) {
            System.out.println("object 1 class: " + object1.getClass().toString());
            System.out.println("object 2 class: " + object2.getClass().toString());
            throw new RuntimeException(cc.getExecutionInstanceID() + " StreamTerminator on one port but not the other - can't perform concatenation");
        } else {

            double[] array1 = (double[]) object1;
            double[] array2 = (double[]) object2;
            int array1NumValues = array1.length;
            int array2NumValues = array2.length;
            int array3NumValues = array1NumValues + array2NumValues;

            double[] array3 = new double[array3NumValues];

            int index = 0;
            for (int d = 0; d < array1NumValues; d++) {
                array3[index++] = array1[d];
            }

            for (int d = 0; d < array2NumValues; d++) {
                array3[index++] = array2[d];
            }

            cc.pushDataComponentToOutput(DATA_OUTPUT, array3);

        }
    /*int array1NumValues = array1.length;
    int array2NumValues = array2.length;
    int array3NumValues = array1NumValues + array2NumValues;
    double[] array3 = new double[array3NumValues];
    int index = 0;
    for (int d = 0; d < array1NumValues; d++) {
    array3[index++] = array1[d];
    //System.out.println(array1[d] + " ");
    }
    //System.out.println();
    for (int d = 0; d < array2NumValues; d++) {
    array3[index++] = array2[d];
    //System.out.println(array2[d] + " ");
    }
    //System.out.println();
    cc.pushDataComponentToOutput(DATA_OUTPUT, array3);
    //this.pushOutput(array3, 0);*/
    }

    //for seasr only
    public void initialize() {

    }

    public void dispose() {
    }

    public void initialize(ComponentContextProperties ccp) {

    }

    public void dispose(ComponentContextProperties ccp) {
    }
}
