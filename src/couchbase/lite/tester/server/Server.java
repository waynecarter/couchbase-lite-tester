package couchbase.lite.tester.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
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

                // Get args from query string.
                Map<String, String> rawArgs = new HashMap<>();
                Args args = new Args();
                String query = httpExchange.getRequestURI().getQuery();
                if (query != null) {
                    for (String param : query.split("&")) {
                        String pair[] = param.split("=");
                        String name = pair[0];
                        String value = (pair.length > 1 ? pair[1] : null);

                        if (value != null) {
                            rawArgs.put(name, value);
                        }

                        args.put(name, ValueSerializer.deserialize(value, memory));
                    }
                }

                try {
                    // Find and invoke the method on the RequestHandler.
                    Object result = null;
                    if ("release".equals(method)) {
                        memory.remove(rawArgs.get("object"));
                    } else {
                        Method target = RequestHandler.class.getMethod(method, Args.class);

                        if (target.getReturnType() == null) {
                            target.invoke(requestHandler, args);
                        } else {
                            result = target.invoke(requestHandler, args);
                        }
                    }

                    httpExchange.getResponseHeaders().set("content-type","text/plain");

                    String body = ValueSerializer.serialize(result, memory);
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