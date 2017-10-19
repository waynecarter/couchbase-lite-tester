# Couchbase Lite Tester

A simple testing framework for cross-platform functional testing of libraries. Couchbase Lite is one example.

The framework consistes of a Server and Client. A Server is written for every platform that needs to be tested (i.e. Cocoa, Java, .NET, etc) and hosts the Library to be tested. A Client is written once in the programming or scripting language of choice and is used by tests to invoke operations on the Library hosted by the Server. Communication between the Client and Server is over HTTP.

The Server and Client only pass scalars of type String, Boolean, Integer, Decimal, and null, all other objects are transfered as Memory Pointers.

## What a simple test looks like:

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

## This is what that test lools like on the wire:

### database = database_create("foo")
* **Request:**  POST http://localhost:3100/database_create?name="foo"
* **Response:** @1

### document = document_create("bar")
* **Request:**  POST http://localhost:3100/document_create?id="bar"
* **Response:** @2

### database_save(database, document)
* **Request:**  POST http://localhost:3100/database_save?database=@1&document=@2
* **Response:** 

### release(document)
* **Request:**  POST http://localhost:3100/release?object=@2
* **Response:** 

### document = database_getDocument(database, "bar")
* **Request:**  POST http://localhost:3100/database_getDocument?database=@1&id="bar"
* **Response:** @3

### String id = document_getId(document)
* **Request:**  POST http://localhost:3100/document_getId?document=@3
* **Response:** "bar"

### release(document);
* **Request:**  POST http://localhost:3100/release?object@3
* **Response:** 

### release(database);
* **Request:**  POST http://localhost:3100/release?object@1
* **Response:**

## A good place to start:
1. Run couchbase.lite.tester.server.Server
2. Click on the links in the example above. The server will process your requests and you will see the responses.
3. Look at couchbase.lite.tester.server.RequestHandler.java. These methods are called from Server.java when a request is recieved.
4. Look at couchbase.lite.tester.client.TestCase.java. These are the mothods used in the test example above. These methods call into Client.java which sends the requests over HTTP to the running Server.
5. Look at Tests/DatabaseTest.java and Tests/DocumentTest.java. These classes derive from TestCase and can be run agianst any Server on any platform.