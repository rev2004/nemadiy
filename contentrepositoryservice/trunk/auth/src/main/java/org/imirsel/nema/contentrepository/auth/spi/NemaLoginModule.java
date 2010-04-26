package org.imirsel.nema.contentrepository.auth.spi;

import java.security.Principal;
import java.security.acl.Group;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.jcr.Credentials;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.apache.jackrabbit.core.security.authentication.AbstractLoginModule;
import org.apache.jackrabbit.core.security.authentication.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides authentication 
 * 
 * @author kumaramit01
 * @since 0.0.1
 * 
 */
public class NemaLoginModule extends AbstractLoginModule {

	private String jdbcUrl;
	private String jdbcUsername;
	private String jdbcPassword;
	private String userAuthenticationSql;
	private String passwordEncoderClass;
	boolean debug = false;
	private PasswordEncoder passwordEncoder;
	private PreparedStatement preparedStatement;
	private String driver;

	private static Logger log = LoggerFactory.getLogger(NemaLoginModule.class);

	@Override
	protected void doInit(CallbackHandler callbackHandler, Session session,
			Map options) throws LoginException {
		this.callbackHandler = callbackHandler;
		jdbcUrl = (String) options.get("jdbcUrl");
		jdbcUsername = (String) options.get("username");
		jdbcPassword = (String) options.get("password");
		driver = (String) options.get("driver");
		userAuthenticationSql = (String) options.get("userAuthenticationSql");
		passwordEncoderClass = (String) options.get("passwordEncoderClass");
		if(passwordEncoderClass==null){
			passwordEncoderClass="org.imirsel.nema.contentrepository.auth.spi.SHAEncoder";
		}
		Class<?> class1 =null;
		try {
			class1= Class.forName(passwordEncoderClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new LoginException("Error finding the password encoder class: "+ passwordEncoderClass);
		}
		
		if(class1==null){
			log.error("Error finding the password encoder class: "+ passwordEncoderClass);
			throw new LoginException("Error finding the password encoder class: "+ passwordEncoderClass);
		}
	
		
		try {
			passwordEncoder = (PasswordEncoder)class1.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new LoginException(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new LoginException(e.getMessage());
		} 
		debug = "true".equalsIgnoreCase((String) options.get("debug"));
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new LoginException(e.getMessage());
		}
		
	 
	}

	@Override
	protected Authentication getAuthentication(final Principal principal,
			Credentials creds) throws RepositoryException {
		if (principal instanceof Group) {
			return null;
		}
		return new Authentication() {
			public boolean canHandle(Credentials credentials) {
				return true;
			}

			public boolean authenticate(Credentials credentials)
			throws RepositoryException {
				if(credentials instanceof SimpleCredentials){
					SimpleCredentials sc = (SimpleCredentials) credentials;
					String userId=sc.getUserID();
					String password = new String(sc.getPassword());
					String encodedPassword = passwordEncoder.encodePassword(password);
					if(log.isDebugEnabled()){
						log.debug("Do query to check the password: " + password + "  username: " + userId);
					}
					Connection con = null;
					ResultSet rs=null;
					try {
						con=DriverManager.getConnection(jdbcUrl, jdbcUsername,jdbcPassword);
						preparedStatement = con.prepareStatement(userAuthenticationSql);
						preparedStatement.setString(1,userId);
						preparedStatement.setString(2,encodedPassword);
						rs=preparedStatement.executeQuery();
						boolean success = rs.first();
						log.info("login username: "+userId+" success: " + success);
						return success;
					} catch (SQLException e) {
						e.printStackTrace();
					}finally{
						try {
							if(rs!=null){
								rs.close();
							}
							preparedStatement.close();
							con.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					return false;
				}else{
					log.info("looking for valid SimpleCredentials -found  "+ credentials.getClass().getName());
					return false;
				}
				
			}
		};
	}

	@Override
	protected Principal getPrincipal(Credentials credentials) {
		String userId = getUserID(credentials);
		Principal principal = principalProvider.getPrincipal(userId);
		if (principal == null || principal instanceof Group) {
			// no matching user principal
			return null;
		} else {
			return principal;
		}
	}

	@Override
	protected boolean impersonate(Principal principal, Credentials credentials)
	throws RepositoryException, LoginException {
		if (principal instanceof Group) {
			return false;
		}
		Subject impersSubject = getImpersonatorSubject(credentials);
		return impersSubject != null;

	}

}
