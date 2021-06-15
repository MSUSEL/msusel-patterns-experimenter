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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.AttributeNotFoundException;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.deciderules.DecideRule;
import org.archive.crawler.deciderules.DecidingFilter;
import org.archive.crawler.framework.Filter;
import org.archive.crawler.settings.SimpleType;
import org.archive.util.TextUtils;


/**
 * Compares passed object -- a CrawlURI, UURI, or String --
 * against a regular expression, accepting matches.
 *
 * @author Gordon Mohr
 * @deprecated As of release 1.10.0.  Replaced by {@link DecidingFilter} and
 * equivalent {@link DecideRule}.
 */
public class URIRegExpFilter
extends Filter {

    private static final long serialVersionUID = 1878356276332865537L;

    private static final Logger logger =
        Logger.getLogger(URIRegExpFilter.class.getName());
    public static final String ATTR_REGEXP = "regexp";
    public static final String ATTR_MATCH_RETURN_VALUE = "if-match-return";

    /**
     * @param name Filter name.
     */
    public URIRegExpFilter(String name) {
        this(name, "URI regexp filter *Deprecated* Use DecidingFilter and " +
        	"equivalent DecideRule instead. ", "");
        addElementToDefinition(
            new SimpleType(ATTR_MATCH_RETURN_VALUE, "What to return when" +
                " regular expression matches. \n", new Boolean(true)));
        addElementToDefinition(
            new SimpleType(ATTR_REGEXP, "Java regular expression.", ""));
    }

    public URIRegExpFilter(String name, String regexp) {
        this(name, "URI regexp filter.", regexp);
    }

    protected URIRegExpFilter(String name, String description, String regexp) {
        super(name, description);
        addElementToDefinition(new SimpleType(ATTR_MATCH_RETURN_VALUE,
            "What to return when" + " regular expression matches. \n",
            new Boolean(true)));
        addElementToDefinition(new SimpleType(ATTR_REGEXP,
            "Java regular expression.", regexp)); 
    }

    protected boolean innerAccepts(Object o) {
        String regexp = getRegexp(o);
        String str = o.toString();
        boolean result = (regexp == null)?
            false: TextUtils.matches(regexp, str);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Tested '" + str + "' match with regex '" +
                getRegexp(o) + " and result was " + result);
        }
        return result;
    }

    /** 
     * Get the regular expression string to match the URI against.
     *
     * @param o the object for which the regular expression should be
     *          matched against.
     * @return the regular expression to match against.
     */
    protected String getRegexp(Object o) {
        try {
            return (String) getAttribute(o, ATTR_REGEXP);
        } catch (AttributeNotFoundException e) {
            logger.severe(e.getMessage());
            // Basically the filter is inactive if this occurs
            // (The caller should be returning false when regexp is null).
            return null;  
        }
    }

    protected boolean returnTrueIfMatches(CrawlURI curi) {
        try {
            return ((Boolean)getAttribute(ATTR_MATCH_RETURN_VALUE, curi)).
                booleanValue();
        } catch (AttributeNotFoundException e) {
            logger.severe(e.getMessage());
            return true;
        }
    }
}
