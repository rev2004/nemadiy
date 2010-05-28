package org.imirsel.nema.contentrepository.auth.spi;

import java.security.Principal;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.core.security.authorization.WorkspaceAccessManager;

public class NemaWorkspaceAccessManager implements WorkspaceAccessManager{

	 /**
     * @see WorkspaceAccessManager#init(Session)
     */
    public void init(Session systemSession) {
        // nothing to do
    }

    /**
     * @see WorkspaceAccessManager#close()
     */
    public void close() throws RepositoryException {
        // nothing to do.
    }

    /**
     * Always returns <code>true</code> allowing any set of principals to
     * access all workspaces.
     *
     * @see WorkspaceAccessManager#grants(java.util.Set, String)
     */
    public boolean grants(Set<Principal> principals, String workspaceName)
            throws RepositoryException {
        return true;
    }

}
