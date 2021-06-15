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

import net.sourceforge.ganttproject.util.TextLengthCalculator;

public class TimeUnitText {
    private String myLongText;

    private String myMediumText;

    private String myShortText;

    private int myMinimaxLong = -1;

    private int myMinimaxMedium = -1;

    private int myMinimaxShort = -1;

    private Object myCalculatorState;

    public TimeUnitText(String longText, String mediumText, String shortText) {
        myLongText = longText;
        myMediumText = mediumText;
        myShortText = shortText;
    }

    public TimeUnitText(String mediumText) {
        myMediumText = mediumText;
        myLongText = mediumText;
        myShortText = mediumText;
    }

    public String getText(int maxLength) {
        return myMediumText;
    }

    public String getText(int requestedMaxLength,
            TextLengthCalculator calculator) {
        String result = null;
        if (!calculator.getState().equals(myCalculatorState)) {
            myCalculatorState = calculator.getState();
            myMinimaxLong = calculator.getTextLength(myLongText);
            myMinimaxMedium = calculator.getTextLength(myMediumText);
            myMinimaxShort = calculator.getTextLength(myShortText);
        }
        result = getCachedText(requestedMaxLength);
        return result == null ? "" : result;
    }

    private String getCachedText(int maxLength) {
        if (myMinimaxLong >= 0 && myMinimaxLong <= maxLength) {
            return myLongText;
        }
        if (myMinimaxMedium >= 0 && myMinimaxMedium <= maxLength) {
            return myMediumText;
        }
        if (myMinimaxShort >= 0 && myMinimaxShort <= maxLength) {
            return myShortText;
        }
        return null;
    }

    public String toString() {
        return "long=" + myLongText + ", medium=" + myMediumText + ", short="
                + myShortText;
    }
}
