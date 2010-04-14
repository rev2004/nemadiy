/**
 * 
 */
package org.imirsel.nema.analytics.util.process;

class StringCommandArgument implements CommandArgument{
	private String string;
	boolean followedBySpace;
	
	public StringCommandArgument(String string, boolean followedBySpace) {
		this.string = string;
		this.followedBySpace = followedBySpace;
	}
	
	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public String toConfigString() {
		return string;
	}
	
	public String toFormattedString() {
		return string;
	}
	
	public boolean followedBySpace() {
		return followedBySpace;
	}
}