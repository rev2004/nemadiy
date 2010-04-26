package org.imirsel.nema.contentrepository;

import java.io.ByteArrayInputStream;
import java.util.Hashtable;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.nodetype.NodeTypeDefinition;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.NodeTypeManager;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.jackrabbit.api.JackrabbitNodeTypeManager;
import org.apache.jackrabbit.commons.NamespaceHelper;
import org.apache.jackrabbit.core.jndi.RegistryHelper;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;


/** Registers custom node types
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public class RegisterCndTypes {
	
	public static void main(String args[]) throws Exception{
		String RMI_URL = "rmi://localhost:2099/jackrabbit.repository";
		ClientRepositoryFactory factory = new ClientRepositoryFactory();
		Repository repository = getRepository();
		//Repository repository = factory.getRepository(RMI_URL);
		String username = "user";
		String password = "user";
	    Session session = repository.login(new SimpleCredentials(username,password.toCharArray()));
	    if(session==null){
	    	System.out.println("session is null");
	    }else{
	    	System.out.println(session);
	    }
	    NamespaceHelper namespaceHelper = new NamespaceHelper(session);
	    namespaceHelper.registerNamespace("imirsel","http://www.imirsel.org/jcr");
	    
	    
	    Workspace wsp = session.getWorkspace();
	    JackrabbitNodeTypeManager manager =
	           (JackrabbitNodeTypeManager) session.getWorkspace().getNodeTypeManager();
	    
	    NodeTypeIterator nti=manager.getAllNodeTypes();
	    
	    while(nti.hasNext()){
	    	System.out.println(nti.nextNodeType().getName());
	    }
	    
	    // create imirsel file
	    if(!manager.hasNodeType("imirsel:file")){
	   	   if (!manager.hasNodeType("ns:file")){
	        String cnd = "<imirsel = 'http://www.imirsel.org/jcr'>\n";
	        cnd += "[imirsel:file] > nt:file\n";
	        cnd += "- locked (BOOLEAN) mandatory ignore\n";
	        cnd += "- layout (STRING) mandatory";
	        byte cndArray[] = cnd.getBytes();
	        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cndArray);
	        manager.registerNodeTypes(byteArrayInputStream,JackrabbitNodeTypeManager.TEXT_X_JCR_CND); 
	        byteArrayInputStream.close();
		  }
	    }	  
         session.save();
         session.logout();
         
    }

	  /**
     * Creates a Repository instance to be used by the example class.
     *
     * @return repository instance
     * @throws Exception on errors
     */
   private static Repository getRepository() throws Exception {
        String configFile = "/Users/amitku/projects/contentrepository/data/repository.xml";
        String repHomeDir = "/Users/amitku/projects/contentrepository/data";

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.jackrabbit.core.jndi.provider.DummyInitialContextFactory");
        env.put(Context.PROVIDER_URL, "localhost");
        InitialContext ctx = new InitialContext(env);

        RegistryHelper.registerRepository(ctx, "repo", configFile, repHomeDir, true);
        return (Repository) ctx.lookup("repo");
    }


}
