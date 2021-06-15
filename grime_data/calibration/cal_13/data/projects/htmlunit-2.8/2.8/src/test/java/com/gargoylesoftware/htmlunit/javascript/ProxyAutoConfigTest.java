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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for the {@link ProxyAutoConfig}.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
public class ProxyAutoConfigTest extends WebTestCase {

    /**
     * Test case.
     */
    @Test
    public void shExpMatch() {
        assertTrue(ProxyAutoConfig.shExpMatch("http://home.netscape.com/people/ari/index.html", "*/ari/*"));
        assertFalse(ProxyAutoConfig.shExpMatch("http://home.netscape.com/people/montulli/index.html", "*/ari/*"));
    }

    /**
     * Test case.
     */
    @Test
    public void weekdayRange() {
        final Calendar calendar = Calendar.getInstance();
        final String today = new SimpleDateFormat("EEE").format(calendar.getTime()).toUpperCase();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        final String tomorrow = new SimpleDateFormat("EEE").format(calendar.getTime()).toUpperCase();
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        final String yesterday = new SimpleDateFormat("EEE").format(calendar.getTime()).toUpperCase();
        assertTrue(ProxyAutoConfig.weekdayRange(today, Undefined.instance, Undefined.instance));
        assertTrue(ProxyAutoConfig.weekdayRange(today, tomorrow, Undefined.instance));
        assertTrue(ProxyAutoConfig.weekdayRange(yesterday, today, Undefined.instance));
        assertTrue(ProxyAutoConfig.weekdayRange(yesterday, tomorrow, Undefined.instance));
        assertFalse(ProxyAutoConfig.weekdayRange(tomorrow, yesterday, Undefined.instance));
    }

    /**
     * Test case.
     */
    @Test
    public void dateRange() {
        final Object undefined = Undefined.instance;
        final Calendar calendar = Calendar.getInstance();
        final int today = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        final int tomorrow = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        final int yesterday = calendar.get(Calendar.DAY_OF_MONTH);
        assertTrue(ProxyAutoConfig.dateRange(String.valueOf(today),
                undefined, undefined, undefined, undefined, undefined, undefined));
        assertFalse(ProxyAutoConfig.dateRange(String.valueOf(yesterday),
                undefined, undefined, undefined, undefined, undefined, undefined));
        assertFalse(ProxyAutoConfig.dateRange(String.valueOf(tomorrow),
                undefined, undefined, undefined, undefined, undefined, undefined));
    }

    /**
     * Test case.
     */
    @Test
    public void timeRange() {
        final Object undefined = Undefined.instance;
        final Calendar calendar = Calendar.getInstance();
        final int now = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        final int after = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.add(Calendar.HOUR_OF_DAY, -4);
        final int before = calendar.get(Calendar.HOUR_OF_DAY);
        assertTrue(ProxyAutoConfig.timeRange(String.valueOf(now),
                undefined, undefined, undefined, undefined, undefined, undefined));
        assertFalse(ProxyAutoConfig.timeRange(String.valueOf(before),
                undefined, undefined, undefined, undefined, undefined, undefined));
        assertFalse(ProxyAutoConfig.timeRange(String.valueOf(after),
                undefined, undefined, undefined, undefined, undefined, undefined));
    }

    /**
     * Test case.
     */
    @Test
    public void bindings() {
        final String content = "ProxyConfig.bindings.com = 'my_com';\n"
            + "ProxyConfig.bindings.org = 'my_org';\n"
            + "ProxyConfig.bindings.net = 'my_net';\n"
            + "ProxyConfig.bindings.edu = 'my_edu';\n"
            + "ProxyConfig.bindings.gov = 'my_gov';\n"
            + "function FindProxyForURL(url, host) {\n"
            + "  var returnValue = '';\n"
            + "  for (var x in ProxyConfig.bindings) {\n"
            + "    returnValue += x + ' ';\n"
            + "  }\n"
            + "  return returnValue;\n"
            + "}\n";
        final String value = ProxyAutoConfig.evaluate(content, URL_FIRST);
        assertEquals("com org net edu gov ", value);
    }

}
