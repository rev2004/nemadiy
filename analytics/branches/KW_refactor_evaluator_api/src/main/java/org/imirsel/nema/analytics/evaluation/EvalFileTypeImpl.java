package org.imirsel.nema.analytics.evaluation;

import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.imirsel.nema.analytics.logging.AnalyticsLogFormatter;

public abstract class EvalFileTypeImpl {

	protected Logger _logger;
	private String typeName;
	private String filenameExtension = ".txt";
	
	public EvalFileTypeImpl(String typeName) {
		this.typeName = typeName;
	}
	
	public Logger getLogger() {
		if (_logger == null){
			_logger = Logger.getLogger(this.getClass().getName());
		}
		return _logger;
	}

	public void addLogDestination(PrintStream stream) {
		Handler handler = new StreamHandler(stream, new AnalyticsLogFormatter());
		getLogger().addHandler(handler);
	}

	public String getTypeName() {
		return typeName;
	}

	public void setFilenameExtension(String filenameExtension) {
		this.filenameExtension = filenameExtension;
	}

	public String getFilenameExtension() {
		return filenameExtension;
	}
}