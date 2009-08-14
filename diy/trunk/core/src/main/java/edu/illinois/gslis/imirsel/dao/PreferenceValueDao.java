package edu.illinois.gslis.imirsel.dao;

import java.util.List;

import edu.illinois.gslis.imirsel.model.PreferenceValue;

public interface PreferenceValueDao extends GenericDao<PreferenceValue, Long> {
	
	
	public void savePreferenceValue(PreferenceValue preferenceValue);
	
	
	public List<PreferenceValue> getPreferenceValues();


	public PreferenceValue getPreferenceValue(String key);


	public void removePreferenceWithKey(String key);


	public void remove(PreferenceValue preferenceValue);

}
