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
package org.archive.crawler.settings;

import java.util.logging.Level;

/**
 * A constraint that checks that an attribute value matches one of the items in
 * the list of legal values.
 *
 * @author John Erik Halse
 */
public class LegalValueListConstraint extends Constraint {

    private static final long serialVersionUID = -4293290799574408033L;

    /**
     * Constructs a new LegalValueListConstraint.
     *
     * @param level the severity level.
     * @param msg the default error message.
     */
    public LegalValueListConstraint(Level level, String msg) {
        super(level, msg);
    }

    /**
     * Constructs a new LegalValueListConstraint using default severity level
     * ({@link Level#WARNING}).
     *
     * @param msg the default error message.
     */
    public LegalValueListConstraint(String msg) {
        this(Level.WARNING, msg);
    }

    /**
     * Constructs a new LegalValueListConstraint using default error message.
     *
     * @param level
     */
    public LegalValueListConstraint(Level level) {
        this(level, "Value not in legal values list");
    }

    /**
     * Constructs a new LegalValueListConstraint using default severity level
     * ({@link Level#WARNING}) and default error message.
     *
     */
    public LegalValueListConstraint() {
        this(Level.WARNING);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.archive.crawler.settings.Constraint#innerCheck(org.archive.crawler.settings.Type,
     *      java.lang.Object)
     */
    public FailedCheck innerCheck(CrawlerSettings settings, ComplexType owner,
            Type definition,
            Object value) {
        FailedCheck res = null;

        // If this attribute is constrained by a list of legal values,
        // check that the value is in that list
        Object legalValues[] = definition.getLegalValues();
        if (legalValues != null) {
            boolean found = false;
            for (int i = 0; i < legalValues.length && !found; i++) {
                if (legalValues[i].equals(value)) {
                    found = true;
                }
            }
            if (!found) {
                res = new FailedCheck(settings, owner, definition, value);
            }
        }
        return res;
    }

}