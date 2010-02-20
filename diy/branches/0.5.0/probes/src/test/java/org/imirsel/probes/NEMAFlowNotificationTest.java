/**
 * 
 */
package org.imirsel.probes;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;


import org.imirsel.probes.NemaFlowNotification;

import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.meandre.configuration.CoreConfiguration;
import org.meandre.core.engine.Conductor;
import org.meandre.core.engine.Executor;
import org.meandre.core.engine.MrProbe;
import org.meandre.core.repository.QueryableRepository;
import org.meandre.core.repository.RepositoryImpl;
import org.meandre.demo.repository.DemoRepositoryGenerator;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author Amit Kumar
 *
 */
public class NEMAFlowNotificationTest {
	
	private Logger logger = Logger.getLogger("testLogger");
	Mockery context = new Mockery();

	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		// initialize the jndi
	/*	ServletConfig servletConfig = context.mock(ServletConfig.class);
		JndiInitializeServlet servlet = new JndiInitializeServlet();
		servlet.setLogger(logger);
		servlet.init(servletConfig);
	*/	
		
		
		Model model = DemoRepositoryGenerator.getTestHelloWorldRepository();//.getTestHelloWorldHetereogenousRepository();
		QueryableRepository qr = new RepositoryImpl(model);
		CoreConfiguration cnf = new CoreConfiguration();
		Conductor conductor = new Conductor(10,cnf);

		
		ByteArrayOutputStream baosOutNema = new ByteArrayOutputStream();
		NemaFlowNotification nemaProbeImpl = new NemaFlowNotification();
		nemaProbeImpl.initialize();
		MrProbe probe = new MrProbe(logger,nemaProbeImpl,false,false);
		
		Executor exec = conductor.buildExecutor(qr, qr.getAvailableFlows().iterator().next(),probe, new PrintStream(baosOutNema));
		runExecutor(exec,baosOutNema);
		nemaProbeImpl.dispose();

	}

	
	private void runExecutor(Executor exec, ByteArrayOutputStream baosOut) {
		PrintStream psOut = System.out;
		PrintStream psErr = System.err;
		ByteArrayOutputStream baosErr = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baosOut));
		System.setErr(new PrintStream(baosErr));
		exec.execute(exec.initWebUI(1707,Math.random()+""));
		System.setOut(psOut);
		System.setErr(psErr);
		// Restore the output
		assertTrue(exec.hadGracefullTermination());
		assertEquals(0,exec.getAbortMessage().size());
		System.out.println(baosErr);
		assertEquals(0,baosErr.size());
		
	}
	
	@Test
	public void testme(){
		System.out.println("Hello World");
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	
	}

}
