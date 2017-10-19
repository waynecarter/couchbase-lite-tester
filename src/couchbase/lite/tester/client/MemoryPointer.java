package couchbase.lite.tester.client;

public class MemoryPointer {
    private final String _address;

    public MemoryPointer(String address) {
        _address = address;
    }

    public String getAddress() {
        return _address;
    }
}
