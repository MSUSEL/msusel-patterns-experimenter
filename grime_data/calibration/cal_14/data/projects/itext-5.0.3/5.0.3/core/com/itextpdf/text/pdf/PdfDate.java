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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

/**
 * <CODE>PdfDate</CODE> is the PDF date object.
 * <P>
 * PDF defines a standard date format. The PDF date format closely follows the format
 * defined by the international standard ASN.1 (Abstract Syntax Notation One, defined
 * in CCITT X.208 or ISO/IEC 8824). A date is a <CODE>PdfString</CODE> of the form:
 * <P><BLOCKQUOTE>
 * (D:YYYYMMDDHHmmSSOHH'mm')
 * </BLOCKQUOTE><P>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 7.2 (page 183-184)
 *
 * @see		PdfString
 * @see		java.util.GregorianCalendar
 */

public class PdfDate extends PdfString {

    private static final int DATE_SPACE[] = {Calendar.YEAR, 4, 0, Calendar.MONTH, 2, -1, Calendar.DAY_OF_MONTH, 2, 0,
        Calendar.HOUR_OF_DAY, 2, 0, Calendar.MINUTE, 2, 0, Calendar.SECOND, 2, 0};
    
    // constructors
    
/**
 * Constructs a <CODE>PdfDate</CODE>-object.
 *
 * @param		d			the date that has to be turned into a <CODE>PdfDate</CODE>-object
 */
    
    public PdfDate(Calendar d) {
        super();
        StringBuffer date = new StringBuffer("D:");
        date.append(setLength(d.get(Calendar.YEAR), 4));
        date.append(setLength(d.get(Calendar.MONTH) + 1, 2));
        date.append(setLength(d.get(Calendar.DATE), 2));
        date.append(setLength(d.get(Calendar.HOUR_OF_DAY), 2));
        date.append(setLength(d.get(Calendar.MINUTE), 2));
        date.append(setLength(d.get(Calendar.SECOND), 2));
        int timezone = (d.get(Calendar.ZONE_OFFSET) + d.get(Calendar.DST_OFFSET)) / (60 * 60 * 1000);
        if (timezone == 0) {
            date.append('Z');
        }
        else if (timezone < 0) {
            date.append('-');
            timezone = -timezone;
        }
        else {
            date.append('+');
        }
        if (timezone != 0) {
            date.append(setLength(timezone, 2)).append('\'');
            int zone = Math.abs((d.get(Calendar.ZONE_OFFSET) + d.get(Calendar.DST_OFFSET)) / (60 * 1000)) - (timezone * 60);
            date.append(setLength(zone, 2)).append('\'');
        }
        value = date.toString();
    }
    
/**
 * Constructs a <CODE>PdfDate</CODE>-object, representing the current day and time.
 */
    
    public PdfDate() {
        this(new GregorianCalendar());
    }
    
/**
 * Adds a number of leading zeros to a given <CODE>String</CODE> in order to get a <CODE>String</CODE>
 * of a certain length.
 *
 * @param		i   		a given number
 * @param		length		the length of the resulting <CODE>String</CODE>
 * @return		the resulting <CODE>String</CODE>
 */
    
    private String setLength(int i, int length) { // 1.3-1.4 problem fixed by Finn Bock
        StringBuffer tmp = new StringBuffer();
        tmp.append(i);
        while (tmp.length() < length) {
            tmp.insert(0, "0");
        }
        tmp.setLength(length);
        return tmp.toString();
    }
    
    /**
     * Gives the W3C format of the PdfDate.
     * @return a formatted date
     */
    public String getW3CDate() {
        return getW3CDate(value);
    }
    
    /**
     * Gives the W3C format of the PdfDate.
     * @param d the date in the format D:YYYYMMDDHHmmSSOHH'mm'
     * @return a formatted date
     */
    public static String getW3CDate(String d) {
        if (d.startsWith("D:"))
            d = d.substring(2);
        StringBuffer sb = new StringBuffer();
        if (d.length() < 4)
            return "0000";
        sb.append(d.substring(0, 4)); //year
        d = d.substring(4);
        if (d.length() < 2)
            return sb.toString();
        sb.append('-').append(d.substring(0, 2)); //month
        d = d.substring(2);
        if (d.length() < 2)
            return sb.toString();
        sb.append('-').append(d.substring(0, 2)); //day
        d = d.substring(2);
        if (d.length() < 2)
            return sb.toString();
        sb.append('T').append(d.substring(0, 2)); //hour
        d = d.substring(2);
        if (d.length() < 2) {
            sb.append(":00Z");
            return sb.toString();
        }
        sb.append(':').append(d.substring(0, 2)); //minute
        d = d.substring(2);
        if (d.length() < 2) {
            sb.append('Z');
            return sb.toString();
        }
        sb.append(':').append(d.substring(0, 2)); //second
        d = d.substring(2);
        if (d.startsWith("-") || d.startsWith("+")) {
            String sign = d.substring(0, 1);
            d = d.substring(1);
            String h = "00";
            String m = "00";
            if (d.length() >= 2) {
                h = d.substring(0, 2);
                if (d.length() > 2) {
                    d = d.substring(3);
                    if (d.length() >= 2)
                        m = d.substring(0, 2);
                }
                sb.append(sign).append(h).append(':').append(m);
                return sb.toString();
            }
        }
        sb.append('Z');
        return sb.toString();
    }
    
    /**
     * Converts a PDF string representing a date into a Calendar.
     * @param s the PDF string representing a date
     * @return a <CODE>Calendar</CODE> representing the date or <CODE>null</CODE> if the string
     * was not a date
     */    
    public static Calendar decode(String s) {
        try {
            if (s.startsWith("D:"))
                s = s.substring(2);
            GregorianCalendar calendar;
            int slen = s.length();
            int idx = s.indexOf('Z');
            if (idx >= 0) {
                slen = idx;
                calendar = new GregorianCalendar(new SimpleTimeZone(0, "ZPDF"));
            }
            else {
                int sign = 1;
                idx = s.indexOf('+');
                if (idx < 0) {
                    idx = s.indexOf('-');
                    if (idx >= 0)
                        sign = -1;
                }
                if (idx < 0)
                    calendar = new GregorianCalendar();
                else {
                    int offset = Integer.parseInt(s.substring(idx + 1, idx + 3)) * 60;
                    if (idx + 5 < s.length())
                        offset += Integer.parseInt(s.substring(idx + 4, idx + 6));
                    calendar = new GregorianCalendar(new SimpleTimeZone(offset * sign * 60000, "ZPDF"));
                    slen = idx;
                }
            }
            calendar.clear();
            idx = 0;
            for (int k = 0; k < DATE_SPACE.length; k += 3) {
                if (idx >= slen)
                    break;
                calendar.set(DATE_SPACE[k], Integer.parseInt(s.substring(idx, idx + DATE_SPACE[k + 1])) + DATE_SPACE[k + 2]);
                idx += DATE_SPACE[k + 1];
            }
            return calendar;
        }
        catch (Exception e) {
            return null;
        }
    }
}