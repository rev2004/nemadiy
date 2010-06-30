package org.imirsel.nema.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.DefaultPreferenceInitializer;
import org.imirsel.nema.dao.PreferenceValueDao;
import org.imirsel.nema.model.PreferenceValue;
import org.imirsel.nema.service.PreferenceValueManager;


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
