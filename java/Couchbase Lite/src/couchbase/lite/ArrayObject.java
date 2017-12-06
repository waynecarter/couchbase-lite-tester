package couchbase.lite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayObject {
    private final List _data;

    public ArrayObject() {
        this(new ArrayList<>());
    }

    public ArrayObject(List data) {
        _data = data;
    }

    public List toList() {
        return Collections.unmodifiableList(_data);
    }
}
