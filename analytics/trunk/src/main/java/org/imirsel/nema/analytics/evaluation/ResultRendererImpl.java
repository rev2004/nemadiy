package org.imirsel.nema.analytics.evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.imirsel.nema.analytics.logging.AnalyticsLogFormatter;
import org.imirsel.nema.model.NemaEvaluationResultSet;

/**
 * Abstract implementation of ResultRenderer.
 * 
 * @author kris.west@gmail.com
 * @since 0.2.0
 *
 */
public abstract class ResultRendererImpl implements ResultRenderer {

	protected Logger _logger;
	protected File workingDir;
	protected File outputDir;
	
	//temporary variables for matlab until we have java implementation of stats tests
	protected boolean performMatlabStatSigTests = true;
	protected File matlabPath = new File("matlab");
	
	public ResultRendererImpl() {
		this._logger = Logger.getLogger(this.getClass().getName());
		this.workingDir = null;
		this.outputDir = null;
	}
	
	public ResultRendererImpl(File workingDir, File outputDir) {
		this._logger = Logger.getLogger(this.getClass().getName());
		this.workingDir = workingDir;
		this.outputDir = outputDir;
	}
	
	public abstract void renderResults(NemaEvaluationResultSet results) throws IOException;

	public void setPerformMatlabStatSigTests(boolean performMatlabStatSigTests) {
		this.performMatlabStatSigTests = performMatlabStatSigTests;
	}

	public boolean getPerformMatlabStatSigTests() {
		return performMatlabStatSigTests;
	}

	public void setMatlabPath(File matlabPath) {
		this.matlabPath = matlabPath;
	}

	public File getMatlabPath() {
		return matlabPath;
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

	public void setOutputDir(File outputDir_) throws FileNotFoundException {
		outputDir = outputDir_;
		outputDir.mkdirs();
		if (!outputDir.exists()){
			throw new FileNotFoundException("Output directory " + outputDir.getAbsolutePath() + " was not found and could not be created!");
		}
	}

	public void setWorkingDir(File workingDir_) throws FileNotFoundException {
		workingDir = workingDir_;
		workingDir.mkdirs();
		if (!workingDir.exists()){
			throw new FileNotFoundException("Working directory " + workingDir.getAbsolutePath() + " was not found and could not be created!");
		}
	}
	

}
