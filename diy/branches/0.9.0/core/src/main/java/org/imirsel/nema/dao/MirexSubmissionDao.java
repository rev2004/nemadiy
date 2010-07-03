package org.imirsel.nema.dao;

import java.util.List;

import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.Contributor;
import org.imirsel.nema.model.MirexTask;
import org.imirsel.nema.model.User;

public interface MirexSubmissionDao extends GenericDao<MirexSubmission,Long>{

	public List<MirexSubmission> getSubmissions(User user);
	public List<MirexSubmission> getSubmissions(Contributor contributor);
	public List<MirexSubmission> getSubmissions(MirexTask task);

}