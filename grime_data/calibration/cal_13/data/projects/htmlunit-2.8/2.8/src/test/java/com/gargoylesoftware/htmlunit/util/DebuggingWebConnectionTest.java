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
package com.gargoylesoftware.htmlunit.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link DebuggingWebConnection}.
 *
 * @version $Revision: 5940 $
 * @author Marc Guillemot
 */
public class DebuggingWebConnectionTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void nameValueListToJsMap() throws Exception {
        assertEquals("{}", DebuggingWebConnection.nameValueListToJsMap(null));
        final List<NameValuePair> emptyList = Collections.emptyList();
        assertEquals("{}", DebuggingWebConnection.nameValueListToJsMap(emptyList));

        List<NameValuePair> list = Collections.singletonList(new NameValuePair("name", "value"));
        assertEquals("{'name': 'value'}", DebuggingWebConnection.nameValueListToJsMap(list));

        list = Collections.singletonList(new NameValuePair("na me", "value"));
        assertEquals("{'na me': 'value'}", DebuggingWebConnection.nameValueListToJsMap(list));

        list = new ArrayList<NameValuePair>();
        list.add(new NameValuePair("na me", "value1"));
        list.add(new NameValuePair("key", "value 2"));
        list.add(new NameValuePair("key 2", "value 3"));
        list.add(new NameValuePair("key 4", "with ' quote")); // can it really happen in header?
        final String expected = "{'na me': 'value1', 'key': 'value 2', 'key 2': 'value 3', 'key 4': 'with \\' quote'}";
        assertEquals(expected, DebuggingWebConnection.nameValueListToJsMap(list));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void escapeJSString() throws Exception {
        assertEquals("", DebuggingWebConnection.escapeJSString(""));
        assertEquals("hello", DebuggingWebConnection.escapeJSString("hello"));
        assertEquals("I\\'m here", DebuggingWebConnection.escapeJSString("I'm here"));
    }
}
