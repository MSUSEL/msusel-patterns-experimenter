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
package org.archive.crawler.url;

import java.io.File;

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CrawlOrder;
import org.archive.crawler.settings.MapType;
import org.archive.crawler.settings.XMLSettingsHandler;
import org.archive.crawler.url.canonicalize.FixupQueryStr;
import org.archive.crawler.url.canonicalize.LowercaseRule;
import org.archive.crawler.url.canonicalize.StripSessionIDs;
import org.archive.crawler.url.canonicalize.StripUserinfoRule;
import org.archive.crawler.url.canonicalize.StripWWWRule;
import org.archive.net.UURIFactory;
import org.archive.util.TmpDirTestCase;

/**
 * Test canonicalization.
 * @author stack
 * @version $Date: 2006-09-26 20:38:48 +0000 (Tue, 26 Sep 2006) $, $Revision: 4667 $
 */
public class CanonicalizerTest extends TmpDirTestCase {
    private File orderFile;
    protected XMLSettingsHandler settingsHandler;

    private MapType rules = null;
    
    protected void setUp() throws Exception {
        super.setUp();
        this.orderFile = new File(getTmpDir(), this.getClass().getName() +
            ".order.xml");
        this.settingsHandler = new XMLSettingsHandler(orderFile);
        this.settingsHandler.initialize();
        
        this.rules = (MapType)(settingsHandler.getSettingsObject(null)).
            getModule(CrawlOrder.ATTR_NAME).
               getAttribute(CrawlOrder.ATTR_RULES);
        this.rules.addElement(null, new LowercaseRule("lowercase"));
        this.rules.addElement(null, new StripUserinfoRule("userinfo"));
        this.rules.addElement(null, new StripWWWRule("www"));
        this.rules.addElement(null, new StripSessionIDs("ids"));
        this.rules.addElement(null, new FixupQueryStr("querystr"));
    }
    
    public void testCanonicalize() throws URIException {
        final String scheme = "http://";
        final String nonQueryStr = "archive.org/index.html";
        final String result = scheme + nonQueryStr;
        assertTrue("Mangled original", result.equals(
            Canonicalizer.canonicalize(UURIFactory.getInstance(result),
                this.rules.iterator(UURIFactory.getInstance(result)))));
        String tmp = scheme + "www." + nonQueryStr;
        assertTrue("Mangled www", result.equals(
            Canonicalizer.canonicalize(UURIFactory.getInstance(tmp),
                this.rules.iterator(UURIFactory.getInstance(result)))));
        tmp = scheme + "www." + nonQueryStr +
            "?jsessionid=01234567890123456789012345678901";
        assertTrue("Mangled sessionid", result.equals(
            Canonicalizer.canonicalize(UURIFactory.getInstance(tmp),
                this.rules.iterator(UURIFactory.getInstance(result)))));
        tmp = scheme + "www." + nonQueryStr +
            "?jsessionid=01234567890123456789012345678901";
        assertTrue("Mangled sessionid", result.equals(
             Canonicalizer.canonicalize(UURIFactory.getInstance(tmp),
                   this.rules.iterator(UURIFactory.getInstance(result)))));       
    }
}
