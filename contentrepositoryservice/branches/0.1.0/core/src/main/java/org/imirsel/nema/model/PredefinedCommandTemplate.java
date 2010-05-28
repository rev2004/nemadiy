package org.imirsel.nema.model;

/**This is a marker interface which allows us to create some predefined templates
 * for C, JAVA, MATLAB and SHELL code. The user supplies parameters that can change
 * the command parameters. For example a user might want to send min and max 
 * memory for java process. These parameters are different from other command line template
 * parameters which are the arguments to the executable. The command parameters are not something
 * that we want user to set arbitrarily. For example we will let them select from drop down
 * a max memory for JVM but not allow them to set security policy or location of temp folder
 * etc. The executor service will be responsible for setting up these restricted properties.
 * 
 * @author kumaramit01
 * @since 0.2.0
 */
public interface PredefinedCommandTemplate {

}
