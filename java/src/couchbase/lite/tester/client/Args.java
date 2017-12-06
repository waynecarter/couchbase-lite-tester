package couchbase.lite.tester.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Args implements Iterable<Args.Entry> {
    private final Map<String, Object> _args = new HashMap<>();

    public void setMemoryPointer(String name, MemoryPointer memoryPointer) {
        _args.put(name, memoryPointer);
    }

    public void setString(String name, String string) {
        _args.put(name, string);
    }

    public void setInt(String name, int integer) {
        _args.put(name, integer);
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

    public void setArray(String name, Collection array) {
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
