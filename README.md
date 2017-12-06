# Couchbase Lite Tester

A simple testing framework for cross-platform functional testing of libraries. Couchbase Lite is one example of a library that can be tested using this framework.

The framework consistes of a `Client` and `Server`. A `Client` is written once in the programming or scripting language of choice and is used by tests to invoke operations on the Library hosted by a `Server`. A `Server` is written for every platform that needs to be tested (e.g. Cocoa, Java, .NET, etc) and hosts the Library to be tested. Communication between the `Client` and `Server` is over HTTP.

The `Client` and `Server` only transfer String, Boolean, Integer, Decimal, Dictionary, Array, and null by value, all other objects are transfered by reference as a Memory Pointer.

## What a test looks like

```java
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
```

## What that test looks like on the wire

### database = database_create("foo")
* **Request:**  POST [http://localhost:3100/database_create?name="foo"](http://localhost:3100/database_create?name="foo")
* **Response:** @1
* **Content-Length:** 2

### document = document_create("bar")
* **Request:**  POST [http://localhost:3100/document_create?id="bar"](http://localhost:3100/document_create?id="bar")
* **Response:** @2
* **Content-Length:** 2

### database_save(database, document)
* **Request:**  POST [http://localhost:3100/database_save?database=@1&document=@2](http://localhost:3100/database_save?database=@1&document=@2)
* **Response:** 
* **Content-Length:** 0

### release(document)
* **Request:**  POST [http://localhost:3100/release?object=@2](http://localhost:3100/release?object=@2)
* **Response:** 
* **Content-Length:** 0

### document = database_getDocument(database, "bar")
* **Request:**  POST [http://localhost:3100/database_getDocument?database=@1&id="bar"](http://localhost:3100/database_getDocument?database=@1&id="bar")
* **Response:** @3
* **Content-Length:** 2

### String id = document_getId(document)
* **Request:**  POST [http://localhost:3100/document_getId?document=@3](http://localhost:3100/document_getId?document=@3)
* **Response:** "bar"
* **Content-Length:** 5

### release(document)
* **Request:**  POST [http://localhost:3100/release?object=@3](http://localhost:3100/release?object=@3)
* **Response:** 
* **Content-Length:** 0

### release(database)
* **Request:**  POST [http://localhost:3100/release?object=@1](http://localhost:3100/release?object=@1)
* **Response:** 
* **Content-Length:** 0

## A good place to start
1. Clone this repo.
2. Open `couchbase-lite-tester/java` in IntelliJ.
3. Run `couchbase.lite.tester.server.Server`.
4. Click on the links in the example above. The `Server` will process your requests and you will see the responses.
5. Look at `couchbase.lite.tester.server.RequestHandler.java`. These methods are called from `Server.java` when a request is recieved.
6. Look at `couchbase.lite.tester.client.TestCase.java`. These are the mothods used in the test example above. These methods call into `Client.java` which sends the requests over HTTP to the running `Server`.
7. Look at `Tests/DatabaseTest.java` and `Tests/DocumentTest.java`. These classes derive from `TestCase` and can be run agianst any `Server` on any platform.
