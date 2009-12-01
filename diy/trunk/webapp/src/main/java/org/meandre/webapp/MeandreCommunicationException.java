package org.meandre.webapp;

import org.imirsel.meandre.client.TransmissionException;

public class MeandreCommunicationException extends Exception {

	public MeandreCommunicationException(TransmissionException e) {
		super(e);
	}

}
