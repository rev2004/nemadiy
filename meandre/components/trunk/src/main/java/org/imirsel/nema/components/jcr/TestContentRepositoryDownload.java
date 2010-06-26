package org.imirsel.nema.components.jcr;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.imirsel.nema.contentrepository.client.ContentRepositoryService;
import org.imirsel.nema.contentrepository.client.ContentRepositoryServiceException;
import org.imirsel.nema.contentrepository.client.ResultStorageService;
import org.imirsel.nema.model.NemaResult;
import org.imirsel.nema.model.RepositoryResourcePath;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

@Component(creator = "Amit Kumar", description = "test content repository", name = "TestContentRepositoryDownload", tags = "content repository")
public class TestContentRepositoryDownload implements ExecutableComponent{
	
	@ComponentProperty(defaultValue = "rmi://nema.lis.uiuc.edu:2099/jackrabbit.repository", description = "Content Repository URI that is used to connect to the repository", name = "contentRepositoryUri")
	private static final String PROPERTY_1  ="contentRepositoryUri";
	
	
	@ComponentOutput(description = "Data Output", name = "message")
	private static final String DATA_OUT_1 ="message";
	
	private ResultStorageService resultStorageService;
	
	public void dispose(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		// TODO Auto-generated method stub
		
	}

	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		RepositoryResourcePath resourcePath = new RepositoryResourcePath("jcr", "default", "/users/admin/flows/admin_1277528709278/results/set-90/mel_adc_04000000.wav.out.txt");
		SimpleCredentials credentials = new SimpleCredentials("admin","b2cebd873228d3e6753d9b39195730694e3d1bbc".toCharArray());
		try {
			NemaResult result=this.resultStorageService.getNemaResult(credentials, resourcePath);
			int len=result.getFileContent().length;
			String fname = result.getResultPath();
			cc.pushDataComponentToOutput(DATA_OUT_1, fname+"="+len);
		} catch (ContentRepositoryServiceException e) {
			throw new ComponentExecutionException(e);
		}
		
	}

	public void initialize(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		String contentRepositoryUri = ccp.getProperty(PROPERTY_1);
		System.out.println("CONTENT REP URI: " + contentRepositoryUri);
		ClientRepositoryFactory factory = new ClientRepositoryFactory();
		Repository repository;
		try {
			repository = factory.getRepository(contentRepositoryUri);
		} catch (MalformedURLException e) {
			throw new ComponentExecutionException(e);
		} catch (ClassCastException e) {
			throw new ComponentExecutionException(e);
		} catch (RemoteException e) {
			throw new ComponentExecutionException(e);
		} catch (NotBoundException e) {
			throw new ComponentExecutionException(e);
		}
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		resultStorageService = crs;
		System.out.println("====> ResultStorageService inited. "+  resultStorageService.toString());
		
	}

	public static void main(String args[]) throws Exception{
		String contentRepositoryUri = "rmi://nema.lis.uiuc.edu:2099/jackrabbit.repository";
		System.out.println("CONTENT REP URI: " + contentRepositoryUri);
		ClientRepositoryFactory factory = new ClientRepositoryFactory();
		Repository repository;
		try {
			repository = factory.getRepository(contentRepositoryUri);
		} catch (MalformedURLException e) {
			throw new ComponentExecutionException(e);
		} catch (ClassCastException e) {
			throw new ComponentExecutionException(e);
		} catch (RemoteException e) {
			throw new ComponentExecutionException(e);
		} catch (NotBoundException e) {
			throw new ComponentExecutionException(e);
		}
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		
		System.out.println("====> ResultStorageService inited. "+  crs.toString());
		RepositoryResourcePath resourcePath = new RepositoryResourcePath("jcr", "default", "/users/admin/flows/admin_1277528709278/results/set-90/mel_adc_04000000.wav.out.txt");
		SimpleCredentials credentials = new SimpleCredentials("admin","b2cebd873228d3e6753d9b39195730694e3d1bbc".toCharArray());
		NemaResult result=crs.getNemaResult(credentials, resourcePath);
		int len=result.getFileContent().length;
		String fname = result.getResultPath();
		System.out.println("===> "+fname+"="+len);
	
	}
	
	

}
