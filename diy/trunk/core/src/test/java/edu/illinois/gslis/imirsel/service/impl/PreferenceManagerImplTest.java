package edu.illinois.gslis.imirsel.service.impl;

import org.junit.Before;
import org.junit.Test;

import edu.illinois.gslis.imirsel.dao.PreferenceValueDao;
import edu.illinois.gslis.imirsel.service.PreferenceValueManager;

public class PreferenceManagerImplTest extends BaseManagerMockTestCase {
	
	private PreferenceValueManager preferenceManager = new PreferenceValueManagerImpl();
	private PreferenceValueDao preferenceValueDao;
	
    @Before
    public void setUp() throws Exception {
    	preferenceValueDao = context.mock(PreferenceValueDao.class);
    	preferenceManager.setPreferenceValueDao(preferenceValueDao);
    }

    
    @Test
    public void testMe(){
    	
    }
}
