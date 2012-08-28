package org.imirsel.nema.repository.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.imirsel.nema.analytics.evaluation.Evaluator;
import org.imirsel.nema.analytics.evaluation.ResultRenderer;
import org.imirsel.nema.analytics.evaluation.qbsht.QBSHTEvaluator;
import org.imirsel.nema.analytics.evaluation.qbsht.QBSHTResultRenderer;
import org.imirsel.nema.analytics.evaluation.tagsClassification.TagAffinityEvaluator;
import org.imirsel.nema.analytics.evaluation.tagsClassification.TagAffinityResultRenderer;
import org.imirsel.nema.analytics.evaluation.tagsClassification.TagClassificationEvaluator;
import org.imirsel.nema.analytics.evaluation.tagsClassification.TagClassificationResultRenderer;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.fileTypes.MultipleTrackEvalFileType;
import org.imirsel.nema.model.fileTypes.SimpleKeyValueTextFile;
import org.imirsel.nema.model.fileTypes.QBSHTResultsTextFile;
import org.imirsel.nema.model.fileTypes.TagAffinityTextFile;
import org.imirsel.nema.model.fileTypes.TagClassificationTextFile;


/**
 * Multi-track evaluator for Query by Singing/Humming and Query by Tapping
 * subtasks.
 * 
 * @author cwillis
 */
public class QueryBySHTEvaluatorMain extends AbstractEvaluatorMain
{
	
	public QueryBySHTEvaluatorMain(String[] args) {
		super(args, Logger.getLogger(QueryBySHTEvaluatorMain.class.getName()));
	}
	
	public static void main(String[] args) throws Exception{
		QueryBySHTEvaluatorMain main = new QueryBySHTEvaluatorMain(args);
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
		Evaluator eval = new QBSHTEvaluator();
		eval.setTask(task);
		eval.setDataset(dataset);
		eval.setGroundTruth(gtData);
		eval.setTestSets(testSets);
		return eval;
	}

	@Override
	public ResultRenderer getRenderer(String matlabPath) throws FileNotFoundException {
		ResultRenderer renderer = new QBSHTResultRenderer();
		renderer.setWorkingDir(outputDirectory);
		renderer.setOutputDir(outputDirectory);
		renderer.setPerformMatlabStatSigTests(matlabPath != null);
		if(matlabPath != null){
			renderer.setMatlabPath(new File(matlabPath));
		}
		return renderer;
	}

}
