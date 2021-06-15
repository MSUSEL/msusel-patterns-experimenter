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
package org.archive.crawler.settings.refinements;

import org.archive.net.UURI;
import org.archive.util.TextUtils;


/**
 * A refinement criteria that test if a URI matches a regular expression.
 *
 * @author John Erik Halse
 */
public class RegularExpressionCriteria implements Criteria {
    private String regexp = "";

    /**
     * Create a new instance of RegularExpressionCriteria.
     */
    public RegularExpressionCriteria() {
        super();
    }

    /**
     * Create a new instance of RegularExpressionCriteria initializing it with
     * a regular expression.
     *
     * @param regexp the regular expression for this criteria.
     */
    public RegularExpressionCriteria(String regexp) {
        setRegexp(regexp);
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.settings.refinements.Criteria#isWithinRefinementBounds(org.archive.crawler.datamodel.UURI, int)
     */
    public boolean isWithinRefinementBounds(UURI uri) {
        return (uri == null || uri == null)?
            false: TextUtils.matches(regexp, uri.toString());
    }

    /**
     * Get the regular expression to be matched against a URI.
     *
     * @return Returns the regexp.
     */
    public String getRegexp() {
        return regexp;
    }
    /**
     * Set the regular expression to be matched against a URI.
     *
     * @param regexp The regexp to set.
     */
    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.settings.refinements.Criteria#getName()
     */
    public String getName() {
        return "Regular expression criteria";
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.settings.refinements.Criteria#getDescription()
     */
    public String getDescription() {
        return "Accept URIs that match the following regular expression: "
            + getRegexp();
    }
}
