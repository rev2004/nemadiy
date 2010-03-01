package org.imirsel.nema.test.matchers;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;



public class FileContentContains <T> extends BaseMatcher<T>  {
	
	private String content;
	
	public  FileContentContains(String content){
		this.content = content;
	}
	

	public boolean matches(Object object) {
	File file = (File)object;
	if(content==null){
		return false;
	}
	if(!file.exists() || !file.canRead()){
		return false;
	}
	
	try {
		String fileContent=FileUtils.readFileToString(file);
		if(fileContent.indexOf(content)!=-1){
			return true;
		}
	} catch (IOException e) {
	}
	return false;
	}


	public void describeTo(Description description) {
		description.appendText(" file contains " + content);
	}

}
