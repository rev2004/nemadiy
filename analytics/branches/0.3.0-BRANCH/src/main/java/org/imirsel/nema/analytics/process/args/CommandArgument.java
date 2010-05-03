/**
 * 
 */
package org.imirsel.nema.analytics.process.args;

public interface CommandArgument{
	public String toConfigString();
	public String toFormattedString();
	public boolean followedBySpace();
}