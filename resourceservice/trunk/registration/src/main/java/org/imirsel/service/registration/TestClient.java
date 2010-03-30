package org.imirsel.service.registration;

import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.lookup.entry.Name;

public class TestClient {
	
	public static void main(String args[]){
		System.setSecurityManager(new RMISecurityManager());
		LookupLocator locator=null;
		try {
			locator = new LookupLocator("jini://localhost");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ServiceRegistrar registrar=null;
		try {
			registrar= locator.getRegistrar();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Registrar found");
		
		Name name = new Name("amit");
		Entry[] attrList = new Entry[]{name};
		 Class[] helloWorldClasses = new Class[1]; 
		 helloWorldClasses[0] = HelloWorld.class;
		 ServiceTemplate template = new ServiceTemplate(null,helloWorldClasses,attrList);
		 
		 try {
			HelloWorld hw=(HelloWorld) registrar.lookup(template);
			
			String val=hw.sayHello();
			System.out.println("val===> " + val);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
				 
                 

	}

}
