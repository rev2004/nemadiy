package org.imirsel.nema.dao.hibernate;

import java.util.List;

import org.imirsel.nema.dao.PreferenceValueDao;
import org.imirsel.nema.model.PreferenceValue;




public class PreferenceValueDaoHibernate extends GenericDaoHibernate<PreferenceValue, Long> implements PreferenceValueDao {
		
	public PreferenceValueDaoHibernate(){
		super(PreferenceValue.class);
	}

	public List<PreferenceValue> getPreferenceValues() {
		return this.getAllDistinct();
	}

	public void savePreferenceValue(PreferenceValue preferenceValue) {
		save(preferenceValue);
	}

	public PreferenceValue getPreferenceValue(String key) {
		  List preferenceValueList = getHibernateTemplate().find("from PreferenceValue where key=?", key);
	        if (preferenceValueList.isEmpty()) {
	            return null;
	        } else {
	            return (PreferenceValue) preferenceValueList.get(0);
	        }
	}
	
	public PreferenceValue getPreference(String key,String value) {
		  List preferenceValueList = getHibernateTemplate().find("from PreferenceValue where key=? and value=?", new String[]{key, value});
		    if (preferenceValueList.isEmpty()) {
	            return null;
	        } else {
	            return (PreferenceValue) preferenceValueList.get(0);
	        }
	}

	public void removePreferenceWithKey(String key) {
		    Object pvalue = getPreferenceValue(key);
	        getHibernateTemplate().delete(pvalue);
		
	}

	public void remove(PreferenceValue preferenceValue) {
		 Object pvalue = getPreference(preferenceValue.getKey(), preferenceValue.getValue());
	     getHibernateTemplate().delete(pvalue);
	}

	

}
