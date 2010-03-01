package org.imirsel.nema.test.matchers;



import java.io.File;
import java.io.IOException;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.apache.commons.io.FileUtils;



public class FileContentEquals <T> extends BaseMatcher<T>  {
	
	private File file;
	
	public  FileContentEquals(File file){
		this.file = file;
	}
	

	public boolean matches(Object object) {
	if(!(object instanceof File)){
		return false;
	}
	File file1 = (File)object;
	boolean result = false;
	try {
		result=FileUtils.contentEquals(file, file1);
	} catch (IOException e) {
		return false;
	}
	return result;
	}


	public void describeTo(Description description) {
		description.appendText("compare the file contents " + file.getAbsolutePath());
	}

}
