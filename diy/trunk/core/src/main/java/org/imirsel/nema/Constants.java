package org.imirsel.nema;


/**
 * Constant values used throughout the application.
 * 
 *
 * @author kumaramit01
 */
public class Constants {
    //~ Static fields/initializers =============================================

    /**
     * The name of the ResourceBundle used in this application
     */
    public static final String BUNDLE_KEY = "ApplicationResources";

    /**
     * File separator from System properties
     */
    public static final String FILE_SEP = System.getProperty("file.separator");

    /**
     * User home from System properties
     */
    public static final String USER_HOME = System.getProperty("user.home") + FILE_SEP;

    /**
     * The name of the configuration hashmap stored in application scope.
     */
    public static final String CONFIG = "appConfig";

    /**
     * Session scope attribute that holds the locale set by the user. By setting this key
     * to the same one that Struts uses, we get synchronization in Struts w/o having
     * to do extra work or have two session-level variables.
     */
    public static final String PREFERRED_LOCALE_KEY = "org.apache.struts2.action.LOCALE";

    /**
     * The request scope attribute under which an editable user form is stored
     */
    public static final String USER_KEY = "userForm";

    /**
     * The request scope attribute that holds the user list
     */
    public static final String USER_LIST = "userList";

    /**
     * The request scope attribute for indicating a newly-registered user
     */
    public static final String REGISTERED = "registered";

    /**
     * The name of the Administrator role, as specified in web.xml
     */
    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    /**
     * The name of the User role, as specified in web.xml
     */
    public static final String USER_ROLE = "ROLE_USER";

    /**
     * The name of the user's role list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String USER_ROLES = "userRoles";

    /**
     * The name of the available roles list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String AVAILABLE_ROLES = "availableRoles";
    
    /**
     * The default preferences, a request-scoped attribute when adding/editing
     * a user
     */
    public static final String DEFAULT_PREFERENCES ="defaultPreferences";

    /**
     * The name of the CSS Theme setting.
     */
    public static final String CSS_THEME = "csstheme";

    /**
     * The name of the available flow list, a request scope attribute to display
     * the list of template flows
     */
	public static final String FLOW_LIST = "flowList";
	
	/**
	 * The name of the flow, a request scope attribute to display the flow metadata
	 * and for the user to create a new flow based on this template
	 */
	public static final String FLOW = "flow";

	/**
	 * The name of the job, a request scope attribute to display the flow metadata
	 */
	public static final String JOB = "job";

	/**
	 * The list of jobs -request scope
	 */
	public static final String JOBLIST = "jobList";

	/**
	 * The components that make up a flow
	 */
	public static final String COMPONENTLIST = "componentList";

	/**
	 * The property hashmap for all the components in a flow.
	 * Keyed with the componentid, the value is a hashmap of property name and 
	 * Property
	 */
	public static final String COMPONENTPROPERTYMAP = "componentPropertyMap";

	public static final String SUBMISSIONLIST = "submissionList";

	public static final String SUBMISSION = "submission";

	public static final String RESULTSET = "resultSet";

	// used to share Dataset information with the form
	public static final String REP_DATASET = "repDataset";

	public static final String FLOW_TYPE = "flowType";

	/**
	 * The list of meandre servers available
	 */
	public static final String MEANDRE_SERVER_LIST = "meandreServerList";

	/**
	 * The server status -number of jobs running and the number of jobs aborted
	 */
	public static final String MEANDRE_SERVER_STATUS = "meandreServerStatus";
}
