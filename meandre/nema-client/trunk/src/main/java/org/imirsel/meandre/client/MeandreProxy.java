/**
 * MeandreProxy creates and maintains a local cache of a remote 
 * Meandre Repository.
 */

package org.imirsel.meandre.client;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.meandre.core.repository.LocationBean;
import org.meandre.core.repository.QueryableRepository;
import org.meandre.core.repository.RepositoryImpl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


/**
 * This is copy of the MeandreProxy from the Meandre 1.4.8 
 * This code at SEASR is not maintained thus has been
 * moved into the nema-client package
 * @maintainer -Amit Kumar
 * @date November 24th 2009
 *
 */
public class MeandreProxy extends MeandreClient {


	/** The logger we'll write to */
	protected Logger log = Logger.getLogger(MeandreProxy.class.getName());

	/** the meandre client to handle the calls to the server */
	private MeandreClient client;

	/** The user name */
	private String userName;

	/** The password */
	private String password;

	/** The base url of the remote server */
	private String baseUrl;

	/** The credentials */
	@SuppressWarnings("unused")
	private String encoding;

	/** Cached roles */
	private Set<String> mapRoles;

	/** Cached repository */
	private QueryableRepository qrCached;

	/** Is the proxy ready? */
	private boolean ready;

	/** Did the last call succeed */
	private boolean ok;

	/**Server version string*/
	private String serverVersion;

	/** Creates an empty Meandre Proxy 
	 */
	private MeandreProxy () {
		super("",0);
		ready = ok = false;
		qrCached = new RepositoryImpl(ModelFactory.createDefaultModel());
	}

	/** Creates a Meandre Proxy and contacts the server to initialize
	 * the cache.
	 * 
	 * @param sUser The user of the proxy
	 * @param sPasswd The password of the proxy
	 * @param sServerHost The Meandre server 
	 * @param iServerPort The Meandre server port
	 */
	public MeandreProxy ( String sUser, String sPasswd, String sServerHost,
			int iServerPort ) {
		super(sServerHost,iServerPort);
		update(sUser,sPasswd,sServerHost,iServerPort);
	}


	/**
	 * @return the username
	 */
	public String getUserName() {
		return this.userName;
	}

	/**Set the username
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**Return the password
	 * 
	 * @return password as string
	 */
	public String getPassword() {
		return password;
	}
	/**Set the password
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 
	 * @return BaseURL as string
	 */
	public String getBaseURL() {
		return this.baseUrl;
	}

	/**Set base url
	 * 
	 * @param baseURL
	 */
	public void setBaseURL(String baseURL) {
		this.baseUrl = baseURL;
	}

	/** Returns true if the proxy was successfully initialized; false otherwise.
	 *  
	 * @return True is successfully initialized
	 */
	public boolean isReady() {
		return ready;
	}

	/** Returns true if the last call was completed successfully.
	 * 
	 * @return True if everything when well. False otherwise
	 */
	public boolean getCallOk () {
		return ok;
	}

	/** Gets the user name.
	 * 
	 * @return The user name
	 */
	public String getName () {
		return userName;
	}    

	/**
	 * sets the logger for warning and error messages. some log messages will
	 * still go to standard out.
	 */
	public void setLogger(Logger newLogger){
		log = newLogger;
	}

	public Logger getLogger(){
		return log;
	}

	/**Call this function when you want to reuse the function
	 *
	 * @param sUser The user of the proxy
	 * @param sPasswd The password of the proxy
	 * @param sServerHost The Meandre server 
	 * @param iServerPort The Meandre server port
	 */
	public void update ( String sUser, String sPasswd, String sServerHost,
			int iServerPort ) {
		this.userName = sUser;
		this.password = sPasswd;

		String hostWithProtocol = "http://"+sServerHost;
		this.baseUrl  = hostWithProtocol +":"+iServerPort +"/";

		this.client = new MeandreClient(sServerHost, iServerPort);
		client.setCredentials(sUser, sPasswd);

		String sUserPassword = userName + ":" + password;
		this.encoding = new String(Base64.encodeBase64(sUserPassword.getBytes()));

		// Force a first authetication for role caching
		this.ready = null!=getRoles();
		// Force the repository caching
		this.qrCached = getRepository();
	}



	/** Flushes the cached roles.
	 * 
	 */
	public void flushRoles () {
		mapRoles = null;
	}



	/** Flushes the cached repository.
	 * 
	 */
	public void flushRepository () {
		qrCached = null;
	}

	/** Return the roles for the user of this proxy.
	 * 
	 * @return The set of granted role for the proxy user
	 */
	public Set<String> getRoles() {
		if ( mapRoles==null ) {
			try{
				//Set<String> roles = this.client.retrieveUserRoles();
				this.mapRoles=this.client.retrieveUserRoles();
				ok = true;
			}catch(TransmissionException e){
				ok = false;
				log("Couldn't retrieve roles: " + e.toString());
			}           
		}
		return mapRoles;
	}

