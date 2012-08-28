package org.imirsel.nema.repository.util;


import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.imirsel.nema.analytics.evaluation.Evaluator;
import org.imirsel.nema.analytics.evaluation.ResultRenderer;
import org.imirsel.nema.analytics.evaluation.qbsht.QBSHTask2Evaluator;
import org.imirsel.nema.analytics.evaluation.qbsht.QBSHTask2ResultRenderer;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.fileTypes.MultipleTrackEvalFileType;
import org.imirsel.nema.model.fileTypes.SimpleKeyValueTextFile;
import org.imirsel.nema.model.fileTypes.QBSHTResultsTextFile;


/**
 * Multi-track evaluator for Query by Singing/Humming and Query by Tapping
 * subtasks.
 * 
 * @author cwillis
 */
public class QueryBySHTEvaluatorMain2 extends AbstractEvaluatorMain
{
	
	public QueryBySHTEvaluatorMain2(String[] args) {
		super(args, Logger.getLogger(QueryBySHTEvaluatorMain2.class.getName()));
	}
	
	public static void main(String[] args) throws Exception{
		QueryBySHTEvaluatorMain2 main = new QueryBySHTEvaluatorMain2(args);
		NemaEvaluationResultSet results = main.evaluate();
		main.resolveSubmissionDetails(results);

		main.render(results);
		
		System.out.println("--exit--");
	}

	@Override
	public List<NemaData> readGTFile(File path) throws IOException{
		MultipleTrackEvalFileType reader = new SimpleKeyValueTextFile();
		reader.getLogger().setLevel(Level.WARNING);
		return reader.readFile(path);
	}

	@Override
	public List<List<NemaData>> readResultDirectory(File path) throws IOException{
		MultipleTrackEvalFileType reader = new QBSHTResultsTextFile();
		reader.getLogger().setLevel(Level.WARNING);
		return reader.readDirectory(path, null);
	}

	@Override
	public Evaluator getEvaluator() {
		Evaluator eval = new QBSHTask2Evaluator();
		eval.setTask(task);
		eval.setDataset(dataset);
		eval.setGroundTruth(gtData);
		eval.setTestSets(testSets);
		return eval;
	}

	@Override
	public ResultRenderer getRenderer(String matlabPath) throws FileNotFoundException {
		ResultRenderer renderer = new QBSHTask2ResultRenderer();
		renderer.setWorkingDir(outputDirectory);
		renderer.setOutputDir(outputDirectory);
		renderer.setPerformMatlabStatSigTests(matlabPath != null);
		if(matlabPath != null){
			renderer.setMatlabPath(new File(matlabPath));
		}
		return renderer;
	}

}
