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
package org.archive.crawler.postprocessor;


import java.util.logging.Logger;

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.crawler.datamodel.CrawlServer;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.datamodel.FetchStatusCodes;
import org.archive.crawler.framework.Processor;


/**
 * A step, late in the processing of a CrawlURI, for updating the per-host
 * information that may have been affected by the fetch. This will initially
 * be robots and ip address info; it could include other per-host stats that
 * would affect the crawl (like total pages visited at the site) as well.
 *
 * @author gojomo
 * @version $Date: 2010-04-02 01:03:46 +0000 (Fri, 02 Apr 2010) $, $Revision: 6803 $
 */
public class CrawlStateUpdater extends Processor implements
        CoreAttributeConstants, FetchStatusCodes {

    private static final long serialVersionUID = -1072728147960180091L;

    private static final Logger logger =
        Logger.getLogger(CrawlStateUpdater.class.getName());

    public CrawlStateUpdater(String name) {
        super(name, "Crawl state updater");
    }

    protected void innerProcess(CrawlURI curi) {
        CrawlServer server =
            getController().getServerCache().getServerFor(curi);
        
        String scheme = curi.getUURI().getScheme().toLowerCase();
        if (scheme.equals("http") || scheme.equals("https") &&
                server != null) {
            // Update connection problems counter
            if( curi.getFetchStatus() == S_CONNECT_FAILED || curi.getFetchStatus() == S_CONNECT_LOST) {
                server.incrementConsecutiveConnectionErrors();
            } else if (curi.getFetchStatus() > 0){
                server.resetConsecutiveConnectionErrors();
            }

            // Update robots info
            try {
                if ("/robots.txt".equals(curi.getUURI().getPath())) {
                    // Update server with robots info
                    // NOTE, this *can* change the curi's fetchStatus from a connection
                    // problem to S_DEEMED_NOT_FOUND to prevent further retries
                    server.updateRobots(curi);
                }
            }
            catch (URIException e) {
                logger.severe("Failed get path on " + curi.getUURI());
            }
        }
    }
}
