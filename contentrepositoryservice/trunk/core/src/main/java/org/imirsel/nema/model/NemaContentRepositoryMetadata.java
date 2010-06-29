package org.imirsel.nema.model;


import org.imirsel.nema.model.ResultType;

/**Interface implemented by NemaResult
 * 
 * @author kumaramit01
 * @since 0.6.0
 * 
 */
interface NemaContentRepositoryMetadata{
	public String getName();
	public ResultType getResultType();
	public ResourcePath getResourcePath();
}