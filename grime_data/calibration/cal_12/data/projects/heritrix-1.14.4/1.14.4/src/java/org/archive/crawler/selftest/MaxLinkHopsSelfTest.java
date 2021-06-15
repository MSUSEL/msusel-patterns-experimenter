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

import javax.management.AttributeNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import org.archive.crawler.framework.CrawlScope;
import org.archive.crawler.scope.ClassicScope;


/**
 * Test the max-link-hops setting.
 *
 * @author stack
 * @version $Id: MaxLinkHopsSelfTest.java 4931 2007-02-21 18:48:17Z gojomo $
 */
public class MaxLinkHopsSelfTest
    extends SelfTestCase
{
    /**
     * Files to find as a list.
     */
    private static final List<File> FILES_TO_FIND =
        Arrays.asList(new File[] {new File("2.html"),
            new File("3.html"), new File("4.html"), new File("5.html")});

    /**
     * Files not to find as a list.
     */
    private static final List FILES_NOT_TO_FIND =
        Arrays.asList(new File[] {new File("1.html"), new File("6.html")});

    /**
     * Assumption is that the setting for max-link-hops is less than this
     * number.
     */
    private static final int MAXLINKHOPS = 5;


    /**
     * Test the max-link-hops setting is being respected.
     */
    public void stestMaxLinkHops()
        throws AttributeNotFoundException, MBeanException, ReflectionException
    {
        assertInitialized();
        CrawlScope scope =
           (CrawlScope)getCrawlJob().getSettingsHandler()
           .getModule(CrawlScope.ATTR_NAME);
        int maxLinkHops =
            ((Integer)scope.getAttribute(ClassicScope.ATTR_MAX_LINK_HOPS))
            .intValue();
        assertTrue("max-link-hops incorrect", MAXLINKHOPS == maxLinkHops);

        // Make sure file we're NOT supposed to find is actually on disk.
        assertTrue("File present on disk", filesExist(FILES_NOT_TO_FIND));

        // Ok.  The file not to find exists.  Lets see if it made it into arc.
        testFilesInArc(FILES_TO_FIND);
    }
}

