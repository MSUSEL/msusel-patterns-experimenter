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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.framework.Processor;
import org.archive.crawler.frontier.AdaptiveRevisitAttributeConstants;

/**
 * Set a URI to be revisited by the ARFrontier. This only makes sense when using
 * the ARFrontier and a decide-rule chain granting only selected access to this
 * processor. This is the opposite of the RejectRevisitProcessor class.
 * 
 * @author mzsanford
 */
public class AcceptRevisitProcessor extends Processor implements
        AdaptiveRevisitAttributeConstants {
    private static final long serialVersionUID = 4310432303089418844L;

    private static final Logger logger = Logger
            .getLogger(AcceptRevisitProcessor.class.getName());

    public AcceptRevisitProcessor(String name) {
        super(name, "Set a URI to be revisited by the ARFrontier.");
    }

    @Override
    protected void initialTasks() {
        CrawlURI.addAlistPersistentMember(A_DISCARD_REVISIT);
    }

    @Override
    protected void innerProcess(CrawlURI curi) throws InterruptedException {
        if (curi != null && curi.containsKey(A_DISCARD_REVISIT)) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Removing DISCARD_REVISIT boolean from crawl URI: "
                        + curi.getUURI().toString());
            }
            curi.remove(A_DISCARD_REVISIT);
        }
    }

}
