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
package com.gargoylesoftware.htmlunit.javascript;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Multi-threaded JavaScript engine test.
 *
 * @version $Revision: 5301 $
 * @author David D. Kilzer
 * @author Ahmed Ashour
 */
public class ThreadTest extends TestCase {

    /**
     * Creates a new instance.
     * @param name the name of the test
     */
    public ThreadTest(final String name) {
        super(name);
    }

    /**
     * @throws InterruptedException if the test fails
     */
    public void testJavaScriptEngineInMultipleThreads() throws InterruptedException {
        final TestThread thread1 = new TestThread("thread1");
        final TestThread thread2 = new TestThread("thread2");
        final TestThread thread3 = new TestThread("thread3");
        final TestThread thread4 = new TestThread("thread4");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();

        assertTrue("thread1 not successful", thread1.isSuccessful());
        assertTrue("thread2 not successful", thread2.isSuccessful());
        assertTrue("thread3 not successful", thread3.isSuccessful());
        assertTrue("thread4 not successful", thread4.isSuccessful());
    }

    /**
     * Runs this test many times.
     * @return a suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite("Run this many times");
        suite.addTestSuite(ThreadTest.class);
        for (int i = 1; i < 100; i++) {
            suite.addTest(new ThreadTest("testJavaScriptEngineInMultipleThreads"));
        }
        return suite;
    }

    /** A test object for threads*/
    private static class TestThread extends Thread {

        private boolean successful_ = false;

        /**
         * Creates an instance.
         * @param name the name of the thread
         */
        public TestThread(final String name) {
            super(name);
        }

        /** @see Thread#run() */
        @Override
        public void run() {
            try {
                testCallInheritedFunction();
                successful_ = true;
            }
            catch (final Exception e) {
                System.err.println(">>> Thread " + getName());
                e.printStackTrace(System.err);
                successful_ = false;
            }
        }

        /** @return true if the test was successful */
        public boolean isSuccessful() {
            return successful_;
        }

        /**
         * @see SimpleScriptableTest#callInheritedFunction()
         * @throws Exception if the test failed
         */
        public void testCallInheritedFunction() throws Exception {
            final String html
                = "<html><head><title>foo</title><script>\n"
                + "function doTest() {\n"
                + "    document.form1.textfield1.focus();\n"
                + "    alert('past focus');\n"
                + "}\n"
                + "</script></head><body onload='doTest()'>\n"
                + "<p>hello world</p>\n"
                + "<form name='form1'>\n"
                + "    <input type='text' name='textfield1' id='textfield1' value='foo'>\n"
                + "</form>\n"
                + "</body></html>";

            final List<String> collectedAlerts = new ArrayList<String>();

            final WebClient webClient = new WebClient();
            webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
            final MockWebConnection connection = new MockWebConnection();
            connection.setDefaultResponse(html);
            webClient.setWebConnection(connection);
            try {
                final HtmlPage page = webClient.getPage(WebTestCase.URL_FIRST);

                assertEquals("foo", page.getTitleText());
                assertEquals("focus not changed to textfield1",
                             page.getFormByName("form1").getInputByName("textfield1"),
                             page.getFocusedElement());
                final List<String> expectedAlerts = Collections.singletonList("past focus");
                assertEquals(expectedAlerts, collectedAlerts);
            }
            finally {
                webClient.closeAllWindows();
            }
        }
    }

}
