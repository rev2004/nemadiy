package edu.illinois.gslis.imirsel.model;

import java.io.Serializable;
import java.util.HashMap;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="user_prefs")
public class UserPreference extends BaseObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String value;
	public UserPreference() {}
	
	
	// list of key value pairs
	private HashMap<String,String> preferences = new HashMap<String,String>();
	
	

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

	
	
	@Override
	public boolean equals(Object o) {
	/*     if (this == o) {
	            return true;
	        }
	        if (!(o instanceof UserPreference)) {
	            return false;
	        }

	        final UserPreference prefs = (UserPreference) o;

	        return !(prefs != null ? !prefs.equals(prefs.getName()) : prefs.getName() != null);
	*/
		return true;
	}

	@Override
	public int hashCode() {
		return (name != null ? name.hashCode() : 0);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
