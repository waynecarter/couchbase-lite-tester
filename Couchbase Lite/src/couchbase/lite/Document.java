package couchbase.lite;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Document {
    private final String _id;
    private final Map<String, Object> _data;

    public Document() {
        this(null, null);
    }

    public Document(Map<String, Object> data) {
        this(null, data);
    }

    public Document(String id) {
        this(id, null);
    }

    public Document(String id, Map<String, Object> data) {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }

        if (data == null) {
            data = new HashMap<>();
        }

        _id = id;
        _data = data;
    }

    public String getId() {
        return _id;
    }

    public String getString(String property) {
        return (String)_data.get(property);
    }

    public void setString(String property, String string) {
        _data.put(property, string);
    }
}
