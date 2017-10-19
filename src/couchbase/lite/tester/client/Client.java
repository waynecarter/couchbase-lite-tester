package couchbase.lite.tester.client;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Client {
    private final URL _baseUrl;

    public Client(URL baseUrl) {
        _baseUrl = baseUrl;
    }

    public <T> T invokeMethod(String method) {
        return invokeMethod(method, null);
    }

    public <T> T invokeMethod(String method, Args args) {
        try {
            // Create query string from args.
            String query = "";
            if (args != null) {
                for (Args.Entry entry : args) {
                    query += (query.length() == 0 ? "?" : "&");
                    query += entry.getKey() + "=";
                    query += URLEncoder.encode(ValueSerializer.serialize(entry.getValue()), "UTF-8");
                }
            }

            // Create connection to method endpoint.
            URL url = new URL(_baseUrl, method + query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            // Process response.
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Get result from response body.
                String result;
                InputStream in = null;
                ByteArrayOutputStream resultStream = null;
                try {
                    in = connection.getInputStream();
                    resultStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = in.read(buffer)) != -1) {
                        resultStream.write(buffer, 0, length);
                    }

                    result = resultStream.toString("UTF-8");
                } finally {
                    if (in != null) in.close();
                    if (resultStream != null) resultStream.close();
                }

                return ValueSerializer.deserialize(result);
            } else {
                throw new MethodInvocationException(responseCode, connection.getResponseMessage());
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public class MethodInvocationException extends RuntimeException {
        private final int _responseCode;
        private final String _responseMessage;

        public MethodInvocationException(int responseCode, String responseMessage) {
            super(responseMessage);

            _responseCode = responseCode;
            _responseMessage = responseMessage;
        }

        public int getResponseCode() {
            return _responseCode;
        }

        public String getResponseMessage() {
            return _responseMessage;
        }
    }
}
