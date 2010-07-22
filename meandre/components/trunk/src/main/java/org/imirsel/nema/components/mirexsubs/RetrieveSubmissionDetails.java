package org.imirsel.nema.components.mirexsubs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaSubmission;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repository.RepositoryClientImpl;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

/**
 * Retrieves submission details for submission code used as keys in the input
 * map, outputting a map of submission code to a NemaSubmission Object.
 * @author kris.west@gmail.com
 * @since 0.4.0
 */
@Component(creator = "Kris West", description = "Retrieves submission details " +
		"for submission code used as keys in the input map, outputting a map of " +
		"submission code to a NemaSubmission Object.", 
		name = "RetrieveSubmissionDetails",
		resources={"../../../../../RepositoryProperties.properties"},
		tags = "submission repository", firingPolicy = Component.FiringPolicy.all)
public class RetrieveSubmissionDetails extends NemaComponent {

	@ComponentInput(description = "A Map that uses submission codes as keys.", name = "submission code map")
	public final static String DATA_INPUT_SUB_CODE_MAP = "submission code map";
	
	@ComponentOutput(description = "A Map of submission code to the submission " +
			"details retrieved from the mirexsubs DB", name = "subCodeToSubDetails")
	private static final String DATA_OUT_SUB_DATA_MAP ="subCodeToSubDetails";

	public RetrieveSubmissionDetails() {

	}

	@Override
	public void initialize(ComponentContextProperties cc) throws ComponentExecutionException, ComponentContextException {
		super.initialize(cc);
	}
	
	@Override
	public void dispose(ComponentContextProperties cc) throws ComponentContextException {
		super.dispose(cc);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		
		Map<String,? extends Object> input = (Map<String,? extends Object>)cc.getDataComponentFromInput(DATA_INPUT_SUB_CODE_MAP);
		List<String> subs = new ArrayList<String>(input.keySet());
		Map<String,NemaSubmission> out = new HashMap<String, NemaSubmission>();
		
		RepositoryClientInterface client = null;
		
		try{
			client = RepositoryClientConnectionPool.getInstance().getFromPool();
			for (Iterator<String> iterator = subs.iterator(); iterator.hasNext();) {
				String subCode = iterator.next();
				cc.getOutputConsole().println("Retrieving submission details for: " + subCode);
				NemaSubmission sub = client.getSubmissionDetails(subCode);
				out.put(subCode,sub);
			}
		}catch(SQLException e){
			throw new ComponentExecutionException("Failed to retrieve submission details!",e);
			
		}finally{
			if (client != null){
				RepositoryClientConnectionPool.getInstance().returnToPool(client);
			}
		}

		cc.getOutputConsole().println("Done retrieving submission details");
		cc.pushDataComponentToOutput(DATA_OUT_SUB_DATA_MAP, out);
	}

}
