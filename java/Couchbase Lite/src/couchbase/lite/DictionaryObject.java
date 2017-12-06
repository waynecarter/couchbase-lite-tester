package couchbase.lite;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DictionaryObject {
    private final Map<String, Object> _data;

    public DictionaryObject() {
        this(new HashMap<>());
    }

    public DictionaryObject(Map<String, Object> data) {
        _data = data;
    }

    public Map<String, Object> toMap() {
        return Collections.unmodifiableMap(_data);
    }
}
