package edu.illinois.gslis.imirsel.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.illinois.gslis.imirsel.DefaultPreferenceInitializer;
import edu.illinois.gslis.imirsel.dao.PreferenceValueDao;
import edu.illinois.gslis.imirsel.model.PreferenceValue;
import edu.illinois.gslis.imirsel.service.PreferenceValueManager;

public class PreferenceValueManagerImpl extends UniversalManagerImpl implements PreferenceValueManager {

	private PreferenceValueDao preferenceValueDao;
	
	public List<PreferenceValue> getDefaultPreferenceValues() {
		Map<String,String> preferences=DefaultPreferenceInitializer.getDefaultPreferences();
		List<PreferenceValue> list = new ArrayList<PreferenceValue>();
		System.out.println("Number of default preferences size -2: " + preferences.size());
		for(String key:preferences.keySet()){
			String value = preferences.get(key);
			System.out.println("here adding the preferences: " +key + "  " + value);
			list.add(new PreferenceValue(key,value));	
		}
		return list;
	}

	public void removePreferenceValue(PreferenceValue preferenceValue) {
		preferenceValueDao.remove(preferenceValue);
	}

	public PreferenceValue savePreferenceValue(PreferenceValue preferenceValue) {
		preferenceValueDao.save(preferenceValue);
		return preferenceValue;
	}

	public PreferenceValueDao getPreferenceValueDao() {
		return preferenceValueDao;
	}

	public void setPreferenceValueDao(PreferenceValueDao preferenceValueDao) {
		this.preferenceValueDao = preferenceValueDao;
	}

	

}
