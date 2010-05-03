package org.imirsel.nema.analytics.evaluation.onset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.imirsel.nema.analytics.evaluation.SingleTrackEvalFileTypeImpl;
import org.imirsel.nema.model.NemaData;

/**
 * Onset detection text file type.
 * 
 * @author afe405@gmail.com
 * @author kris.west@gmail.com
 * @since 0.1.0
 */
public class OnsetTextFile extends SingleTrackEvalFileTypeImpl {

	public static final String TYPE_NAME = "Onset text file";
	
	/**
	 * Constructor
	 */
	public OnsetTextFile() {
		super(TYPE_NAME);
	}
	
	@Override
	public NemaData readFile(File theFile) throws IllegalArgumentException,
			FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeFile(File theFile, NemaData data)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		// TODO Auto-generated method stub

	}

}
