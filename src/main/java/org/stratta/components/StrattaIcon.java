package org.stratta.components;

import java.net.URL;
import javax.swing.ImageIcon;
import org.stratta.Stratta;

/**
 *
 * @author <a href="mailto:joshuas3521@gmail.com">Joshua Swank</a>
 */
public final class StrattaIcon extends ImageIcon {
    public static final StrattaIcon CONNECT = new StrattaIcon("connect.png"),
            DISCONNECT = new StrattaIcon("disconnect.png");
    
    private static final String _baseDir = "/images";
    
    private StrattaIcon(String file) {
        super(getResourceURL(String.format("%s/%s", _baseDir, file)));
    }
    
    private static URL getResourceURL(String name) {
        return Stratta.class.getResource(name);
    }
}
