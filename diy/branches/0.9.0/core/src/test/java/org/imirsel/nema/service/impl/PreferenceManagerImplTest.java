package org.imirsel.nema.service.impl;

import org.imirsel.nema.dao.PreferenceValueDao;
import org.imirsel.nema.service.PreferenceValueManager;
import org.imirsel.nema.service.impl.BaseManagerMockTestCase;
import org.imirsel.nema.service.impl.PreferenceValueManagerImpl;
import org.junit.Before;
import org.junit.Test;


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
