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
package net.sourceforge.ganttproject.task;

import net.sourceforge.ganttproject.time.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * 
 * @author bard Date: 31.01.2004
 */
public class TaskLengthImpl implements TaskLength {
    private final TimeUnit myUnit;

    private float myCount;

    public TaskLengthImpl(TimeUnit unit, long count) {
        myUnit = unit;
        myCount = count;
    }

    /**
     * @param unit
     * @param length
     */
    public TaskLengthImpl(TimeUnit unit, float length) {
        myUnit = unit;
        myCount = length;
    }

    public float getValue() {
        return myCount;
    }

    public long getLength() {
        return (long) myCount;
    }

    public TimeUnit getTimeUnit() {
        return myUnit;
    }

    public void setLength(TimeUnit unit, long length) {
        if (!unit.equals(myUnit)) {
            throw new IllegalArgumentException("Can't convert unit=" + unit
                    + " to my unit=" + myUnit);
        }
        myCount = length;
    }

    public float getLength(TimeUnit unit) {
        if (myUnit.isConstructedFrom(unit)) {
            return (float) myCount * myUnit.getAtomCount(unit);
        } else if (unit.isConstructedFrom(myUnit)) {
            return (float) myCount / unit.getAtomCount(myUnit);
        } else if (!unit.equals(myUnit)) {
            throw new IllegalArgumentException("Can't convert unit=" + unit
                    + " to my unit=" + myUnit);
        }
        return myCount;
    }

    public String toString() {
        return "" + myCount + " " + myUnit.getName();
    }
}
