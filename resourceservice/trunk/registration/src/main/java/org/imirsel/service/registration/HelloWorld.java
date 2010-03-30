package org.imirsel.service.registration;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloWorld extends Remote{
	
	public String sayHello() throws RemoteException;
	

}
