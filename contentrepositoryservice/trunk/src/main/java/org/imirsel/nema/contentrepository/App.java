package org.imirsel.nema.contentrepository;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;


public class App {
	
	public static void main(String args[]) throws Exception{
		String RMI_URL = "rmi://localhost:2099/jackrabbit.repository";
		ClientRepositoryFactory factory = new ClientRepositoryFactory();
		//Repository repository = getRepository();
		Repository repository = factory.getRepository(RMI_URL);
		String username = "user";
		String password = "user";
	    Session session = repository.login(new SimpleCredentials(username,password.toCharArray()));
	    if(session==null){
	    	System.out.println("session is null");
	    }else{
	    	System.out.println(session);
	    }
	     Node root = session.getRootNode();
	     boolean nameExists=session.itemExists("/users/"+username);
	    	// Store content
           /* Node hello = root.addNode("hello");
            Node world = hello.addNode("world");
            world.setProperty("message", "Hello, World!");
            session.save();
            */
            // Retrieve content
          /*  Node node = root.getNode("edu/ucla/msc/www/oceanglobe/pdf");
            System.out.println(node.getPath());
            PropertyIterator pit=node.getProperties();
            
            while(pit.hasNext()){
            	Property property = pit.nextProperty();
            	System.out.println(property.getName()+ " " + property.getDefinition().getDeclaringNodeType().getName());
            }
            */
            //System.out.println(node.getProperty("message").getString());

            // Remove content
           //root.getNode("hello").remove();
            //session.save();
         //} finally {
           // session.logout();
        //}
         
	     Node userNode = null;
         if(!nameExists){
        	 userNode = root.addNode("users/"+username,"nt:folder");
        	 System.out.println("Created a new user node");
         }else{
        	 userNode = root.getNode("users/"+username);
             System.out.println("User already created -reusing the node");	 
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
 /*   private static Repository getRepository() throws Exception {
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
*/


		
	

}
