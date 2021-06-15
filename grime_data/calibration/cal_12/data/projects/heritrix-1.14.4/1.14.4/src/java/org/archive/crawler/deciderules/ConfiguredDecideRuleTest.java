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
package org.archive.crawler.deciderules;

import java.io.File;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import org.archive.crawler.datamodel.CrawlOrder;
import org.archive.crawler.settings.MapType;
import org.archive.crawler.settings.SettingsHandler;
import org.archive.crawler.settings.XMLSettingsHandler;
import org.archive.util.TmpDirTestCase;

/**
 * @author stack
 * @version $Date: 2005-04-05 01:12:11 +0000 (Tue, 05 Apr 2005) $, $Revision: 3318 $
 */
public class ConfiguredDecideRuleTest extends TmpDirTestCase {
    /**
     * Gets setup by {@link #setUp()}.
     */
    private ConfiguredDecideRule rule = null;
    
    protected void setUp() throws Exception {
        super.setUp();
        final String name = this.getClass().getName();
        SettingsHandler settingsHandler = new XMLSettingsHandler(
            new File(getTmpDir(), name + ".order.xml"));
        settingsHandler.initialize();
        // Create a new ConfigureDecideRule instance and add it to a MapType
        // (I can change MapTypes after instantiation).  The chosen MapType
        // is the rules canonicalization rules list.
        this.rule = (ConfiguredDecideRule)((MapType)settingsHandler.getOrder().
            getAttribute(CrawlOrder.ATTR_RULES)).addElement(settingsHandler.
                getSettingsObject(null), new ConfiguredDecideRule(name));
    }
    
    public void testDefault() {
        Object decision = rule.decisionFor(new Object());
        assertTrue("Wrong answer " + decision, decision == DecideRule.ACCEPT);
    }
    
    public void testACCEPT()
    throws AttributeNotFoundException, InvalidAttributeValueException,
    MBeanException, ReflectionException {
        runTest(DecideRule.ACCEPT);
    }
    
    public void testPASS()
    throws AttributeNotFoundException, MBeanException, ReflectionException {
        String exceptionMessage = null;
        try {
            runTest(DecideRule.PASS);
        } catch(InvalidAttributeValueException e) {
            exceptionMessage = e.getMessage();
        }
        assertNotNull("Did not get expected exception", exceptionMessage);
    }
    
    public void testREJECT()
    throws AttributeNotFoundException, InvalidAttributeValueException,
    MBeanException, ReflectionException {
        runTest(DecideRule.REJECT);
    }
    
    protected void runTest(String expectedResult)
    throws AttributeNotFoundException, InvalidAttributeValueException,
    MBeanException, ReflectionException {
        configure(expectedResult);
        Object decision = rule.decisionFor(new Object());
        assertTrue("Expected " + expectedResult + " but got answer " +
            decision, decision == expectedResult);
    }
    
    protected void configure(String setting)
    throws AttributeNotFoundException, InvalidAttributeValueException,
    MBeanException, ReflectionException {
        this.rule.setAttribute(
            new Attribute(ConfiguredDecideRule.ATTR_DECISION, setting));
    }
}
