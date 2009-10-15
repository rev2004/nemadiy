package org.imirsel.nema.service.impl;

import org.imirsel.nema.dao.FlowDao;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.service.FlowManager;

public class FlowManagerImpl extends GenericManagerImpl<Flow, Long> implements FlowManager {

	public FlowManagerImpl(FlowDao genericDao) {
		super(genericDao);
	}

}
