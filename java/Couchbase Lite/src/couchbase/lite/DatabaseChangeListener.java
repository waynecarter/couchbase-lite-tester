package couchbase.lite;

public interface DatabaseChangeListener {
    void changed(Database database, Change change);

    interface Change {
        String getDocumentId();
        boolean isDelete();
    }
}