	/** Gets the current cached repository.
	 * 
	 * @return The cached queryable repository 
	 */
	public QueryableRepository getRepository () {
		if ( this.qrCached==null ) {
			try{
				this.qrCached = this.client.retrieveRepository();
				ok = true;
			}catch(TransmissionException e){
				ok = false;
				log("Couldn't retrieve Repository: " +e.toString());
			}

		}
		return this.qrCached;
	}



	/** Retrieves the public repository from the server (no cacheing).
	 * 
	 * @return The public queryable repository 
	 */
	public QueryableRepository getPublicRepository () {
		// The public repository
		try{
			QueryableRepository qr = this.client.retrieveRepository();
			ok = true;
			return qr;
		}catch(TransmissionException e){
			ok = false;
			log("Couldn't retrieve Public Repository: " + e.toString());
		}
		return null;
	}


	/** Forces the repository to be recached before returning it.
	 * 
	 * @return The recached repository
	 */
	public QueryableRepository getRepositoryFlush () {
		this.qrCached = null;
		return getRepository();
	}

	/** Return the list of locations for the user of this proxy.
	 * 
	 * @return The array of location for this user
	 */

	public LocationBean[] getLocations() {
		ok = true;
		Set<LocationBean> loca = null;
		try{
			loca = this.client.retrieveLocations();
			ok = true;
		}catch(TransmissionException e){
			ok = false;
			log("Couldn't retrieve locations: " + e.toString());
		}
		LocationBean[] locArray = new LocationBean[loca.size()];
		return loca.toArray(locArray);
	}

	/** regenerates the  remote user repository and updates the local cache.
	 * 
	 * @return The result of the process. true if succesfull
	 */
	public boolean getRegenerate () {
		boolean localWasCallOK = true;

		try{
			localWasCallOK = this.client.regenerate();
			ok = true;
		}catch(TransmissionException e){
			localWasCallOK = false;		
			log("Proxy couldn't regenerate repository:") ;
		}
		getRepositoryFlush();

		//set was call ok to true only if the local regenerate and the
		//repository flush both succeeded.
		ok = localWasCallOK && ok;
		return ok;
	}

	/** Gets the result of attempting to add a new location to the user repository.
	 * 
	 * @param sLocation The URL location
	 * @param sDescription The location description
	 * @return The result of the process. True if it was succesful
	 */
	public boolean getAddLocation (String sLocation, String sDescription ) {
		ok = true;
		try{
			if ( mapRoles!=null ) {
				ok = this.client.addLocation(sLocation, sDescription);
				ok = true;
			}
		}catch(Exception e){
			ok = false;
			log("Proxy couldn't add location:" + e.toString());
		}
		return ok;
	}


	/** Gets the result of attempting to remove a location from the user repository.
	 * 
	 * @param sLocation The URL location
	 * @return true if the removal was successful
	 */
	public boolean getRemoveLocation (String sLocation ) {
		ok = true;
		try{
			if ( mapRoles!=null ) {
				ok = this.client.removeLocation(sLocation);
				ok = true;
			}
		}catch(Exception e){
			ok = false;
			log("Proxy couldn't remove location:" + e.toString());
		}
		return ok;
	}

	/** publishes a component or flow (identified by it's uri) at the remote
	 * server.
	 * 
	 * @param sURI The resource URI to publish
	 * @return The result of the process. Returns true if successful
	 **/
	public boolean getPublish (String sURI ) {
		if ( mapRoles!=null ) {
			try {
				ok = this.client.publish(sURI);
				ok = true;
			} catch (TransmissionException e) {
				ok = false;
				log.warning("Proxy couldn't perform publish: " + e);
			}
		}
		return ok;
	}

	/** unpublishes a component or flow (identified by it's uri) at the remote
	 * server.
	 *
	 * returns true no matter what as long as the server received and understood
	 * the request.
	 * 
	 * @param sURI The resource URI to publish
	 * @return The result of the process. Returns true if successful
	 **/
	public boolean getUnpublish (String sURI ) {
		if (mapRoles!=null) {
			try {
				ok = this.client.unpublish(sURI);
				ok = true;
			} catch (TransmissionException e) {
				ok = false;
				log.warning("Proxy couldn't perform unpublish: " + e);
			}
		}
		return ok;
	}



	/** Gets the result of attempting to remove a component or flow, identified
	 * by it's, URI from the user repository.
	 * 
	 * @param sURI The resource URI to remove
	 * @return  true if successful
	 */
	public boolean getRemove (String sURI ) {
		if ( mapRoles!=null ) {
			try {
				ok = this.client.removeResource(sURI);
				ok = true;
			} catch (TransmissionException e) {
				ok = false;
				log.warning("Proxy couldn't perform remove: " + e);
			}
			if(ok){
				flushRepository();
			}
		}
		return ok;
	}

