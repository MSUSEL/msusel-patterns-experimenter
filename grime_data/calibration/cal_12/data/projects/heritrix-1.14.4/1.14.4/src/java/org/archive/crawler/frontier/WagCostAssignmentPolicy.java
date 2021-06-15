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
/* WagCostAssignmentPolicy
*
* $Id: WagCostAssignmentPolicy.java 3704 2005-07-18 17:30:21Z stack-sf $
*
* Created on Dec 10, 2004
*
* Copyright (C) 2004 Internet Archive.
*
* This file is part of the Heritrix web crawler (crawler.archive.org).
*
* Heritrix is free software; you can redistribute it and/or modify
* it under the terms of the GNU Lesser Public License as published by
* the Free Software Foundation; either version 2.1 of the License, or
* any later version.
*
* Heritrix is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser Public License for more details.
*
* You should have received a copy of the GNU Lesser Public License
* along with Heritrix; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/ 
package org.archive.crawler.frontier;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.net.UURI;

/**
 * A CostAssignmentPolicy based on some wild guesses of kinds of URIs
 * that should be deferred into the (potentially never-crawled) future.
 * 
 * @author gojomo
 */
public class WagCostAssignmentPolicy extends CostAssignmentPolicy {

    /**
     * Add constant penalties for certain features of URI (and
     * its 'via') that make it more delayable/skippable. 
     * 
     * @param curi CrawlURI to be assigned a cost
     * 
     * @see org.archive.crawler.frontier.CostAssignmentPolicy#costOf(org.archive.crawler.datamodel.CrawlURI)
     */
    public int costOf(CrawlURI curi) {
        int cost = 1;
        UURI uuri = curi.getUURI();
        if (uuri.hasQuery()) {
            // has query string
            cost++;
            int qIndex = uuri.toString().indexOf('?');
            if (curi.flattenVia().startsWith(uuri.toString().substring(0,qIndex))) {
                // non-query-string portion of URI is same as previous
                cost++;
            }
            // TODO: other potential query-related cost penalties:
            //  - more than X query-string attributes
            //  - calendarish terms
            //  - query-string over certain size
        }
        // TODO: other potential path-based penalties
        //  - new path is simply extension of via path
        //  - many path segments
        // TODO: other potential hops-based penalties
        //  - more than X hops
        //  - each speculative hop
        return cost;
    }
}
