package org.imirsel.nema.webapp.service.impl;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import org.imirsel.nema.model.FileDataType;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.webapp.service.impl.ResourceTypeServiceImpl;

public class ResourceTypeServiceImplTest {
	

	@Test
	public void testTasks() throws SQLException{
		System.out.println("Starting the pool task");
		RepositoryClientConnectionPool pool = RepositoryClientConnectionPool.getInstance();
		System.out.println("Starting: " + new Date());
		ResourceTypeServiceImpl rtsi = new ResourceTypeServiceImpl();
		rtsi.setRepositoryClientConnectionPool(pool);
		List<NemaTask> list=rtsi.getSupportedTasks();
		
		for(NemaTask nt:list){
			System.out.println(nt.getName() +"   " + nt.getId());
		}
		
		System.out.println("Ending: " + new Date());
		
		
	}

	@Test
	public void testGetSupportedOutputFileDataTypes() throws SQLException {
		System.out.println("Starting the pool output");
		RepositoryClientConnectionPool pool = RepositoryClientConnectionPool.getInstance();
		System.out.println("Starting: " + new Date());
		ResourceTypeServiceImpl rtsi = new ResourceTypeServiceImpl();
		rtsi.setRepositoryClientConnectionPool(pool);
		List<FileDataType> list=rtsi.getSupportedOutputFileDataTypes(17);
		
		for(FileDataType nt:list){
			System.out.println(nt.getName() +"   " + nt.getValue());
		}
		
		System.out.println("Ending: " + new Date());
		
		
	}

	@Test
	public void  testGetSupportedInputFileDataTypes() throws SQLException {
		System.out.println("Starting the pool input");
		RepositoryClientConnectionPool pool = RepositoryClientConnectionPool.getInstance();
		System.out.println("Starting: " + new Date());
		ResourceTypeServiceImpl rtsi = new ResourceTypeServiceImpl();
		rtsi.setRepositoryClientConnectionPool(pool);
		List<FileDataType> list=rtsi.getSupportedOutputFileDataTypes(17);
		
		for(FileDataType nt:list){
			System.out.println(nt.getName() +"   " + nt.getValue());
		}
		
		System.out.println("Ending: " + new Date());
		
		
	}

}
