package org.imirsel.nema.webapp.service;

/**
 * An list of objects that allow certain operation.
 * It is persistent in some way. The bulk should be stable, but it allows adding new item.
 * @author gzhu1
 * @param <T>
 * @param <Index>
 */
public interface Dictionary<T, Index> {
        /**
         * Find Item with index
         */
	T find(Index id);

        /**
         * Add new entry and preserve it.
         */
	T add(T newEntry);

        /**
         * Refresh the local cache with the persistent dictionary.
         */
	void refresh();
}
