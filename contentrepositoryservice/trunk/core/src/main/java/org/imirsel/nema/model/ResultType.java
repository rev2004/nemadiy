package org.imirsel.nema.model;

/**
 * ResultType enum
 * 
 * @author kumaramit01
 * @since 0.6.0
 */
public enum ResultType {
	FILE(-1), DIR(0);

	private final int code;

	private ResultType(int code) {
		this.code = code;
	}

	/**
	 * @return integer code
	 */
	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		String name = "file";
		switch (code) {

		case -1: {
			name = "file";
			break;
		}
		case 0: {
			name = "dir";
			break;
		}

		}
		return name;
	}

	/**
	 * Return the result type
	 * 
	 * @param code
	 * @return Result type enum object
	 */
	static public ResultType toResultType(int code) {
		ResultType status = ResultType.FILE;
		switch (code) {
		case -1: {
			status = ResultType.FILE;
			break;
		}
		case 0: {
			status = ResultType.DIR;
			break;
		}
		}
		return status;
	}

}
