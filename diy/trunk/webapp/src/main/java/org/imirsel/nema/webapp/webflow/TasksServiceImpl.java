package org.imirsel.nema.webapp.webflow;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.model.Role;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.controller.JobController;

/**
 * 
 * @author gzhu1
 *
 */
public class TasksServiceImpl implements TasksService {

	static private Log logger=LogFactory.getLog(JobController.class);
	FlowService flowService;
	UserManager userManager;
	
	public void setFlowService(FlowService flowService){
		this.flowService=flowService;
	}
	
	public void setUserManager(UserManager userManager){
		this.userManager=userManager;
	}
	

	public boolean fillFlow() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean run(Flow flow) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean testRun(Flow flow) {
		// TODO Auto-generated method stub
		return true;
	}
	public String[] getRoles(){
		Set<Role> roleList=this.userManager.getCurrentUser().getRoles();
		int size=roleList.size();
		
		String[] roles = new String[size];
		int i=0;
		for(Role role:roleList){
			roles[i]= role.getName();
			i++;
		}
		return roles;
	}
	public int test(String input){
		logger.debug(input);
		return 1;
	}

	public Map<String, String> fillDefaultParameter(Flow flow) {

		Map<String,String> parameters=new HashMap<String,String>();
		List<Component> componentList=flowService.getComponents(flow.getUri());
		Collections.sort(componentList);
		logger.info("componentList: " + componentList.size());
		for(int i=0;i<componentList.size();i++){
			Map<String, Property> m=flowService.getComponentPropertyDataType(componentList.get(i), flow.getUri());
			for (Entry<String,Property> entry:m.entrySet()){
				parameters.put(entry.getKey(),entry.getValue().getDefaultValue().toString());
			}		
		}		
		return parameters;
	}
}
