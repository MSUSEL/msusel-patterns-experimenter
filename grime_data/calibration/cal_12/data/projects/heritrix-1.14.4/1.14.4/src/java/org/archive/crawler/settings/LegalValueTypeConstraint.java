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

/**
 * A constraint that checks that an attribute value is of the right type
 *
 * @author John Erik Halse
 */
public class LegalValueTypeConstraint
extends Constraint implements Serializable {
    private static final long serialVersionUID = 6106774072922858976L;

    /**
     * Constructs a new LegalValueListConstraint.
     *
     * @param level the severity level.
     * @param msg the default error message.
     */
    public LegalValueTypeConstraint(Level level, String msg) {
        super(level, msg);
    }

    /**
     * Constructs a new LegalValueListConstraint using default severity level
     * ({@link Level#WARNING}).
     *
     * @param msg the default error message.
     */
    public LegalValueTypeConstraint(String msg) {
        this(Level.SEVERE, msg);
    }

    /**
     * Constructs a new LegalValueListConstraint using default error message.
     *
     * @param level
     */
    public LegalValueTypeConstraint(Level level) {
        this(level, "Value of illegal type: ''{3}'', ''{4}'' was expected.");
    }

    /**
     * Constructs a new LegalValueListConstraint using default severity level
     * ({@link Level#WARNING}) and default error message.
     *
     */
    public LegalValueTypeConstraint() {
        this(Level.SEVERE);
    }

    public FailedCheck innerCheck(CrawlerSettings settings, ComplexType owner,
            Type definition, Object value) {
        FailedCheck res = null;

        // Check that the value is of right type
        if (!definition.getLegalValueType().isInstance(value)) {
            res = new FailedCheck(settings, owner, definition, value);
            res.messageArguments.add((value != null)?
                value.getClass().getName(): "null");
            res.messageArguments.add(definition.getLegalValueType().getName());
        }
        return res;
    }
}