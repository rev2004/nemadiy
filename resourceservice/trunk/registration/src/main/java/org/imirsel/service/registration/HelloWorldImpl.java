package org.imirsel.service.registration;

import java.rmi.RemoteException;

public class HelloWorldImpl implements HelloWorld {

	public String sayHello() throws RemoteException{
		System.out.println("Here returning hello world");
		return "Hello World";
	}

}
