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
package com.gargoylesoftware.htmlunit.javascript.background;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.util.MemoryLeakDetector;

/**
 * <p>Tests for memory leaks. This test passes when run independently in Eclipse or via
 * Maven (mvn test -Dtest=MemoryLeakTest), but not when all tests are run via Maven
 * (mvn test). Note that this probably constitutes a false positive, meaning that the
 * window isn't leaked in this situation -- we just can't get the JVM to GC it because
 * there is no surefire way to force complete garbage collection.</p>
 *
 * <p>All suspected memory leaks should be verified with a profiler before a fix and/or
 * unit test is committed. In JProfiler, for example, you can run a unit test and tell
 * the profiler to keep the VM alive when it has finished running; once it has finished
 * running you can go to the Heap Walker, take a snapshot of the heap, sort the resultant
 * table by class name and find the class you're interested in, double-click on it, choose
 * to view References to instances of this class, then right-click on the class and choose
 * Show Paths to GC Root. This will give you a list of references which need to be
 * eliminated. Once you have a fix, repeat the above steps in order to verify that the
 * fix works.</p>
 *
 * @version $Revision: 5301 $
 * @author Daniel Gredler
 */
public class MemoryLeakTest extends WebTestCase {

    /**
     * Empty test which keeps Maven/JUnit from complaining about a test class with no tests. This test
     * class cannot be run automatically via Maven for the reasons outlined above.
     * @throws Exception if an error occurs
     */
    @Test
    public void testLeaks() throws Exception {
        // windowLeaks();
    }

    /**
     * Verifies that windows don't get leaked, especially when there are long-running background JS tasks
     * scheduled via <tt>setTimeout</tt> or <tt>setInterval</tt>. See the following bugs:
     *    https://sourceforge.net/tracker/index.php?func=detail&aid=2003396&group_id=47038&atid=448266
     *    https://sourceforge.net/tracker/index.php?func=detail&aid=2014629&group_id=47038&atid=448266
     *
     * @throws Exception if an error occurs
     */
    protected void windowLeaks() throws Exception {
        final MemoryLeakDetector detector = new MemoryLeakDetector();

        WebClient client = new WebClient();
        detector.register("w", client.getCurrentWindow());

        MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, "<html><body><script>setInterval('alert(1)',5000)</script></body></html>");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);

        client = null;
        conn = null;

        assertTrue("Window can't be garbage collected.", detector.canBeGCed("w"));
    }

}
