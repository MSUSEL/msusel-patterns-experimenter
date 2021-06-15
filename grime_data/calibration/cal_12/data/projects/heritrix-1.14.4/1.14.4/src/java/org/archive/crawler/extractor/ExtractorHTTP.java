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
package org.archive.crawler.extractor;

import java.util.logging.Logger;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.framework.Processor;

/**
 * Extracts URIs from HTTP response headers.
 * @author gojomo
 */
public class ExtractorHTTP extends Processor
implements CoreAttributeConstants {

    private static final long serialVersionUID = 8499072198570554647L;

    private static final Logger LOGGER =
        Logger.getLogger(ExtractorHTTP.class.getName());
    protected long numberOfCURIsHandled = 0;
    protected long numberOfLinksExtracted = 0;

    public ExtractorHTTP(String name) {
        super(name,
            "HTTP extractor. Extracts URIs from HTTP response headers.");
    }

    public void innerProcess(CrawlURI curi) {
        if (!curi.isHttpTransaction() || curi.getFetchStatus() <= 0) {
            // If not http or if an error status code, skip.
            return;
        }
        numberOfCURIsHandled++;
        HttpMethod method = (HttpMethod)curi.getObject(A_HTTP_TRANSACTION);
        addHeaderLink(curi, method.getResponseHeader("Location"));
        addHeaderLink(curi, method.getResponseHeader("Content-Location"));
    }

    protected void addHeaderLink(CrawlURI curi, Header loc) {
        if (loc == null) {
            // If null, return without adding anything.
            return;
        }
        // TODO: consider possibility of multiple headers
        try {
            curi.createAndAddLink(loc.getValue(), loc.getName() + ":",
                Link.REFER_HOP);
            numberOfLinksExtracted++;
        } catch (URIException e) {
            // There may not be a controller (e.g. If we're being run
            // by the extractor tool).
            if (getController() != null) {
                getController().logUriError(e, curi.getUURI(), loc.getValue());
            } else {
                LOGGER.info(curi + ", " + loc.getValue() + ": " +
                    e.getMessage());
            }
        }

    }

    public String report() {
        StringBuffer ret = new StringBuffer();
        ret.append("Processor: org.archive.crawler.extractor.ExtractorHTTP\n");
        ret.append("  Function:          " +
            "Extracts URIs from HTTP response headers\n");
        ret.append("  CrawlURIs handled: " + numberOfCURIsHandled + "\n");
        ret.append("  Links extracted:   " + numberOfLinksExtracted + "\n\n");
        return ret.toString();
    }
}
