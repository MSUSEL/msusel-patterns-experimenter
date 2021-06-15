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

import java.util.Iterator;

/**
 * A mailet configuration object used by a mailet container to pass information
 * to a mailet during initialization.
 * <p>
 * The configuration information contains initialization parameters, which are a set
 * of name/value pairs, and a MailetContext object, which gives the mailet information
 * about the server.
 *
 * @version 1.0.0, 24/04/1999
 */
public interface MailetConfig {

    /**
     * Returns a String containing the value of the named initialization
     * parameter, or null if the parameter does not exist.
     *
     * @param name - a String specifying the name of the initialization parameter
     * @return a String containing the value of the initialization parameter
     */
    String getInitParameter(String name);

    /**
     * Returns the names of the mailet's initialization parameters as an
     * Iterator of String objects, or an empty Iterator if the mailet has
     * no initialization parameters.
     *
     * @return an Iterator of String objects containing the names of the mailet's
     *      initialization parameters
     */
    Iterator getInitParameterNames();

    /**
     * Returns a reference to the MailetContext in which the mailet is
     * executing.
     *
     * @return a MailetContext object, used by the mailet to interact with its
     *      mailet container
     */
    MailetContext getMailetContext();

    /**
     * Returns the name of this mailet instance. The name may be provided via
     * server administration, assigned in the application deployment descriptor,
     * or for an unregistered (and thus unnamed) mailet instance it will be the
     * mailet's class name.
     *
     * @return the name of the mailet instance
     */
    String getMailetName();
}
