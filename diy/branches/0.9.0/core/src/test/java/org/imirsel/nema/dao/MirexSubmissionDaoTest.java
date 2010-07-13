package org.imirsel.nema.dao;

import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.MirexTask;
import org.imirsel.nema.model.User;
import org.imirsel.nema.model.MirexSubmission.SubmissionStatus;
import org.junit.Test;
import org.springframework.dao.DataAccessException;

public class MirexSubmissionDaoTest extends BaseDaoTestCase{

	private MirexSubmissionDao dao=null;
	private UserDao userDao=null;
	private MirexTaskDao mirexTaskDao=null;

	@Test
	public final void testSave() {
		assertEquals(4, dao.getAll().size());
		MirexSubmission submission=new MirexSubmission();
		User user=userDao.get(-1L);
		MirexTask task=mirexTaskDao.get(1L);
		submission.setMirexTask(task);
		submission.setUser(user);
		submission.setHashcode("ABCD1");
		submission.setStatus(SubmissionStatus.READY_FOR_RUN);
		submission=dao.save(submission);
		flush();
		assertEquals(5, dao.getAll().size());
		
		
		
		dao.remove(submission.getId());
		flush();
		assertEquals(4,dao.getAll().size());
		
		//save the same ID object twice should only add one record 
		submission=dao.save(submission);
		flush();
		submission.setStatus(SubmissionStatus.FINISHED);
		dao.save(submission);
		flush();
		assertEquals(5, dao.getAll().size());
		
		dao.remove(submission.getId());
		flush();
		assertEquals(4,dao.getAll().size());
		try {
            dao.get(submission.getId());
            fail("getSubmission didn't throw DataAccessException after removed");
        } catch (DataAccessException d) {
            assertNotNull(d);
        }
	}

	public void setMirexSubmissionDao(MirexSubmissionDao mirexSubmissionDao) {
		this.dao = mirexSubmissionDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setMirexTaskDao(MirexTaskDao mirexTaskDao) {
		this.mirexTaskDao = mirexTaskDao;
	}


}
