package couchbase.lite;

import java.util.*;

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

    public int getInt(String property) {
        return (int)_data.get(property);
    }

    public void setInt(String property, int i) {
        _data.put(property, i);
    }

    public long getLong(String property) {
        return (long)_data.get(property);
    }

    public void setLong(String property, long l) {
        _data.put(property, l);
    }

    public float getFloat(String property) {
        return (float)_data.get(property);
    }

    public void setFloat(String property, float f) {
        _data.put(property, f);
    }

    public double getDouble(String property) {
        return (double)_data.get(property);
    }

    public void setDouble(String property, double d) {
        _data.put(property, d);
    }

    public Number getNumber(String property) {
        return (Number)_data.get(property);
    }

    public void setNumber(String property, Number number) {
        _data.put(property, number);
    }

    public DictionaryObject getDictionary(String property) {
        DictionaryObject dictionary;

        Object object = _data.get(property);
        if (object instanceof Map) {
            dictionary = new DictionaryObject((Map<String, Object>)object);
            _data.put(property, dictionary);
        } else {
            dictionary = (DictionaryObject)object;
        }

        return dictionary;
    }

    public void setDictionary(String property, DictionaryObject dictionary) {
        _data.put(property, dictionary);
    }

    public ArrayObject getArray(String property) {
        ArrayObject array;

        Object object = _data.get(property);
        if (object instanceof List) {
            array = new ArrayObject((List)_data);
            _data.put(property, array);
        } else {
            array = (ArrayObject)object;
        }

        return array;
    }

    public void setArray(String property, ArrayObject array) {
        _data.put(property, array);
    }

    public Map<String, Object> toMap() {
        return Collections.unmodifiableMap(_data);
    }
}
