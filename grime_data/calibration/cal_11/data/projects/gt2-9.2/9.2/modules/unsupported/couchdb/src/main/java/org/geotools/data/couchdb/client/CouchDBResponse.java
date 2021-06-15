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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.couchdb.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.geotools.util.logging.Logging;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Ian Schneider (OpenGeo)
 *
 * @source $URL$
 */
public final class CouchDBResponse {
    private final HttpMethod request;
    private final int result;
    private final IOException exception;
    private final Object json;
    private final boolean isArray;
    private static final Logger logger =  Logging.getLogger(CouchDBResponse.class);

    public CouchDBResponse(HttpMethod request, int result, IOException exception) throws IOException {
        this.request = request;
        this.result = result;
        this.exception = exception;
        
        boolean err = !isHttpOK();
        
        InputStream response = request.getResponseBodyAsStream();
        if (err) {
            if (exception != null) {
                throw new IOException("HTTP error",exception);
            }
            if (response == null) {
                throw new IOException("HTTP error : " + result);
            }
        }
        json = JSONValue.parse(new InputStreamReader(request.getResponseBodyAsStream()));
        if (err) {
            isArray = false;
        } else {
            isArray = json instanceof JSONArray;
        }
    }
    
    public void checkOK(String msg) throws CouchDBException {
        if (!isHttpOK()) {
            String fullMsg = msg + "," + getErrorAndReason();
            try {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE,"Request URI : " + request.getURI());
                }
            } catch (URIException ex) {
                logger.warning("An invalid URI was used " + request.getPath() + 
                        "," + request.getQueryString());
            }
            throw new CouchDBException(fullMsg,exception);
        } 
    }
    
    private String getErrorAndReason() {
        if (!isHttpOK()) {
            JSONObject err = (JSONObject) json;
            String res = null;
            if (err.containsKey("error")) {
                res = err.get("error").toString();
            }
            if (err.containsKey("reason")) {
                res = res == null ? "" : res + ",";
                res = res + err.get("reason").toString();
            }
            if (res == null) {
                res = err.toJSONString();
            }
            return res;
        } else {
            throw new IllegalStateException("not an error");
        }
    }

    public boolean isHttpOK() {
        return result >= 200 && result < 300;
    }
    
    public boolean isCouchOK() {
        boolean ok = false;
        JSONObject obj = (JSONObject) json;
        return obj.get("ok").equals("true");
    }

    public Throwable getException() {
        return exception;
    }
    
    public JSONArray getBodyAsJSONArray() throws IOException {
        if (!isArray) {
            throw new IllegalStateException("Response is not an array");
        }
        return (JSONArray) json;
    }

    public JSONObject getBodyAsJSONObject() throws IOException {
        if (isArray) {
            throw new IllegalStateException("Response is not an object");
        }
        return (JSONObject) json;
    }
    
    

    
}
