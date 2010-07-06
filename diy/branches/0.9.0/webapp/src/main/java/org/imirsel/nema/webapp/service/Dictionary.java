package org.imirsel.nema.webapp.service;

public interface Dictionary<T, Index> {
	T find(Index id);

	void add(T task);

	void refresh();
}
