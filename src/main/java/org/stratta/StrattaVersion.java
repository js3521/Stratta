package org.stratta;

public final class StrattaVersion {
	public static final int MAJOR = 0;
	public static final String MINOR = ".1";

	public static final String TITLE = String.format("Stratta %d%s",
			StrattaVersion.MAJOR, StrattaVersion.MINOR);

	private StrattaVersion() {
	}
}