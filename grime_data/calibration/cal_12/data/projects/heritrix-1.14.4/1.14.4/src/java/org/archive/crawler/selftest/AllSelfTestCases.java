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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.archive.crawler.admin.CrawlJob;


/**
 * All registered heritrix selftests.
 *
 * @author stack
 * @version $Id: AllSelfTestCases.java 4931 2007-02-21 18:48:17Z gojomo $
 */
public class AllSelfTestCases
{
    /**
     * All known selftests as a list.
     *
     * Gets initialized by the static block that immediately follows.
     */
    private static List allKnownSelftests;
    static {
        // List of all known selftests.
        Class [] tmp = {
                BackgroundImageExtractionSelfTestCase.class,
                FramesSelfTestCase.class,
                MaxLinkHopsSelfTest.class,
                CharsetSelfTest.class,
                AuthSelfTest.class,
                BadURIsStopPageParsingSelfTest.class,
                // Works locally but not on builds.archive.org.
                // FlashParseSelfTest.class
                CheckpointSelfTest.class,
            };
        AllSelfTestCases.allKnownSelftests =
            Collections.unmodifiableList(Arrays.asList(tmp));
    }

    /**
     * Run all known tests in the selftest suite.
     *
     * Each unit test to run as part of selftest needs to be added here.
     *
     * @param selftestURL Base url to selftest webapp.
     * @param job The completed selftest job.
     * @param jobDir Job output directory.  Has the seed file, the order file
     * and logs.
     * @param htdocs Expanded webapp directory location.
     *
     * @return Suite of all selftests.
     */
    public static Test suite(final String selftestURL, final CrawlJob job,
            final File jobDir, final File htdocs)
    {
        return suite(selftestURL, job, jobDir, htdocs,
             AllSelfTestCases.allKnownSelftests);
    }

    /**
     * Run list of passed tests.
     *
     * This method is exposed so can run something less than all of the
     * selftests.
     *
     * @param selftestURL Base url to selftest webapp.
     * @param job The completed selftest job.
     * @param jobDir Job output directory.  Has the seed file, the order file
     * and logs.
     * @param htdocs Expanded webapp directory location.
     * @param selftests List of selftests to run.
     *
     * @return Suite of all selftests.
     */
    public static Test suite(final String selftestURL, final CrawlJob job,
            final File jobDir, final File htdocs, final List selftests) {
        TestSuite suite =
            new TestSuite("Test(s) for org.archive.crawler.selftest");
        for (Iterator i = selftests.iterator(); i.hasNext();) {
            suite.addTest(new AltTestSuite((Class)i.next(),"stest"));
        }

        return new TestSetup(suite) {
                protected void setUp() throws Exception {
                    SelfTestCase.initialize(selftestURL, job, jobDir, htdocs);
                }
        };
    }
}
