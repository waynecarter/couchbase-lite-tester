package couchbase.lite;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Database {
    private final String _name;
    private final Map<String, Document> _database = new HashMap();
    private final Set<DatabaseChangeListener> _changeListeners = new HashSet();

    public Database(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public Document getDocument(String id) {
        return _database.get(id);
    }

    public void save(Document document) {
        _database.put(document.getId(), document);
        _notifyChangeListeners(document, false);
    }

    public void delete(Document document) {
        _database.remove(document.getId());
        _notifyChangeListeners(document, true);
    }

    public boolean contains(String id) {
        return _database.containsKey(id);
    }
    
    public void inBatch(Runnable batch) {
        batch.run();
    }

    public void addChangeListener(DatabaseChangeListener listener) {
        _changeListeners.add(listener);
    }

    public void removeChangeListener(DatabaseChangeListener listener) {
        _changeListeners.remove(listener);
    }

    private void _notifyChangeListeners(Document document, boolean isDelete) {
        for (DatabaseChangeListener listener : _changeListeners) {
            listener.changed(this, new DatabaseChangeListener.Change() {
                @Override
                public String getDocumentId() {
                    return document.getId();
                }

                @Override
                public boolean isDelete() {
                    return isDelete;
                }
            });
        }
    }
}
