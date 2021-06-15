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
package org.archive.crawler.frontier;

import org.apache.commons.httpclient.URIException;
import org.archive.util.TmpDirTestCase;

/**
 * @author stack
 * @version $Date: 2006-09-26 20:38:48 +0000 (Tue, 26 Sep 2006) $, $Revision: 4667 $
 */
public class RecoveryJournalTest extends TmpDirTestCase {
    private RecoveryJournal rj;

    protected void setUp() throws Exception {
        super.setUp();
        this.rj = new RecoveryJournal(this.getTmpDir().getAbsolutePath(),
            this.getClass().getName());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        if (this.rj != null) {
            this.rj.close();
        }
    }

    public static void main(String [] args) {
        junit.textui.TestRunner.run(RecoveryJournalTest.class);
    }

    public void testAdded() throws URIException {
        /*
        CandidateURI c = new CandidateURI(UURIFactory.
            getInstance("http://www.archive.org"), "LLLLL",
            UURIFactory.getInstance("http://archive.org"),
            "L");
        this.rj.added(new CrawlURI(c, 0));
        this.rj.added(new CrawlURI(c, 1));
        this.rj.added(new CrawlURI(c, 2));
        */
    }

}
