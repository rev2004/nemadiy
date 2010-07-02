package org.imirsel.nema.dao;

import java.util.List;

import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.ParticipantProfile;

public interface ContributorDao extends GenericDao<ParticipantProfile,Long>{

	public abstract List<ParticipantProfile> getContributors(MirexSubmission submission);

}