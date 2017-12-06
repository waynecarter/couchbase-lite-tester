import couchbase.lite.tester.client.MemoryPointer;
import couchbase.lite.tester.client.TestCase;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseTest extends TestCase {

    public void testCreate() throws Exception {
        MemoryPointer database = null;

        try {
            database = database_create("foo");

            Assert.assertNotNull(database);
        } finally {
            release(database);
        }
    }

    public void testGetName() throws Exception {
        MemoryPointer database = null;

        try {
            database = database_create("foo");
            String name = database_getName(database);

            Assert.assertEquals("foo", name);
        } finally {
            release(database);
        }
    }

    public void testGetDocument() throws Exception {
        MemoryPointer database = null;
        MemoryPointer document = null;

        try {
            database = database_create("foo");
            document = document_create("bar");
            database_save(database, document);

            release(document);

            document = database_getDocument(database, "bar");
            String id = document_getId(document);

            Assert.assertEquals("bar", id);
        } finally {
            release(document);
            release(database);
        }
    }

    public void testGetDocuments() throws Exception {
        MemoryPointer database = null;

        try {
            database = database_create("foo");

            Map<String, Map<String, Object>> documents = new HashMap<>();
            for (int i=0; i<5; i++) {
                String id = "doc" + i;

                Map<String, Object> data = new HashMap<>();
                data.put("name" + i, "value" + i);

                documents.put(id, data);
            }
            database_saveDocuments(database, documents);

            List<String> ids = new ArrayList<>();
            for (int i=0; i<5; i++) {
                String id = "doc" + i;

                ids.add(id);
            }
            documents = database_getDocuments(database, ids);

            for (int i=0; i<5; i++) {
                String id = "doc" + i;
                Map<String, Object> document = documents.get(id);

                assertEquals("value" + i, document.get("name" + i));
            }
        } finally {
            release(database);
        }
    }

    public void testSave() throws Exception {
        MemoryPointer database = null;
        MemoryPointer document = null;

        try {
            database = database_create("foo");
            document = document_create("bar");
            database_save(database, document);

            Assert.assertTrue(database_contains(database, "bar"));
        } finally {
            release(document);
            release(database);
        }
    }

    public void testSaveDocuments() throws Exception {
        testGetDocuments();
    }

    public void testDelete() throws Exception {
        MemoryPointer database = null;
        MemoryPointer document = null;

        try {
            database = database_create("foo");
            document = document_create("bar");
            database_save(database, document);

            database_delete(database, document);

            Assert.assertFalse(database_contains(database, "bar"));
        } finally {
            release(document);
            release(database);
        }
    }

    public void testChangeListener() throws Exception {
        MemoryPointer database = null;
        MemoryPointer changeListener = null;
        MemoryPointer document = null;
        MemoryPointer change = null;

        try {
            database = database_create("foo");
            changeListener = database_addChangeListener(database);

            // Create.
            changeListener = database_addChangeListener(database);
            for (int i=0; i<5; i++) {
                document = document_create("doc" + i);
                database_save(database, document);
                change = databaseChangeListener_getChange(changeListener, i);

                Assert.assertTrue(databaseChangeListener_changesCount(changeListener) == i + 1);
                Assert.assertTrue(("doc" + i).equals(databaseChange_getDocumentId(change)));
                Assert.assertFalse(databaseChange_isDelete(change));

                release(document);
                release(change);
            }
            database_removeChangeListener(database, changeListener);

            release(changeListener);

            // Update.
            changeListener = database_addChangeListener(database);
            for (int i=0; i<5; i++) {
                document = database_getDocument(database, "doc" + i);
                document_setString(document, "foo", "bar");
                database_save(database, document);
                change = databaseChangeListener_getChange(changeListener, i);

                Assert.assertTrue(databaseChangeListener_changesCount(changeListener) == i + 1);
                Assert.assertTrue(("doc" + i).equals(databaseChange_getDocumentId(change)));
                Assert.assertFalse(databaseChange_isDelete(change));

                release(document);
                release(change);
            }
            database_removeChangeListener(database, changeListener);

            release(changeListener);

            // Delete.
            changeListener = database_addChangeListener(database);
            for (int i=0; i<5; i++) {
                document = database_getDocument(database, "doc" + i);
                database_delete(database, document);
                change = databaseChangeListener_getChange(changeListener, i);

                Assert.assertTrue(databaseChangeListener_changesCount(changeListener) == i + 1);
                Assert.assertTrue(("doc" + i).equals(databaseChange_getDocumentId(change)));
                Assert.assertTrue(databaseChange_isDelete(change));

                release(document);
                release(change);
            }
            database_removeChangeListener(database, changeListener);

            release(changeListener);

            // Remove change listener.
            changeListener = database_addChangeListener(database);
            database_removeChangeListener(database, changeListener);
            document = document_create("foo");
            database_save(database, document);
            Assert.assertTrue(databaseChangeListener_changesCount(changeListener) == 0);
        } finally {
            release(change);
            release(document);
            release(changeListener);
            release(database);
        }
    }
}
