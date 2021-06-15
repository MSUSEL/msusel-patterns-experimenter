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

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.deciderules.DecideRule;
import org.archive.crawler.deciderules.DecidingFilter;
import org.archive.crawler.framework.Filter;
import org.archive.crawler.settings.SimpleType;
import org.archive.net.UURI;

/**
 * Accepts all urls passed in with a path depth
 * less or equal than the max-path-depth
 * value.
 *
 * @author Igor Ranitovic
 * @deprecated As of release 1.10.0.  Replaced by {@link DecidingFilter} and
 * equivalent {@link DecideRule}.
 */
public class PathDepthFilter extends Filter {

    private static final long serialVersionUID = 1626115117327154205L;

    private static final Logger logger =
        Logger.getLogger(PathDepthFilter.class.getName());
    public static final String ATTR_MATCH_RETURN_VALUE =
        "path-less-or-equal-return";
    public static final String ATTR_MAX_PATH_DEPTH = "max-path-depth";
    Integer maxPathDepth = new Integer(Integer.MAX_VALUE);
    final static char slash = '/';

    /**
     * @param name
     */
    public PathDepthFilter(String name) {
        super(name, "Path depth less or equal filter  *Deprecated* Use" +
        		"DecidingFilter and equivalent DecideRule instead.");
        addElementToDefinition(new SimpleType(ATTR_MAX_PATH_DEPTH, "Max path" +
                " depth for which this filter will match", maxPathDepth));
        addElementToDefinition(new SimpleType(ATTR_MATCH_RETURN_VALUE,
                "What to return when path depth is less or equal to max path" +
                " depth. \n", new Boolean(true)));
    }

    protected boolean innerAccepts(Object o) {
        String path = null;
        if (o == null) {
            return false;
        }
        
        if (o instanceof CandidateURI) {
            try {
                if (((CandidateURI)o).getUURI() != null) {
                    path = ((CandidateURI)o).getUURI().getPath();
                }
            }
            catch (URIException e) {
                logger.severe("Failed getpath for " +
                    ((CandidateURI)o).getUURI());
            }
        } else if (o instanceof UURI) {
            try {
                path = ((UURI)o).getPath();
            }
            catch (URIException e) {
                logger.severe("Failed getpath for " + o);
            }
        }

        if (path == null) {
            return true;
        }

        int count = 0;
        for (int i = path.indexOf(slash); i != -1;
        		i = path.indexOf(slash, i + 1)) {
            count++;
        }
        
        if (o instanceof CrawlURI) {
            try {
                this.maxPathDepth = (Integer) getAttribute(
                        ATTR_MAX_PATH_DEPTH, (CrawlURI) o);
            } catch (AttributeNotFoundException e) {
                logger.severe(e.getMessage());
            }
        }
        
        return (this.maxPathDepth != null) ?
            count <= this.maxPathDepth.intValue():
            false;
    }

    protected boolean returnTrueIfMatches(CrawlURI curi) {
       try {
           return ((Boolean) getAttribute(ATTR_MATCH_RETURN_VALUE, curi)).
               booleanValue();
       } catch (AttributeNotFoundException e) {
           logger.severe(e.getMessage());
           return true;
       }
    }
    
    protected boolean getFilterOffPosition(CrawlURI curi) {
        return returnTrueIfMatches(curi);
    }
}