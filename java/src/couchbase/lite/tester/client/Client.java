package couchbase.lite.tester.client;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
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
            // Create connection to method endpoint.
            URL url = new URL(_baseUrl, method);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            // Write args to request body.
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            try {
                if (args != null) {
                    int i = 0;

                    for (Args.Entry entry : args) {
                        if (i > 0) {
                            out.write("&");
                        }

                        out.write(URLEncoder.encode(entry.getKey(), "UTF-8") + "=");
                        out.write(URLEncoder.encode(ValueSerializer.serialize(entry.getValue()), "UTF-8"));

                        i++;
                    }
                }
            } finally {
                out.close();
            }

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

    public void release(MemoryPointer object) {
        Args args = new Args();
        args.setMemoryPointer("object", object);

        this.invokeMethod("release", args);
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
