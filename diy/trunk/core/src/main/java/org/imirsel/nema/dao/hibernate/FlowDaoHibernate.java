package org.imirsel.nema.dao.hibernate;

import java.util.List;

import org.imirsel.nema.dao.FlowDao;
import org.imirsel.nema.model.Flow;


/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Flow objects.
 */
public class FlowDaoHibernate extends GenericDaoHibernate<Flow, Long> implements FlowDao {

    public FlowDaoHibernate() {
        super(Flow.class);
    }
    
    @SuppressWarnings("unchecked")
	public List<Flow> getFlowsByCreatorId(Long userId) {
        return getHibernateTemplate().find("from Flow where creatorId=?", userId);
	}
    
    @SuppressWarnings("unchecked")
    public List<Flow> getFlowTemplates() {
    	return getHibernateTemplate().find("from Flow where template=?", true);
    }

}
