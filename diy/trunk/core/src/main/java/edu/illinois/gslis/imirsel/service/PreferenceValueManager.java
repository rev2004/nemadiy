package edu.illinois.gslis.imirsel.service;

import java.util.List;

import edu.illinois.gslis.imirsel.dao.PreferenceValueDao;
import edu.illinois.gslis.imirsel.model.PreferenceValue;

public interface PreferenceValueManager extends UniversalManager {

	// return the default preference values
	List<PreferenceValue>  getDefaultPreferenceValues();
	
	PreferenceValue savePreferenceValue(PreferenceValue  preferenceValue);
	
	void removePreferenceValue(PreferenceValue  preferenceValue);
	
	public void setPreferenceValueDao(PreferenceValueDao preferenceValueDao);
	public PreferenceValueDao  getPreferenceValueDao();
	


}
