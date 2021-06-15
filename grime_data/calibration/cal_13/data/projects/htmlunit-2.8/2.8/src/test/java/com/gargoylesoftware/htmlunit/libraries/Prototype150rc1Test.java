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
package com.gargoylesoftware.htmlunit.libraries;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for compatibility with version 1.5.0-rc1 of
 * <a href="http://prototype.conio.net/">Prototype JavaScript library</a>.
 *
 * @version $Revision: 5301 $
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class Prototype150rc1Test extends PrototypeTestBase {

    /**
     * @throws Exception if test fails
     */
    @Test
    public void ajax() throws Exception {
        test("ajax.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void array() throws Exception {
        test("array.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void base() throws Exception {
        test("base.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void dom() throws Exception {
        test("dom.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void elementMixins() throws Exception {
        test("element_mixins.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void enumerable() throws Exception {
        test("enumerable.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void form() throws Exception {
        test("form.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void hash() throws Exception {
        test("hash.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented
    public void position() throws Exception {
        test("position.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void range() throws Exception {
        test("range.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    public void selector() throws Exception {
        test("selector.html");
    }

    /**
     * Blocked by Rhino bug 369860 (https://bugzilla.mozilla.org/show_bug.cgi?id=369860).
     * @throws Exception if test fails
     */
    @Test
    public void string() throws Exception {
        test("string.html");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getVersion() {
        return "1.5.0-rc1";
    }
}
