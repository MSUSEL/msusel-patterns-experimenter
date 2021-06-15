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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Object is a native JavaScript object and therefore provided by Rhino but some tests are needed here
 * to be sure that we have the expected results (for instance EcmaScript 5 adds methods that are not
 * available in FF2 or FF3).
 *
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class NativeObjectTest extends WebDriverTestCase {

    /**
     * Test for the methods with the same expectations for all browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "constructor: function", "create: undefined", "defineProperties: undefined", "defineProperty: undefined",
        "freeze: undefined", "getOwnPropertyDescriptor: undefined", "getOwnPropertyNames: undefined",
        "getPrototypeOf: undefined", "hasOwnProperty: function", "isExtensible: undefined", "isFrozen: undefined",
        "isPrototypeOf: function", "isSealed: undefined", "keys: undefined", "preventExtensions: undefined",
        "propertyIsEnumerable: function", "seal: undefined", "toLocaleString: function", "toString: function",
        "valueOf: function" })
    public void methods_common() throws Exception {
        final String[] methods = {"constructor", "create", "defineProperties", "defineProperty", "freeze",
            "getOwnPropertyDescriptor", "getOwnPropertyNames", "getPrototypeOf", "hasOwnProperty", "isExtensible",
            "isFrozen", "isPrototypeOf", "isSealed", "keys", "preventExtensions", "propertyIsEnumerable", "seal",
            "toLocaleString", "toString", "valueOf"};
        final String html = NativeDateTest.createHTMLTestMethods("new Object()", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with the different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "__defineGetter__: function", "__defineSetter__: function", "__lookupGetter__: function",
            "__lookupSetter__: function", "toSource: function" },
            IE = { "__defineGetter__: undefined", "__defineSetter__: undefined", "__lookupGetter__: undefined",
            "__lookupSetter__: undefined", "toSource: undefined" })
    public void methods_different() throws Exception {
        final String[] methods = {"__defineGetter__", "__defineSetter__", "__lookupGetter__", "__lookupSetter__",
            "toSource"};
        final String html = NativeDateTest.createHTMLTestMethods("new Object()", methods);
        loadPageWithAlerts2(html);
    }
}
