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
package net.sourceforge.ganttproject.task.algorithm;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import net.sourceforge.ganttproject.task.Task;

public class ProjectBoundsAlgorithm {
    public static class Result {
        public final Date lowerBound;

        public final Date upperBound;

        private Result(Date lowerBound, Date upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }
    }

    public Result getBounds(Collection/* <Task> */tasks) {
        Date lowerBound = null;
        Date upperBound = null;
        for (Iterator it = tasks.iterator(); it.hasNext();) {
            Task next = (Task) it.next();
            Date start = next.getStart().getTime();
            Date end = next.getEnd().getTime();
            if (lowerBound == null || lowerBound.after(start)) {
                lowerBound = start;
            }
            if (upperBound == null || upperBound.before(end)) {
                upperBound = end;
            }
        }
        return new Result(lowerBound, upperBound);
    }
}
