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
package com.gargoylesoftware.htmlunit;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Runs the HtmlUnit "traditional" tests that have been captured using WebDriver.
 * Tests gets "captured" when they use {@link WebTestCase#createTestPageForRealBrowserIfNeeded(String, List)}.
 * In this case an HTML file (as well as the expected results) that can be run by this class is saved
 * in the target/generated_tests folder.
 *
 * @version $Revision: 5563 $
 * @author Marc Guillemot
 */
@RunWith(Parameterized.class)
public class WebDriverOldTestsTest extends WebDriverTestCase {
    private static final Log LOG = LogFactory.getLog(WebDriverOldTestsTest.class);
    private final URL testFile_;
    private final List<String> expectedLog_ = new ArrayList<String>();

    /**
     * @param expectedFile the expected file
     * @throws Exception if the test fails
     */
    @SuppressWarnings("unchecked")
    public WebDriverOldTestsTest(final File expectedFile) throws Exception {
        final FileInputStream fis = new FileInputStream(expectedFile);
        final ObjectInputStream oos = new ObjectInputStream(fis);
        final List<String> list = (List<String>) oos.readObject();
        for (final String s : list) {
            expectedLog_.add(s.trim());
        }
        oos.close();

        final String testFileName = expectedFile.getName().replaceFirst("\\.html\\..*", ".html");
        testFile_ = new File(expectedFile.getParentFile(), testFileName).toURI().toURL();
    }

    /**
     * Provides the data, i.e. the files on which the tests should run.
     * TODO: use a dedicated test runner instead of this parameterized runner.
     * @return the tests files on which to run the tests
     */
    @org.junit.runners.Parameterized.Parameters
    public static Collection<File[]> data() {
        final File testsDir = new File("target/generated_tests");
        final List<File[]> response = new ArrayList<File[]>();

        if (testsDir.exists()) {
            final File[] testFiles = testsDir.listFiles(new FileFilter() {
                public boolean accept(final File pathname) {
                    final String name = pathname.getName();
                    return (name.endsWith(".html.expected") || name.endsWith(".html.FF3.expected"));
                }
            });

            for (final File f : testFiles) {
                response.add(new File[] {f});
            }
        }
        LOG.info(response.size() + " tests found in folder " + testsDir);
        return response;
    }

    /**
     * Runs the test contained in the test file and compares the result with the expected ones.
     * @throws Throwable if the test fails
     */
    @Test
    @Ignore
    public void test() throws Throwable {
        LOG.info("Running " + testFile_);

        final WebDriver webDriver = getWebDriver();
        final JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;
        getWebDriver().get(testFile_.toExternalForm());

        // retrieve captured "alerts"
        final JSONArray resp = (JSONArray) jsExecutor.executeScript("return top.__huCatchedAlerts");
        final List<String> actualResults = new ArrayList<String>();
        if (resp != null) {
            for (int i = 0; i < resp.length(); ++i) {
                actualResults.add(resp.getString(i));
            }
        }

        // verifications
        Assert.assertEquals(testFile_.toExternalForm(), expectedLog_, actualResults);
    }
}
