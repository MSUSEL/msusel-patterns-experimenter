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

import java.util.Iterator;
import java.util.logging.Logger;

import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.deciderules.DecideRule;
import org.archive.crawler.deciderules.DecidingFilter;
import org.archive.crawler.framework.Filter;
import org.archive.crawler.settings.CrawlerSettings;
import org.archive.crawler.settings.MapType;
import org.archive.crawler.settings.SimpleType;

/**
 * OrFilter allows any number of other filters to be set up
 * inside it, as <filter> child elements. If any of those
 * children accept a presented object, the OrFilter will
 * also accept it.
 *
 * @author gojomo
 * @deprecated As of release 1.10.0.  Replaced by {@link DecidingFilter} and
 * {@link DecideRule}.
 */
public class OrFilter extends Filter {

    private static final long serialVersionUID = -6835737313105835112L;

    private static final Logger logger =
        Logger.getLogger(OrFilter.class.getName());
    public static final String ATTR_MATCH_RETURN_VALUE = "if-matches-return";
    public static final String ATTR_FILTERS = "filters";

    public OrFilter(String name, String description) {
        this(name);
        setDescription(description);
    }

    /**
     * @param name
     */
    public OrFilter(String name) {
        super(
            name,
            "OR Filter *Deprecated* Use DecidingFilter instead. " +
            "A filter that serves as a placeholder for other" +
            " filters whose functionality should be logically OR'ed together.");

        addElementToDefinition(
            new SimpleType(
                ATTR_MATCH_RETURN_VALUE,
                "What to return when one of the filters matches. \nIf true, "
                    + "this filter will return true if one of the subfilters "
                    + "return true, false otherwise. If false, this filter "
                    + "will return false if one of the subfilters"
                    + " returns true, false otherwise.",
                new Boolean(true)));

        addElementToDefinition(new MapType(ATTR_FILTERS,
                "List of filters whose functionality should be" +
                " logically or'ed together by the OrFilter.", Filter.class));
    }

    private MapType getFilters (Object o) {
        MapType filters = null;
        try {
            filters = (MapType)getAttribute(o, ATTR_FILTERS);
        } catch (AttributeNotFoundException e) {
            logger.severe(e.getLocalizedMessage());
        }
        return filters;
    }

    protected boolean innerAccepts(Object o) {
        if (isEmpty(o)) {
            return true;
        }
        for (Iterator iter = iterator(o); iter.hasNext();) {
            Filter f = (Filter)iter.next();
            if (f.accepts(o)) {
                return true;
            }
        }
        return false;
    }

    public void addFilter(CrawlerSettings settings, Filter f) {
        try {
            getFilters(settings).addElement(settings, f);
        } catch (InvalidAttributeValueException e) {
            logger.severe(e.getMessage());
        }
    }
    
    public boolean isEmpty(Object o) {
        return getFilters(o).isEmpty(o);
    }

    public Iterator iterator(Object o) {
        return getFilters(o).iterator(o);
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

    /**
     * Note that configuration updates may be necessary. Pass to 
     * constituent filters. 
     */
    public void kickUpdate() {
        // TODO: figure out if there's any way to reconcile this with
        // overrides/refinement filters 
        Iterator iter = iterator(null);
        while(iter.hasNext()) {
            ((Filter)iter.next()).kickUpdate();
        }
    }
}
