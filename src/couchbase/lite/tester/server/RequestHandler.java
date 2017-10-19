package couchbase.lite.tester.server;

import couchbase.lite.Database;
import couchbase.lite.DatabaseChangeListener;
import couchbase.lite.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHandler {

    /* ------------ */
    /* - Database - */
    /* ------------ */

    public Database database_create(Args args) {
        String name = args.get("name");

        return new Database(name);
    }

    public Database database_create(String name) {
        return new Database(name);
    }

    public String database_getName(Args args) {
        Database database = args.get("database");

        return database.getName();
    }

    public Document database_getDocument(Args args) {
        Database database = args.get("database");
        String id = args.get("id");

        return database.getDocument(id);
    }

    public void database_save(Args args) {
        Database database = args.get("database");
        Document document = args.get("document");

        database.save(document);
    }

    public void database_delete(Args args) {
        Database database = args.get("database");
        Document document = args.get("document");

        database.delete(document);
    }

    public boolean database_contains(Args args) {
        Database database = args.get("database");
        String id = args.get("id");

        return database.contains(id);
    }

    private interface ChangeListener<T> {
        List<T> getChanges();
    }

    public ChangeListener<DatabaseChangeListener.Change> database_addChangeListener(Args args) {
        Database database = args.get("database");

        class MyChangeListener implements DatabaseChangeListener, ChangeListener<DatabaseChangeListener.Change> {
            private List<Change> _changes = new ArrayList<>();

            @Override
            public void changed(Database database, Change change) {
                _changes.add(change);
            }

            @Override
            public List<Change> getChanges() {
                return _changes;
            }
        }
        MyChangeListener changeListener = new MyChangeListener();

        database.addChangeListener(changeListener);

        return changeListener;
    }

    public void database_removeChangeListener(Args args) {
        Database database = args.get("database");
        DatabaseChangeListener changeListener = args.get("changeListener");

        database.removeChangeListener(changeListener);
    }

    public int databaseChangeListener_changesCount(Args args) {
        ChangeListener<DatabaseChangeListener.Change> changeListener = args.get("changeListener");

        return changeListener.getChanges().size();
    }

    public DatabaseChangeListener.Change databaseChangeListener_getChange(Args args) {
        ChangeListener<DatabaseChangeListener.Change> changeListener = args.get("changeListener");
        int index = args.get("index");

        return changeListener.getChanges().get(index);
    }

    public String databaseChange_getDocumentId(Args args) {
        DatabaseChangeListener.Change change = args.get("change");

        return change.getDocumentId();
    }

    public boolean databaseChange_isDelete(Args args) {
        DatabaseChangeListener.Change change = args.get("change");

        return change.isDelete();
    }

    /* ------------ */
    /* - Document - */
    /* ------------ */

    public Document document_create(Args args) {
        String id = args.get("id");
        Map<String, Object> dictionary = args.get("dictionary");

        if (id != null) {
            if (dictionary == null) {
                return new Document(id);
            } else {
                return new Document(id, dictionary);
            }
        } else {
            if (dictionary == null) {
                return new Document();
            } else {
                return new Document(dictionary);
            }
        }
    }

    public String document_getId(Args args) {
        Document document = args.get("document");

        return document.getId();
    }

    public String document_getString(Args args) {
        Document document = args.get("document");
        String property = args.get("property");

        return document.getString(property);
    }

    public void document_setString(Args args) {
        Document document = args.get("document");
        String property = args.get("property");
        String string = args.get("string");

        document.setString(property, string);
    }

    /* -------------- */
    /* - Dictionary - */
    /* -------------- */

    public Map dictionary_create(Args args) {
        return new HashMap();
    }

    public Object dictionary_get(Args args) {
        Map<String, Object> map = args.get("dictionary");
        String key = args.get("key");

        return map.get(key);
    }

    public void dictionary_put(Args args) {
        Map<String, Object> map = args.get("dictionary");
        String key = args.get("key");
        String string = args.get("string");

        map.put(key, string);
    }
}
