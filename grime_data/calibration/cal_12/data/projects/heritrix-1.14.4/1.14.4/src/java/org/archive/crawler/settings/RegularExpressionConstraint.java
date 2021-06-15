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

import java.io.Serializable;
import java.util.logging.Level;

import org.archive.util.TextUtils;

/**
 * A constraint that checks that a value matches a regular expression. This
 * constraint can only be applied to textual attributes.
 *
 * @author John Erik Halse
 */
public class RegularExpressionConstraint
extends Constraint implements Serializable {
    private static final long serialVersionUID = -5916211981136071809L;
    private final String pattern;

    /**
     * Constructs a new RegularExpressionConstraint.
     *
     * @param pattern the regular expression pattern the value must match.
     * @param level the severity level.
     * @param msg the default error message.
     */
    public RegularExpressionConstraint(String pattern, Level level, String msg) {
        super(level, msg);
        this.pattern = pattern;
    }

    /**
     * Constructs a new RegularExpressionConstraint using default severity level
     * ({@link Level#WARNING}).
     *
     * @param pattern the regular expression pattern the value must match.
     * @param msg the default error message.
     */
    public RegularExpressionConstraint(String pattern, String msg) {
        this(pattern, Level.WARNING, msg);
    }

    /**
     * Constructs a new RegularExpressionConstraint using the default error
     * message.
     *
     * @param pattern the regular expression pattern the value must match.
     * @param level the severity level.
     */
    public RegularExpressionConstraint(String pattern, Level level) {
        this(pattern, level, "Value did not match pattern: \"" + pattern + "\"");
    }

    /**
     * Constructs a new RegularExpressionConstraint.
     *
     * @param pattern the regular expression pattern the value must match.
     */
    public RegularExpressionConstraint(String pattern) {
        this(pattern, Level.WARNING);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.archive.crawler.settings.Constraint#innerCheck(org.archive.crawler.settings.Type,
     *      javax.management.Attribute)
     */
    public FailedCheck innerCheck(CrawlerSettings settings, ComplexType owner,
            Type definition, Object value) {
        if (value instanceof CharSequence) {
            if (!TextUtils
                    .matches(pattern, (CharSequence) value)) {
                return new FailedCheck(settings, owner, definition, value);

            }
        } else {
            return new FailedCheck(settings, owner, definition, value,
                    "Can't do regexp on non CharSequence.");
        }
        return null;
    }

}