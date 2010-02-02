package org.imirsel.meandre.client;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.meandre.core.repository.ExecutableComponentDescription;
import org.meandre.core.repository.FlowDescription;
import org.meandre.core.repository.LocationBean;
import org.meandre.core.repository.QueryableRepository;
import org.meandre.core.repository.RepositoryImpl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


/**
 * Programmatic interface to the Meandre server webservices API. Mimicks opening
 * a session with the server and allowing the client to interact with it,
 * although in reality the session has no state and MeandreClient simply sends an
 * independent http request for every operation.
 *
 * <p>The setCredentials() method must be called before authorized calls on
 * a MeandreClient can be invoked. All calls are authorized unless they say
 * specifically that they do not require authoriziation.
 *
 * @author pgroves
 *
 */
public class MeandreClient extends MeandreBaseClient{

	/**
	 * initialize to talk to a particular server. You need to call the
	 * "setCredentials" method in MeandreBaseClient before you can make
	 * authorized calls to the server.
	 *
	 *
	 * @param serverHost just the hostname, e.g. "localhost",
	 *      NOT "http://localhost"
	 * @param serversPort the port on the serverhost that the server is listening
	 */
	public MeandreClient(String serverHost, int serversPort){
		super(serverHost, serversPort);
	}



	/////////
	//About
	/////////

	/**
	 * requests all java properties of the server's Store.
	 *
	 *<p> calls:
	 * http://<meandre_host>:<meandre_port>/services/about/installation.rdf
	 *
	 * TODO: need a java object instance to represent installation properties
	 * @return JSONObject
	 * @throws TransmissionException 
	 */
	public JSONObject retrieveInstallationProperties()
	throws TransmissionException{
		String sRestCommand = "services/about/installation.json";
		try {
			JSONTokener jt = executeGetRequestJSON(sRestCommand, null);
			return (new JSONArray(jt)).getJSONObject(0);
		} catch (JSONException e) {
			throw new TransmissionException(e);
		}
	}

	/**
	 * requests a list of assigned roles of the user (defined by the
	 * credentials of this MeandreClient).
	 *
	 * @return a list of roles
	 *
	 *<p> calls:
	 * http://<meandre_host>:<meandre_port>/services/about/user_roles.json
	 * @throws TransmissionException
	 *
	 * TODO: Need java object to represent valid roles
	 */

	public Set<String> retrieveUserRoles() throws TransmissionException {
		try {
			String sRestCommand = "services/about/user_roles.json";
			JSONTokener jtRoles = executeGetRequestJSON(sRestCommand, null);
			//        String jArrayKey = "meandre_user_role";
			//        String jElementKey = "meandre_role";
			//        Set<String> ss = unpackJSONArray(jtRoles, jArrayKey, jElementKey);
			JSONArray ja = new JSONArray(jtRoles);
			Set<String> ss = new HashSet<String>();
			for ( int i=0, iMax=ja.length() ; i<iMax ; i++ )
				ss.add(ja.getJSONObject(i).getString("meandre_role_uri"));
			return ss;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
		}
	}

	/**
	 * requests the list of all valid roles the server supports. the roles
	 * are returned in their url form.
	 *
	 * this is equivalent to getValidRoles() in MeandreAdminClient, but
	 * this version requires only the 'about' role and not the 'admin'
	 * role to access it. Also, this returns the url representation of
	 * the roles, not Role objects.
	 *
	 * @return list of all valid roles
	 * @throws TransmissionException 
	 */
	public Set<String> retrieveValidRoles() throws TransmissionException{
		try {
			String sRestCommand = "services/about/valid_roles.json";
			JSONTokener jtRoles = executeGetRequestJSON(sRestCommand, null);
			//        String jArrayKey = "meandre_user_role";
			//        String jElementKey = "meandre_role";
			//        Set<String> ss = unpackJSONArray(jtRoles, jArrayKey, jElementKey);
			JSONArray ja = new JSONArray(jtRoles);
			Set<String> ss = new HashSet<String>();
			for ( int i=0, iMax=ja.length() ; i<iMax ; i++ )
				ss.add(ja.getJSONObject(i).getString("meandre_role_uri"));
			return ss;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
		}

	}


	/////////
	//Locations (known peers of the server)
	//////////

	/**
	 * requests the locations (urls) of all meandre repositories the server
	 * is aware of.
	 *
	 *<p> calls:
	 * http://<meandre_host>:<meandre_port>/services/locations/list.json
	 * @return Set<LocationBean>
	 * @throws TransmissionException 
	 */
	public Set<LocationBean> retrieveLocations() throws TransmissionException{
		try {
			String sRestCommand = "services/locations/list.json";
			JSONTokener jtLocs = executeGetRequestJSON(sRestCommand, null);
			//        String jArrayKey = "location_information";
			//        String jURLElementKey = "location";
			//        String jDescElementKey = "description";
			//        Map<String, String> smLocs = unpackJSONMap(jtLocs, jArrayKey, jURLElementKey,
			//                jDescElementKey);
			//        Iterator<String> locIter = smLocs.keySet().iterator();
			//        HashSet<LocationBean> beanSet = new HashSet<LocationBean>();
			//        while (locIter.hasNext()){
			//            String sLoc = locIter.next();
			//            String sDesc = smLocs.get(sLoc);
			//            beanSet.add(new LocationBean(sLoc, sDesc));
			//        }
			JSONArray ja = new JSONArray(jtLocs);
			HashSet<LocationBean> beanSet = new HashSet<LocationBean>();
			for ( int i=0, iMax=ja.length() ; i<iMax ; i++ ) {
				JSONObject jo = ja.getJSONObject(i);
				beanSet.add(new LocationBean(jo.getString("location"), jo.getString("description")));
			}

			return beanSet;
		}
		catch (Exception e) {
			throw new TransmissionException(e);
		}
	}

