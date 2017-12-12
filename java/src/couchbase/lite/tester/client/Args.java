package couchbase.lite.tester.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Args implements Iterable<Args.Entry> {
    private final Map<String, Object> _args = new HashMap<>();

    public void setMemoryPointer(String name, MemoryPointer memoryPointer) {
        _args.put(name, memoryPointer);
    }

    public void setString(String name, String string) {
        _args.put(name, string);
    }

    public void setInt(String name, int i) {
        _args.put(name, i);
    }

    public void setLong(String name, long l) {
        _args.put(name, l);
    }

    public void setFloat(String name, float f) {
        _args.put(name, f);
    }

    public void setDouble(String name, double d) {
        _args.put(name, d);
    }

    public void setNumber(String name, Number number) {
        _args.put(name, number);
    }

    public void setBoolean(String name, boolean bool) {
        _args.put(name, bool);
    }

    public void setDictionary(String name, Map<String, Object> dictionary) {
        _args.put(name, dictionary);
    }

    public void setArray(String name, List array) {
        _args.put(name, array);
    }

    @Override
    public Iterator<Entry> iterator() {
        Iterator<Map.Entry<String, Object>> iterator = _args.entrySet().iterator();

        return new Iterator<Entry>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Entry next() {
                Map.Entry<String, Object> entry = iterator.next();

                return new Entry() {
                    @Override
                    public String getKey() {
                        return entry.getKey();
                    }

                    @Override
                    public Object getValue() {
                        return entry.getValue();
                    }

                    @Override
                    public Object setValue(Object value) {
                        return entry.setValue(value);
                    }
                };
            }
        };
    }

    public interface Entry extends Map.Entry<String, Object> { }
}
