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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.teradata;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread local table that holds transaction local query bands.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 *
 *
 * @source $URL$
 */
public class QueryBand {

    /**
     * Session query band for name of application.
     */
    public static String APPLICATION = "Application";
    
    /**
     * Local query band for user name executing request.
     */
    public static String CLIENT_USER = "ClientUser";
    /**
     * Local query band for host name executing request.
     */
    public static String CLIENT_HOST = "ClientHostName";
    /**
     * Local query band for layer being worked against.
     */
    public static String LAYER = "Layer";
    /**
     * Local query band for type of statement, SELECT, DELETE, etc...
     */
    public static String PROCESS = "Process";
    
    static ThreadLocal<Map<String,String>> LOCAL = new ThreadLocal<Map<String,String>>() {
        protected Map<String,String> initialValue() {
            return new HashMap<String, String>();
        };
    };
    
    /**
     * Returns local query band map.
     */
    public static Map<String,String> local() {
        return LOCAL.get();
    }
    
    /**
     * Clears the local query band thread local.
     */
    public static void remove() {
        LOCAL.remove();
    }

}
