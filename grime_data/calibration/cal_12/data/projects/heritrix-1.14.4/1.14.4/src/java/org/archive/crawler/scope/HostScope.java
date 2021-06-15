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
package org.archive.crawler.scope;

import java.util.Iterator;

import org.archive.crawler.deciderules.DecidingScope;
import org.archive.crawler.filter.FilePatternFilter;
import org.archive.crawler.filter.TransclusionFilter;
import org.archive.crawler.framework.Filter;
import org.archive.net.UURI;

/**
 * A core CrawlScope suitable for the most common
 * crawl needs.
 *
 * Roughly, its logic is that a URI is included if:
 *
 *    (( isSeed(uri) || focusFilter.accepts(uri) )
 *      || transitiveFilter.accepts(uri) )
 *     && ! excludeFilter.accepts(uri)
 *
 * The focusFilter may be specified by either:
 *   - adding a 'mode' attribute to the
 *     <code>scope</code> element. mode="broad" is equivalent
 *     to no focus; modes "path", "host", and "domain"
 *     imply a SeedExtensionFilter will be used, with
 *     the <code>scope</code> element providing its configuration
 *   - adding a <code>focus</code> subelement
 * If unspecified, the focusFilter will default to
 * an accepts-all filter.
 *
 * The transitiveFilter may be specified by supplying
 * a <code>transitive</code> subelement. If unspecified, a
 * TransclusionFilter will be used, with the <code>scope</code>
 * element providing its configuration.
 *
 * The excludeFilter may be specified by supplying
 * a <code>exclude</code> subelement. If unspecified, a
 * accepts-none filter will be used -- meaning that
 * no URIs will pass the filter and thus be excluded.
 *
 * @author gojomo
 * @deprecated As of release 1.10.0.  Replaced by {@link DecidingScope}.
 */
public class HostScope extends SeedCachingScope {

    private static final long serialVersionUID = -6257664892667267266L;

    public static final String ATTR_TRANSITIVE_FILTER = "transitiveFilter";
    public static final String ATTR_ADDITIONAL_FOCUS_FILTER =
        "additionalScopeFocus";

    Filter additionalFocusFilter;
    Filter transitiveFilter;

    public HostScope(String name) {
        super(name);
        setDescription(
            "HostScope: A scope for host crawls *Deprecated* Use " +
            "DecidingScope instead. Crawls made with this scope" +
            " will be limited to the hosts its seeds. Thus if one of" +
            " the seeds is 'archive.org' the subdomain" +
            " 'crawler.archive.org' will not be crawled." +
            " 'www.host' is considered to be the same as host.");
       additionalFocusFilter = (Filter) addElementToDefinition(
                new FilePatternFilter(ATTR_ADDITIONAL_FOCUS_FILTER));
        this.transitiveFilter = (Filter) addElementToDefinition(
                new TransclusionFilter(ATTR_TRANSITIVE_FILTER));
    }

    /**
     * @param o
     * @return True if transitive filter accepts passed object.
     */
    protected boolean transitiveAccepts(Object o) {
        if (this.transitiveFilter == null) {
            return true;
        }
        return this.transitiveFilter.accepts(o);
    }

    /**
     * @param o
     * @return True if focus filter accepts passed object.
     */
    protected boolean focusAccepts(Object o) {
        UURI u = UURI.from(o);
        if (u == null) {
            return false;
        }
        // Get the seeds to refresh 
        Iterator iter = seedsIterator();
        while(iter.hasNext()) {
            if (isSameHost((UURI)iter.next(), u)) {
                checkClose(iter);
                return true;
            }
        }
        // if none found, fail
        checkClose(iter);
        return false;
    }

   
    // Javadoc inherited.
    @Override
    protected boolean additionalFocusAccepts(Object o) {
        return additionalFocusFilter.accepts(o);
    }

}
