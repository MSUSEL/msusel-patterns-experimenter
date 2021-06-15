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
package com.itextpdf.text.pdf;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * This class expands a string into a list of numbers. The main use is to select a
 * range of pages.
 * <p>
 * The general syntax is:<br>
 * [!][o][odd][e][even]start-end
 * <p>
 * You can have multiple ranges separated by commas ','. The '!' modifier removes the
 * range from what is already selected. The range changes are incremental, that is,
 * numbers are added or deleted as the range appears. The start or the end, but not both, can be omitted.
 */
public class SequenceList {
    protected static final int COMMA = 1;
    protected static final int MINUS = 2;
    protected static final int NOT = 3;
    protected static final int TEXT = 4;
    protected static final int NUMBER = 5;
    protected static final int END = 6;
    protected static final char EOT = '\uffff';

    private static final int FIRST = 0;
    private static final int DIGIT = 1;
    private static final int OTHER = 2;
    private static final int DIGIT2 = 3;
    private static final String NOT_OTHER = "-,!0123456789";

    protected char text[];
    protected int ptr;
    protected int number;
    protected String other;

    protected int low;
    protected int high;
    protected boolean odd;
    protected boolean even;
    protected boolean inverse;

    protected SequenceList(String range) {
        ptr = 0;
        text = range.toCharArray();
    }

    protected char nextChar() {
        while (true) {
            if (ptr >= text.length)
                return EOT;
            char c = text[ptr++];
            if (c > ' ')
                return c;
        }
    }

    protected void putBack() {
        --ptr;
        if (ptr < 0)
            ptr = 0;
    }

    protected int getType() {
        StringBuffer buf = new StringBuffer();
        int state = FIRST;
        while (true) {
            char c = nextChar();
            if (c == EOT) {
                if (state == DIGIT) {
                    number = Integer.parseInt(other = buf.toString());
                    return NUMBER;
                }
                else if (state == OTHER) {
                    other = buf.toString().toLowerCase();
                    return TEXT;
                }
                return END;
            }
            switch (state) {
                case FIRST:
                    switch (c) {
                        case '!':
                            return NOT;
                        case '-':
                            return MINUS;
                        case ',':
                            return COMMA;
                    }
                    buf.append(c);
                    if (c >= '0' && c <= '9')
                        state = DIGIT;
                    else
                        state = OTHER;
                    break;
                case DIGIT:
                    if (c >= '0' && c <= '9')
                        buf.append(c);
                    else {
                        putBack();
                        number = Integer.parseInt(other = buf.toString());
                        return NUMBER;
                    }
                    break;
                case OTHER:
                    if (NOT_OTHER.indexOf(c) < 0)
                        buf.append(c);
                    else {
                        putBack();
                        other = buf.toString().toLowerCase();
                        return TEXT;
                    }
                    break;
            }
        }
    }

    private void otherProc() {
        if (other.equals("odd") || other.equals("o")) {
            odd = true;
            even = false;
        }
        else if (other.equals("even") || other.equals("e")) {
            odd = false;
            even = true;
        }
    }

    protected boolean getAttributes() {
        low = -1;
        high = -1;
        odd = even = inverse = false;
        int state = OTHER;
        while (true) {
            int type = getType();
            if (type == END || type == COMMA) {
                if (state == DIGIT)
                    high = low;
                return type == END;
            }
            switch (state) {
                case OTHER:
                    switch (type) {
                        case NOT:
                            inverse = true;
                            break;
                        case MINUS:
                            state = DIGIT2;
                            break;
                        default:
                            if (type == NUMBER) {
                                low = number;
                                state = DIGIT;
                            }
                            else
                                otherProc();
                            break;
                    }
                    break;
                case DIGIT:
                    switch (type) {
                        case NOT:
                            inverse = true;
                            state = OTHER;
                            high = low;
                            break;
                        case MINUS:
                            state = DIGIT2;
                            break;
                        default:
                            high = low;
                            state = OTHER;
                            otherProc();
                            break;
                    }
                    break;
                case DIGIT2:
                    switch (type) {
                        case NOT:
                            inverse = true;
                            state = OTHER;
                            break;
                        case MINUS:
                            break;
                        case NUMBER:
                            high = number;
                            state = OTHER;
                            break;
                        default:
                            state = OTHER;
                            otherProc();
                            break;
                    }
                    break;
            }
        }
    }

    /**
     * Generates a list of numbers from a string.
     * @param ranges the comma separated ranges
     * @param maxNumber the maximum number in the range
     * @return a list with the numbers as <CODE>Integer</CODE>
     */
    public static List<Integer> expand(String ranges, int maxNumber) {
        SequenceList parse = new SequenceList(ranges);
        LinkedList<Integer> list = new LinkedList<Integer>();
        boolean sair = false;
        while (!sair) {
            sair = parse.getAttributes();
            if (parse.low == -1 && parse.high == -1 && !parse.even && !parse.odd)
                continue;
            if (parse.low < 1)
                parse.low = 1;
            if (parse.high < 1 || parse.high > maxNumber)
                parse.high = maxNumber;
            if (parse.low > maxNumber)
                parse.low = maxNumber;

            //System.out.println("low="+parse.low+",high="+parse.high+",odd="+parse.odd+",even="+parse.even+",inverse="+parse.inverse);
            int inc = 1;
            if (parse.inverse) {
                if (parse.low > parse.high) {
                    int t = parse.low;
                    parse.low = parse.high;
                    parse.high = t;
                }
                for (ListIterator<Integer> it = list.listIterator(); it.hasNext();) {
                    int n = it.next().intValue();
                    if (parse.even && (n & 1) == 1)
                        continue;
                    if (parse.odd && (n & 1) == 0)
                        continue;
                    if (n >= parse.low && n <= parse.high)
                        it.remove();
                }
            }
            else {
                if (parse.low > parse.high) {
                    inc = -1;
                    if (parse.odd || parse.even) {
                        --inc;
                        if (parse.even)
                            parse.low &= ~1;
                        else
                            parse.low -= (parse.low & 1) == 1 ? 0 : 1;
                    }
                    for (int k = parse.low; k >= parse.high; k += inc)
                        list.add(new Integer(k));
                }
                else {
                    if (parse.odd || parse.even) {
                        ++inc;
                        if (parse.odd)
                            parse.low |= 1;
                        else
                            parse.low += (parse.low & 1) == 1 ? 1 : 0;
                    }
                    for (int k = parse.low; k <= parse.high; k += inc) {
                        list.add(new Integer(k));
                    }
                }
            }
//            for (int k = 0; k < list.size(); ++k)
//                System.out.print(((Integer)list.get(k)).intValue() + ",");
//            System.out.println();
        }
        return list;
    }
}