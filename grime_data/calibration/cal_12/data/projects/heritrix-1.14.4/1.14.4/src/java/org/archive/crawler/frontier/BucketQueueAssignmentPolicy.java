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
import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CrawlHost;
import org.archive.crawler.framework.CrawlController;

/**
 * Uses the target IPs as basis for queue-assignment,
 * distributing them over a fixed number of sub-queues.
 * 
 * @author Christian Kohlschuetter
 */
public class BucketQueueAssignmentPolicy extends QueueAssignmentPolicy {
    private static final int DEFAULT_NOIP_BITMASK = 1023;
    private static final int DEFAULT_QUEUES_HOSTS_MODULO = 1021;

    public String getClassKey(final CrawlController controller,
        final CandidateURI curi) {
        
        CrawlHost host;
        try {
            host = controller.getServerCache().getHostFor(
                curi.getUURI().getReferencedHost());
        } catch (URIException e) {
            // FIXME error handling
            e.printStackTrace();
            host = null;
        }
        if(host == null) {
            return "NO-HOST";
        } else if(host.getIP() == null) {
            return "NO-IP-".concat(Integer.toString(Math.abs(host.getHostName()
                .hashCode())
                & DEFAULT_NOIP_BITMASK));
        } else {
            return Integer.toString(Math.abs(host.getIP().hashCode())
                % DEFAULT_QUEUES_HOSTS_MODULO);
        }
    }

    public int maximumNumberOfKeys() {
        return DEFAULT_NOIP_BITMASK + DEFAULT_QUEUES_HOSTS_MODULO + 2;
    }
}
