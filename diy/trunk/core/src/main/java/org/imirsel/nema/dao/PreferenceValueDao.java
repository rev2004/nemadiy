package org.imirsel.nema.dao;

import java.util.List;

import org.imirsel.nema.model.PreferenceValue;


public interface PreferenceValueDao extends GenericDao<PreferenceValue, Long> {
	
	
	public void savePreferenceValue(PreferenceValue preferenceValue);
	
	
	public List<PreferenceValue> getPreferenceValues();


	public PreferenceValue getPreferenceValue(String key);


	public void removePreferenceWithKey(String key);


	public void remove(PreferenceValue preferenceValue);

}
