package org.imirsel.nema.analytics.evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.chord.ChordEvaluator;
import org.imirsel.nema.analytics.evaluation.classification.ClassificationEvaluator;
import org.imirsel.nema.analytics.evaluation.key.KeyEvaluator;
import org.imirsel.nema.analytics.evaluation.melody.MelodyEvaluator;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;

/**
 * Factory class that can setup known evaluator types keyed on the String
 * metadata id.
 *  
 * @author kris.west@gmail.com
 * @since 0.1.0
 */
public class EvaluatorFactory {
	
	private static final Map<String,Class<? extends Evaluator>> EVALUATOR_REGISTRY = new HashMap<String, Class<? extends Evaluator>>();;
	static{
		//register all the known evaluators for known metadata keys
		EVALUATOR_REGISTRY.put(NemaDataConstants.CHORD_LABEL_SEQUENCE, ChordEvaluator.class);
		EVALUATOR_REGISTRY.put(NemaDataConstants.MELODY_EXTRACTION_DATA, MelodyEvaluator.class);
		EVALUATOR_REGISTRY.put(NemaDataConstants.KEY_DETECTION_DATA, KeyEvaluator.class);
		
			//classification tasks
		EVALUATOR_REGISTRY.put(NemaDataConstants.CLASSIFICATION_ALBUM, ClassificationEvaluator.class);
		EVALUATOR_REGISTRY.put(NemaDataConstants.CLASSIFICATION_ARTIST, ClassificationEvaluator.class);
		EVALUATOR_REGISTRY.put(NemaDataConstants.CLASSIFICATION_TITLE, ClassificationEvaluator.class);
		EVALUATOR_REGISTRY.put(NemaDataConstants.CLASSIFICATION_GENRE, ClassificationEvaluator.class);
		EVALUATOR_REGISTRY.put(NemaDataConstants.CLASSIFICATION_COMPOSER, ClassificationEvaluator.class);
	}
	
	public static Evaluator getEvaluator(String metadataKey,
			NemaTask task,
            NemaDataset dataset,
            File outputDir,
            File workingDir, 
            List<NemaTrackList> trainingSets,
			List<NemaTrackList> testSets,
            boolean performMatlabStatSigTests,
            File matlabPath) throws InstantiationException, IllegalAccessException, FileNotFoundException{
		
		Class<? extends Evaluator> evalClass = EVALUATOR_REGISTRY.get(metadataKey);
		Evaluator out = evalClass.newInstance();
		out.setTask(task);
		out.setDataset(dataset);
		out.setOutputDir(outputDir);
		out.setWorkingDir(workingDir);
		out.setTrainingSets(trainingSets);
		out.setTestSets(testSets);
		out.setPerformMatlabStatSigTests(performMatlabStatSigTests);
		out.setMatlabPath(matlabPath);
		
		return out;
	}
}
