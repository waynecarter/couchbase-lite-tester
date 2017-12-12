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
        testGetString();
    }

    public void testGetInt() throws Exception {
        MemoryPointer document = null;

        try {
            document = document_create();
            document_setInt(document, "min", Integer.MIN_VALUE);
            document_setInt(document, "max", Integer.MAX_VALUE);

            int min = document_getInt(document, "min");
            int max = document_getInt(document, "max");

            Assert.assertEquals(Integer.MIN_VALUE, min);
            Assert.assertEquals(Integer.MAX_VALUE, max);
        } finally {
            release(document);
        }
    }

    public void testSetInt() throws Exception {
        testGetInt();
    }

    public void testGetLong() throws Exception {
        MemoryPointer document = null;

        try {
            document = document_create();
            document_setLong(document, "min", Long.MIN_VALUE);
            document_setLong(document, "max", Long.MAX_VALUE);

            long min = document_getLong(document, "min");
            long max = document_getLong(document, "max");

            Assert.assertEquals(Long.MIN_VALUE, min);
            Assert.assertEquals(Long.MAX_VALUE, max);
        } finally {
            release(document);
        }
    }

    public void testSetLong() throws Exception {
        testGetLong();
    }

    public void testGetFloat() throws Exception {
        MemoryPointer document = null;

        try {
            document = document_create();
            document_setFloat(document, "min", Float.MIN_VALUE);
            document_setFloat(document, "max", Float.MAX_VALUE);

            float min = document_getFloat(document, "min");
            float max = document_getFloat(document, "max");

            Assert.assertEquals(Float.MIN_VALUE, min);
            Assert.assertEquals(Float.MAX_VALUE, max);
        } finally {
            release(document);
        }
    }

    public void testSetFloat() throws Exception {
        testGetFloat();
    }

    public void testGetDouble() throws Exception {
        MemoryPointer document = null;

        try {
            document = document_create();
            document_setDouble(document, "min", Double.MIN_VALUE);
            document_setDouble(document, "max", Double.MAX_VALUE);

            double min = document_getDouble(document, "min");
            double max = document_getDouble(document, "max");

            Assert.assertEquals(Double.MIN_VALUE, min);
            Assert.assertEquals(Double.MAX_VALUE, max);
        } finally {
            release(document);
        }
    }

    public void testGetNumber() throws Exception {
        MemoryPointer document = null;

        try {
            document = document_create();
            document_setNumber(document, "min", Double.MIN_VALUE);
            document_setNumber(document, "max", Double.MAX_VALUE);

            Number min = document_getNumber(document, "min");
            Number max = document_getNumber(document, "max");

            Assert.assertEquals(Double.MIN_VALUE, min);
            Assert.assertEquals(Double.MAX_VALUE, max);
        } finally {
            release(document);
        }
    }

    public void testSetNumber() throws Exception {
        testGetNumber();
    }

    public void testSetDouble() throws Exception {
        testGetDouble();
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
