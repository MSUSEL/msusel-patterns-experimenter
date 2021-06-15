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
 * Simple test to ensure we can extract links from multibyte pages.
 *
 * That is, can we regex over a multibyte stream.
 *
 * @author stack
 * @version $Revision: 4931 $, $Date: 2007-02-21 18:48:17 +0000 (Wed, 21 Feb 2007) $
 */
public class CharsetSelfTest extends SelfTestCase
{
    /**
     * Files to find as a list.
     */
    private static final List<File> FILES_TO_FIND =
        Arrays.asList(new File[]
            {new File("utf8.jsp"),
                new File("shiftjis.jsp"),
                new File("charsetselftest_end.html")});

    /**
     * Look for last file in link chain.
     *
     * The way the pages are setup under the CharsetSelfTest directory under
     * the webapp is that we have one multibyte page w/ a single link buried in
     * it that points off to another multibyte page.  On the end of the link
     * chain is a page named END_OF_CHAIN_PAGE.  This test looks to see that
     * arc has all pages in the chain.
     */
    public void stestCharset()
    {
        assertInitialized();
        testFilesInArc(FILES_TO_FIND);
    }
}
