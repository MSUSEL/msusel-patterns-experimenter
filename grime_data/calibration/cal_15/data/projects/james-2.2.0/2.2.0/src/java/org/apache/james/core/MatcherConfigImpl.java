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
package org.apache.james.core;

import org.apache.mailet.MailetContext;
import org.apache.mailet.MatcherConfig;

/**
 * Implements the configuration object for a Matcher.
 *
 */
public class MatcherConfigImpl implements MatcherConfig {

    /**
     * A String representation of the value for the matching condition
     */
    private String condition;

    /**
     * The name of the Matcher
     */
    private String name;

    /**
     * The MailetContext associated with the Matcher configuration
     */
    private MailetContext context;

    /**
     * The simple condition defined for this matcher, e.g., for
     * SenderIs=admin@localhost, this would return admin@localhost.
     *
     * @return a String containing the value of the initialization parameter
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Set the simple condition defined for this matcher configuration.
     */
    public void setCondition(String newCondition) {
        condition = newCondition;
    }

    /**
     * Returns the name of this matcher instance. The name may be provided via server
     * administration, assigned in the application deployment descriptor, or for
     * an unregistered (and thus unnamed) matcher instance it will be the matcher's
     * class name.
     *
     * @return the name of the matcher instance
     */
    public String getMatcherName() {
        return name;
    }

    /**
     * Sets the name of this matcher instance.
     *
     * @param newName the name of the matcher instance
     */
    public void setMatcherName(String newName) {
        name = newName;
    }

    /**
     * Returns a reference to the MailetContext in which the matcher is executing
     *
     * @return a MailetContext object, used by the matcher to interact with its
     *      mailet container
     */
    public MailetContext getMailetContext() {
        return context;
    }

    /**
     * Sets a reference to the MailetContext in which the matcher is executing
     *
     * @param newContext a MailetContext object, used by the matcher to interact
     *      with its mailet container
     */
    public void setMailetContext(MailetContext newContext) {
        context = newContext;
    }
}
