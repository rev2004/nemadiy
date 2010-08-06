package org.imirsel.nema.repository.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.imirsel.nema.analytics.evaluation.Evaluator;
import org.imirsel.nema.analytics.evaluation.ResultRenderer;
import org.imirsel.nema.analytics.evaluation.tagsClassification.TagClassificationEvaluator;
import org.imirsel.nema.analytics.evaluation.tagsClassification.TagClassificationResultRenderer;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.fileTypes.MultipleTrackEvalFileType;
import org.imirsel.nema.model.fileTypes.TagClassificationTextFile;


/**
 * 
 * @author kris.west@gmail.com
 * @since 0.4.0
 */
public class TagClassificationEvaluatorMain extends AbstractEvaluatorMain{
	
	public TagClassificationEvaluatorMain(String[] args) {
		super(args, Logger.getLogger(TagClassificationEvaluatorMain.class.getName()));
	}
	
	public static void main(String[] args) throws Exception{
		TagClassificationEvaluatorMain main = new TagClassificationEvaluatorMain(args);
		NemaEvaluationResultSet results = main.evaluate();
		main.resolveSubmissionDetails(results);
		main.render(results);
		
		System.out.println("--exit--");
	}

	@Override
	public List<NemaData> readGTFile(File path) throws IOException{
		MultipleTrackEvalFileType reader = new TagClassificationTextFile();
		reader.getLogger().setLevel(Level.WARNING);
		return reader.readFile(path);
	}

	@Override
	public List<List<NemaData>> readResultDirectory(File path) throws IOException{
		MultipleTrackEvalFileType reader = new TagClassificationTextFile();
		reader.getLogger().setLevel(Level.WARNING);
		return reader.readDirectory(path, null);
	}

	@Override
	public Evaluator getEvaluator() {
		Evaluator eval = new TagClassificationEvaluator();
		eval.setTask(task);
		eval.setDataset(dataset);
		eval.setGroundTruth(gtData);
		eval.setTestSets(testSets);
		return eval;
	}

	@Override
	public ResultRenderer getRenderer(String matlabPath) throws FileNotFoundException {
		ResultRenderer renderer = new TagClassificationResultRenderer();
		renderer.setWorkingDir(outputDirectory);
		renderer.setOutputDir(outputDirectory);
		renderer.setPerformMatlabStatSigTests(matlabPath != null);
		if(matlabPath != null){
			renderer.setMatlabPath(new File(matlabPath));
		}
		return renderer;
	}

}
