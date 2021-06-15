/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.ows;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

import org.geotools.data.Base64;

/**
 * A simple {@link HTTPClient} that creates a new {@link HttpURLConnection HTTP connection} for each
 * request.
 * 
 * @author groldan
 * 
 */
public class SimpleHttpClient implements HTTPClient {

    private static final int DEFAULT_TIMEOUT = 30;// 30 seconds

    private String user;

    private String password;

    private int connectTimeout = DEFAULT_TIMEOUT;

    private int readTimeout = DEFAULT_TIMEOUT;

    private boolean tryGzip = true;

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int getConnectTimeout() {
        return connectTimeout;
    }

    @Override
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    @Override
    public int getReadTimeout() {
        return readTimeout;
    }

    @Override
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#get(java.net.URL)
     */
    public HTTPResponse get(final URL url) throws IOException {

        URLConnection connection = openConnection(url);
        if(connection instanceof HttpURLConnection){
            ((HttpURLConnection) connection).setRequestMethod("GET");
        }

        connection.connect();

        return new SimpleHTTPResponse(connection);
    }

    /**
     * @see org.geotools.data.ows.HTTPClient#post(java.net.URL, java.io.InputStream,
     *      java.lang.String)
     */
    public HTTPResponse post(final URL url, final InputStream postContent,
            final String postContentType) throws IOException {

        URLConnection connection = openConnection(url);
        if (connection instanceof HttpURLConnection) {
            ((HttpURLConnection) connection).setRequestMethod("POST");
        }
        connection.setDoOutput(true);
        if (postContentType != null) {
            connection.setRequestProperty("Content-type", postContentType);
        }

        connection.connect();

        OutputStream outputStream = connection.getOutputStream();
        try {
            byte[] buff = new byte[512];
            int count;
            while ((count = postContent.read(buff)) > -1) {
                outputStream.write(buff, 0, count);
            }
        } finally {
            outputStream.flush();
            outputStream.close();
        }

        return new SimpleHTTPResponse(connection);
    }

    private HttpURLConnection openConnection(URL finalURL) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) finalURL.openConnection();
        if(tryGzip){
            connection.addRequestProperty("Accept-Encoding", "gzip");
        }
        // mind, connect timeout is in seconds
        if (getConnectTimeout() > 0) {
            connection.setConnectTimeout(1000 * getConnectTimeout());
        }
        if (getReadTimeout() > 0) {
            connection.setReadTimeout(1000 * getReadTimeout());
        }

        final String username = getUser();
        final String password = getPassword();

        if (username != null && password != null) {
            String userpassword = username + ":" + password;
            String encodedAuthorization = Base64.encodeBytes(userpassword.getBytes("UTF-8"));
            connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
        }
        return connection;
    }

    public static class SimpleHTTPResponse implements HTTPResponse {

        private URLConnection connection;

        private InputStream responseStream;

        public SimpleHTTPResponse(final URLConnection connection) throws IOException {
            this.connection = connection;
            InputStream inputStream = connection.getInputStream();

            final String contentEncoding = connection.getContentEncoding();

            if (contentEncoding != null && connection.getContentEncoding().indexOf("gzip") != -1) {
                inputStream = new GZIPInputStream(inputStream);
            }
            responseStream = inputStream;
        }

        /**
         * @see org.geotools.data.ows.HTTPResponse#dispose()
         */
        public void dispose() {
            if (responseStream != null) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    // ignore
                }
                responseStream = null;
            }
            if (connection != null) {
                if (connection instanceof HttpURLConnection) {
                    ((HttpURLConnection) connection).disconnect();
                }
                connection = null;
            }
        }

        /**
         * @see org.geotools.data.ows.HTTPResponse#getContentType()
         */
        @Override
        public String getContentType() {
            return connection.getContentType();
        }


        @Override
        public String getResponseHeader(String headerName) {
            return connection.getHeaderField(headerName);
        }

        /**
         * @see org.geotools.data.ows.HTTPResponse#getResponseStream()
         */
        public InputStream getResponseStream() throws IOException {
            return responseStream;
        }

        /**
         * @see org.geotools.data.ows.HTTPResponse#getResponseCharset()
         */
        @Override
        public String getResponseCharset() {
            String contentType = getContentType();
            if (null == contentType) {
                return null;
            }
            String[] split = contentType.split(";");

            for (int i = 1; i < split.length; i++) {
                String[] mimeParam = split[i].split("=");
                if (mimeParam.length == 2 && "charset".equalsIgnoreCase(mimeParam[0])) {
                    String charset = mimeParam[1];
                    return charset.trim();
                }
            }
            return null;
        }
    }

    /**
     * @param tryGZIP
     * @see org.geotools.data.ows.HTTPClient#setTryGzip(boolean)
     */
    @Override
    public void setTryGzip(boolean tryGZIP) {
        this.tryGzip = tryGZIP;
    }

    /**
     * @return
     * @see org.geotools.data.ows.HTTPClient#isTryGzip()
     */
    @Override
    public boolean isTryGzip() {
        return tryGzip;
    }
}
