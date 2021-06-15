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
package org.archive.crawler.scope;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.net.UURI;

/**
 * A CrawlScope that caches its seed list for the
 * convenience of scope-tests that are based on the 
 * seeds. 
 *
 * @author gojomo
 *
 */
public class SeedCachingScope extends ClassicScope {

    private static final long serialVersionUID = 300230673616424926L;

    //private static final Logger logger =
    //    Logger.getLogger(SeedCachingScope.class.getName());
    List<UURI> seeds;

    public SeedCachingScope(String name) {
        super(name);
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.framework.CrawlScope#addSeed(org.archive.crawler.datamodel.UURI)
     */
    public boolean addSeed(CrawlURI curi) {
        if (super.addSeed(curi) == false) {
            // failed
            return false;
        }
        // FIXME: This is not thread-safe.
        List<UURI> newSeeds = new ArrayList<UURI>(seeds);
        newSeeds.add(curi.getUURI());
        seeds = newSeeds;
        return true;
    }
    
    /* (non-Javadoc)
     * @see org.archive.crawler.framework.CrawlScope#refreshSeeds()
     */
    public synchronized void refreshSeeds() {
        super.refreshSeeds();
        seeds = null;
        fillSeedsCache();
    }
    
    /* (non-Javadoc)
     * @see org.archive.crawler.framework.CrawlScope#seedsIterator()
     */
    public Iterator<UURI> seedsIterator() {
        fillSeedsCache();
        return seeds.iterator();
    }

    /**
     * Ensure seeds cache is created/filled
     */
    protected synchronized void fillSeedsCache() {
        if (seeds==null) {
            seeds = new ArrayList<UURI>();
            Iterator<UURI> iter = super.seedsIterator();
            while(iter.hasNext()) {
                seeds.add(iter.next());
            }
        }
    }
}
