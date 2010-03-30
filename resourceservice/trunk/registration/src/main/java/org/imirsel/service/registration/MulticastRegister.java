package org.imirsel.service.registration;

import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import net.jini.core.lookup.ServiceRegistrar;
import net.jini.discovery.DiscoveryEvent;
import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.LookupDiscovery;


/**This class is a listener for multicast registrars.
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public class MulticastRegister implements DiscoveryListener {
	
	
	
	
	public static void main(String args[]){
		System.setSecurityManager(new RMISecurityManager());
		MulticastRegister multicastRegister = new MulticastRegister();
		LookupDiscovery discover=null;
		try {
			discover = new LookupDiscovery(LookupDiscovery.ALL_GROUPS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		discover.addDiscoveryListener(multicastRegister);
		try {
			Thread.sleep(1000000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public MulticastRegister(){}
	
	public void discarded(DiscoveryEvent discoveryEvent) {
		System.out.println("A discovery agent was discarded.");

	}

	public void discovered(DiscoveryEvent evt) {
		ServiceRegistrar[] registrars = evt.getRegistrars();
		for (int n = 0; n < registrars.length; n++) {
		ServiceRegistrar registrar = registrars[n];
		// the code takes separate routes from here for client or service
		try {
			System.out.println("found a service locator at " +
					registrar.getLocator().getHost() +
					" at port "+ registrar.getLocator().getPort());
		} catch(RemoteException e) {
			e.printStackTrace();
		}

	}
	}

}
