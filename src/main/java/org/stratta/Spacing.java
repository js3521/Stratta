package org.stratta;

import java.awt.GridLayout;
import java.awt.Insets;

/**
 *
 * @author <a href="mailto:joshuas3521@gmail.com">Joshua Swank</a>
 */
public final class Spacing {
    public static final int DEFAULT_GAP = 6, SMALL_GAP = 4, LARGE_GAP = 10;
    
    public static Insets getDefaultInsets() {
        return new Insets(DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP);
    }
    
    public static Insets getBottomInsets(boolean far) {
        return new Insets(far ? LARGE_GAP : 0, DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP);
    }
    
    public static Insets getLeftInsets(boolean far) {
        return new Insets(DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP, far ? LARGE_GAP : 0);
    }
    
    public static Insets getRightInsets(boolean far) {
        return new Insets(DEFAULT_GAP, far ? LARGE_GAP : 0, DEFAULT_GAP, DEFAULT_GAP);
    }
    
    public static Insets getTopInsets(boolean far) {
        return new Insets(DEFAULT_GAP, DEFAULT_GAP, far ? LARGE_GAP : 0, DEFAULT_GAP);
    }
    
    public static GridLayout getHorizontalLayout(int cols) {
        return new GridLayout(1, cols, DEFAULT_GAP, 0);
    }
    
    private Spacing() { }
}
