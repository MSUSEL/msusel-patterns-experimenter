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
package com.gargoylesoftware.htmlunit.javascript.configuration;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link JavaScriptConfiguration}.
 *
 * @version $Revision: 5627 $
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class ClassConfigurationTest extends WebTestCase {

    /**
     * Constructor.
     */
    public ClassConfigurationTest() {
        JavaScriptConfiguration.resetClassForTesting();
    }

    /**
     * Tests equality on a class configuration.
     * @throws Exception - Exception on error
     */
    @Test
    public void testConfigurationSimplePropertyEquality() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        final ClassConfiguration config2 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);

        config1.addProperty("test", true, true);
        Assert.assertFalse("Configs should not be equal", config1.equals(config2));
        config2.addProperty("test", true, true);
        assertTrue("Configs should now be equal", config1.equals(config2));
    }

    /**
     * Tests equality on a class configuration for function.
     * @throws Exception - Exception on error
     */
    @Test
    public void testConfigurationSimpleFunctionEquality() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        final ClassConfiguration config2 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);

        config1.addFunction("testFunction");
        Assert.assertFalse("Configs should not be equal", config1.equals(config2));
        config2.addFunction("testFunction");
        assertTrue("Configs should now be equal", config1.equals(config2));
    }

    /**
     * Tests equality on a class configuration.
     * @throws Exception - Exception on error
     */
    @Test
    public void testConfigurationSimpleUnequalProperties() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        final ClassConfiguration config2 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);

        config1.addProperty("test", true, true);
        Assert.assertFalse("Configs should not be equal", config1.equals(config2));
        config2.addProperty("test", true, false);
        Assert.assertFalse("Configs should not be equal due to different property values", config1.equals(config2));
    }

    /**
     * @throws Exception on error
     */
    @Test
    public void testForJSFlagTrue() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        assertTrue("JSObject Flag should have been set", config1.isJsObject());
    }

    /**
     * @throws Exception on error
     */
    @Test
    public void testForJSFlagFalse() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, false);
        Assert.assertFalse("JSObject Flag should not have been set", config1.isJsObject());
    }

    /**
     * Tests equality on a class configuration.
     * @throws Exception - Exception on error
     */
    @Test
    public void testConfigurationPropertyEqualityWithBrowser() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        final ClassConfiguration config2 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);

        config1.addProperty("test", true, true);
        config2.addProperty("test", true, true);
        config1.setBrowser("test", "Netscape");
        Assert.assertFalse("Should not be equal with browser added", config1.equals(config2));
        config2.setBrowser("test", "Netscape");
        assertTrue("Should be equal with browser added", config1.equals(config2));
    }

    /**
     * Tests equality on a class configuration mis-matched browsers.
     * @throws Exception - Exception on error
     */
    @Test
    public void testConfigurationPropertyEqualityWithDifferentBrowsers() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        final ClassConfiguration config2 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);

        config1.addProperty("test", true, true);
        config2.addProperty("test", true, true);
        config1.setBrowser("test", "Netscape");
        Assert.assertFalse("Should not be equal with browser added", config1.equals(config2));
        config2.setBrowser("test", "Microsoft Internet Explorer");
        Assert.assertFalse("Should be equal with different browser added", config1.equals(config2));
    }

    /**
     * Test for throwing exception when setter method is not defined.
     * @throws Exception - Exception on error
     */
    @Test
    public void testNoSetterMethod() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        try {
            config1.addProperty("getterOnly", true, true);
            fail("Should produce an exception due to not finding the methods");
        }
        catch (final IllegalStateException e) {
            assertTrue(true);
        }
    }

    /**
     * Test for throwing exception when setter method is not defined.
     * @throws Exception - Exception on error
     */
    @Test
    public void testNoFunctionMethod() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        try {
            config1.addFunction("noTestFunction");
            fail("Should produce an exception due to not finding the methods");
        }
        catch (final IllegalStateException e) {
            assertTrue(true);
        }
    }

    /**
     * Test class.
     */
    protected class ConfigTestClass {
        private boolean test_ = false;

        /**
         * Dummy function.
         */
        public void jsxFunction_testFunction() {
        }

        /**
         * @return boolean
         */
        public boolean jsxGet_test() {
            return test_;
        }

        /**
         * @return boolean
         */
        public boolean jsxGet_getterOnly() {
            return test_;
        }

        /**
         * @param testFlag - test value
         */
        public void jsxSet_test(final boolean testFlag) {
            test_ = testFlag;
        }
    }
}