	/**
	 * Adds or updates the location of a meandre server peer. returns true
	 * if the location is registered with the server after the call (whether
	 * it was added or was already present).
	 *
	 *<p> calls:
	 * http://<meandre_host>:<meandre_port>/services/locations/add.json
	 *
	 * TODO: Handle possible bad_request errors in http response
	 * @param sLocationUrl 
	 * @param description 
	 * @return success
	 * @throws TransmissionException 
	 */
	public boolean addLocation(String sLocationUrl, String description)
	throws TransmissionException{
		String sRestCommand = "services/locations/add.json";

		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		nvps.add(new NameValuePair("location", sLocationUrl));
		nvps.add(new NameValuePair("description", description));

		JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, nvps);
		JSONArray ja;
		try {
			ja = new JSONArray(jtRetrieved);
		} catch (JSONException e) {
			ja = null;
		}
		if(ja == null){
			return false;
		}
		try{
			JSONObject joRetrieved = ja.getJSONObject(0);
			String loc = joRetrieved.getString("location");
			String descr = joRetrieved.getString("description");
			return ((loc.equals(sLocationUrl)) && (descr.equals(description)));
		}catch(JSONException je){
			return false;
		}
	}


	/**
	 * removes the input location from the server's list of peers. returns
	 * true if the location is not a peer after this method is called
	 * (regardless of whether this removed it or if it wasn't there in
	 * the first place).
	 *
	 *<p> calls:
	 * http://<meandre_host>:<meandre_port>/services/locations/remove.json
	 * @param sUrl 
	 * @return success
	 * @throws TransmissionException 
	 */
	public boolean removeLocation(String sUrl) throws TransmissionException{
		String sRestCommand = "services/locations/remove.json";

		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		nvps.add(new NameValuePair("location", sUrl));

		JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, nvps);
		try{
			JSONArray ja = new JSONArray(jtRetrieved);
			if( ja.length()!=1 ){
				return false;
			}
			String loc = ja.getJSONObject(0).getString("location");
			if ( loc!=null )
				return (loc.equals(sUrl));
			else
				return false;
		}catch(JSONException je){
			return false;
		}
	}


	////////////
	//Repository
	/////////////

	/**
	 * Locally recreates a Repository from the rdf model from the server.
	 * The contents of the repository are dependent on the user requesting
	 * it.
	 *
	 *<p> calls:
	 * http://<meandre_host>:<meandre_port/services/repository/dump.nt
	 * @return repository Jena's QueryableRepository
	 * @throws TransmissionException
	 */
	public QueryableRepository retrieveRepository() throws TransmissionException {
		String sRestCommand = "services/repository/dump.nt";
		Model model = executeGetRequestModel(sRestCommand, null);
		QueryableRepository qr = new RepositoryImpl(model);
		return qr;
	}


	/**
	 * Tells the server to rebuild it's repository by (re-)querying all
	 * of it's peers for information on available components and flows.
	 *
	 *<p> calls:
	 * http://<meandre_host>:<meandre_port>/services/repository/regenerate.json
	 * @return success
	 * @throws TransmissionException 
	 */
	public boolean regenerate() throws TransmissionException{
		try {
			String sRestCommand = "services/repository/regenerate.json";
			JSONTokener jt = executeGetRequestJSON(sRestCommand, null);
			JSONArray ja = new JSONArray(jt);
			String sSuccess = "Repository successfully regenerated";
			return (sSuccess.equals(ja.getJSONObject(0).getString("message")));
		}
		catch ( JSONException e ) {
			throw new TransmissionException(e);
		}
	}


	/**
	 * requests the urls of all components in the server repository.
	 *
	 *<p> calls:
	 * http://<meandre_host>:<meandre_port>/services/repository/list_components.json
	 * @return Set<URI> set of URI
	 * @throws TransmissionException
	 */
	public Set<URI> retrieveComponentUris() throws TransmissionException {
		try {
			String sRestCommand = "services/repository/list_components.json";
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, null);
			JSONArray ja = new JSONArray(jtRetrieved);
			Set<URI> setCompURIs = new HashSet<URI>();
			for ( int i=0, iMax=ja.length() ; i<iMax ; i++ )
				setCompURIs.add(new URI(ja.getJSONObject(i).getString("meandre_uri")));
			return setCompURIs;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
		}
	}

	/**
	 * requests the urls of all flows in the server repository.
	 *
	 *<p> calls:
	 * http://<meandre_host>:<meandre_port>/services/repository/list_flows.json
	 * @return Set<URI> set of uri
	 * @throws TransmissionException
	 */
	public Set<URI> retrieveFlowUris() throws TransmissionException {
		try {
			String sRestCommand = "services/repository/list_flows.json";
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, null);
			JSONArray ja = new JSONArray(jtRetrieved);
			Set<URI> setCompURIs = new HashSet<URI>();
			for ( int i=0, iMax=ja.length() ; i<iMax ; i++ )
				setCompURIs.add(new URI(ja.getJSONObject(i).getString("meandre_uri")));
			return setCompURIs;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
		}
	}


	/**
	 * requests all tags for any and all components and flows.
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/services/repository/tags.json
	 * @return Set<String>
	 * @throws TransmissionException
	 *
	 * TODO:return tag objects instead of strings
	 */
	public Set<String> retrieveAllTags() throws TransmissionException {
		try {
			String sRestCommand = "services/repository/tags.json";
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, null);
			JSONArray ja = new JSONArray(jtRetrieved);
			Set<String> setTags = new HashSet<String>();
			for ( int i=0,iMax=ja.length() ; i<iMax ; i++ )
				setTags.add(ja.getJSONObject(i).getString("meandre_tag"));
			return setTags;
		}
		catch ( Exception  e ) {
			throw new TransmissionException(e);
		}
	}

	/**
	 * requests all tags for all components.
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/services/repository/tags_components.json
	 * @return Set<String>
	 * @throws TransmissionException
	 * TODO:return tag objects instead of strings
	 */
	public Set<String> retrieveComponentTags() throws TransmissionException {
		try {
			String sRestCommand = "services/repository/tags_components.json";
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, null);
			JSONArray ja = new JSONArray(jtRetrieved);
			Set<String> setTags = new HashSet<String>();
			for ( int i=0,iMax=ja.length() ; i<iMax ; i++ )
				setTags.add(ja.getJSONObject(i).getString("meandre_tag"));
			return setTags;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
		}
	}

	/**
	 * requests all tags for all flows.
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/services/repository/tags_flows.json
	 * @return Set<String>
	 * @throws TransmissionException
	 * TODO:return tag objects instead of strings
	 */
	public Set<String> retrieveFlowTags() throws TransmissionException {
		try {
			String sRestCommand = "services/repository/tags_flows.json";
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, null);
			JSONArray ja = new JSONArray(jtRetrieved);
			Set<String> setTags = new HashSet<String>();
			for ( int i=0,iMax=ja.length() ; i<iMax ; i++ )
				setTags.add(ja.getJSONObject(i).getString("meandre_tag"));
			return setTags;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
		}
	}

	/**
	 * requests the urls of all components that have the input tag.
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/services/repository/components_by_tag.json
	 * @param sTag 
	 * @return Set<URI>
	 * @throws TransmissionException
	 * TODO:input a tag object instead of string
	 */
	public Set<URI> retrieveComponentsByTag(String sTag) throws TransmissionException {
		try {
			String sRestCommand = "services/repository/components_by_tag.json";

			Set<NameValuePair> nvps = new HashSet<NameValuePair>();
			nvps.add(new NameValuePair("tag", sTag));
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, nvps);
			JSONArray ja = new JSONArray(jtRetrieved);
			Set<URI> setURIs = new HashSet<URI>();
			for ( int i=0,iMax=ja.length() ; i<iMax ; i++ )
				setURIs.add(new URI(ja.getJSONObject(i).getString("meandre_uri")));
			return setURIs;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
		}
	}

	/**
	 * requests the urls of all flows that have the input tag.
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/services/repository/flows_by_tag.json
	 * @param sTag query tag
	 * @return  Set<URI>
	 * @throws TransmissionException
	 * TODO:input a tag object instead of string
	 */
	public Set<URI> retrieveFlowsByTag(String sTag) throws TransmissionException {
		try {
			String sRestCommand = "services/repository/flows_by_tag.json";

			Set<NameValuePair> nvps = new HashSet<NameValuePair>();
			nvps.add(new NameValuePair("tag", sTag));
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, nvps);
			JSONArray ja = new JSONArray(jtRetrieved);
			Set<URI> setURIs = new HashSet<URI>();
			for ( int i=0,iMax=ja.length() ; i<iMax ; i++ )
				setURIs.add(new URI(ja.getJSONObject(i).getString("meandre_uri")));
			return setURIs;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
		}
	}


	/**
	 * requests the component description model from the server.
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/services/repository/describe_component.nt
	 * @param sComponentUrl 
	 * @return ExecutableComponentDescription
	 * @throws TransmissionException
	 */
	public ExecutableComponentDescription retrieveComponentDescriptor(
			String sComponentUrl) throws TransmissionException {

		String sRestCommand = "services/repository/describe_component.nt";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		nvps.add(new NameValuePair("uri", sComponentUrl));
		Model compModel = executeGetRequestModel(sRestCommand, nvps);

		QueryableRepository repo = new RepositoryImpl(compModel);
		Set<ExecutableComponentDescription> repoComps =
			repo.getAvailableExecutableComponentDescriptions();
		Iterator<ExecutableComponentDescription> iter = repoComps.iterator();
		ExecutableComponentDescription comp = iter.next();

		if(iter.hasNext()){
			throw new TransmissionException("More than one Component " +
			"Description was returned by the server.");
		}

		return comp;
	}

	/**
	 * requests a flow description model from the server.
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/services/repository/describe_flow.nt
	 * @param sFlowUrl 
	 * @return FlowDescription
	 * @throws TransmissionException
	 */
	public FlowDescription retrieveFlowDescriptor(String sFlowUrl) throws TransmissionException {
		String sRestCommand = "services/repository/describe_flow.nt";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		nvps.add(new NameValuePair("uri", sFlowUrl));
		Model flowModel = executeGetRequestModel(sRestCommand, nvps);

		QueryableRepository repo = new RepositoryImpl(flowModel);
		Set<FlowDescription> repoFlows =
			repo.getAvailableFlowDescriptions();
		Iterator<FlowDescription> iter = repoFlows.iterator();
		FlowDescription flow = iter.next();

		if(iter.hasNext()){
			throw new TransmissionException("More than one Flow " +
			"Description was returned by the server.");
		}
		return flow;
	}


	/**
	 *TODO: need serious docs on this or a query object to input instead of
	 * a string.
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/services/repository/search_components.json
	 * @param sQuery 
	 * @return Set<URI>
	 * @throws TransmissionException
	 */
	public Set<URI> retrieveComponentUrlsByQuery(String sQuery)
	throws TransmissionException {
		try {
			String sRestCommand = "services/repository/search_components.json";
			Set<NameValuePair> nvps = new HashSet<NameValuePair>();
			nvps.add(new NameValuePair("q", sQuery));
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, null);
			JSONArray ja = new JSONArray(jtRetrieved);
			Set<URI> setCompURIs = new HashSet<URI>();
			for ( int i=0, iMax=ja.length() ; i<iMax ; i++ )
				setCompURIs.add(new URI(ja.getJSONObject(i).getString("meandre_uri")));
			return setCompURIs;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
		}

	}

	/**
	 * TODO: need serious docs on this or a query object to input instead of
	 * a string.
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/services/repository/search_flows.json
	 * @param sQuery 
	 * @return Set<URI>
	 * @throws TransmissionException
	 */
	public Set<URI> retrieveFlowUrlsByQuery(String sQuery)
	throws TransmissionException {

		try {
			String sRestCommand = "services/repository/search_flows.json";
			Set<NameValuePair> nvps = new HashSet<NameValuePair>();
			nvps.add(new NameValuePair("q", sQuery));
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, null);
			JSONArray ja = new JSONArray(jtRetrieved);
			Set<URI> setCompURIs = new HashSet<URI>();
			for ( int i=0, iMax=ja.length() ; i<iMax ; i++ )
				setCompURIs.add(new URI(ja.getJSONObject(i).getString("meandre_uri")));
			return setCompURIs;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
		}
	}


	/**
	 * uploads a single flow to the server.
	 *
	 * <p> calls:
	 * http://<meandre_host>:<meandre_port>/services/repository/add.nt
	 * @param flow 
	 * @param overwrite 
	 * @return success
	 * @throws TransmissionException 
	 *
	 */
	public boolean uploadFlow(FlowDescription flow, boolean overwrite)
	throws TransmissionException{

		Model mod = flow.getModel();
		return uploadModel(mod, null, overwrite);
	}

	/**
	 * uploads a set of flows to the server.
	 *
	 * <p> calls:
	 * http://<meandre_host>:<meandre_port>/services/repository/add.nt
	 * TODO:Need test
	 * @param flows 
	 * @param overwrite 
	 * @return success
	 * @throws TransmissionException 
	 */
	public boolean uploadFlowBatch(Set<FlowDescription> flows, boolean overwrite)
	throws TransmissionException{

		HashSet<Model> hsFlowModels = new HashSet<Model>();
		Iterator<FlowDescription> flowIter = flows.iterator();
		while(flowIter.hasNext()){
			hsFlowModels.add(flowIter.next().getModel());
		}
		return uploadModelBatch(hsFlowModels, null, overwrite);
	}

	/**
	 * uploads a single component to the server.
	 *
	 * <p> calls:
	 * http://<meandre_host>:<meandre_port>/services/repository/add.nt
	 * @param component 
	 * @param jarFileContexts 
	 * @param overwrite 
	 * @return success
	 * @throws TransmissionException 
	 *
	 */
	public boolean uploadComponent(ExecutableComponentDescription component,
			Set<File> jarFileContexts, boolean overwrite)
	throws TransmissionException{

		Model mod = component.getModel();
		return uploadModel(mod, jarFileContexts, overwrite);
	}

	/**
	 * uploads a set of flows to the server.
	 *
	 * <p> calls:
	 * http://<meandre_host>:<meandre_port>/services/repository/add.nt
	 * TODO:Need test
	 * @param comps 
	 * @param jarFileContexts 
	 * @param overwrite 
	 * @return success
	 * @throws TransmissionException 
	 */
	public boolean uploadComponentBatch(Set<ExecutableComponentDescription> comps,
			Set<File> jarFileContexts, boolean overwrite)
	throws TransmissionException {

		HashSet<Model> hsComponentModels = new HashSet<Model>();
		Iterator<ExecutableComponentDescription> compIter = comps.iterator();
		while(compIter.hasNext()){
			hsComponentModels.add(compIter.next().getModel());
		}
		return uploadModelBatch(hsComponentModels, jarFileContexts, overwrite);

	}


	/**
	 * uploads all resources of a repository to a server, merging it with
	 * the server's repository.
	 *
	 * the jar files set may be null.
	 *
	 * <p> calls:
	 * http://<meandre_host>:<meandre_port>/services/repository/add.nt
	 * TODO:Need test
	 * @param repo 
	 * @param jarFileContexts 
	 * @param overwrite 
	 * @return success
	 * @throws TransmissionException 
	 */
	public boolean uploadRepository(QueryableRepository repo,
			Set<File> jarFileContexts, boolean overwrite)
	throws TransmissionException {

		Model mod = repo.getModel();
		return uploadModel(mod, jarFileContexts, overwrite);
	}

	/**
	 * uploads a single model containing flows and/or components and any
	 * jar files.
	 * the jarfiles set may be null.
	 *
	 * <p> calls:
	 * http://<meandre_host>:<meandre_port>/services/repository/add.nt
	 * TODO:Need test
	 */
	private boolean uploadModel(Model mod, Set<File> jarFileContexts,
			boolean overwrite) throws TransmissionException {

		HashSet<Model> modSet = new HashSet<Model>(1);
		modSet.add(mod);
		return uploadModelBatch(modSet, jarFileContexts, overwrite);
	}

	/**
	 * uploads a set of component or flow resources and any jar files.
	 * the jarfiles set may be null.
	 *
	 * Note: this is the main upload function that actually does the
	 * upload. all other upload* methods call this.
	 *
	 * <p> calls:
	 * http://<meandre_host>:<meandre_port>/services/repository/add.nt
	 * TODO:Need test
	 * @param msResourceModels 
	 * @param jarFileContexts 
	 * @param overwrite 
	 * @return success
	 * @throws TransmissionException 
	 */
	public boolean uploadModelBatch(Set<Model> msResourceModels,
			Set<File> jarFileContexts, boolean overwrite)
	throws TransmissionException {

		String sRestCommand = "services/repository/add.json";

		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		nvps.add(new NameValuePair("overwrite", Boolean.toString(overwrite)));
		nvps.add(new NameValuePair("dump", "false"));

		Set<Part> postParts = new HashSet<Part>();

		for(Model modUpload : msResourceModels){
			ByteArrayOutputStream osModel = new ByteArrayOutputStream();
			modUpload.write(osModel, "N-TRIPLE");
			byte[] baModel = osModel.toByteArray();
			//NOTE: "InMemoryBytes" is given as the filename, and it is not
			//clear what it's used for by httpclient in this context
			PartSource source = new ByteArrayPartSource("InMemoryBytes",
					baModel);
			postParts.add(new FilePart("repository", source));
		}

		if(jarFileContexts != null){
			for(File jarFile : jarFileContexts){
				//System.out.println("MeandreClient Upload, Jar file:" +
				//jarFile.getAbsolutePath());
				try{
					if(!jarFile.exists()){
						throw new FileNotFoundException(jarFile.toString());
					}
					postParts.add(new FilePart("context", jarFile));
				}catch(FileNotFoundException fne){
					String msg = "Jar file to upload could not be found.";
					throw new TransmissionException(msg, fne);
				}
			}
		}
		@SuppressWarnings("unused")
		byte[] baRetrieved = executePostRequestBytes(sRestCommand, nvps, postParts);
		//System.out.println("returned: " + (new String(baRetrieved)));
		return true;
	}

	/**
	 * Uploads a set of jar files to the resources directory of the server.
	 * For instance, jar files required by an applet that a component
	 * uses in it's web UI, which are not uploaded with the component itself
	 * because the component has no direct dependency on them, would be
	 * uploaded via this method and then be available to the applet.
	 *
	 *
	 * <p> calls:
	 * http://<meandre_host>:<meandre_port>/services/repository/add.nt
	 * TODO:Need test
	 * @param files 
	 * @param overwrite 
	 * @return success
	 * @throws TransmissionException 
	 */
	public boolean uploadFiles(Set<File> files, boolean overwrite)
	throws TransmissionException {
		//just use the regular uploader with no models
		Set<Model> emptyModelSet = new HashSet<Model>(0);
		boolean ret = uploadModelBatch(emptyModelSet, files, overwrite);
		return ret;
	}

	/**
	 *removes (deletes) either a component or flow from the server. returns
	 *true if the resource was deleted.
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/services/repository/remove.json
	 * TODO: need more specific error reporting when the server returns an empty
	 * json string
	 * @param sResourceUrl 
	 * @return success
	 * @throws TransmissionException 
	 */
	public boolean removeResource(String sResourceUrl) throws TransmissionException{
		String sRestCommand = "services/repository/remove.json";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		nvps.add(new NameValuePair("uri", sResourceUrl));
		JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, nvps);
		try{
			JSONArray ja = new JSONArray(jtRetrieved);
			if ( ja.length()!=1 )
				return false;

			JSONObject joRetrieved = ja.getJSONObject(0);
			if(joRetrieved.isNull("meandre_uri")){
				return false;
			}
			String sRetrieved = joRetrieved.getString("meandre_uri");
			//server returns the url if the resource was successfully removed
			return (sRetrieved.equals(sResourceUrl));
		}catch(JSONException e){
			throw new TransmissionException(e);
		}
	}



	/////////
	//Publish
	/////////

	/**
	 * commands the server to change a component or flow's status to "published."
	 * returns true if the resource is in a state of "published" after this
	 * method returns.
	 *
	 *<p> calls:
	 * http://<meandre_host>:<meandre_port>/services/publish/publish.json
	 * @param sResourceUrl 
	 * @return success
	 * @throws TransmissionException 
	 */
	public boolean publish(String sResourceUrl) throws TransmissionException {
		try {
			String sRestCommand = "services/publish/publish.json";

			Set<NameValuePair> nvps = new HashSet<NameValuePair>();
			nvps.add(new NameValuePair("uri", sResourceUrl));
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, nvps);
			JSONArray ja = new JSONArray(jtRetrieved);
			if ( ja.length()==1 )
				return sResourceUrl.equals(ja.getJSONObject(0).getString("meandre_uri"));
			else
				return false;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
		}
	}


	/**
	 * commands the server to change a component or flow's status to
	 * "not published."
	 *
	 * returns true no matter what as long as the server received and understood
	 * the request.
	 *
	 * TODO: modify so returns true if the resource is not in a state of
	 * "published" after this method returns.
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/services/publish/unpublish.json
	 * @param sResourceUrl 
	 * @return success
	 * @throws TransmissionException 
	 */
	public boolean unpublish(String sResourceUrl) throws TransmissionException {
		try {
			String sRestCommand = "services/publish/unpublish.json";

			Set<NameValuePair> nvps = new HashSet<NameValuePair>();
			nvps.add(new NameValuePair("uri", sResourceUrl));
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, nvps);
			JSONArray ja = new JSONArray(jtRetrieved);
			if ( ja.length()==1 )
				return sResourceUrl.equals(ja.getJSONObject(0).getString("meandre_uri"));
			else
				return false;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
		}
	}


	/////////
	//Execution
	///////////

	/**
	 * commands the server to run the flow with the given url-name. the returned
	 * string is a human readable printout of stdout from the components in the
	 * flow and (optionally, if verbose=true) statistics about the flow run.
	 *
	 * This method currently blocks waiting for flow to complete -> it does
	 * not return the result string until the flow has completely finished.
	 *
	 *<p> calls:
	 * http://<meandre_host>:<meandre_port>/services/execute/flow.txt
	 * @param sFlowUrl 
	 * @param verbose 
	 * @return response as string
	 * @throws TransmissionException 
	 */

	public String runFlow(String sFlowUrl, boolean verbose)
	throws TransmissionException{
		String sRestCommand = "services/execute/flow.txt";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		nvps.add(new NameValuePair("uri", sFlowUrl));
		nvps.add(new NameValuePair("statistics", Boolean.toString(verbose)));


		String sResults = executeGetRequestString(sRestCommand, nvps);
		return sResults;
	}

	/**Executes the flow with the list of probes that will be turned on.
	 * 
	 * @param sFlowUrl
	 * @param token 
	 * @param probeList
	 * @return response as string
	 * @throws TransmissionException
	 * <p> calls:
	 * http://<meandre_host>:<meandre_port>/services/execute/flow.txt
	 * 
	 */
	public String runFlow(String sFlowUrl, String token, HashMap<String,String>  probeList) throws TransmissionException{
		String sRestCommand = "services/execute/flow.txt";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		nvps.add(new NameValuePair("uri", sFlowUrl));
		nvps.add(new NameValuePair("token",token));
		if(!probeList.isEmpty()){
			Set<String> keys = probeList.keySet();
			Iterator<String> itK = keys.iterator();
			String key = null;
			while(itK.hasNext()){
				key = itK.next();
				nvps.add(new NameValuePair(key, probeList.get(key)));
			}
		}
		String sResults = executeGetRequestString(sRestCommand, nvps);
		return sResults;
	}
	
	



	/**Returns True or false. If true then the request for execution
	 * was successfully delivered 2xx http codes
	 * 
	 * @param sFlowUrl
	 * @param token
	 * @param probeList
	 * @return success
	 * @throws TransmissionException
	 */
	public boolean runAsyncFlow(String sFlowUrl, String token,HashMap<String, String> probeList) throws TransmissionException {
		String sRestCommand = "services/execute/flow.txt";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		nvps.add(new NameValuePair("uri", sFlowUrl));
		nvps.add(new NameValuePair("token",token));
		if(!probeList.isEmpty()){
			Set<String> keys = probeList.keySet();
			Iterator<String> itK = keys.iterator();
			String key = null;
			while(itK.hasNext()){
				key = itK.next();
				nvps.add(new NameValuePair(key, probeList.get(key)));
			}
		}
		int httpCode=executeGetRequestNoWait(sRestCommand, nvps);
		if(httpCode==200){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param fileName
	 * @param token
	 * @param probeList
	 * @return true false
	 * @throws TransmissionException
	 */
	public boolean runAsyncModel(String fileName, String token,HashMap<String, String> probeList) 
	throws TransmissionException {
		String sRestCommand = "services/execute/repository.txt";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		Set<Part> postParts = new HashSet<Part>();
		ByteArrayOutputStream osModel = new ByteArrayOutputStream();
		File file = new File(fileName);
		FileInputStream fos=null;
		byte[] baModel=null;
		try {
			fos = new FileInputStream(file);
			System.out.println("Reading file: " + fileName);
			baModel= new byte[(int) file.length()];
			System.out.println("Size of file: " + file.length());
			fos.read(baModel);
		} catch (FileNotFoundException e) {
			throw new TransmissionException(e);
		} catch (IOException e) {
			throw new TransmissionException(e);
		}finally{
			
		}
		//NOTE: "InMemoryBytes" is given as the filename, and it is not
		//clear what it's used for by httpclient in this context
		PartSource source = new ByteArrayPartSource("InMemoryBytes",baModel);
		postParts.add(new FilePart("repository", source));
		nvps.add(new NameValuePair("token",token));
		if(!probeList.isEmpty()){
			Set<String> keys = probeList.keySet();
			Iterator<String> itK = keys.iterator();
			String key = null;
			while(itK.hasNext()){
				key = itK.next();
				nvps.add(new NameValuePair(key, probeList.get(key)));
			}
		}
		

		int httpCode= executePostRequestNoWait(sRestCommand, nvps, postParts);
		if(httpCode==200){
			return true;
		}else{
			return false;
		}
	
	}


	/**
	 * This method uploads and executes all the flows in the provided model
	 *
	 * <p> calls:
	 * http://<meandre_host>:<meandre_port>/services/execute/repository.txt
	 * TODO:Need test
	 * @param model 
	 * @return response as string
	 * @throws TransmissionException 
	 */
	public String runRepository(Model model)
	throws TransmissionException {

		String sRestCommand = "services/execute/repository.txt";

		Set<NameValuePair> nvps = new HashSet<NameValuePair>();

		Set<Part> postParts = new HashSet<Part>();


		ByteArrayOutputStream osModel = new ByteArrayOutputStream();
		model.write(osModel, "N-TRIPLE");
		byte[] baModel = osModel.toByteArray();
		//NOTE: "InMemoryBytes" is given as the filename, and it is not
		//clear what it's used for by httpclient in this context
		PartSource source = new ByteArrayPartSource("InMemoryBytes",
				baModel);
		postParts.add(new FilePart("repository", source));

		byte[] baRetrieved = executePostRequestBytes(sRestCommand, nvps, postParts);

		return new String(baRetrieved);
	}

	/**
	 * commands the server to run the flow with the given url-name. the returned
	 * string is a human readable printout of stdout from the components in the
	 * flow and (optionally, if verbose=true) statistics about the flow run.
	 *
	 * This method currently blocks waiting for flow to complete -> it does
	 * not return the result string until the flow has completely finished.
	 *
	 *<p> calls:
	 * http://<meandre_host>:<meandre_port>/services/execute/flow.txt
	 * @param sFlowUrl 
	 * @param verbose 
	 * @return InputStream
	 * @throws TransmissionException 
	 */

	public InputStream runFlowStreamOutput(String sFlowUrl, boolean verbose)
	throws TransmissionException{

		return runFlowStreamOutput(sFlowUrl, null, verbose);
	}

	public InputStream runFlowStreamOutput(String sFlowUrl, String token, boolean verbose)
	throws TransmissionException {

		String sRestCommand = "services/execute/flow.txt";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		nvps.add(new NameValuePair("uri", sFlowUrl));
		if (token != null)
			nvps.add(new NameValuePair("token", token));
		nvps.add(new NameValuePair("statistics", Boolean.toString(verbose)));
		
		nvps.add(new NameValuePair("nema", "true"));
		return executeGetRequestStream(sRestCommand, nvps);
	}

	/**
	 * Retrieves the WebUI information for the flow referenced by 'token'
	 * Example of WebUI information returned:
	 *          port=1716
	 *          hostname=192.168.0.2
	 *          token=1213938009687
	 *          uri=meandre://test.org/flow/webmonkflow/1213938147793/1565344277
	 *
	 * Note: Not yet unit tested
	 *
	 * @param token The token of the flow to return the WebUI information for
	 * @return  A JSONObject containing the WebUI information
	 * @throws TransmissionException
	 */
	public JSONObject retrieveWebUIInfo(String token) throws TransmissionException {
		String sRestCommand = "services/execute/uri_flow.txt";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		nvps.add(new NameValuePair("token", token));

		InputStream results = executeGetRequestStream(sRestCommand, nvps);

		Properties properties = new Properties();
		try {
			properties.load(results);
		}
		catch (IOException e) {
			throw new TransmissionException(e);
		}

		System.out.println(properties.toString());

		JSONObject joWebUIInfo = (properties.isEmpty()) ? null : new JSONObject();

		for (Entry<Object, Object> prop : properties.entrySet())
			try {
				joWebUIInfo.put(prop.getKey().toString(), prop.getValue());
			}
		catch (JSONException e) {
			throw new TransmissionException(e);
		}

		return joWebUIInfo;
	}

	/**
	 * returns the url name of any running flows and the url assigned to
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
	 * @throws TransmissionException 
	 */
	public Map<URI,URI> retrieveRunningFlows() throws TransmissionException{
		try {
			String sRestCommand = "services/execute/list_running_flows.json";
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, null);
			JSONArray ja = new JSONArray(jtRetrieved);
			Map<URI, URI> muRetrievedPairs = new Hashtable<URI,URI>();
			for ( int i=0,iMax=ja.length() ; i<iMax ; i++ ) {
				JSONObject jo = ja.getJSONObject(i);
				muRetrievedPairs.put(
						new URI(jo.getString("flow_instance_uri")),
						new URI(jo.getString("flow_instance_webui_uri"))
				);
			}
			return muRetrievedPairs;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
		}
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
	 * @throws TransmissionException 
	 */
	public Map<URI,Map<String,URI>> retrieveRunningFlowsInformation() throws TransmissionException{
		try {
			String sRestCommand = "services/execute/list_running_flows.json";
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, null);
			JSONArray ja = new JSONArray(jtRetrieved);
			Map<URI, Map<String,URI>> muRetrievedPairs = new Hashtable<URI,Map<String,URI>>();
			for ( int i=0,iMax=ja.length() ; i<iMax ; i++ ) {
				JSONObject jo = ja.getJSONObject(i);
				Map<String,URI> map = new Hashtable<String,URI>(3);
				URI furi = new URI(jo.getString("flow_instance_uri"));
				map.put("flow_instance_uri",furi);
				map.put("flow_instance_webui_uri",new URI(jo.getString("flow_instance_webui_uri")));
				map.put("flow_instance_proxy_webui_uri",new URI(jo.getString("flow_instance_proxy_webui_uri")));
				muRetrievedPairs.put(furi,map);
			}
			return muRetrievedPairs;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
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
	 * @throws TransmissionException 
	 */
	public Vector<Map<String,String>> retrieveJobStatuses() throws TransmissionException{
		try {
			String sRestCommand = "services/jobs/list_jobs_statuses.json";
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, null);
			JSONArray ja = new JSONArray(jtRetrieved);
			Vector<Map<String,String>> vecStatuses = new Vector<Map<String,String>>();
			for ( int i=0,iMax=ja.length() ; i<iMax ; i++ ) {
				JSONObject jo = ja.getJSONObject(i);
				Map<String,String> map = new Hashtable<String,String>(4);
				map.put("status",jo.getString("status"));
				map.put("server_id",jo.getString("server_id"));
				map.put("ts",jo.getString("ts"));
				map.put("job_id",jo.getString("job_id"));
				vecStatuses.add(map);
			}
			return vecStatuses;
		}
		catch ( Exception e ) {
			throw new TransmissionException(e);
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
	 * @throws TransmissionException 
	 */
	public String retrieveJobConsole(String sFUID) throws TransmissionException{
		try {
			String sRestCommand = "services/jobs/job_console.json?uri="+sFUID;
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, null);
			JSONArray ja = new JSONArray(jtRetrieved);
			return ja.getJSONObject(0).getString("console");
		}
		catch ( Exception e ) {
			return "Console not available";
		}
	}



	////////////
	//Public
	//////////////

	/**
	 * retrieves the public repository of published resources. does not
	 * require authorization.
	 *
	 *<p> calls:
	 * http://<meandre_host>:<meandre_port>/public/services/repository.nt
	 * @return QueryableRepository
	 * @throws TransmissionException
	 */
	public QueryableRepository retrievePublicRepository()
	throws TransmissionException {
		String sRestCommand = "public/services/repository.nt";
		byte[] baResponse = executeGetRequestBytes(sRestCommand, null);
		Model model = ModelFactory.createDefaultModel();
		model.read(new ByteArrayInputStream(baResponse), null, "N-TRIPLE");
		return new RepositoryImpl(model);
	}

	/**
	 * retrieves the demo repository of published resources. does not
	 * require authorization
	 *
	 *<p> calls:
	 *http://<meandre_host>:<meandre_port>/public/services/demo_repository.nt
	 * @return QueryableRepository
	 * @throws TransmissionException 
	 */
	public QueryableRepository retrieveDemoRepository()
	throws TransmissionException {
		String sRestCommand = "public/services/demo_repository.nt";
		byte[] baResponse = executeGetRequestBytes(sRestCommand, null);
		Model model = ModelFactory.createDefaultModel();
		model.read(new ByteArrayInputStream(baResponse), null, "N-TRIPLE");
		return new RepositoryImpl(model);
	}


	////////////////////////
	//Admin of Running Flows
	////////////////////////


	/**
	 * commands the WEBUI of a flow abort a running flow. the currently running
	 * component will be allowed to complete but no other components in the
	 * active flow will fire. the flow is specified by the port on the server that
	 * it's webui is running on.
	 *
	 * if this method returns true, it simply means that the abort
	 * request was received by the server, it does not necessarily mean
	 * that the currently running component(s) are no longer running.
	 *
	 *
	 *<p> calls:
	 *http://<meandre_host>:<webui_port>/admin/abort.txt
	 * FIXME: This is totally untested.
	 * @param iRunningFlowPort 
	 * @return  success
	 * @throws TransmissionException 
	 */
	public boolean abortFlow(int iRunningFlowPort)
	throws TransmissionException {
		//we must modify the global port, so save the value to reset it at the
		//end
		int masterPort = this.getPort();
		this.setServerAddress(this.getServerHost(), iRunningFlowPort);

		String sRestCommand = "admin/abort.txt";
		boolean success = false;
		String sExpected = "Abort request dispatched...";
		try{
			String sRetrieved = executeGetRequestString(sRestCommand, null);
			success = (sRetrieved == sExpected);
		}catch(TransmissionException e){
			throw e;
		}finally{
			//reset the port for this client instance
			this.setServerAddress(this.getServerHost(), masterPort);
		}
		return success;
	}

	/**
	 * requests the current statistics of the currently running flow from
	 * the WEBUI. the flow is specified by the port on the server that
	 * it's webui is running on.
	 *
	 * the returned json data is in the format produced by
	 * StatisticsProbeImpl.getSerializedStatistics()
	 *
	 * *<p> calls:
	 * http://<meandre_host>:<webui_port>/admin/statistics.json
	 *
	 * TODO: refactor StatisticsProbeImpl so that a RunningFlowStatistics
	 * "bean" can be read and written to/from json, and StatisticsProbeImpl
	 * simply constructs the bean.
	 * FIXME: This is totally untested.
	 * @param iRunningFlowPort 
	 * @return JSONObject
	 * @throws TransmissionException 
	 */
	public JSONObject retrieveRunningFlowStatisitics(int iRunningFlowPort)
	throws TransmissionException{

		//we must modify the global port, so save the value to reset it at the
		//end
		int masterPort = this.getPort();
		this.setServerAddress(this.getServerHost(), iRunningFlowPort);

		String sRestCommand = "admin/statistics.json";
		JSONObject joRetrieved = null;
		try{
			JSONTokener jtRetrieved = executeGetRequestJSON(sRestCommand, null);
			joRetrieved = new JSONObject(jtRetrieved);
		}catch(TransmissionException e){
			throw e;
		}catch(JSONException je){
			throw new TransmissionException(je);
		}finally{
			//reset the port for this client instance
			this.setServerAddress(this.getServerHost(), masterPort);
		}

		return joRetrieved;
	}

	//----- Amit's patch comented by Xavier and are UNTESTED ----
	// TODO: Write test cases for the methods below

	/** Return Component Descriptor as String
	 *
	 * @param sComponentUrl The component url
	 * @return The RDF description of the component
	 * @throws TransmissionException The component could not be retrieved
	 */
	public String retrieveComponentDescriptorAsString(
			String sComponentUrl) throws TransmissionException {
		String sRestCommand = "services/repository/describe_component.nt";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		nvps.add(new NameValuePair("uri", sComponentUrl));
		String model= executeGetRequestString(sRestCommand, nvps);
		return model;
	}

	/** Gets the server version.
	 *
	 * @return The server version
	 * @throws TransmissionException Could not get the server version
	 */
	public String getServerVersion() throws TransmissionException {
		String sRestCommand = "services/about/version.txt";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		String sResults = executeGetRequestString(sRestCommand, nvps);
		return sResults;
	}

	/** Return the JSON content describing the plugins available.
	 *
	 * @return JSONString  string
	 * @throws TransmissionException Fail to retrieve the plugins' information
	 */
	public String getServerPlugins() throws TransmissionException {
		String sRestCommand = "services/about/plugins.json";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		String sResults = executeGetRequestString(sRestCommand, nvps);
		return sResults;
	}


	/** Returns jar information
	 *
	 * @param jarFile The jar file to get the info from
	 * @return Returns the information associated to the jar
	 * @throws TransmissionException The plugin failed to retrieve the information
	 */
	public String getComponentJarInfo(String jarFile)
	throws TransmissionException {
		String sRestCommand = "plugin/jar/" + jarFile + "/info";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		String value = executeGetRequestString(sRestCommand, nvps);
		return value;
	}


	/** Pings the server
	 *
	 *	@return True if it successfully pinged the server
	 * @throws TransmissionException 
	 */
	public boolean ping() throws TransmissionException {
		String sRestCommand = "public/services/ping.txt";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		String sPing = executeGetRequestString(sRestCommand, nvps);
		if (sPing == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**Reads the flow properties for a token and returns the flow execution instance Id
	 * 
	 * @param token
	 * @return ExecResponse
	 * @throws TransmissionException
	 */
	public ExecResponse getFlowExecutionInstanceId(String token) throws TransmissionException{
		String sRestCommand = "services/execute/uri_flow.txt";
		Set<NameValuePair> nvps = new HashSet<NameValuePair>();
		nvps.add(new NameValuePair("token", token));
		String value = executeGetRequestString(sRestCommand, nvps);
		if(value==null){
			throw new TransmissionException("Invalid response from the server: getFlowExecutionInstanceId " + token);
		}
		String uri=null;
		String hostname = null;
		String _port = null;
		String tokenFromServer = null;
		int port = -1;
		try {
			InputStream is = new ByteArrayInputStream(value.getBytes("UTF-8"));
			Properties properties  = new Properties();
			properties.load(is);
			uri=properties.getProperty("uri");
			tokenFromServer=properties.getProperty("token");
			hostname=properties.getProperty("hostname");
			_port=properties.getProperty("port");
			port = Integer.parseInt(_port);
		} catch (UnsupportedEncodingException e) {
			throw new TransmissionException(e);
		} catch (IOException e) {
			throw new TransmissionException(e);
		}catch(Exception e){
			throw new TransmissionException(e);
		}
		if(uri==null){
			throw new TransmissionException("Error could not get uri for the token: " + token+ "\nThe properties returned from the server are: " + value );
		}
		ExecResponse response = new ExecResponse();
		response.setPort(port);
		response.setHostname(hostname);
		response.setUri(uri);
		response.setToken(tokenFromServer);
		return response;
	}



	public void destroy() {
		// here  cleanup resources...	
	}

}
