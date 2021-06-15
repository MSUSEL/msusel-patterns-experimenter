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
package org.archive.crawler.settings;

import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import org.archive.crawler.datamodel.CrawlOrder;
import org.archive.crawler.framework.Processor;

/**
 * Test the concept of overrides.
 *
 * As this test is testing a concept, it involves more than one class to be
 * tested. Thus the name of this test doesn't match a class name.
 *
 * @author John Erik Halse
 *
 */
public class OverrideTest extends SettingsFrameworkTestCase {

    /*
     * @see SettingsFrameworkTestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @see SettingsFrameworkTestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testOverridingOfGlobalAttribute()
            throws AttributeNotFoundException, MBeanException,
            ReflectionException, InvalidAttributeValueException {

        final String MODULE_NAME = "module1";
        ModuleType module1 = new ModuleType(MODULE_NAME);
        ModuleType module2 = new Processor(MODULE_NAME, "Descr");

        // Set up override
        MapType headers = (MapType) getSettingsHandler().getOrder()
                .getAttribute(CrawlOrder.ATTR_HTTP_HEADERS);
        headers.addElement(getGlobalSettings(), module1);
        headers.setAttribute(getPerDomainSettings(), module2);

        // Read back values to see if we get the right ones
        ModuleType getMod;
        getMod = (ModuleType) headers.getAttribute(getGlobalSettings(),
                MODULE_NAME);
        assertSame("Wrong global value", module1, getMod);
        assertEquals("Wrong class type", module1.getClass().getName(), headers
                .getAttributeInfo(getGlobalSettings(), MODULE_NAME).getType());

        getMod = (ModuleType) headers.getAttribute(getPerDomainSettings(),
                MODULE_NAME);
        assertSame("Wrong domain value", module2, getMod);
        assertEquals("Wrong class type", module2.getClass().getName(), headers
                .getAttributeInfo(getPerDomainSettings(), MODULE_NAME)
                .getType());

        getMod = (ModuleType) headers.getAttribute(getPerHostSettings(),
                MODULE_NAME);
        assertSame("Wrong host value", module2, getMod);
        assertEquals("Wrong class type", module2.getClass().getName(), headers
                .getAttributeInfo(getPerHostSettings(), MODULE_NAME).getType());
    }

    public void testOverridingOfNonGlobalAttribute()
            throws AttributeNotFoundException, MBeanException,
            ReflectionException, InvalidAttributeValueException {
        final String MODULE_NAME = "module1";
        ModuleType module1 = new ModuleType(MODULE_NAME);
        ModuleType module2 = new Processor(MODULE_NAME, "Descr");

        // Set up override
        MapType headers = (MapType) getSettingsHandler().getOrder()
                .getAttribute(CrawlOrder.ATTR_HTTP_HEADERS);
        headers.addElement(getPerDomainSettings(), module1);
        headers.setAttribute(getPerHostSettings(), module2);

        // Read back values to see if we get the right ones
        ModuleType getMod;
        try {
            getMod = (ModuleType) headers.getAttribute(getGlobalSettings(),
                    MODULE_NAME);
            fail("Global value should not exist");
        } catch (AttributeNotFoundException e) {
            // OK! this should throw an exception;
        }

        getMod = (ModuleType) headers.getAttribute(getPerDomainSettings(),
                MODULE_NAME);
        assertSame("Wrong domain value", module1, getMod);
        assertEquals("Wrong class type", module1.getClass().getName(), headers
                .getAttributeInfo(getPerDomainSettings(), MODULE_NAME)
                .getType());

        getMod = (ModuleType) headers.getAttribute(getPerHostSettings(),
                MODULE_NAME);
        assertSame("Wrong host value", module2, getMod);
        assertEquals("Wrong class type", module2.getClass().getName(), headers
                .getAttributeInfo(getPerHostSettings(), MODULE_NAME).getType());
    }

}