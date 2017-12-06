package couchbase.lite.tester.server;

import couchbase.lite.*;

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

    public Map<String, Map<String, Object>> database_getDocuments(Args args) {
        Database database = args.get("database");
        List<String> ids = args.get("ids");
        Map<String, Map<String, Object>> documents = new HashMap<>();

        for (String id : ids) {
            Document document = database.getDocument(id);

            if (document != null) {
                documents.put(id, document.toMap());
            }
        }

        return documents;
    }

    public void database_save(Args args) {
        Database database = args.get("database");
        Document document = args.get("document");

        database.save(document);
    }

    public void database_saveDocuments(Args args) {
        Database database = args.get("database");
        Map<String, Map<String, Object>> documents = args.get("documents");

        database.inBatch(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<String, Map<String, Object>> entry : documents.entrySet()) {
                    String id = entry.getKey();
                    Map<String, Object> data = entry.getValue();
                    Document document = new Document(id, data);

                    database.save(document);
                }
            }
        });
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

    public Map<String, Object> document_getDictionary(Args args) {
        Document document = args.get("document");
        String property = args.get("property");

        DictionaryObject dictionary = document.getDictionary(property);

        return (dictionary != null ? dictionary.toMap() : null);
    }

    public void document_setDictionary(Args args) {
        Document document = args.get("document");
        String property = args.get("property");
        Map<String, Object> map = args.get("dictionary");

        DictionaryObject dictionary = new DictionaryObject(map);

        document.setDictionary(property, dictionary);
    }

    public List document_getArray(Args args) {
        Document document = args.get("document");
        String property = args.get("property");

        ArrayObject array = document.getArray(property);

        return (array != null ? array.toList() : null);
    }

    public void document_setArray(Args args) {
        Document document = args.get("document");
        String property = args.get("property");
        List list = args.get("array");

        ArrayObject array = new ArrayObject(list);

        document.setArray(property, array);
    }

    public Map<String, Object> document_toMap(Args args) {
        Document document = args.get("document");

        return document.toMap();
    }
}
