package org.imirsel.nema;

import java.util.HashMap;
import java.util.Map;

public class DefaultPreferenceInitializer {
	
	private static Map<String,String> defaultPreferenceMap = new HashMap<String,String>();
	static {
		defaultPreferenceMap.put("emailPrefStartFlow", "true");
		defaultPreferenceMap.put("emailPrefEndFlow", "true");
		defaultPreferenceMap.put("emailPrefAbortFlow", "true");
		defaultPreferenceMap.put("emailPrefFlowResult", "true");
	}
	
	public static Map<String,String> getDefaultPreferences(){
		return defaultPreferenceMap;
	}

}
