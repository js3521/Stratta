package org.stratta.components;

import java.awt.Insets;

public class StrattaInsets extends Insets {
	public static final int DEFAULT_GAP = 6, SMALL_GAP = 4, LARGE_GAP = 10;

	public static final StrattaInsets DEFAULT = new StrattaInsets(true, true,
			true, true);
	public static final StrattaInsets EMPTY = new StrattaInsets(0, 0, 0, 0);
	public static final StrattaInsets TOP_NEAR = new StrattaInsets(
			DEFAULT_GAP, DEFAULT_GAP, 0, DEFAULT_GAP);
	public static final StrattaInsets TOP_FAR = new StrattaInsets(
			DEFAULT_GAP, DEFAULT_GAP, LARGE_GAP, DEFAULT_GAP);
	public static final StrattaInsets BOTTOM_NEAR = new StrattaInsets(
			SMALL_GAP, DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP);
	public static final StrattaInsets BOTTOM_FAR = new StrattaInsets(
			LARGE_GAP, DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP);

	public StrattaInsets(boolean top, boolean left, boolean bottom,
			boolean right) {
		super(top ? DEFAULT_GAP : 0, left ? DEFAULT_GAP : 0,
				bottom ? DEFAULT_GAP : 0, right ? DEFAULT_GAP : 0);
	}

	public StrattaInsets(int top, int left, int bottom, int right) {
		super(top, left, bottom, right);
	}
}
