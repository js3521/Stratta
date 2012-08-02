package org.stratta.io;

import com.google.common.base.Preconditions;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.stratta.model.DataModelProviders;

public final class FileAccessor {

    private static final String _HISTORY_FILE = "history.dat";
    private static final String _CATALOGS_FILE = "catalogs.dat";

    public FileAccessor() {
    }

    public void writeCatalogs(DataModelProviders.ProviderCatalogs catalogs) throws IOException {
        writeObject(_CATALOGS_FILE, catalogs);
    }

    public DataModelProviders.ProviderCatalogs readCatalogs() {
        return readObject(_CATALOGS_FILE, DataModelProviders.ProviderCatalogs.class);
    }

    public <T> T readObject(String file, Class<T> c) {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(c);

        Object obj = null;

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(
                    file));

            try {
                obj = in.readObject();
            } catch (ClassNotFoundException e) {
                // Ignore
            }

            in.close();
        } catch (IOException e) {
            // Ignore
        }

        return (T) obj;
    }

    public void writeObject(String file, Serializable obj) throws IOException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(obj);

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
                file));

        out.writeObject(obj);

        out.close();
    }
}
