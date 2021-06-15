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
package org.hibernate.envers.test.performance;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public abstract class AbstractPerformanceTest extends AbstractEntityManagerTest {
    protected String getSecondsString(long milliseconds) {
        return (milliseconds/1000) + "." + (milliseconds%1000);
    }

    protected abstract void doTest();

    private void printResults(long unaudited, long audited) {
        System.out.println("Unaudited: " + getSecondsString(unaudited));
        System.out.println("  Audited: " + getSecondsString(audited));
        System.out.println("    Delta: " + getSecondsString(audited-unaudited));
        System.out.println("   Factor: " + (double)audited/unaudited);
    }

    private long startTime;
    private long runTotal;

    protected void start() {
        startTime = System.currentTimeMillis();
    }

    protected void stop() {
        long stopTime = System.currentTimeMillis();
        runTotal += stopTime - startTime;
    }

    protected void reset() {
        runTotal = 0;
    }

    public long run(int numberOfRuns, List<Long> results) {
        long total = 0;
        for (int i=0; i<=numberOfRuns; i++) {
            System.out.println();
            System.out.println("RUN " + i);
            reset();
            doTest();
            results.add(runTotal);
            total += runTotal;

            newEntityManager();

            /*System.gc();
            System.gc();
            System.gc();
            System.out.println(Runtime.getRuntime().freeMemory() + ", " + Runtime.getRuntime().totalMemory() + ", "
                    + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));*/
        }

        return total;
    }

    public void test(int numberOfRuns) throws IOException {
        List<Long> unauditedRuns = new ArrayList<Long>();
        List<Long> auditedRuns = new ArrayList<Long>();

        init(true, null);
        long audited = run(numberOfRuns, auditedRuns);
        close();

        init(false, null);
        long unaudited = run(numberOfRuns, unauditedRuns);
        close();

        for (int i=0; i<=numberOfRuns; i++) {
            System.out.println("RUN " + i);
            printResults(unauditedRuns.get(i), auditedRuns.get(i));
            System.out.println();
        }

        System.out.println("TOTAL");
        printResults(unaudited, audited);
    }
}
