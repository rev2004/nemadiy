package org.imirsel.service.registration;

import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.lookup.entry.Name;

public class UnicastRegister{
	
	
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
		HelloWorldImpl hwImpl  = new HelloWorldImpl();
		Name pt = new Name("amit");
		Entry[] attrList = new Entry[]{pt};
		
		try {
			System.out.println("Registering HelloWorld....");
			HelloWorld hwStub=(HelloWorld) UnicastRemoteObject.exportObject(hwImpl, 0);
			ServiceItem item = new ServiceItem(null, hwStub,attrList);
			registrar.register(item, 1000000L);
			System.out.println("Registered....");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
