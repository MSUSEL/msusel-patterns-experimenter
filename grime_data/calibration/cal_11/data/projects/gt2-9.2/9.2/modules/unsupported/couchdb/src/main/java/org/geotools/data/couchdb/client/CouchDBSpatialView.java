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
import org.apache.commons.httpclient.NameValuePair;
import org.json.simple.JSONObject;

/**
 *
 * @author Ian Schneider (OpenGeo)
 *
 * @source $URL$
 */
public class CouchDBSpatialView extends CouchDBViewSupport {

    public CouchDBSpatialView(CouchDBClient client, CouchDBConnection connection, String path) {
        super(client, connection, path);
    }
    
    private NameValuePair bbox(double llx,double lly,double urx,double ury) {
        return new NameValuePair("bbox",llx + "," + lly + "," + urx + "," + ury);
    }
    
    public JSONObject get(double llx,double lly,double urx,double ury) throws IOException, CouchDBException {
        return get(bbox(llx, lly, urx, ury));
    }
    
    public long count(double llx,double lly,double urx,double ury) throws IOException, CouchDBException {
        JSONObject count = get(bbox(llx, lly, urx, ury),new NameValuePair("count","true"));
        return (Long) count.get("count");
    }
    
}
