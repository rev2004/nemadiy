package org.imirsel.nema.repository.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.imirsel.nema.analytics.evaluation.Evaluator;
import org.imirsel.nema.analytics.evaluation.ResultRenderer;
import org.imirsel.nema.analytics.evaluation.beat.BeatEvaluator;
import org.imirsel.nema.analytics.evaluation.beat.BeatResultRenderer;
import org.imirsel.nema.analytics.evaluation.chord.*;
import org.imirsel.nema.analytics.evaluation.onset.OnsetEvaluator;
import org.imirsel.nema.analytics.evaluation.onset.OnsetResultRenderer;
import org.imirsel.nema.analytics.evaluation.tempo.TempoEvaluator;
import org.imirsel.nema.analytics.evaluation.tempo.TempoResultRenderer;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.fileTypes.BeatTextFile;
import org.imirsel.nema.model.fileTypes.ChordNumberTextFile;
import org.imirsel.nema.model.fileTypes.ChordShortHandTextFile;
import org.imirsel.nema.model.fileTypes.OnsetTextFile;
import org.imirsel.nema.model.fileTypes.SingleTrackEvalFileType;
import org.imirsel.nema.model.fileTypes.TempoTextFile;






/**
 * 
 * @author kris.west@gmail.com
 * @since 0.4.0
 */
public class OnsetEvaluatorMain extends AbstractEvaluatorMainSingleTrack{
	
	public OnsetEvaluatorMain(String[] args) {
		super(args, Logger.getLogger(OnsetEvaluatorMain.class.getName()));
	}
	
	public static void main(String[] args) throws Exception{
		OnsetEvaluatorMain main = new OnsetEvaluatorMain(args);
		NemaEvaluationResultSet results = main.evaluate();
		main.resolveSubmissionDetails(results);
		main.render(results);	
		System.out.println("--exit--");
	}

	@Override
	public List<NemaData> readGTFile(File path) throws IOException{
		SingleTrackEvalFileType reader = new OnsetTextFile(); // EDIT FOR OTHER TASKS	    
		reader.getLogger().setLevel(Level.WARNING);
		return reader.readDirectory(path, null);

	}

	@Override
	public SingleTrackEvalFileType readResultDirectory(File path) throws IOException{
		SingleTrackEvalFileType reader;
		reader = new OnsetTextFile();  // EDIT FOR OTHER TASKS
		reader.getLogger().setLevel(Level.WARNING);
		return reader;
	}

	@Override
	public Evaluator getEvaluator() {
		Evaluator eval = new OnsetEvaluator();	// EDIT FOR OTHER TASKS
		eval.setTask(task);
		eval.setDataset(dataset);
		eval.setGroundTruth(gtData);
		eval.setTestSets(testSets);
		return eval;
	}

	@Override
	public ResultRenderer getRenderer(String matlabPath) throws FileNotFoundException {
		ResultRenderer renderer = new OnsetResultRenderer(); // EDIT FOR OTHER TASKS
		renderer.setWorkingDir(outputDirectory);
		renderer.setOutputDir(outputDirectory);
		renderer.setPerformMatlabStatSigTests(matlabPath != null);
		if(matlabPath != null){
			renderer.setMatlabPath(new File(matlabPath));
		}
		return renderer;
	}
}
