package org.imirsel.nema.analytics.evaluation;

import java.io.File;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class EvalFileTypeImpl {

	protected Logger _logger;
	private static String WINDOWS_PATH_REGEX = "[A-Z]:\\\\";

	public Logger getLogger() {
		return _logger;
	}

	public void addLogHandler(Handler logHandler) {
		getLogger().addHandler(logHandler);
	}

	public EvalFileTypeImpl() {
		super();
	}

}