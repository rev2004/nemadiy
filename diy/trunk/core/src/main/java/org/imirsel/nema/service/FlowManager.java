package org.imirsel.nema.service;

import java.util.List;

import org.imirsel.nema.dao.PreferenceValueDao;
import org.imirsel.nema.model.PreferenceValue;


public interface FlowManager extends UniversalManager {

	List<PreferenceValue>  getDefaultPreferenceValues();
	
	PreferenceValue savePreferenceValue(PreferenceValue  preferenceValue);
	
	void removePreferenceValue(PreferenceValue  preferenceValue);
	
	public void setPreferenceValueDao(PreferenceValueDao preferenceValueDao);
	public PreferenceValueDao  getPreferenceValueDao();
	


}
