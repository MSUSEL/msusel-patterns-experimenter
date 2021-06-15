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

import java.io.File;
import java.io.IOException;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Represents a connection to an individual database.
 * @author Ian Schneider (OpenGeo)
 *
 * @source $URL$
 */
public class CouchDBConnection extends CouchDBClient.Component {
    private static final String PATH_DESIGN_MAIN = "_design/main";
    private static final String BULK_DOCS = "_bulk_docs";

    CouchDBConnection(CouchDBResponse resp, CouchDBClient client) throws CouchDBException {
        super(readDBName(resp),client);
    }
    
    static String readDBName(CouchDBResponse resp) throws CouchDBException {
        JSONObject details;
        try {
            details = resp.getBodyAsJSONObject();
        } catch (IOException ex) {
            throw new CouchDBException("Error reading JSON from http response", ex);
        }
        return details.get("db_name").toString();
    }
    
    public CouchDBSpatialView spatialView(String spatialViewName) {
        return new CouchDBSpatialView(client,this,"/_design/main/_spatial/" + spatialViewName);
    }
    
    public CouchDBView view(String viewName) {
        return new CouchDBView(client,this,"/_design/main/_view/" + viewName);
    }
    
    public String getName() {
        return root;
    }

    public void delete() throws IOException, CouchDBException {
        CouchDBResponse delete = client.delete(root);
        delete.checkOK("could not delete");
    }
    
    public void postBulk(JSONArray docs) throws IOException, CouchDBException {
        // @todo need to handle responses
        JSONObject obj = new JSONObject();
        obj.put("docs", docs);
        postBulk(obj.toJSONString());
    }
    
    public void postBulk(String content) throws IOException, CouchDBException {
        CouchDBResponse resp = client.post(uri(BULK_DOCS),content);
        // @todo need to handle responses
        resp.checkOK("Error posting bulk documents");
    }
    
    
    
    public void putDesignDocument(String content) throws IOException, CouchDBException {
        CouchDBResponse resp = client.put(uri(PATH_DESIGN_MAIN),content);
        resp.checkOK("Error putting design document");
    }
    
    public void putDesignDocument(File content) throws IOException, CouchDBException {
        CouchDBResponse resp = client.put(uri(PATH_DESIGN_MAIN),content);
        resp.checkOK("Error putting design document");
    }
    
    public JSONObject getDesignDocument() throws IOException {
        return client.get(uri("_design/main")).getBodyAsJSONObject();
    }

    public void postBulk(RequestEntity entity) throws IOException, CouchDBException {
        CouchDBResponse resp = client.post(uri(BULK_DOCS),entity);
        // @todo need to handle responses
        resp.checkOK("Error posting stream");
    }
    
}
