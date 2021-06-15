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
package org.geotools.data.efeature.tests.unit;

import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.tests.unit.conditions.EAttributeFilterAllTests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;


/**
 * Test suite for {@link EFeature} implementation.
 * 
 * @author kengu
 *
 *
 * @source $URL$
 */
public class EFeatureAllTests extends TestSuite {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public EFeatureAllTests(String name) {
        super(name);
    }
    
    public static Test suite() {
        //
        // Create test suite
        //
        TestSuite suite = new EFeatureAllTests("EFeature Tests");
        //
        // Add tests
        //
        suite.addTestSuite(DataBuilderTest.class);
        suite.addTestSuite(EFeatureContextTest.class);
        suite.addTestSuite(EFeatureQueryTest.class);
        suite.addTestSuite(EFeatureDataStoreTest.class);
        suite.addTestSuite(EFeatureReaderTest.class);        
        suite.addTestSuite(EFeatureHintsTest.class);        
        suite.addTest(EAttributeFilterAllTests.suite());
        //
        // Ready to execute
        //
        return suite;
    }


} //EFeatureAllTests
