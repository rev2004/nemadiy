package org.imirsel.nema.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class represents the basic "user" object in AppFuse that allows for authentication
 * and user management.  It implements Acegi Security's UserDetails interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *         Updated by Dan Kibler (dan@getrolling.com)
 *         Extended to implement Acegi UserDetails interface
 *         by David Carter david@carter.net
 */
@Entity
@Table(name="app_user")
public class User extends BaseObject implements Serializable, UserDetails {
    private static final long serialVersionUID = 3832626162173359411L;

    private Long id;
    private String username;                    // required
    private String password;                    // required
    private String confirmPassword;
    private String passwordHint;
    private String firstName;                   // required
    private String lastName;                    // required
    private String email;                       // required; unique
    private Integer version;
    private Set<Role> roles = new HashSet<Role>();
    private boolean enabled;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;
    private Set<PreferenceValue> preferences = new HashSet<PreferenceValue>();


    /**
     * Default constructor - creates a new instance with no values set.
     */
    public User() {}

    /**
     * Create a new instance and set the username.
     * @param username login name for user.
     */
    public User(final String username) {
        this.username = username;
    }

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Column(nullable=false,length=50,unique=true)
    public String getUsername() {
        return username;
    }

    @Column(nullable=false)
    public String getPassword() {
        return password;
    }

    @Transient
    public String getConfirmPassword() {
        return confirmPassword;
    }

    @Column(name="password_hint")
    public String getPasswordHint() {
        return passwordHint;
    }

    @Column(name="first_name",nullable=false,length=50)
    public String getFirstName() {
        return firstName;
    }

    @Column(name="last_name",nullable=false,length=50)
    public String getLastName() {
        return lastName;
    }

    @Column(nullable=false,unique=true)
    public String getEmail() {
        return email;
    }

  /*  @Column(name="phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWebsite() {
        return website;
    }
*/
    /**
     * Returns the full name.
     * @return firstName + ' ' + lastName
     */
    @Transient
    public String getFullName() {
        return firstName + ' ' + lastName;
    }

  /*  @Embedded
    public Address getAddress() {
        return address;
    }
*/
    @ManyToMany(fetch = FetchType.EAGER) 
    @JoinTable(
            name="user_role",
            joinColumns = { @JoinColumn( name="user_id") },
            inverseJoinColumns = @JoinColumn( name="role_id")
    )    
    public Set<Role> getRoles() {
        return roles;
    }
    
    

    @OneToMany(fetch = FetchType.EAGER,  cascade=CascadeType.ALL)
    @JoinTable(
            name="user_prefs",
            joinColumns = { @JoinColumn( name="user_id") }
            			 
            		
    )  
    public Set<PreferenceValue> getPreferences(){
    	return preferences;
    }
    
    @Transient
    public void addPreference(PreferenceValue pvalue) {
 		preferences.add(pvalue);
	}

    @Transient
	public void addPreference(String key, String value) {
    	PreferenceValue pvalue = new PreferenceValue(key,value);
		preferences.add(pvalue);
	}
    
    @Transient
    public String getPreference(String key){
    	for(PreferenceValue pv: preferences){
    		if(pv.getKey().equals(key)){
    			return pv.getValue();
    		}
    	}
    	return null;
    }
    
    @Transient
	public boolean updatePreference(String key, String value) {
    	boolean updated=Boolean.FALSE;
    	
    	for(PreferenceValue pv: preferences){
    		if(pv.getKey().equals(key)){
    			pv.setValue(value);
    			updated= Boolean.TRUE;
    			return updated;
    		}
    	}
    	
    	if(!updated){
    		preferences.add(new PreferenceValue(key,value));
    	}
    	return updated;
	}
 
    @Transient
    public boolean removePreference(String key){
    	Iterator<PreferenceValue> it =  preferences.iterator();
    	boolean success= Boolean.FALSE;
    	while(it.hasNext()){
    		PreferenceValue pval=it.next();
    		if(pval.getKey().equals(key)){
    			it.remove();
    			success = Boolean.TRUE;
    		}
    	}
    	return success;
    }


    @Transient
    public List<LabelValue> getPreferenceValueList(){
    	 List<LabelValue> list= new ArrayList<LabelValue>();
         if (this.roles != null) {
             for (PreferenceValue p : preferences) {
                 // convert the user's preference to LabelValue Objects
                list.add(new LabelValue(p.getKey(), p.getValue()));
             }
         }
         return list;
    }


    /**
     * Convert user roles to LabelValue objects for convenience.
     * @return a list of LabelValue objects with role information
     */
    @Transient
    public List<LabelValue> getRoleList() {
        List<LabelValue> userRoles = new ArrayList<LabelValue>();

        if (this.roles != null) {
            for (Role role : roles) {
                // convert the user's roles to LabelValue Objects
                userRoles.add(new LabelValue(role.getName(), role.getName()));
            }
        }

        return userRoles;
    }

    /**
     * Adds a role for the user
     * @param role the fully instantiated role
     */
    public void addRole(Role role) {
        getRoles().add(role);
    }
    
    /**
     * @see org.springframework.security.userdetails.UserDetails#getAuthorities()
     * @return Collection<GrantedAuthority>
     */
    @Transient
    public Collection<GrantedAuthority> getAuthorities() {
    	Collection<GrantedAuthority> grantedAuthorityCollection = new ArrayList<GrantedAuthority>();
    	for(Role role:roles){ grantedAuthorityCollection.add(role);}
    	return grantedAuthorityCollection;
    }

    @Version
    public Integer getVersion() {
        return version;
    }
  
    @Column(name="account_enabled")
    public boolean isEnabled() {
        return enabled;
    }
    
    @Column(name="account_expired",nullable=false)
    public boolean isAccountExpired() {
        return accountExpired;
    }
    
    /**
     * @see org.springframework.security.userdetails.UserDetails#isAccountNonExpired()
     */
    @Transient
    public boolean isAccountNonExpired() {
        return !isAccountExpired();
    }

    @Column(name="account_locked",nullable=false)
    public boolean isAccountLocked() {
        return accountLocked;
    }
    
    /**
     * @see org.springframework.security.userdetails.UserDetails#isAccountNonLocked()
     */
    @Transient
    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }

    @Column(name="credentials_expired",nullable=false)
    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }
    
    /**
     * @see org.springframework.security.userdetails.UserDetails#isCredentialsNonExpired()
     */
    @Transient
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
/*
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
*/
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    public void setPreferences(Set<PreferenceValue> pvalues){
    	this.preferences=pvalues;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }
    
    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }

        final User user = (User) o;

        return !(username != null ? !username.equals(user.getUsername()) : user.getUsername() != null);

    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (username != null ? username.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("username", this.username)
                .append("enabled", this.enabled)
                .append("accountExpired", this.accountExpired)
                .append("credentialsExpired", this.credentialsExpired)
                .append("accountLocked", this.accountLocked);

        Collection<GrantedAuthority> auths = this.getAuthorities();
        if (auths != null) {
            sb.append("Granted Authorities: ");
           int i=0; 
            for (GrantedAuthority auth:auths) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(auth.toString());
                i++;
            }
        } else {
            sb.append("No Granted Authorities");
        }
        return sb.toString();
    }

}
