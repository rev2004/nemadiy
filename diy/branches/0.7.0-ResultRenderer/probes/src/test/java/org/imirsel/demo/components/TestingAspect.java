package org.imirsel.demo.components;

public class TestingAspect {

	
	public static void  main(String args[]){
		//System.out.println("Hello world");
		TestingAspect t = new  TestingAspect();
		t.helloWorld();
		System.exit(0);
	}
	
	public void  helloWorld(){
		System.out.println("hello world aspect capture this");
		System.exit(0);
	}
}