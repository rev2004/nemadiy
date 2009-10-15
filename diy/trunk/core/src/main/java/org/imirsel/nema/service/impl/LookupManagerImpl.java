package org.imirsel.nema.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.DefaultPreferenceInitializer;
import org.imirsel.nema.dao.LookupDao;
import org.imirsel.nema.model.LabelValue;
import org.imirsel.nema.model.Role;
import org.imirsel.nema.service.LookupManager;


/**
 * Implementation of LookupManager interface to talk to the persistence layer.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class LookupManagerImpl extends UniversalManagerImpl implements LookupManager {
    private LookupDao dao;

    /**
     * Method that allows setting the DAO to talk to the data store with.
     * @param dao the dao implementation
     */
    public void setLookupDao(LookupDao dao) {
        super.dao = dao;
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllRoles() {
        List<Role> roles = dao.getRoles();
        List<LabelValue> list = new ArrayList<LabelValue>();

        for (Role role1 : roles) {
            list.add(new LabelValue(role1.getName(), role1.getName()));
        }

        return list;
    }

	public Map<String, String> getAllDefaultPreferences() {
		return DefaultPreferenceInitializer.getDefaultPreferences();
	}


}