	/**
	 * returns the url name of any running flows and the urls assigned to
	 * the webui component of the flow.
	 *
	 * @return a map where the keys are flow id urls, and the values are webui
	 * urls
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/services/execute/list_running_flows.json
	 *TODO: need to reverse the order in the map so that the always unique
	 * webui_url is the key and the not-always-unique flow intance url is
	 * the value. requires a server side change.
	 * FIXME: This is totally untested.
	 */
	public Set<Map<String,URI>> getRunningFlowsInformation() {
		try {
			Set<Map<String,URI>> setRes = new HashSet<Map<String,URI>>(10);
			Map<URI,Map<String,URI>> mapTmp = this.client.retrieveRunningFlowsInformation();
			for ( URI uri:mapTmp.keySet() )
				setRes.add(mapTmp.get(uri));
			ok = true;
			return setRes;
		} catch (TransmissionException e) {
			ok = false;
			return new HashSet<Map<String,URI>>();
		}
	}


	/**
	 * returns the job statuses.
	 *
	 * @return a vector of maps where the keys are status information keys.
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/services/jobs/list_jobs_statuses.json
	 *TODO: need to reverse the order in the map so that the always unique
	 * webui_url is the key and the not-always-unique flow intance url is
	 * the value. requires a server side change.
	 * FIXME: This is totally untested.
	 */
	public Vector<Map<String,String>> getJobStatuses() {
		try {
			Vector<Map<String, String>> js = this.client.retrieveJobStatuses();
			ok = true;
			return js;
		}
		catch ( Exception e ) {
			ok = false;
			return new Vector<Map<String,String>>();
		}
	}

	/**
	 * returns the job console.
	 *
	 * @param sFUID The flow ID
	 * @return a string with the console for the given string.
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/services/jobs/job_console.json
	 *TODO: need to reverse the order in the map so that the always unique
	 * webui_url is the key and the not-always-unique flow intance url is
	 * the value. requires a server side change.
	 * FIXME: This is totally untested.
	 */
	public String getJobConsole(String sFUID) {
		try {
			String jc = this.client.retrieveJobConsole(sFUID);
			ok = true;
			return jc;
		}
		catch ( Exception e ) {
			ok = false;
			return "Console not available";
		}
	}

	/** Runs the requested model on the server
	 * 
	 * @param mod The model to run
	 * @return The output
	 */
	public String runRepository (Model mod) {
		try {
			return client.runRepository(mod);
		} catch (TransmissionException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			e.printStackTrace(ps);
			return "Failed to run the requested repository!!!\n"+baos.toString();
		}
	}


	/**
	 * handle generic logging messages for this proxy's default logging level
	 * @param msg
	 */
	private void log(String msg){
		System.out.println(msg);
	}


	/** Gets the RDF component description
	 *
	 * @param componentUri The component URI
	 * @return The string containing the RDF
	 */
	public String getComponentDescriptor(String componentUri) {
		String descriptor=null;
		try {
			descriptor = this.client.retrieveComponentDescriptorAsString(componentUri);
		} catch (TransmissionException e) {
			log.severe(e.getMessage());
		}
		return descriptor;
	}


	/** Gets the server version.
	 * 
	 * @return The server version
	 * @throws TransmissionException Could not get the server version
	 */
	public String getComponentJarInfo(String jarFile) {
		String jarInfo=null;
		try {
			jarInfo=this.client.getComponentJarInfo(jarFile);
		} catch (TransmissionException e) {
			log.severe(e.getMessage());
		}
		return jarInfo;
	}

	/** Pings the server
	 *
	 *	@return True if it successfully pinged the server
	 */
	public boolean ping() {
		try {
			return this.client.ping();
		} catch (TransmissionException e) {
			log.severe(e.getMessage());
		}
		return false;
	}

	/** Gets the server version.
	 * 
	 * @return The server version
	 * @throws TransmissionException Could not get the server version
	 */
	public String getServerVersion() {
		String versionString = null;
		int status= 500;
		try {
			versionString = this.client.getServerVersion();
		} catch (TransmissionException e) {
			log.severe(e.getMessage());
		}
		if ( versionString==null || status == 404){
			ok = false;	
			this.serverVersion = "N/A";
		}else{
			int i=versionString.indexOf("=");
			if(i==-1){
				log.warning("Error could not get the server version");
				this.serverVersion ="N/A";
				return this.serverVersion;
			}
			this.serverVersion = versionString.substring(i+1);
		}
		return this.serverVersion;
	}

	/** Return the JSON content describing the plugins available.
	 * 
	 * @return The JSON string
	 * @throws TransmissionException Fail to retrieve the plugins' information
	 */
	public String getServerPluginsAsJSON() {
		String pluginString = null;
		try {
			pluginString = this.client.getServerPlugins();
		} catch (TransmissionException e) {
			log.fine(e.getMessage());
		}
		return pluginString;
	}

	/**Cleanup method. Destroys the http client.
	 * 
	 */
	public void detroy(){
		log.info("destroy MeandreProxy -end of session");
		this.client.destroy();
	}

}
