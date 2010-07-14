package org.imirsel.nema.components.control;


import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.imirsel.nema.components.NemaComponent;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContext;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContextProperties;

@Component(creator="Tester", description="", tags="seasr fork redundant", name="ForkX5")
public class ForkX5 extends NemaComponent {

    static public final int s_REFERENCE = 0;
    static public final int s_CLONE_SHALLOW = 1;
    static public final int s_SERIALIZE_DEEP = 2;
    static public final int s_CONSTRUCTOR = 3;
    static public final int s_CUSTOM = 4;

    @ComponentInput(description="input object", name="input_object")
    static public final String s_INPUT_1 = "input_object";
    @ComponentOutput(description="output object", name="output_object_1")
    static public final String s_OUTPUT_1 = "output_object_1";
    @ComponentOutput(description="output object", name="output_object_2")
    static public final String s_OUTPUT_2 = "output_object_2";
    @ComponentOutput(description="output object", name="output_object_3")
    static public final String s_OUTPUT_3 = "output_object_3";
    @ComponentOutput(description="output object", name="output_object_4")
    static public final String s_OUTPUT_4 = "output_object_4";
    @ComponentOutput(description="output object", name="output_object_5")
    static public final String s_OUTPUT_5 = "output_object_5";

    @ComponentProperty(defaultValue="0", description="Replication Mode", name="Replication_Mode")
    static public final String s_REPLICATION_METHOD = "Replication_Mode";
    @ComponentProperty(defaultValue=" ", description="Custom Copy Method Name", name="Custom_Copy_Method_Name")
    static public final String s_CUSTOM_COPY_METHOD_NAME =
            "Custom_Copy_Method_Name";

  
    public ForkX5() {
    }

    
    @Override
	public void initialize(ComponentContextProperties ccp) throws ComponentExecutionException, ComponentContextException {
    	super.initialize(ccp);
    }

    @Override
	public void dispose(ComponentContextProperties ccp) throws ComponentContextException {
    	super.dispose(ccp);
    }


    @Override
	public void execute(ComponentContext cc) throws ComponentExecutionException,
            ComponentContextException {

        String fn = cc.getProperty(ForkX5.s_REPLICATION_METHOD);
        if (fn == null || fn.length() == 0) {
            throw new RuntimeException("No replication mode given.");
        }

        try {
            int repMode = Integer.parseInt(fn);
            Object dat = cc.getDataComponentFromInput(ForkX5.s_INPUT_1);
            switch (repMode) {
            case 0: //REFERENCE
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_1, dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_2, dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_3, dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_4, dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_5, dat);
                break;

            case 1: //CLONE Shallow Copy

                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_1, dat);
                Object obj = makeClone(dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_2, obj);
                obj = makeClone(dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_3, obj);
                obj = makeClone(dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_4, obj);
                obj = makeClone(dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_5, obj);
                break;

            case 2: //SERIALIZE Deep Copy

                obj = null;
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_1, dat);
                obj = makeDeepCopy(dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_2, obj);
                obj = makeDeepCopy(dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_3, obj);
                obj = makeDeepCopy(dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_4, obj);
                obj = makeDeepCopy(dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_5, obj);
                break;

            case 3: //CONSTRUCTOR

                obj = null;
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_1, dat);
                obj = copyViaConstructor(dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_2, obj);
                obj = copyViaConstructor(dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_3, obj);
                obj = copyViaConstructor(dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_4, obj);
                obj = copyViaConstructor(dat);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_5, obj);
                break;

            case 4: //CUSTOM

                String meth = cc.getProperty(ForkX5.s_CUSTOM_COPY_METHOD_NAME);
                obj = null;
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_1, dat);
                obj = copyViaCustomMethod(dat, meth);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_2, obj);
                obj = copyViaCustomMethod(dat, meth);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_3, obj);
                obj = copyViaCustomMethod(dat, meth);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_4, obj);
                obj = copyViaCustomMethod(dat, meth);
                cc.pushDataComponentToOutput(ForkX5.s_OUTPUT_5, obj);
                break;

            default:
                throw new Exception(
                        "No anticipated replication mode matches requested value.");
            }

        } catch (Exception e) {
            throw new ComponentExecutionException("ForkX2 Component: " +
                                                  e.getMessage(), e);
        }

    } // end method doit

    //=================
    // Package Methods
    //=================

    /**
     * Need to use reflection here because "Cloneable" interface is
     * a marker interface (i.e. it does not define the method "clone").
     * The incoming objects must still implement the cloneable interface
     * however, otherwise a CloneNotSupported error will be thrown.
     *
     * Also, the clone method in Object (which is protected) must be
     * overriden and declared public, otherwise the reflection methods
     * will fail since they can only access public methods by default.
     *
     * @param dat Object Object to clone.
     * @return Object Cloned object.
     * @throws Exception Reflection exception.
     */
    Object makeClone(Object dat) throws Exception {
        return dat.getClass().getMethod("clone", (Class[])null).invoke(dat, (Object[])null);
    }

    /**
     * These objects must be serializable.  If they are not then
     * when we attempt serialization an error will be generated.
     *
     * @param dat Object Object to copy.
     * @return Object Copied object.
     * @throws Exception IO Exception
     */
    Object makeDeepCopy(Object dat) throws Exception {
        Object obj = null;
        // Write the object out to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(dat);
        out.flush();
        out.close();

        // Make an input stream from the byte array and read
        // a copy of the object back in.
        ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
        obj = in.readObject();
        return obj;
    }

    /**
     * These objects must supply a publically accessible constructor
     * that takes the same class type object as input.  Otherwise,
     * the reflection calls will generate an error.
     *
     * @param dat Object Object to copy.
     * @return Object Copied object.
     * @throws Exception Reflection exception.
     */
    Object copyViaConstructor(Object dat) throws Exception{
        Object obj = null;
        Class cls = dat.getClass();
        Class[] clses = new Class[] {cls};
        Object[] objs = new Object[] {dat};
        obj = cls.getConstructor(clses).newInstance(objs);
        return obj;
    }

    /**
     * These objects must supply a publically accessible method, the
     * name of which matches the supplied string via property
     * s_CUSTOM_COPY_METHOD_NAME, and returns an object (copy) of the same type.
     * Otherwise, the reflection calls will generate an error.
     *
     * @param dat Object Object being copied.
     * @param meth String Name of copy method.
     * @return Object Copy of object.
     * @throws Exception Reflection exception.
     */
    Object copyViaCustomMethod(Object dat, String meth) throws Exception {
        Object obj = null;
        if (meth == null || meth.length() == 0) {
            throw new RuntimeException(
                    "No custom method name provided.");
        }
        Class cls = dat.getClass();
        obj = dat.getClass().getMethod(meth, (Class[])null).invoke(dat, (Object[])null);
        if (!(cls.isInstance(obj))) {
            throw new RuntimeException("Copy produced is not an " +
                                       "instance of the input object.");
        }
        return obj;
    }

}
