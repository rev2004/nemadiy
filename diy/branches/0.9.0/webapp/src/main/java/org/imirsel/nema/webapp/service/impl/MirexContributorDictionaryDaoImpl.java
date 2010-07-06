package org.imirsel.nema.webapp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.dao.ContributorDao;
import org.imirsel.nema.dao.MirexTaskDao;
import org.imirsel.nema.model.Contributor;
import org.imirsel.nema.model.MirexTask;
import org.imirsel.nema.webapp.service.Dictionary;
import org.imirsel.nema.webapp.service.MirexContributorDictionary;
import org.imirsel.nema.webapp.service.NemaServiceException;

public class MirexContributorDictionaryDaoImpl implements
		MirexContributorDictionary {
	private ContributorDao dao;
	private Map<Long, Contributor> map;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.imirsel.nema.webapp.service.MirexTaskDictionary#find(long)
	 */
	public Contributor find(Long id) {
		return map.get(id);
	}

	public void setDao(ContributorDao dao) {
		this.dao = dao;
		refresh();
	}

	public void add(Contributor contributor) {
		contributor = dao.save(contributor);
		map.put(contributor.getId(), contributor);
	}

	public void refresh() {
		if (dao == null) {
			throw new NemaServiceException("Dao needs to be set before using "
					+ this.getClass().getSimpleName());
		}
		List<Contributor> list = dao.getAllDistinct();
		map = new HashMap<Long, Contributor>();
		for (Contributor element : list) {
			map.put(element.getId(), element);
		}
	}

	public List<Contributor> findSimilar(String str) {
		return dao.findSimilar(str);
	}

}
