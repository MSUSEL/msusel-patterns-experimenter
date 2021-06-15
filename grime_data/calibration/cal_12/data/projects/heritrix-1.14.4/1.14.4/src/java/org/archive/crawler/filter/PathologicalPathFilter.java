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

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.deciderules.DecideRule;
import org.archive.crawler.deciderules.DecidingFilter;
import org.archive.crawler.settings.SimpleType;
import org.archive.crawler.settings.Type;

/** 
 * Checks if a URI contains a repeated pattern.
 *
 * This filter is checking if a pattern is repeated a specific number of times.
 * The use is to avoid crawler traps where the server adds the same pattern to
 * the requested URI like: <code>http://host/img/img/img/img....</code>. This
 * filter returns TRUE if the path is pathological.  FALSE otherwise.
 *
 * @author John Erik Halse
 * @deprecated As of release 1.10.0.  Replaced by {@link DecidingFilter} and
 * equivalent {@link DecideRule}.
 */
public class PathologicalPathFilter extends URIRegExpFilter {

    private static final long serialVersionUID = 2797805167250054353L;

    private static final Logger logger =
        Logger.getLogger(PathologicalPathFilter.class.getName());

    public static final String ATTR_REPETITIONS = "repetitions";

    public static final Integer DEFAULT_REPETITIONS = new Integer(3);
    
    private final String REGEX_PREFIX = ".*?/(.*?/)\\1{";
    private final String REGEX_SUFFIX = ",}.*";

    /** Constructs a new PathologicalPathFilter.
     *
     * @param name the name of the filter.
     */
    public PathologicalPathFilter(String name) {
        super(name);
        setDescription("Pathological path filter *Deprecated* Use" +
        		"DecidingFilter and equivalent DecideRule instead. " +
        		"The Pathologicalpath filter" +
                " is used to avoid crawler traps by adding a constraint on" +
                " how many times a pattern in the URI could be repeated." +
                " Returns false if the path is NOT pathological (There" +
                " are no subpath reptitions or reptitions are less than" +
                " the '" + ATTR_REPETITIONS + "' limit).");

        Type type = getElementFromDefinition(ATTR_MATCH_RETURN_VALUE);
        type.setTransient(true);

        type = getElementFromDefinition(ATTR_REGEXP);
        type.setTransient(true);

        addElementToDefinition(new SimpleType(ATTR_REPETITIONS,
                "Number of times the pattern should be allowed to occur. \n" +
                "This filter returns true if number of repetitions of a" +
                " pattern exceeds this value",
                DEFAULT_REPETITIONS));
    }

    /** 
     * Construct the regexp string to be matched aginst the URI.
     * @param o an object to extract a URI from.
     * @return the regexp pattern.
     */
    protected String getRegexp(Object o) {
        int rep = 0;
        try {
            rep = ((Integer)getAttribute(o, ATTR_REPETITIONS)).intValue();
        } catch (AttributeNotFoundException e) {
            logger.severe(e.getMessage());
        }
        return rep == 0? null: REGEX_PREFIX + (rep - 1) + REGEX_SUFFIX;
    }
    
    protected boolean getFilterOffPosition(CrawlURI curi) {
        return false;
    }
}
