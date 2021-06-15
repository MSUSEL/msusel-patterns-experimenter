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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link TextUtil}.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class TextUtilTest extends WebTestCase {
    /**
     * Test startsWithIgnoreCase() with null values.
     */
    @Test
    public void testStartsWithIgnoreCase_nulls() {
        try {
            TextUtil.startsWithIgnoreCase(null, "foo");
            fail("Expected null pointer exception");
        }
        catch (final NullPointerException e) {
            // Expected path
        }

        try {
            TextUtil.startsWithIgnoreCase("foo", null);
            fail("Expected null pointer exception");
        }
        catch (final NullPointerException e) {
            // Expected path
        }
    }

    /**
     * Test startsWithIgnoreCase() with an empty prefix.
     */
    @Test
    public void testStartsWithIgnoreCase_emptyPrefix() {
        try {
            TextUtil.startsWithIgnoreCase("foo", "");
            fail("Expected IllegalArgumentException");
        }
        catch (final IllegalArgumentException e) {
            // Expected path
        }
    }

    /**
     * Test a variety of cases that should return true.
     */
    @Test
    public void testStartsWithIgnoreCase_ShouldReturnTrue() {
        final String[][] data = {
            {"foo", "foo"},
            {"foo:bar", "foo"},
            {"FOO:BAR", "foo"},
            {"foo:bar", "FOO"},
        };

        for (final String[] entry : data) {
            final String stringToCheck = entry[0];
            final String prefix = entry[1];

            Assert.assertTrue(
                "stringToCheck=[" + stringToCheck + "] prefix=[" + prefix + "]",
                TextUtil.startsWithIgnoreCase(stringToCheck, prefix));
        }
    }

    /**
     * Test a variety of cases that should return false.
     */
    @Test
    public void testStartsWithIgnoreCase_ShouldReturnFalse() {
        final String[][] data = {
            {"", "foo"},
            {"fobar", "foo"},
            {"fo", "foo"},
        };

        for (final String[] entry : data) {
            final String stringToCheck = entry[0];
            final String prefix = entry[1];

            Assert.assertFalse(
                "stringToCheck=[" + stringToCheck + "] prefix=[" + prefix + "]",
                TextUtil.startsWithIgnoreCase(stringToCheck, prefix));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testToInputStream_null() throws Exception {
        try {
            TextUtil.toInputStream(null);
            fail("Expected NullPointerException");
        }
        catch (final NullPointerException e) {
            // Expected path
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testToInputStream() throws Exception {
        final String[][] data = {
            {"", null},
            {"a", "a"},
            {"abcdefABCDEF", "abcdefABCDEF"},
        };
        final String encoding = "ISO-8859-1";

        for (final String[] entry : data) {
            final String input = entry[0];
            final String expectedResult = entry[1];

            final InputStream inputStream = TextUtil.toInputStream(input, encoding);
            final String actualResult = new BufferedReader(new InputStreamReader(inputStream, encoding)).readLine();
            Assert.assertEquals(expectedResult, actualResult);
        }
    }
}
