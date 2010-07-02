package org.imirsel.nema.dao;

import java.util.List;

import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.Contributor;

public interface ContributorDao extends GenericDao<Contributor,Long>{

	public  List<Contributor> getContributors(MirexSubmission submission);

}