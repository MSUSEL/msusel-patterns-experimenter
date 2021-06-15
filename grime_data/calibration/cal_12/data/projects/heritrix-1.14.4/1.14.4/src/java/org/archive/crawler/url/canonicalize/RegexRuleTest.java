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
package org.archive.crawler.url.canonicalize;

import java.io.File;

import javax.management.InvalidAttributeValueException;

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CrawlOrder;
import org.archive.crawler.settings.MapType;
import org.archive.crawler.settings.XMLSettingsHandler;
import org.archive.net.UURIFactory;
import org.archive.util.TmpDirTestCase;


/**
 * Test the regex rule.
 * @author stack
 * @version $Date: 2005-07-18 17:30:21 +0000 (Mon, 18 Jul 2005) $, $Revision: 3704 $
 */
public class RegexRuleTest extends TmpDirTestCase {
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
    }
    
    public void testCanonicalize()
    throws URIException, InvalidAttributeValueException {
        final String url = "http://www.aRchive.Org/index.html";
        RegexRule rr = new RegexRule("Test " + this.getClass().getName());
        this.rules.addElement(null, rr);
        rr.canonicalize(url, UURIFactory.getInstance(url));
        String product = rr.canonicalize(url, null);
        assertTrue("Default doesn't work.",  url.equals(product));
    }

    public void testSessionid()
    throws InvalidAttributeValueException {
        final String urlBase = "http://joann.com/catalog.jhtml";
        final String urlMinusSessionid = urlBase + "?CATID=96029";
        final String url = urlBase +
		    ";$sessionid$JKOFFNYAAKUTIP4SY5NBHOR50LD3OEPO?CATID=96029";
        RegexRule rr = new RegexRule("Test",
            "^(.+)(?:;\\$sessionid\\$[A-Z0-9]{32})(\\?.*)+$",
        	"$1$2");
        this.rules.addElement(null, rr);
        String product = rr.canonicalize(url, null);
        assertTrue("Failed " + url, urlMinusSessionid.equals(product));
    }
    
    public void testNullFormat()
    throws InvalidAttributeValueException {
        final String urlBase = "http://joann.com/catalog.jhtml";
        final String url = urlBase +
            ";$sessionid$JKOFFNYAAKUTIP4SY5NBHOR50LD3OEPO";
        RegexRule rr = new RegexRule("Test",
            "^(.+)(?:;\\$sessionid\\$[A-Z0-9]{32})$",
            "$1$2");
        this.rules.addElement(null, rr);
        String product = rr.canonicalize(url, null);
        assertTrue("Failed " + url, urlBase.equals(product));
    }
}
