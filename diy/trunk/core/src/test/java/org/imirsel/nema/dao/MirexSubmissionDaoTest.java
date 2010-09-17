package org.imirsel.nema.dao;

import org.imirsel.nema.model.MirexSubmission;
import org.junit.Test;

public class MirexSubmissionDaoTest extends BaseDaoTestCase{

	private MirexSubmissionDao dao;
	@Test
	public final void testGetSubmissions() {
		assertTrue(true);
	}

	@Test
	public final void testSave() {
		MirexSubmission submission=new MirexSubmission();
		submission.setHashcode("ABCD1");
		dao.save(submission);
		assertEquals(1, dao.getAll().size());
	}

	public void setMirexSubmissionDao(MirexSubmissionDao mirexSubmissionDao) {
		this.dao = mirexSubmissionDao;
	}


}
