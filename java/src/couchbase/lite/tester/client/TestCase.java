package couchbase.lite.tester.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TestCase extends junit.framework.TestCase {
    private final Client _client;

    public TestCase() {
        URL baseUrl = null;

        // Try to load the base url from config.properties.
        String filename = "config.properties";
        InputStream in = TestCase.class.getClassLoader().getResourceAsStream(filename);
        try {
            if (in != null) {
                Properties properties = new Properties();

                properties.load(in);

                String baseUrlString = properties.getProperty("baseUrl");
                if (baseUrlString != null) {
                    baseUrl = new URL(baseUrlString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // If no base url was specified then use a default.
        if (baseUrl == null) {
            try {
                baseUrl = new URL("http://localhost:3100");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        _client = new Client(baseUrl);
    }

    /* ------------ */
    /* - Database - */
    /* ------------ */

    public MemoryPointer database_create(String name) {
        Args args = new Args();
        args.setString("name", name);

        return _client.invokeMethod("database_create", args);
    }

    public String database_getName(MemoryPointer database) {
        Args args = new Args();
        args.setMemoryPointer("database", database);

        return _client.invokeMethod("database_getName", args);
    }

    public MemoryPointer database_getDocument(MemoryPointer database, String id) {
        Args args = new Args();
        args.setMemoryPointer("database", database);
        args.setString("id", id);

        return _client.invokeMethod("database_getDocument", args);
    }

    public Map<String, Map<String, Object>> database_getDocuments(MemoryPointer database, List<String> ids) {
        Args args = new Args();
        args.setMemoryPointer("database", database);
        args.setArray("ids", ids);

        return _client.invokeMethod("database_getDocuments", args);
    }

    public void database_save(MemoryPointer database, MemoryPointer document) {
        Args args = new Args();
        args.setMemoryPointer("database", database);
        args.setMemoryPointer("document", document);

        _client.invokeMethod("database_save", args);
    }

    public void database_saveDocuments(MemoryPointer database, Map<String, Map<String, Object>> documents) {
        Args args = new Args();
        args.setMemoryPointer("database", database);
        // HACK: IntelliJ incorrectly flags Map<String, Map<String, Object>> as a compile error
        // so wrapping in a new HashMap<String, Object> to work around that issue.
        args.setDictionary("documents", new HashMap<>(documents));

        _client.invokeMethod("database_saveDocuments", args);
    }

    public void database_delete(MemoryPointer database, MemoryPointer document) {
        Args args = new Args();
        args.setMemoryPointer("database", database);
        args.setMemoryPointer("document", document);

        _client.invokeMethod("database_delete", args);
    }

    public boolean database_contains(MemoryPointer database, String id) {
        Args args = new Args();
        args.setMemoryPointer("database", database);
        args.setString("id", id);

        return _client.invokeMethod("database_contains", args);
    }

    public MemoryPointer database_addChangeListener(MemoryPointer database) {
        Args args = new Args();
        args.setMemoryPointer("database", database);

        return _client.invokeMethod("database_addChangeListener", args);
    }

    public void database_removeChangeListener(MemoryPointer database, MemoryPointer changeListener) {
        Args args = new Args();
        args.setMemoryPointer("database", database);
        args.setMemoryPointer("changeListener", changeListener);

        _client.invokeMethod("database_removeChangeListener", args);
    }

    public int databaseChangeListener_changesCount(MemoryPointer changeListener) {
        Args args = new Args();
        args.setMemoryPointer("changeListener", changeListener);

        return _client.invokeMethod("databaseChangeListener_changesCount", args);
    }

    public MemoryPointer databaseChangeListener_getChange(MemoryPointer changeListener, int index) {
        Args args = new Args();
        args.setMemoryPointer("changeListener", changeListener);
        args.setInt("index", index);

        return _client.invokeMethod("databaseChangeListener_getChange", args);
    }

    public String databaseChange_getDocumentId(MemoryPointer change) {
        Args args = new Args();
        args.setMemoryPointer("change", change);

        return _client.invokeMethod("databaseChange_getDocumentId", args);
    }

    public boolean databaseChange_isDelete(MemoryPointer change) {
        Args args = new Args();
        args.setMemoryPointer("change", change);

        return _client.invokeMethod("databaseChange_isDelete", args);
    }

    /* ------------ */
    /* - Document - */
    /* ------------ */

    public MemoryPointer document_create() {
        return _client.invokeMethod("document_create");
    }

    public MemoryPointer document_create(Map<String, Object> dictionary) {
        Args args = new Args();
        args.setDictionary("dictionary", dictionary);

        return _client.invokeMethod("document_create", args);
    }

    public MemoryPointer document_create(String id) {
        Args args = new Args();
        args.setString("id", id);

        return _client.invokeMethod("document_create", args);
    }

    public MemoryPointer document_create(String id, Map<String, Object> dictionary) {
        Args args = new Args();
        args.setString("id", id);
        args.setDictionary("dictionary", dictionary);

        return _client.invokeMethod("document_create", args);
    }

    public String document_getId(MemoryPointer document) {
        Args args = new Args();
        args.setMemoryPointer("document", document);

        return _client.invokeMethod("document_getId", args);
    }

    public String document_getString(MemoryPointer document, String property) {
        Args args = new Args();
        args.setMemoryPointer("document", document);
        args.setString("property", property);

        return _client.invokeMethod("document_getString", args);
    }

    public void document_setString(MemoryPointer document, String property, String string) {
        Args args = new Args();
        args.setMemoryPointer("document", document);
        args.setString("property", property);
        args.setString("string", string);

        _client.invokeMethod("document_setString", args);
    }

    public Map<String, Object> document_getDictionary(MemoryPointer document, String property) {
        Args args = new Args();
        args.setMemoryPointer("document", document);
        args.setString("property", property);

        return _client.invokeMethod("document_getDictionary", args);
    }

    public void document_setDictionary(MemoryPointer document, String property, Map<String, Object> dictionary) {
        Args args = new Args();
        args.setMemoryPointer("document", document);
        args.setString("property", property);
        args.setDictionary("dictionary", dictionary);

        _client.invokeMethod("document_setDictionary", args);
    }

    public List document_getArray(MemoryPointer document, String property) {
        Args args = new Args();
        args.setMemoryPointer("document", document);
        args.setString("property", property);

        return _client.invokeMethod("document_getArray", args);
    }

    public void document_setArray(MemoryPointer document, String property, List array) {
        Args args = new Args();
        args.setMemoryPointer("document", document);
        args.setString("property", property);
        args.setArray("array", array);

        _client.invokeMethod("document_setArray", args);
    }

    public Map<String, Object> document_toMap(MemoryPointer document) {
        Args args = new Args();
        args.setMemoryPointer("document", document);

        return _client.invokeMethod("document_toMap", args);
    }

    /* ---------- */
    /* - Memory - */
    /* ---------- */

    public void release(MemoryPointer object) {
        _client.release(object);
    }
}
