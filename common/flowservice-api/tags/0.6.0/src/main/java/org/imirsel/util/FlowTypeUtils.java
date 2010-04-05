package org.imirsel.util;

import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Flow.FlowType;

/**List of selected job types
 * 
 * @author kumaramit01
 * @since 0.4.0
 * @deprecated
 *
 */
@Deprecated
public class FlowTypeUtils {
	
	public static FlowType DEFAULT_FLOW=Flow.FlowType.INHERITS;
	
	public static String DEFAULT_TYPE ="Inherits";
	public static String TYPE_1 = "Feature Extraction";
	public static String TYPE_2 = "Classification";
	public static String TYPE_3 = "Evaluation";
	public static String TYPE_4 = "Analysis";
}
