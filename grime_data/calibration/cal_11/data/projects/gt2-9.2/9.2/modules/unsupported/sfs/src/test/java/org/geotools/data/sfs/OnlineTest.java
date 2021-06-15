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
package org.geotools.data.sfs;

import java.io.Serializable;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.sfs.SFSDataStoreFactory;
import org.geotools.data.sfs.mock.MockSimpleFeatureService;

import junit.framework.TestCase;

/**
 * To have the online tests run start the {@link MockSimpleFeatureService} class 
 *
 *
 *
 * @source $URL$
 */
public abstract class OnlineTest extends TestCase {

    protected static final String URL = "http://localhost:8082/simplefeatureservice/";
    protected static final String NAMESPACE = "http://geo-solutions.it/sfs";
    protected static final boolean ONLINE_TEST;

    static {
        // check if the URL is online
        boolean test = false;
        try {
            URLConnection connection = new java.net.URL(URL).openConnection();
            connection.connect();
            test = true;
        } catch (Exception e) {

            test = false;
        }
        ONLINE_TEST = test;
    }

    protected static Map<String, Serializable> createParams() {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(SFSDataStoreFactory.URLP.key, URL);
        params.put(SFSDataStoreFactory.NAMESPACEP.key, NAMESPACE);
        return params;
    }

    public OnlineTest() {
        super();
    }

    public OnlineTest(String name) {
        super(name);
    }

    protected boolean onlineTest(String testName) {
        if (!ONLINE_TEST) {
            System.out.println(testName + " Test skipped");
            return false;
        }
        return true;
    }
}
