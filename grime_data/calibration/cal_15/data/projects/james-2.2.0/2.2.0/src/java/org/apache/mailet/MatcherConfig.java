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
package org.apache.mailet;

/**
 * A matcher configuration object used by a mailet container used to pass information
 * to a matcher during initialization.
 * <p>
 * The configuration information contains an initialization parameter,
 * which is set as a condition String, and a MailetContext object,
 * which gives the mailet information about the mailet container.
 *
 * @version 1.0.0, 24/04/1999
 */
public interface MatcherConfig {

    /**
     * The simple condition defined for this matcher, e.g., for
     * SenderIs=admin@localhost, this would return admin@localhost.
     *
     * @return a String containing the value of the initialization parameter
     */
    String getCondition();

    /**
     * Returns a reference to the MailetContext in which the matcher is executing
     *
     * @return a MailetContext object, used by the matcher to interact with its
     *      mailet container
     */
    MailetContext getMailetContext();

    /**
     * Returns the name of this matcher instance. The name may be provided via server
     * administration, assigned in the application deployment descriptor, or for
     * an unregistered (and thus unnamed) matcher instance it will be the matcher's
     * class name.
     *
     * @return the name of the matcher instance
     */
    String getMatcherName();
}
