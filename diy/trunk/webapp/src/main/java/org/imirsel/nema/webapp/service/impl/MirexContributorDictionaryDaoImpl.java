package org.imirsel.nema.webapp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.dao.ProfileDao;
import org.imirsel.nema.dao.MirexTaskDao;
import org.imirsel.nema.model.Profile;
import org.imirsel.nema.model.MirexTask;
import org.imirsel.nema.webapp.service.Dictionary;
import org.imirsel.nema.webapp.service.MirexContributorDictionary;
import org.imirsel.nema.webapp.service.NemaServiceException;

/**
 * Implement {@link MirexContributorDictionary} with a database back-end via
 * {@link ProfileDao}.
 * @author gzhu1
 */
public class MirexContributorDictionaryDaoImpl implements
		MirexContributorDictionary {
	private ProfileDao dao;
	private Map<Long, Profile> map;

	/**
	 * {@inheritDoc }
	 */
	public Profile find(Long id) {
		return map.get(id);
	}

        /**
	 * {@inheritDoc }
	 */
	public void setDao(ProfileDao dao) {
		this.dao = dao;
		refresh();
	}

        /**
	 * {@inheritDoc }
         *  Note: Input task object should not has the id field set, it should be left for DAO to set
	 * 		in order to avoid clash.  
	 */
	public Profile add(Profile contributor) {
		contributor = dao.save(contributor);
		map.put(contributor.getId(), contributor);
		return contributor;
	}

	/**
	 * {@inheritDoc }
	 */
        public void refresh() {
		if (dao == null) {
			throw new NemaServiceException("Dao needs to be set before using "
					+ this.getClass().getSimpleName());
		}
		List<Profile> list = dao.getAllDistinct();
		map = new HashMap<Long, Profile>();
		for (Profile element : list) {
			map.put(element.getId(), element);
		}
	}

        /**
	 * {@inheritDoc }
	 */
	public List<Profile> findSimilar(String str) {
		return dao.findSimilar(str);
	}

}
