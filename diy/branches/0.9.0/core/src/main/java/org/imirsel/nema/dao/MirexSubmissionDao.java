package org.imirsel.nema.dao;

import java.util.List;

import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.User;

public interface MirexSubmissionDao extends GenericDao<MirexSubmission,Long>{

	public abstract List<MirexSubmission> getSubmissions(User user);

}