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
package org.archive.crawler.filter;

import java.util.logging.Logger;

import javax.management.AttributeNotFoundException;

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.extractor.Link;
import org.archive.crawler.framework.CrawlScope;
import org.archive.crawler.framework.Filter;
import org.archive.crawler.scope.ClassicScope;

/**
 * Accepts (returns  for)) for all CandidateURIs passed in
 * with a link-hop-count greater than the max-link-hops
 * value.
 *
 * @author gojomo
 * @deprecated As of release 1.10.0.  Replaced by {@link DecidingFilter} and
 * equivalent {@link DecideRule}.
 */
public class HopsFilter extends Filter {

    private static final long serialVersionUID = -5943030310651023640L;

    private static final Logger logger =
        Logger.getLogger(HopsFilter.class.getName());

    /**
     * @param name
     */
    public HopsFilter(String name) {
        super(name, "Hops filter *Deprecated* Use" +
            "DecidingFilter and equivalent DecideRule instead");
    }

    int maxLinkHops = Integer.MAX_VALUE;
    int maxTransHops = Integer.MAX_VALUE;

    /* (non-Javadoc)
     * @see org.archive.crawler.framework.Filter#innerAccepts(java.lang.Object)
     */
    protected boolean innerAccepts(Object o) {
        if(! (o instanceof CandidateURI)) {
            return false;
        }
        String path = ((CandidateURI)o).getPathFromSeed();
        int linkCount = 0;
        int transCount = 0;
        for(int i=path.length()-1;i>=0;i--) {
            if(path.charAt(i)==Link.NAVLINK_HOP) {
                linkCount++;
            } else if (linkCount==0) {
                transCount++;
            }
        }
        if (o instanceof CrawlURI) {
            CrawlURI curi = (CrawlURI) o;
            CrawlScope scope =
                (CrawlScope) globalSettings().getModule(CrawlScope.ATTR_NAME);
            try {
                maxLinkHops =
                    ((Integer) scope
                        .getAttribute(ClassicScope.ATTR_MAX_LINK_HOPS, curi))
                        .intValue();
                maxTransHops =
                    ((Integer) scope
                        .getAttribute(ClassicScope.ATTR_MAX_TRANS_HOPS, curi))
                        .intValue();
            } catch (AttributeNotFoundException e) {
                logger.severe(e.getMessage());
                // Basically, true means the filter is PASSing this URI.
                return true; 
            }
        }

        return (linkCount > maxLinkHops)|| (transCount>maxTransHops);
    }
}
