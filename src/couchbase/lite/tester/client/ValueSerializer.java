package couchbase.lite.tester.client;

public class ValueSerializer {
    public static String serialize(Object value) {
        if (value instanceof MemoryPointer) {
            MemoryPointer memoryPointer = (MemoryPointer) value;

            return memoryPointer.getAddress();
        } else if (value instanceof String) {
            String string = (String) value;

            return ("\"" + string + "\"");
        } else if (value instanceof Number) {
            Number number = (Number) value;

            return number.toString();
        } else if (value instanceof Boolean) {
            Boolean bool = (Boolean) value;

            return (bool ? "true" : "false");
        } else {
            throw new RuntimeException("Invalid value type: " + value.getClass().getName());
        }
    }

    public static <T> T deserialize(String value) {
        if (value == null || value.length() == 0 || value.equals("null")) {
            return null;
        } else if (value.startsWith("@")) {
            return (T)new MemoryPointer(value);
        } else if (value.equals("true")) {
            return (T)Boolean.TRUE;
        } else if (value.equals("false")) {
            return (T)Boolean.FALSE;
        } else if (value.startsWith("\"") && value.endsWith("\"")) {
            return (T)value.substring(1, value.length() - 1);
        } else {
            if (value.contains(".")) {
                return (T)new Double(value);
            } else {
                return (T)new Integer(value);
            }
        }
    }
}
