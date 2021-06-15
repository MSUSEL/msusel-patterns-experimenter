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
package org.apache.james.transport;

/**
 * A set of constants used inside the James transport.
 *
 * @version 1.0.0, 24/04/1999
 */
public class Resources {

    //Already defined in Constants
    //public static final String SERVER_NAMES = "SERVER_NAMES";

    /**
     * Don't know what this is supposed to be. 
     *
     * @deprecated this is unused
     */
    public static final String USERS_MANAGER = "USERS_MANAGER";

    //Already defined in Constants
    //public static final String POSTMASTER = "POSTMASTER";

    /**
     * Don't know what this is supposed to be. 
     *
     * @deprecated this is unused
     */
    public static final String MAIL_SERVER = "MAIL_SERVER";

    /**
     * Don't know what this is supposed to be. 
     *
     * @deprecated this is unused
     */
    public static final String TRANSPORT = "TRANSPORT";

    /**
     * Don't know what this is supposed to be. 
     *
     * @deprecated this is unused
     */
    public static final String TMP_REPOSITORY = "TMP_REPOSITORY";

    /**
     * Key for looking up the MailetLoader
     */
    public static final String MAILET_LOADER = "MAILET_LOADER";

    /**
     * Key for looking up the MatchLoader
     */
    public static final String MATCH_LOADER = "MATCH_LOADER";

    /**
     * Private constructor to prevent instantiation of the class
     */
    private Resources() {}
}
