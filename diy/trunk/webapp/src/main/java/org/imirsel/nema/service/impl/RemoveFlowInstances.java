package org.imirsel.nema.service.impl;

import java.util.Set;

import org.imirsel.meandre.client.TransmissionException;

import com.hp.hpl.jena.rdf.model.Resource;

public class RemoveFlowInstances {
	
	public static void main(String args[]){
		 MeandreProxyWrapper mpw = new  MeandreProxyWrapper();
		 mpw.init();
		 try {
			Set<Resource> flows= mpw.getAvailableFlows();
			
			for(Resource resource:flows){
				int len=resource.getURI().split("/").length;
				String flowuri=resource.getURI();
				String flowname=resource.getURI().split("/")[len-1];
				int vs = flowname.length()-13;
				if(vs>0){
					String  val = flowname.substring(vs);
					try{
						Long v = Long.parseLong(val);
						System.out.println(v);
						boolean b=mpw.removeResource(flowuri);
						System.out.println("Removed: " + flowuri + " " + b);
					}catch(Exception ex){
					
					}
				}
			}
			
		} catch (TransmissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
	}

}
