package org.imirsel.nema.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="preference_value")
public class PreferenceValue extends BaseObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String key;
	private String value;
	
	
	public PreferenceValue(){}

	

	public PreferenceValue(String key2, String value2) {
		this.key = key2;
		this.value=value2;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name="value",nullable=false,length=100,unique=false)
	public String getValue() {
		return value;
	}

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="mkey", nullable=false,length=100,unique=false)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
            return true;
        }
		if(!(o instanceof PreferenceValue)) return false; 

        final PreferenceValue pref = (PreferenceValue) o;
        if(value==null || key==null || pref.value ==null || pref.key==null){
        	return false;
        }
        
        if(key.equals(pref.key) && value.equals(pref.value)){
        	return true;
        }
        	return false;
     }

	@Override
	public int hashCode() {
		int hash=1;
		hash = hash * 31 + value.hashCode();
		hash = hash * 31 + key.hashCode();
		return hash;
	}

	@Override
	public String toString() {
		return "("+id+") "+key+"="+value;
	}
}
