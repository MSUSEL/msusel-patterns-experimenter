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
package org.geotools.xml.xsi;

import java.sql.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.util.Converters;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.impl.ElementValueGT;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XSISimpleTypesTest extends TestCase {

    public void testParseDate() throws Exception {
        SimpleType dateBinding = XSISimpleTypes.Date.getInstance();

        Element element = null;
        Attributes attrs = null;
        Map hints = null;
        ElementValue[] value;
        Date expected;
        String sval;
        Object actual;

        sval = "2012-02-14";
        value = new ElementValue[] { new ElementValueGT(null, sval) };
        actual = dateBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Date.class);
        assertEquals(expected.getClass().getName() + "[" + expected + "] : "
                + actual.getClass().getName() + "[" + actual + "]", expected, actual);

        sval = "2012-02-14Z";
        value = new ElementValue[] { new ElementValueGT(null, sval) };
        actual = dateBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Date.class);
        assertEquals(expected.getClass().getName() + "[" + expected + "] : "
                + actual.getClass().getName() + "[" + actual + "]", expected, actual);

        sval = "2011-10-24T10:53:24.200Z";
        value = new ElementValue[] { new ElementValueGT(null, sval) };
        actual = dateBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Date.class);
        assertEquals(expected.getClass().getName() + "[" + expected + "] : "
                + actual.getClass().getName() + "[" + actual + "]", expected, actual);

        sval = "10:53:24.255+03:00";
        value = new ElementValue[] { new ElementValueGT(null, sval) };
        try {
            dateBinding.getValue(element, value, attrs, hints);
        } catch (SAXException e) {
            assertTrue(true);
        }
    }

    public void testParseDateTime() throws Exception {
        SimpleType dateTimeBinding = XSISimpleTypes.DateTime.getInstance();

        Element element = null;
        Attributes attrs = null;
        Map hints = null;
        ElementValue[] value;
        java.util.Date expected;
        String sval;
        Object actual;

        sval = "2012-02-14";
        value = new ElementValue[] { new ElementValueGT(null, sval) };
        actual = dateTimeBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Timestamp.class);
        assertEquals(expected.getClass().getName() + "[" + expected + "] : "
                + actual.getClass().getName() + "[" + actual + "]", expected, actual);

        sval = "2012-02-14Z";
        value = new ElementValue[] { new ElementValueGT(null, sval) };
        actual = dateTimeBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Timestamp.class);
        assertEquals(expected.getClass().getName() + "[" + expected + "] : "
                + actual.getClass().getName() + "[" + actual + "]", expected, actual);

        sval = "2011-10-24T10:53:24.200Z";
        value = new ElementValue[] { new ElementValueGT(null, sval) };
        actual = dateTimeBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Timestamp.class);
        assertEquals(expected.getClass().getName() + "[" + expected + "] : "
                + actual.getClass().getName() + "[" + actual + "]", expected, actual);

        sval = "2011-10-24T00:00:00.200+03:00";
        value = new ElementValue[] { new ElementValueGT(null, sval) };
        actual = dateTimeBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Timestamp.class);
        assertEquals(expected.getClass().getName() + "[" + expected + "] : "
                + actual.getClass().getName() + "[" + actual + "]", expected, actual);

        sval = "10:53:24.255+03:00";
        value = new ElementValue[] { new ElementValueGT(null, sval) };
        try {
            dateTimeBinding.getValue(element, value, attrs, hints);
        } catch (SAXException e) {
            assertTrue(true);
        }
    }

    public void testParseTime() throws Exception {
        SimpleType timeBinding = XSISimpleTypes.Time.getInstance();

        Element element = null;
        Attributes attrs = null;
        Map hints = null;
        ElementValue[] value;
        java.util.Date expected;
        String sval;
        Object actual;

        sval = "10:53:24Z";
        value = new ElementValue[] { new ElementValueGT(null, sval) };
        actual = timeBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Time.class);
        assertEquals(expected.getClass().getName() + "[" + expected + "] : "
                + actual.getClass().getName() + "[" + actual + "]", expected, actual);

        sval = "10:53:24-03:00";
        value = new ElementValue[] { new ElementValueGT(null, sval) };
        actual = timeBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Time.class);
        assertEquals(expected.getClass().getName() + "[" + expected + "] : "
                + actual.getClass().getName() + "[" + actual + "]", expected, actual);

        sval = "10:53:24.255+03:00";
        value = new ElementValue[] { new ElementValueGT(null, sval) };
        actual = timeBinding.getValue(element, value, attrs, hints);
        assertNotNull(actual);
        expected = Converters.convert(sval, java.sql.Time.class);
        assertEquals(expected.getClass().getName() + "[" + expected + "] : "
                + actual.getClass().getName() + "[" + actual + "]", expected, actual);

        sval = "2012-02-14";
        value = new ElementValue[] { new ElementValueGT(null, sval) };
        try {
            timeBinding.getValue(element, value, attrs, hints);
        } catch (SAXException e) {
            assertTrue(true);
        }
    }
}
