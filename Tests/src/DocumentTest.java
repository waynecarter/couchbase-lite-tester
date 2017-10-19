import couchbase.lite.tester.client.MemoryPointer;
import couchbase.lite.tester.client.TestCase;
import junit.framework.Assert;

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
        MemoryPointer dictionary = null;
        MemoryPointer document = null;

        try {
            dictionary = dictionary_create();
            dictionary_put(dictionary, "foo", "bar");
            document = document_create(dictionary);

            String foo = document_getString(document, "foo");

            Assert.assertEquals("bar", foo);
        } finally {
            release(document);
            release(dictionary);
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
        MemoryPointer dictionary = null;
        MemoryPointer document = null;

        try {
            dictionary = dictionary_create();
            dictionary_put(dictionary, "foo", "bar");
            document = document_create("foo", dictionary);

            String id = document_getId(document);
            String foo = document_getString(document, "foo");

            Assert.assertEquals("foo", id);
            Assert.assertEquals("bar", foo);
        } finally {
            release(document);
            release(dictionary);
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
}
