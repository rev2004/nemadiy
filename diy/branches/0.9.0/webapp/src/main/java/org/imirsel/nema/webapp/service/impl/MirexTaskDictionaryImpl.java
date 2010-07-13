/**
 * 
 */
package org.imirsel.nema.webapp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.dao.MirexTaskDao;
import org.imirsel.nema.model.MirexTask;
import org.imirsel.nema.webapp.service.MirexTaskDictionary;
import org.imirsel.nema.webapp.service.NemaServiceException;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Facade of the {@link MirexTaskDao} with simple cache and manual refresh.   
 * It implement a internal map with the id as key. 
 * @author gzhu1
 *
 */
public class MirexTaskDictionaryImpl implements MirexTaskDictionary {

	private MirexTaskDao dao;
	private Map<Long,MirexTask> map;
	
	

	public MirexTask find(Long id) {
		return map.get(id);
	}


	public void setDao(MirexTaskDao dao) {
		this.dao = dao;
		refresh();
	}


	/**
	 * Add the task into the dictionary and persist it in the database. 
	 * Note: Input task object should not has the id field set, it should be left for DAO to set 
	 * 		in order to avoid clash.  
	 */
	public void add(MirexTask task) {
		
		task=dao.save(task);
		//It is important to merge/save the task first because Id is generated during the process
		map.put(task.getId(),task);
		
	}


	public void refresh() {
		if (dao==null) {throw new NemaServiceException("MirexTaskDao needs to be set before using MirexTaskDictionary");}
		List<MirexTask> list=dao.getAll();
		map=new HashMap<Long,MirexTask>();
		for (MirexTask task:list){
			map.put(task.getId(),task);
		}
	}



	public List<MirexTask> getAll() {
		refresh();
		List<MirexTask> list=new ArrayList<MirexTask>(map.values());
		Collections.sort(list);
		return list;
	}


	public List<MirexTask> findAllActive() {
		refresh();
		List<MirexTask> list=new ArrayList<MirexTask>();
		for (MirexTask task:map.values()){
			if (task.isActive()) {list.add(task);}
		}
		Collections.sort(list);
		return list;
	}
	
	

}
