package org.imirsel.meandre.m2k.math;

//import ncsa.d2k.core.modules.*;
import java.lang.Math.*;
/*import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;*/
import org.imirsel.m2k.math.Mathematics;
import org.imirsel.meandre.m2k.StreamTerminator;
import org.meandre.annotations.Component;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;
//import ncsa.d2k.core.modules.ComputeModule;
//import ncsa.d2k.core.modules.CustomModuleEditor;
//import ncsa.d2k.core.modules.PropertyDescription;

import org.meandre.core.ComponentContextProperties;

/**
 *
 * Computes the Norm of an array of double numbers.
 * A D2K/M2K module that reads in a double array, and returns
 * the value of norm. Norm type can be specified by the user.
 *
 * @author Andreas Ehmann
 * Modified by Lily Dong
 */

@Component(creator="Andreas Ehmann",
           description="compute the vector norm of specified type",
           name="Norm",
           tags="computation")

public class Norm implements ExecutableComponent {//extends ComputeModule {
    @ComponentInput(description="input vector for norm computation(1-D double array)",
                    name= "input")
    final static String DATA_INPUT = "input";

    @ComponentOutput(description="vector norm(double 1x1 array)",
                     name="output")
    final static String DATA_OUTPUT = "output";

    @ComponentProperty(defaultValue="2",
                       description="the type of norm to calculate",
                       name="NormType")
    final static String DATA_PROPERTY_1 = "NormType";
    @ComponentProperty(defaultValue="2.0",
                       description="field for entering the desired p-norm",
                       name="p")
    final static String DATA_PROPERTY_2 = "p";

    private String NormType = "2";

    /**
     * Sets norm type.
     * @param value the type of the norm
     */
    public void setNormType(String value)
    {
        this.NormType = value;
    }

    /**
     * Gets norm type.
     * @return norm type
     */
    public String getNormType()
    {
        return this.NormType;
    }

    private double p = 2.0;

    /**
     * Sets the p.
     * @param value the value of p
     */
    public void setp(double value)
    {
        this.p = value;
    }

    /**
     * Returns p
     * @return p
     */
    public double getp()
    {
        return this.p;
    }

    /**
     * Returns an array of description objects for each property of
     * the Module.
     * @return an array of description objects for each property of
     * the Module.
     */
/*    public PropertyDescription[] getPropertiesDescriptions()
    {
        PropertyDescription[] pds = new PropertyDescription[2];
        pds[0] = new PropertyDescription("normType", "Norm Type",
                "The type of norm to calculate.");
        pds[1] = new PropertyDescription("p",
                "p",
                "Field for entering the desired p-norm when p is selected from" +
                "the pull-down menu.");
        return pds;
    }*/

    /**
     * Returns the name of the module.
     * @return the module name
     */
    public String getModuleName()
    {
        return "Norm";
    }

    /**
     * Returns information about the module.
     * @return Module information
     */

    public String getModuleInfo()
    {
        return "<p>Overview: This module computes the vector norm of specified type " +
                "(1-Norm, 2-Norm, inf-Norm, -inf-Norm, and p-Norm)." +
                "</p><p>Detailed Description: This module computes the type of norm " +
                "specified from the pull-down menu.  A p-norm is defined as the p-th root of the " +
                " sum of the absolute value of each vector element raised to the p-th power, i.e. " +
                "(sum |xn|^p)^(1/p), where xn is the nth array element.  Therefore, the 1-norm is the sum " +
                "of the absolute values of the elements.  The 2-norm is commonly known as the RMS of the input vector" +
                "Inf norm corresponds to the maximum " +
                "absolute value in the input array.  -inf norm corresponds to the minimum " +
                "absolute value in the input array.  When p-norm is selected, the p used in the" +
                "calculation is specified in the p text field." +
                "</p><p>Data Handling: The input data is not modified. ";
    }

    /**
     * Returns a text name for the given input.
     * @param i the index of the input
     * @return the name of the indexed input
     *
     */
    public String getInputName(int i)
    {
        switch (i)
        {
            case 0:
                return "Input";
            default:
                return "Error!  No such input.  ";
        }
    }

    /**
     * Returns a text description for the indicated input.
     * @param i the index of the input
     * @return a text description of the indexed input
     */
    public String getInputInfo(int i)
    {
        switch (i)
        {
            case 0:
                return "Input vector for norm computation (1-D double Array)";
            default:
                return "Error!  No such input.  ";
        }
    }

    /**
     * Returns an array of strings containing the Java data types of
     * the input.
     * @return the fully qualified java types for each of the inputs
     *
     */
    public String[] getInputTypes()
    {
        String[] types = { "[D" };
        return types;
    }

    /**
     * Returns a text name for the given output.
     * @param i the index of the output
     * @return the name of the indexed output
     */
    public String getOutputName(int i)
    {
        switch (i)
        {
            case 0:
                return "Vector Norm";
            default:
                return "Error!  No such output.  ";
        }
    }

    /**
     * Returns a text description for the given output.
     * @param i the index of the output
     * @return the name of the indexed output
     */
    public String getOutputInfo(int i)
    {
        switch (i)
        {
            case 0:
                return "Vector norm (Double 1x1 Array)";
            default:
                return "Error!  No such output.  ";
        }
    }

    /**
     * Returns an array of strings containing the Java data types of
     * the outputs.
     * @return the fully qualified java types for each of the outputs.
     */
    public String[] getOutputTypes()
    {
        String[] types = { "[D" };
        return types;
    }

    /**
     *Calculate the norm.
     */
    public void execute(ComponentContext cc)
            throws ComponentExecutionException, ComponentContextException {//doit(){
        //System.out.println("\nNorm");

        //for seasr only
        NormType = (String)(cc.getProperty(DATA_PROPERTY_1));
        p = Double.valueOf(cc.getProperty(DATA_PROPERTY_2));

        Object object1 = cc.getDataComponentFromInput(DATA_INPUT);
        if (object1 instanceof StreamTerminator){
            cc.pushDataComponentToOutput(DATA_OUTPUT, object1);
        }else{
            double[] input = (double []) object1;

            double[] norm = new double[1];
            int normType = 2;
            if (NormType.equals("1"))
                normType = 1;
            if (NormType.equals("2"))
                normType = 2;
            if (NormType.equals("inf"))
                normType = 3;
            if (NormType.equals("-inf"))
                normType = 4;
            if (NormType.equals("p"))
                normType = 5;

            switch (normType)
            {
                case 1: /* 1-Norm */
                    norm[0] = Mathematics.norm(input, 1.0);
                    break;

                case 2: /* 2-Norm */
                    norm[0] = Mathematics.norm(input, 2.0);
                    break;

                case 3: /* inf-Norm */
                    norm[0] = Mathematics.normInf(input);
                    break;

                case 4: /* -inf-Norm */
                    norm[0] = Mathematics.normNegInf(input);
                    break;

                case 5: /* p-Norm */
                    norm[0] = Mathematics.norm(input, p);
                    break;

            }

            //System.out.println("norm[0] = " + norm[0]);

            cc.pushDataComponentToOutput(DATA_OUTPUT, norm);
        }
        //this.pushOutput(norm, 0);
    }

    //for seasr only
    public void initialize() {}
    public void dispose() {}

    public void initialize(ComponentContextProperties ccp) {}
    public void dispose(ComponentContextProperties ccp) {}
}
