/**
 * 
 */
package org.imirsel.nema.webapp.mock;

import java.io.Serializable;
import java.util.List;

import javax.jcr.SimpleCredentials;

import org.imirsel.nema.dao.UserDao;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.UserExistsException;
import org.imirsel.nema.service.UserManager;
import org.springframework.security.userdetails.UsernameNotFoundException;

/**
 * @author gzhu1
 *
 */
public class MockUserManager implements UserManager {

	SimpleCredentials credentials;
	User currentUser;
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.service.UserManager#getCurrentUser()
	 */
	@Override
	public User getCurrentUser() {
		
		return currentUser;
	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.service.UserManager#getCurrentUserCredentials()
	 */
	@Override
	public SimpleCredentials getCurrentUserCredentials() {
		
		return credentials;
	}
	
	public void setCurrentUserCredentials(SimpleCredentials credentials){
		this.credentials=credentials;
	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.service.UserManager#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String userId) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.service.UserManager#getUserByUsername(java.lang.String)
	 */
	@Override
	public User getUserByUsername(String username)
			throws UsernameNotFoundException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.service.UserManager#getUsers(org.imirsel.nema.model.User)
	 */
	@Override
	public List getUsers(User user) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.service.UserManager#removeUser(java.lang.String)
	 */
	@Override
	public void removeUser(String userId) {
		

	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.service.UserManager#saveUser(org.imirsel.nema.model.User)
	 */
	@Override
	public User saveUser(User user) throws UserExistsException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.service.UserManager#setUserDao(org.imirsel.nema.dao.UserDao)
	 */
	@Override
	public void setUserDao(UserDao userDao) {
		

	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.service.UniversalManager#get(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public Object get(Class clazz, Serializable id) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.service.UniversalManager#getAll(java.lang.Class)
	 */
	@Override
	public List getAll(Class clazz) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.service.UniversalManager#remove(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public void remove(Class clazz, Serializable id) {
		

	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.service.UniversalManager#save(java.lang.Object)
	 */
	@Override
	public Object save(Object o) {
		
		return null;
	}

}
