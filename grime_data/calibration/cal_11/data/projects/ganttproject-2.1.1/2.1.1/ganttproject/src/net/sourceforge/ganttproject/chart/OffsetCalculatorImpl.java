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
package net.sourceforge.ganttproject.chart;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


import net.sourceforge.ganttproject.chart.ChartModelBase.Offset;
import net.sourceforge.ganttproject.time.TimeFrame;
import net.sourceforge.ganttproject.time.TimeUnit;
import net.sourceforge.ganttproject.time.TimeUnitStack;

public class OffsetCalculatorImpl {
    private TimeUnitStack myStack;

    public OffsetCalculatorImpl(TimeUnitStack timeunitStack) {
        myStack = timeunitStack;
    }

    public Offset[] calculateOffsets(TimeFrame timeFrame, TimeUnit frameBottomUnit, Date bottomUnitStartDate, TimeUnit offsetUnit, int frameBottomUnitWidth) {
        Offset[] result;
        //Date bottomUnitEndDate = timeFrame.getUnitFinish(frameBottomUnit, unitIndex);
        if (offsetUnit.equals(frameBottomUnit)) {
            result = new Offset[] {new Offset(offsetUnit, bottomUnitStartDate, bottomUnitStartDate, 0, null)};
        }
        else if (frameBottomUnit.isConstructedFrom(offsetUnit)) {
            //java.util.List buf = new ArrayList();
            int outerFrameUnitCount = timeFrame.getUnitCount(offsetUnit);
            TimeFrame innerFrame = myStack.createTimeFrame(bottomUnitStartDate,
                    frameBottomUnit, offsetUnit);
            int offsetUnitCount = innerFrame.getUnitCount(offsetUnit);
            result = new Offset[offsetUnitCount];
            for (int i=0; i<offsetUnitCount; i++) {
                Date offsetEnd = innerFrame.getUnitFinish(offsetUnit, i);
                int offsetPixels = (i+1)*frameBottomUnitWidth/outerFrameUnitCount;
                result[i] = new Offset(offsetUnit, bottomUnitStartDate, offsetEnd, offsetPixels, null);
            }
        } else {
            throw new RuntimeException("We should not be here");
        }
        return result;

    }
}

class CachingOffsetCalculatorImpl {
	private static final int MAX_CACHE_SIZE = 200;
    final private Map/*<UnitPair,Map<Date,Offset[]>>*/ myUnitPair_StartDate = new HashMap();
	final private OffsetCalculatorImpl myCalculator;

	CachingOffsetCalculatorImpl(TimeUnitStack timeUnitStack) {
		myCalculator = new OffsetCalculatorImpl(timeUnitStack);
	}

	Offset[] calculateOffsets( TimeFrame timeFrame, TimeUnit frameBottomUnit, Date bottomUnitStartDate, TimeUnit offsetUnit,int frameBottomUnitWidth) {
		Offset[] result = null;
		UnitPair unitPair = new UnitPair(frameBottomUnit, offsetUnit);
		LinkedHashMap startDate_Offsets = (LinkedHashMap) myUnitPair_StartDate.get(unitPair);
		if (startDate_Offsets==null) {
			startDate_Offsets = new LinkedHashMap();
			myUnitPair_StartDate.put(unitPair, startDate_Offsets);
		}
		result = (Offset[]) startDate_Offsets.get(bottomUnitStartDate);
		if (result==null) {
			result = myCalculator.calculateOffsets(timeFrame, frameBottomUnit, bottomUnitStartDate, offsetUnit, frameBottomUnitWidth);
			startDate_Offsets.put(bottomUnitStartDate, result);
            if (startDate_Offsets.size()>MAX_CACHE_SIZE) {
                Iterator iter = startDate_Offsets.entrySet().iterator();
                iter.next();
                iter.remove();
            }
		}
		return result;
	}

	void reset() {
		myUnitPair_StartDate.clear();
	}

	private static class UnitPair {
		private final TimeUnit myMainUnit;
		private final TimeUnit myOffsetUnit;
		private UnitPair(TimeUnit mainUnit, TimeUnit offsetUnit) {
			myMainUnit = mainUnit;
			myOffsetUnit = offsetUnit;
		}
		public boolean equals(Object o) {
			if (false==o instanceof UnitPair) {
				return false;
			}
			UnitPair rvalue = (UnitPair) o;
			return rvalue.myMainUnit.equals(myMainUnit) && rvalue.myOffsetUnit.equals(myOffsetUnit);
		}

		public int hashCode() {
			return myMainUnit.hashCode()+myOffsetUnit.hashCode();
		}
	}
}
