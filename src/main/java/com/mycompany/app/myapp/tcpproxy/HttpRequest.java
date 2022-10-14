package com.mycompany.app.myapp.tcpproxy;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * HttpRequest - HTTP request container and parser
 * <p>
 * $Id: HttpRequest.java,v 1.2 2003/11/26 18:11:53 kangasha Exp $
 */
public class HttpRequest {
    /**
     * Help variables
     */
    final static String CRLF = "\r\n";
    final static int HTTP_PORT = 80;

    private final String method;
    private final String uri;
    private final String version;
    private final String host;
    private final int port;

    public HttpRequest(String method, String uri, String version, String host, int port) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.host = host;
        this.port = port;
    }

    public static HttpRequest getHttpRequestFromSocketInputStream(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String method;
        String uri;
        String version;
        String host = null;
        int port = HTTP_PORT;


        String firstLine = bufferedReader.readLine();

        String[] tmp = firstLine.split(" ");
        method = tmp[0];
        uri = tmp[1];
        version = tmp[2];

        String line = bufferedReader.readLine();
        while (line.length() != 0) {
            /* We need to find host header to know which server to
             * contact in case the request URI is not complete. */
            if (line.startsWith("Host:")) {
                tmp = line.split(" ");
                if (tmp[1].indexOf(':') > 0) {
                    String[] tmp2 = tmp[1].split(":");
                    host = tmp2[0];
                    port = Integer.parseInt(tmp2[1]);
                } else {
                    host = tmp[1];
                    port = HTTP_PORT;
                }
            }
            line = bufferedReader.readLine();
        }

        return new HttpRequest(method, uri, version, host, port);
    }


    /**
     * Convert request into a string for easy re-sending.
     */
    public String toString() {
        String req = "";
        req = method + " " + uri + " " + version + CRLF;
        req += "Connection: close" + CRLF;
        req += CRLF;
        return req;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getVersion() {
        return version;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}