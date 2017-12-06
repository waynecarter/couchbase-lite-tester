package couchbase.lite.tester.server;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ValueSerializer {
    public static String serialize(Object value, Memory memory) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            String string = (String) value;

            return "\"" + string + "\"";
        } else if (value instanceof Number) {
            Number number = (Number) value;

            return number.toString();
        } else if (value instanceof Boolean) {
            Boolean bool = (Boolean) value;

            return (bool ? "true" : "false");
        } else if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>)value;
            Map<String, String> stringMap = new HashMap<>();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String string = serialize(entry.getValue(), memory);

                stringMap.put(key, string);
            }

            return new Gson().toJson(stringMap);
        } else if (value instanceof Collection) {
            Collection<Object> collection = (Collection<Object>)value;
            Collection<String> stringCollection = new ArrayList<>();

            for (Object object : collection) {
                String string = serialize(object, memory);

                stringCollection.add(string);
            }

            return new Gson().toJson(stringCollection);
        } else {
            return memory.add(value);
        }
    }

    public static <T> T deserialize(String value, Memory memory) {
        if (value == null) {
            return null;
        } else if (value.startsWith("@")) {
            return memory.get(value);
        } else if (value.equals("true")) {
            return (T)Boolean.TRUE;
        } else if (value.equals("false")) {
            return (T)Boolean.FALSE;
        } else if (value.startsWith("\"") && value.endsWith("\"")) {
            return (T)value.substring(1, value.length() - 1);
        } else if (value.startsWith("{")) {
            Map<String, String> stringMap = new Gson().fromJson(value, Map.class);
            Map<String, Object> map = new HashMap<>();

            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                String key = entry.getKey();
                Object object = deserialize(entry.getValue(), memory);

                map.put(key, object);
            }

            return (T)map;
        } else if (value.startsWith("[")) {
            Collection<String> stringCollection = new Gson().fromJson(value, Collection.class);
            Collection<Object> collection = new ArrayList<>();

            for (String string : stringCollection) {
                Object object = deserialize(string, memory);

                collection.add(object);
            }

            return (T)collection;
        } else {
            if (value.contains(".")) {
                return (T)new Double(value);
            } else {
                return (T)new Integer(value);
            }
        }
    }
}
