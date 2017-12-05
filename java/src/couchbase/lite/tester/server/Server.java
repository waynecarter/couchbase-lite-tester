package couchbase.lite.tester.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public static void main(String[] args) throws IOException {
        int port = (args.length > 0 ? Integer.parseInt(args[0]) : 3100);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        RequestHandler requestHandler = new RequestHandler();

        server.createContext("/", new HttpHandler() {
            private Memory memory = new Memory();

            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                // Get method from path.
                String path = httpExchange.getRequestURI().getPath();
                String method = (path.startsWith("/") ? path.substring(1) : path);

                // Get args from request.
                Map<String, String> rawArgs = new HashMap<>();
                Args args = new Args();
                // First from the query string (Clients send args in the request body so this is mostly a convenience for dev).
                String query = httpExchange.getRequestURI().getRawQuery();
                // Then from the request body.
                if (query == null) {
                    Reader in = new InputStreamReader(httpExchange.getRequestBody());

                    try {
                        StringBuilder queryBuffer = new StringBuilder();

                        char[] buffer = new char[1024];
                        int read = 0;
                        while ((read = in.read(buffer)) != -1) {
                            queryBuffer.append(buffer, 0, read);
                        }

                        if (queryBuffer.length() > 0) {
                            query = queryBuffer.toString();
                        }
                    } finally {
                        in.close();
                    }
                }
                // If we found args then decode them.
                if (query != null) {
                    for (String param : query.split("&")) {
                        String pair[] = param.split("=", 2);
                        String name = URLDecoder.decode(pair[0], "UTF8");
                        String value = URLDecoder.decode(pair.length > 1 ? pair[1] : null, "UTF8");

                        if (value != null) {
                            rawArgs.put(name, value);
                        }

                        args.put(name, ValueSerializer.deserialize(value, memory));
                    }
                }

                try {
                    // Find and invoke the method on the RequestHandler.
                    String body = null;
                    if ("release".equals(method)) {
                        memory.remove(rawArgs.get("object"));
                    } else {
                        Method target = RequestHandler.class.getMethod(method, Args.class);

                        if (target.getReturnType().equals(Void.TYPE)) {
                            target.invoke(requestHandler, args);
                        } else {
                            Object result = target.invoke(requestHandler, args);

                            body = ValueSerializer.serialize(result, memory);
                        }
                    }

                    httpExchange.getResponseHeaders().set("content-type","text/plain");

                    if (body != null) {
                        httpExchange.sendResponseHeaders(200, body.length());

                        OutputStream out = httpExchange.getResponseBody();
                        out.write(body.getBytes());
                        out.close();
                    } else {
                        httpExchange.sendResponseHeaders(200, -1);
                        httpExchange.close();
                    }

                } catch (Exception e) {
                    // TODO: How should we handle exceptions?
                    e.printStackTrace(System.out);

                    httpExchange.sendResponseHeaders(400, 0);
                }
            }
        });

        server.start();
    }
}