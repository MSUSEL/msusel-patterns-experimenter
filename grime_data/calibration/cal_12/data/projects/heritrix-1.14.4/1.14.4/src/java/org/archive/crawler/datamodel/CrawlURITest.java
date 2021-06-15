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
package org.archive.crawler.datamodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.httpclient.URIException;
import org.archive.net.UURIFactory;
import org.archive.util.TmpDirTestCase;

/**
 * @author stack
 * @version $Revision: 3771 $, $Date: 2005-08-29 21:52:36 +0000 (Mon, 29 Aug 2005) $
 */
public class CrawlURITest extends TmpDirTestCase {
    
    CrawlURI seed = null;
    
    protected void setUp() throws Exception {
        super.setUp();
        final String url = "http://www.dh.gov.uk/Home/fs/en";
        this.seed = new CrawlURI(UURIFactory.getInstance(url));
        this.seed.setSchedulingDirective(CandidateURI.MEDIUM);
        this.seed.setIsSeed(true);
        // Force caching of string.
        this.seed.toString();
        // TODO: should this via really be itself?
        this.seed.setVia(UURIFactory.getInstance(url));
    }

    /**
     * Test serialization/deserialization works.
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     */
    final public void testSerialization()
    		throws IOException, ClassNotFoundException {
        File serialize = new File(getTmpDir(), 
            this.getClass().getName() + ".serialize");
        try {
            FileOutputStream fos = new FileOutputStream(serialize);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.seed);
            oos.reset();
            oos.writeObject(this.seed);
            oos.reset();
            oos.writeObject(this.seed);
            oos.close();
            // Read in the object.
            FileInputStream fis = new FileInputStream(serialize);
            ObjectInputStream ois = new ObjectInputStream(fis);
            CrawlURI deserializedCuri = (CrawlURI)ois.readObject();
            deserializedCuri = (CrawlURI)ois.readObject();
            deserializedCuri = (CrawlURI)ois.readObject();
            assertTrue("Deserialized not equal to original",
                this.seed.toString().equals(deserializedCuri.toString()));
            String host = this.seed.getUURI().getHost();
            assertTrue("Deserialized host not null",
                host != null && host.length() >= 0);
        } finally {
            serialize.delete();
        }
    }
    
    public void testCandidateURIWithLoadedAList()
    throws URIException {
        CandidateURI c = CandidateURI.
            createSeedCandidateURI(UURIFactory.
                getInstance("http://www.archive.org"));
        c.putString("key", "value");
        CrawlURI curi = new CrawlURI(c, 0);
        assertTrue("Didn't find AList item",
            curi.getString("key").equals("value"));
    }
    
// TODO: move to QueueAssignmentPolicies
//    public void testCalculateClassKey() throws URIException {
//        final String uri = "http://mprsrv.agri.gov.cn";
//        CrawlURI curi = new CrawlURI(UURIFactory.getInstance(uri));
//        String key = curi.getClassKey();
//        assertTrue("Key1 is bad " + key,
//            key.equals(curi.getUURI().getAuthorityMinusUserinfo()));
//    	final String baduri = "ftp://pfbuser:pfbuser@mprsrv.agri.gov.cn/clzreceive/";
//        curi = new CrawlURI(UURIFactory.getInstance(baduri));
//        key = curi.getClassKey();
//        assertTrue("Key2 is bad " + key,
//            key.equals(curi.getUURI().getAuthorityMinusUserinfo()));
//	}
}
