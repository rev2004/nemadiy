package org.imirsel.nema.model;

public class NemaContributor {
	private String title;
	private String firstName;
	private String lastName;
	private String affiliationUrl;
	private String affiliation;
	private String affiliationDept;
	private String affiliationDeptUnit;
	private static String delim = ";";
	
	public NemaContributor(String title, String firstName, String lastName,
			String affiliationUrl, String affiliation, String affiliationDept,
			String affiliationDeptUnit) {
		super();
		this.title = title;
		this.firstName = firstName;
		this.lastName = lastName;
		this.affiliationUrl = affiliationUrl;
		this.affiliation = affiliation;
		this.affiliationDept = affiliationDept;
		this.affiliationDeptUnit = affiliationDeptUnit;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setAffiliationUrl(String affiliationUrl) {
		this.affiliationUrl = affiliationUrl;
	}
	public String getAffiliationUrl() {
		return affiliationUrl;
	}
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
	public String getAffiliation() {
		return affiliation;
	}
	public void setAffiliationDept(String affiliationDept) {
		this.affiliationDept = affiliationDept;
	}
	public String getAffiliationDept() {
		return affiliationDept;
	}
	public void setAffiliationDeptUnit(String affiliationDeptUnit) {
		this.affiliationDeptUnit = affiliationDeptUnit;
	}
	public String getAffiliationDeptUnit() {
		return affiliationDeptUnit;
	}
	
	public String toString() {
		String theString;		
		theString = title + delim + firstName + delim + lastName + delim 
		+ affiliationUrl + delim + affiliation + delim + affiliationDept
		+ delim + affiliationDeptUnit; 
		return theString;
	}
	
	public static NemaContributor fromString(String theString) {
		String[] stringComps = theString.split(delim);
		String title = null;
		String firstName = null;
		String lastName = null;
		String affiliationUrl = null;
		String affiliation = null;
		String affiliationDept = null;		
		String affiliationDeptUnit = null;
		if (stringComps.length == 1){
			title = stringComps[0];
		} else if (stringComps.length == 2){
			title = stringComps[0];
			firstName = stringComps[1];
		} else if (stringComps.length == 3){
			title = stringComps[0];
			firstName = stringComps[1];
			lastName = stringComps[2]; 
		} else if (stringComps.length == 4){
			title = stringComps[0];
			firstName = stringComps[1];
			lastName = stringComps[2];
			affiliationUrl = stringComps[3];
		} else if (stringComps.length == 5){
			title = stringComps[0];
			firstName = stringComps[1];
			lastName = stringComps[2];
			affiliationUrl = stringComps[3];
			affiliation = stringComps[4]; 
		} else if (stringComps.length == 6){
			title = stringComps[0];
			firstName = stringComps[1];
			lastName = stringComps[2];
			affiliationUrl = stringComps[3];
			affiliation = stringComps[4];
			affiliationDept = stringComps[5];
		} else {
			title = stringComps[0];
			firstName = stringComps[1];
			lastName = stringComps[2];
			affiliationUrl = stringComps[3];
			affiliation = stringComps[4];
			affiliationDept = stringComps[5];
			affiliationDeptUnit = stringComps[6];
		}

		return new NemaContributor(title, firstName, lastName,
				affiliationUrl, affiliation, affiliationDept,
				affiliationDeptUnit);
	}
}
