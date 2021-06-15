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
package org.archive.crawler.framework;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.AttributeNotFoundException;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.settings.ComplexType;
import org.archive.crawler.settings.MapType;
import org.archive.crawler.settings.ModuleType;
import org.archive.crawler.settings.SimpleType;

/**
 * Base class for filter classes.
 * <p>
 * Several classes allow 'filters' to be applied to them. Filters are classes
 * that, based on an arbitrary object passed to them, return a boolean stating
 * if if passes the filter. Thus applying filters can affect the behavior of
 * those classes. This class provides the basic framework for filters. All
 * detailed implementation of filters inherit from it and it is considered to
 * be a 'null' filter (always returns true).
 *
 * @author Gordon Mohr
 *
 * @see org.archive.crawler.framework.Processor
 */
public class Filter extends ModuleType {

    private static final long serialVersionUID = -356718306794776802L;

    private static Logger logger =
        Logger.getLogger("org.archive.crawler.framework.Filter");

    public static final String ATTR_ENABLED = "enabled";

    /**
     * Creates a new 'null' filter.
     * @param name the name of the filter.
     * @param description an description of the filter suitable for showing in
     * the user interface.
     */
    public Filter(String name, String description) {
        super(name, description);
        addElementToDefinition(
            new SimpleType(ATTR_ENABLED,
                "Filter is enabled.", new Boolean(true)));
    }

    /**
     * Creates a new 'null' filter.
     * @param name the name of the filter.
     */
    public Filter(String name) {
        this(name, "Null filter - accepts everything.");
    }

    public boolean accepts(Object o) {
        CrawlURI curi = (o instanceof CrawlURI) ? (CrawlURI) o : null;

        // Skip the evaluation if the filter is disabled
        try {
            if (!((Boolean)getAttribute(ATTR_ENABLED, curi)).booleanValue()) {
                return getFilterOffPosition(curi);
            }
        } catch (AttributeNotFoundException e) {
            logger.severe(e.getMessage());
        }

        boolean accept = returnTrueIfMatches(curi) == innerAccepts(o);
        if (accept && logger.isLoggable(Level.FINEST)) {
            // Log if filter returns true
            ComplexType p = this.getParent();
            if (p instanceof MapType) {
                p = p.getParent();
            }
            String msg = this.toString() + " belonging to " + p.toString()
                + " accepted " + o.toString();
            logger.finest(msg);
        }
        return accept;
    }
    
    /**
     * If the filter is disabled, the value returned by this method is
     * what filters return as their disabled setting.
     * Default is that we return 'true', continue processing, but some
     * filters -- the exclude filters for example -- will want to return
     * false if disabled so processing can continue.
     * @param curi CrawlURI to use as context. Passed curi can be null.
     * @return This filters 'off' position.
     */
    protected boolean getFilterOffPosition(CrawlURI curi) {
        return true;
    }

    /**
     * Checks to see if filter functionality should be inverted for this
     * curi.<p>
     *
     * All filters will by default return true if curi is accepted by the
     * filter. If this method returns false, then the filter will return true
     * if doesn't match.<p>
     *
     * Classes extending this class should override this method with
     * appropriate code.
     *
     * @param curi Current CrawlURI
     * @return true for default behaviour, false otherwise.
     */
    protected boolean returnTrueIfMatches(CrawlURI curi){
        return true;
    }

    /**
     * Classes subclassing this one should override this method to perfrom
     * their custom determination of whether or not the object given to it.
     *
     * @param o The object
     * @return True if it passes the filter.
     */
    protected boolean innerAccepts(Object o) {
        return true;
    }

    public String toString() {
        return "Filter<" + getName() + ">";
    }

    public void kickUpdate() {
        // by default, do nothing
    }
}
