package com.mycompany.app.myapp.tcpproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * HttpResponse - Handle HTTP replies
 * <p>
 * $Id: HttpResponse.java,v 1.2 2003/11/26 18:12:42 kangasha Exp $
 */
public class HttpResponse {
    final static String CRLF = "\r\n";

    /**
     * Maximum size of objects that this proxy can handle. For the
     * moment set to 100 KB. You can adjust this as needed.
     */
    final static int MAX_OBJECT_SIZE = 100000;

    private final String statusLine;
    private final String headers;
    private final int[] body;
    /* Body of reply */

    public HttpResponse(String statusLine, StringBuilder headers, int[] body) {
        this.statusLine = statusLine;
        this.headers = headers.toString();
        this.body = body;
    }

    public static HttpResponse getHttpRequestFromSocketInputStream(InputStream inputStream) throws IOException {
        String statusLine = "";
        StringBuilder headers = new StringBuilder();
        int[] body = new int[MAX_OBJECT_SIZE];

        BufferedReader fromServer = new BufferedReader(new InputStreamReader(inputStream));
        /* Length of the object */
        int length = -1;
        boolean gotStatusLine = false;


        String line = fromServer.readLine();
        while (line.length() != 0) {
            if (!gotStatusLine) {
                statusLine = line;
                gotStatusLine = true;
            } else {
                headers.append(line).append(CRLF);
            }

            /* Get length of content as indicated by
             * Content-Length header. Unfortunately this is not
             * present in every response. Some servers return the
             * header "Content-Length", others return
             * "Content-length". You need to check for both
             * here. */
            if (line.startsWith("Content-Length:") ||
                    line.startsWith("Content-length:")) {
                String[] tmp = line.split(" ");
                length = Integer.parseInt(tmp[1]);
            }
            line = fromServer.readLine();
        }


        /* Read the body in chunks of BUF_SIZE and copy the chunk
         * into body. Usually replies come back in smaller chunks
         * than BUF_SIZE. The while-loop ends when either we have
         * read Content-Length bytes or when the connection is
         * closed (when there is no Connection-Length in the
         * response. */

        int count = 0;
        while (length != -1 && count < length) {
            /* Read it in as binary data */
            int res = fromServer.read();
            if (res == -1) {
                break;
            }
            /* Copy the bytes into body. Make sure we don't exceed
             * the maximum object size. */
            for (int i = 0; i < MAX_OBJECT_SIZE; i++) {
                body[i] = res;
                count++;
            }
        }

        return new HttpResponse( statusLine, headers, body);
    }

    /**
     * Convert response into a string for easy re-sending. Only
     * converts the response headers, body is not converted to a
     * string.
     */
    public String toString() {
        String res = "";

        res = statusLine + CRLF;
        res += headers;
        res += CRLF;
        res += Arrays.toString(body);
        return res;
    }
}