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
package org.archive.crawler.selftest;

import java.io.File;
import java.util.Arrays;
import java.util.List;


/**
 * Test authentications, both basic/digest auth and html form logins.
 *
 * @author stack
 * @version $Id: AuthSelfTest.java 4931 2007-02-21 18:48:17Z gojomo $
 */
public class AuthSelfTest
    extends SelfTestCase
{
    private static final File BASIC = new File("basic");
    private static final File FORM = new File("form");
    private static final File GET = new File(FORM, "get");
    private static final File POST = new File(FORM, "post");

    /**
     * Files to find as a list.
     */
    private static final List<File> FILES_TO_FIND =
        Arrays.asList(new File[] {
                BASIC,
                new File(BASIC, "basic-loggedin.html"),
                FORM,
                new File(POST, "success.jsp"),
                new File(POST, "post-loggedin.html"),
                new File(GET, "success.jsp"),
                new File(GET, "get-loggedin.html")
        });


    /**
     * Test the max-link-hops setting is being respected.
     */
    public void stestAuth() {
        assertInitialized();
        testFilesInArc(FILES_TO_FIND);
    }
}

