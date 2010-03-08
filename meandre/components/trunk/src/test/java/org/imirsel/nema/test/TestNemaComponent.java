package org.imirsel.nema.test;

import org.imirsel.nema.components.NemaComponent;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

public class TestNemaComponent extends NemaComponent{

	@Override
	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
	}

	public void initialize(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
	}
	
	
	public static void main(String args[]){
		TestNemaComponent tnc = new TestNemaComponent();
		ComponentContextProperties ccp=null;
		try {
			tnc.initialize(ccp);
		} catch (ComponentExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ComponentContextException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
