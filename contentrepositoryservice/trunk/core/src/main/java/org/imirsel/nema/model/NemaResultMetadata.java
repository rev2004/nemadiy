package org.imirsel.nema.model;


import org.imirsel.nema.model.NemaResult.ResultType;


interface NemaResultMetadata{
	public String getName();
	public ResultType getResultType();
	public ResourcePath getResourcePath();
}