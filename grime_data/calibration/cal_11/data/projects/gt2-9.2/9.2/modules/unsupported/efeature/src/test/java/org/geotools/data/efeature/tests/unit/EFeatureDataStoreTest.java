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

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.efeature.EFeatureDataStore;
import org.geotools.data.efeature.EFeatureDataStoreFactory;
import org.geotools.data.efeature.EFeatureFactoryFinder;
import org.geotools.data.efeature.tests.impl.EFeatureTestsContextHelper;
import org.geotools.feature.NameImpl;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/**
 * 
 * @author kengu - 4. mai 2011  
 *
 *
 * @source $URL$
 */
public class EFeatureDataStoreTest extends AbstractResourceTest {
    
    /** 
     * Static logger for all {@link EFeatureDataStoreTest} instances 
     */
    //private static final Logger LOGGER = Logging.getLogger(EFeatureDataStoreTest.class); 
    
    private static final Set<String> TYPE_NAMES = new HashSet<String>(
            Arrays.asList("efeature.EFeatureData","efeature.EFeatureCompatibleData"));

    private ParameterInfoTestData eParams;
    private EFeatureTestsContextHelper eContextHelper;
    private EFeatureDataStoreFactory eStoreFactory;
 
    // ----------------------------------------------------- 
    //  Tests
    // -----------------------------------------------------

    @org.junit.Test
    public void testParametersInfo() {
        Param[] params = eStoreFactory.getParametersInfo();
        assertParams(eParams.getParametersInfo(),params);
    }
    
    @org.junit.Test
    public void testDataStore() {
        Map<String,Serializable> params = eParams.createParams(null, null);
        assertFalse("Invalid parameters can be processed",eStoreFactory.canProcess(params));
        params = eParams.createParams(eResourceURI.toString(), null);
        assertTrue("Valid parameters can not be processed",eStoreFactory.canProcess(params));
        try {
            EFeatureDataStore eStore = eStoreFactory.createDataStore(params);
            String[] typeNames = eStore.getTypeNames();
            for(String it : typeNames) {
                assertTrue("EFeatureDataStore does not contain type ["+it+"]",TYPE_NAMES.contains(it));                
                Name name = new NameImpl(it);
                SimpleFeatureType eTypeI = eStore.eStructure().eGetFeatureInfo(it).getFeatureType();
                SimpleFeatureType eTypeN = eStore.getSchema(name);
                assertTrue("SimpleFeatureTypes are not same instance (Name)",eTypeI==eTypeN);
                SimpleFeatureType eTypeL = eStore.getSchema(it);
                assertTrue("SimpleFeatureTypes are not same instance (Local)",eTypeI==eTypeL);
            }
            eStore.dispose();
        } catch (Exception e) {
            fail(e);
        }
    }
    
    // ----------------------------------------------------- 
    //  TestCase setup
    // -----------------------------------------------------

    /**
     * Main for test runner.
     */
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     * Constructor with test name.
     */
    public EFeatureDataStoreTest(String name) {
        super(name, "xmi", true, false);
    }
    
    /**
     * Required suite builder.
     * @return A test suite for this unit test.
     */
    public static Test suite() {
        return new TestSuite(EFeatureDataStoreTest.class);
    }
        
    @Override
    protected void doSetUp() throws Exception {
        //
        // Initialize commonly used objects
        //
        eParams = new ParameterInfoTestData();        
        eContextHelper = new EFeatureTestsContextHelper(true, false);
        eStoreFactory = EFeatureFactoryFinder.getDataStoreFactory();
    }
    
    // ----------------------------------------------------- 
    //  AbstractStandaloneTest implementation
    // -----------------------------------------------------
            
    @Override
    protected ResourceSet createResourceSet() {        
        return eContextHelper.getResourceSet();
    }
    
    @Override
    protected EditingDomain createEditingDomain(ResourceSet rset) {
        return eContextHelper.getEditingDomain();
    }
        
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
        
    // ----------------------------------------------------- 
    //  Test assertion methods
    // -----------------------------------------------------
    
    private static void assertParams(Param[] test, Param[] actual)
    {
        assertEquals("Parameter count mismatch",test.length,actual.length);
        Map<String,Param> params = new HashMap<String, Param>(test.length);
        for(Param it :  actual) {
            params.put(it.key, it);
        }
        for(Param it : test) {
            assertTrue("Test parameter " + it.key + " not found.",params.containsKey(it.key));
        }
    }
    
    
    
    
}
