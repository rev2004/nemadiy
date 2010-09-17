package org.imirsel.nema.webapp.service;

public interface Dictionary<T, Index> {
	T find(Index id);

	T add(T newEntry);

	void refresh();
}
