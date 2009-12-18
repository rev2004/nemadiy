package org.imirsel.nema.service;

import java.util.HashMap;
import java.util.List;

import org.imirsel.meandre.client.TransmissionException;
import org.imirsel.nema.model.Component;
import org.meandre.webapp.CorruptedFlowException;
import org.meandre.webapp.MeandreCommunicationException;

public interface FlowMetadataService {
	/**Returns the list of components that make a flow
	 * 
	 * @param flowUrl
	 * @return
	 */
	public List<Component> getComponents(String flowUrl) throws TransmissionException;

	/**Returns the uri of the new flow this would create after
	 * merging the flowUri and the parameterMap
	 * 
	 * @param paramMap
	 * @param flowUri
	 * @return
	 * @throws MeandreCommunicationException 
	 * @throws CorruptedFlowException 
	 */
	public String createNewFlow(final HashMap<String, String> paramMap, String flowUri) throws TransmissionException, MeandreCommunicationException, CorruptedFlowException;
	
	/** Removes the flow
	 * 
	 * @param uri
	 * @return
	 * @throws TransmissionException
	 */
	public boolean removeFlow(String uri) throws TransmissionException;
}
