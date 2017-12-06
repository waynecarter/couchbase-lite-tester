import couchbase.lite.tester.client.MemoryPointer;
import couchbase.lite.tester.client.TestCase;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentTest extends TestCase {

    public void testCreate() throws Exception {
        MemoryPointer document = null;

        try {
            document = document_create();

            Assert.assertNotNull(document);
        } finally {
            release(document);
        }
    }

    public void testCreateWithDictionary() throws Exception {
        MemoryPointer document = null;

        try {
            Map<String, Object> dictionary = new HashMap<>();
            dictionary.put("foo", "bar");
            document = document_create(dictionary);

            String foo = document_getString(document, "foo");

            Assert.assertEquals("bar", foo);
        } finally {
            release(document);
        }
    }

    public void testCreateWithId() throws Exception {
        MemoryPointer document = null;

        try {
            document = document_create("foo");
            String id = document_getId(document);

            Assert.assertEquals("foo", id);
        } finally {
            release(document);
        }
    }

    public void testCreateWithIdAndDictionary() throws Exception {
        MemoryPointer document = null;

        try {
            Map<String, Object> dictionary = new HashMap<>();
            dictionary.put("foo", "bar");
            document = document_create("foo", dictionary);

            String id = document_getId(document);
            String foo = document_getString(document, "foo");

            Assert.assertEquals("foo", id);
            Assert.assertEquals("bar", foo);
        } finally {
            release(document);
        }
    }

    public void testGetId() throws Exception  {
        MemoryPointer document = null;

        try {
            document = document_create("foo");
            String id = document_getId(document);

            Assert.assertEquals("foo", id);
        } finally {
            release(document);
        }
    }

    public void testGetString() throws Exception {
        MemoryPointer document = null;

        try {
            document = document_create();
            document_setString(document, "foo", "bar");

            String foo = document_getString(document, "foo");

            Assert.assertEquals("bar", foo);
        } finally {
            release(document);
        }
    }

    public void testSetString() throws Exception {
        MemoryPointer document = null;

        try {
            document = document_create();
            document_setString(document, "foo", "bar");

            String foo = document_getString(document, "foo");

            Assert.assertEquals("bar", foo);
        } finally {
            release(document);
        }
    }

    public void testGetDictionary() throws Exception {
        MemoryPointer document = null;

        try {
            document = document_create();

            Map<String, Object> data = new HashMap<>();
            data.put("street", "123 Anywhere St.");
            document_setDictionary(document, "address", data);

            Map<String, Object> address = document_getDictionary(document, "address");

            Assert.assertEquals("123 Anywhere St.", address.get("street"));
        } finally {
            release(document);
        }
    }

    public void testSetDictionary() throws Exception {
        testGetDictionary();
    }

    public void testGetArray() throws Exception {
        MemoryPointer document = null;

        try {
            document = document_create();

            List data = new ArrayList();
            data.add("java");
            document_setArray(document, "skills", data);

            List skills = document_getArray(document, "skills");

            Assert.assertTrue(skills.contains("java"));
        } finally {
            release(document);
        }
    }

    public void testSetArray() throws Exception {
        testGetArray();
    }

    public void testToMap() throws Exception  {
        MemoryPointer document = null;

        try {
            Map<String, Object> dictionary = new HashMap<>();
            dictionary.put("foo", "bar");
            document = document_create(dictionary);

            Map<String, Object> map = document_toMap(document);

            Assert.assertEquals("bar", map.get("foo"));
        } finally {
            release(document);
        }
    }
}
