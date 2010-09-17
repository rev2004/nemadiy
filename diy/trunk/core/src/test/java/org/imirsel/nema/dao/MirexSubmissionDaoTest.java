package org.imirsel.nema.dao;

import java.util.Date;
import java.util.List;

import org.imirsel.nema.model.MirexNote;
import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.MirexTask;
import org.imirsel.nema.model.User;
import org.imirsel.nema.model.MirexNote.NoteType;
import org.imirsel.nema.model.MirexSubmission.SubmissionStatus;
import org.junit.Test;
import org.springframework.dao.DataAccessException;

public class MirexSubmissionDaoTest extends BaseDaoTestCase{

	private MirexSubmissionDao dao=null;
	private UserDao userDao=null;
	private MirexTaskDao mirexTaskDao=null;

	@Test
	public final void testSaveAndRemove() {
		assertEquals(4, dao.getAll().size());
		MirexSubmission submission=new MirexSubmission();
		User user=userDao.get(-1L);
		MirexTask task=mirexTaskDao.get(1L);
		MirexNote note=new MirexNote();
		note.setAuthor(user);
		note.setContent("test");
		note.setSubmission(submission);
		note.setType(NoteType.PUBLIC);
		submission.setMirexTask(task);
		submission.setUser(user);
		submission.setHashcode("ABCD1");
		submission.setStatus(SubmissionStatus.READY_FOR_RUN);
		submission.addNote(note);
		submission=dao.save(submission);
		flush();
		assertEquals(5, dao.getAll().size());
		
		
		
		dao.remove(submission.getId());
		flush();
		assertEquals(4,dao.getAll().size());
		
		//save the same ID object twice should only add one record 
		submission=dao.save(submission);
		Date createTime=submission.getCreateTime();
		Date updateTime=submission.getUpdateTime();
		assertNotNull(createTime);
		assertNotNull(updateTime);
		flush();
		submission.setStatus(SubmissionStatus.FINISHED);
		submission=dao.save(submission);
		assertNotSame(updateTime,submission.getUpdateTime());
		assertEquals(createTime,submission.getCreateTime());
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
	
	
	@Test
	public void testFindByHashcodeBeginning(){
		List<MirexSubmission> list=dao.findByHashcodeBeginning("ABC");
		assertEquals(2, list.size());
		list=dao.findByHashcodeBeginning("GZ");
		assertEquals(1, list.size());
		list=dao.findByHashcodeBeginning("ABCD");
		assertEquals(0, list.size());
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
