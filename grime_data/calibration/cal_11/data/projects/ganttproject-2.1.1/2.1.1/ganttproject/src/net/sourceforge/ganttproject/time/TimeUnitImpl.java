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
package net.sourceforge.ganttproject.time;

import java.util.Date;

import net.sourceforge.ganttproject.time.TimeUnitGraph.Composition;

/**
 * Created by IntelliJ IDEA.
 * 
 * @author bard Date: 01.02.2004
 */
public class TimeUnitImpl implements TimeUnit {
    private final String myName;

    private final TimeUnitGraph myGraph;

    private final TimeUnit myDirectAtomUnit;

    private TextFormatter myTextFormatter;

    public TimeUnitImpl(String name, TimeUnitGraph graph,
            TimeUnit directAtomUnit) {
        myName = name;
        myGraph = graph;
        myDirectAtomUnit = directAtomUnit;
    }

    public String getName() {
        return myName;
    }

    public boolean isConstructedFrom(TimeUnit atomUnit) {
        return myGraph.getComposition(this, atomUnit) != null;
    }

    public int getAtomCount(TimeUnit atomUnit) {
        Composition composition = myGraph.getComposition(this, atomUnit);
        if (composition == null) {
            throw new RuntimeException(
                    "Failed to find a composition of time unit=" + this
                            + " from time unit=" + atomUnit);
        }
        return composition.getAtomCount();
    }

    public TimeUnit getDirectAtomUnit() {
        return myDirectAtomUnit;
    }

    public String toString() {
        return getName() + " hash=" + hashCode();
    }

    public void setTextFormatter(TextFormatter formatter) {
        myTextFormatter = formatter;
    }

    public TimeUnitText format(Date baseDate) {
        return myTextFormatter == null ? new TimeUnitText("") : myTextFormatter
                .format(this, baseDate);
    }

    protected TextFormatter getTextFormatter() {
        return myTextFormatter;
    }

    public Date adjustRight(Date baseDate) {
        throw new UnsupportedOperationException("Time unit=" + this
                + " doesnt support this operation");
    }

    public Date adjustLeft(Date baseDate) {
        throw new UnsupportedOperationException("Time unit=" + this
                + " doesnt support this operation");
    }

    public Date jumpLeft(Date baseDate) {
        throw new UnsupportedOperationException("Time unit=" + this
                + " doesnt support this operation");
    }

}
