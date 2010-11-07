package org.imirsel.nema.dao;

import java.util.List;

import org.imirsel.nema.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;

public class ContributorDaoTest extends BaseDaoTestCase {
	ProfileDao contributorDao;

	@Autowired
	public void setContributorDao(ProfileDao contributorDao) {
		this.contributorDao = contributorDao;
	}
	
	public void testSimilarSearch(){
		List<Profile> list=contributorDao.findSimilar("imirsel");
		assertEquals(3, list.size());
		list=contributorDao.findSimilar("Nema");
		assertEquals(2, list.size());
		list=contributorDao.findSimilar("UI");
		assertEquals(2, list.size());
		list=contributorDao.findSimilar("UIUC");
		assertEquals(1, list.size());
		list=contributorDao.findSimilar("Purdue");
		assertEquals(0,list.size());
	}
}
