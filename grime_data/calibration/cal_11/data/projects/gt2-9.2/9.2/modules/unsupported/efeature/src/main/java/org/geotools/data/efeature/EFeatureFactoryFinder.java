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
package org.geotools.data.efeature;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Iterator;

import org.geotools.data.DataAccessFactory;
import org.geotools.data.DataAccessFinder;
import org.geotools.factory.BufferedFactory;
import org.geotools.factory.FactoryCreator;
import org.geotools.factory.FactoryFinder;
import org.geotools.factory.FactoryRegistry;
import org.geotools.factory.FactoryRegistryException;

/**
 * A {@link FactoryFinder} implementation  
 * 
 * @author kengu
 *
 *
 * @source $URL$
 */
public class EFeatureFactoryFinder extends FactoryFinder {
    
    private static WeakReference<EFeatureDataStoreFactory> eFeatureStoreFactory;
    private static WeakReference<EFeatureContextFactory> eFeatureContextFactory;
    
    /**
     * Get cached {@link EFeatureDataStoreFactory} instance.
     */
    public static EFeatureDataStoreFactory getDataStoreFactory()
            throws FactoryRegistryException
    {
        // Not set or is garbage collected?
        //
        if(eFeatureStoreFactory==null || eFeatureStoreFactory.get()==null) {
            
            // Do the lousy slow system scan
            //
            synchronized (EFeatureFactoryFinder.class) {
                
                // Query all factories
                //
                Iterator<DataAccessFactory> factories = DataAccessFinder.getAllDataStores();
                
                // Get the factory instance
                //
                while(factories.hasNext())
                {
                    DataAccessFactory it = factories.next();
                    if(it instanceof EFeatureDataStoreFactory)
                    {
                        eFeatureStoreFactory = 
                            new WeakReference<EFeatureDataStoreFactory>(
                                    (EFeatureDataStoreFactory)it);
                    }
                }                
            }            
        }
        // Verify that instance was found 
        //
        if(eFeatureStoreFactory==null || eFeatureStoreFactory.get()==null)
        {
            throw new FactoryRegistryException("EFeatureDataStoreFactory instance not found. " +
            		"Have you registered a EFeatureContext instance?");
        }
        return eFeatureStoreFactory.get();
    }
    
    /**
     * Get cached {@link EFeatureContextFactory} instance.
     */
    public static EFeatureContextFactory getContextFactory()
            throws FactoryRegistryException
    {
        // Not set or is garbage collected?
        //
        if(eFeatureContextFactory==null || eFeatureContextFactory.get()==null) {
            
            // Do the lousy slow system scan
            //
            synchronized (EFeatureFactoryFinder.class) {
                
                // Query all factories
                //
                Iterator<BufferedFactory> factories = 
                       getServiceRegistry().getServiceProviders(
                               BufferedFactory.class, null, null);
                
                if(factories.hasNext())
                {
                    EFeatureContextFactory it = (EFeatureContextFactory)factories.next();
                    eFeatureContextFactory = new WeakReference<EFeatureContextFactory>(it);
                }
            }            
        }
        // Verify that instance was found 
        //
        if(eFeatureContextFactory==null || eFeatureContextFactory.get()==null)
        {
            throw new FactoryRegistryException("EFeatureContextFactory instance not found. ");
        }
        return eFeatureContextFactory.get();
    }            
    
    /**
     * The service registry for this manager.
     * Will be initialized only when first needed.
     */
    private static FactoryRegistry registry;

    /**
     * Do not allows any instantiation of this class.
     */
    private EFeatureFactoryFinder() {
        // singleton
    }
    
    /**
     * Get the service registry instance.
     * <p> 
     * The registry is lazily created the first time this method is invoked.
     * </p>
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(EFeatureFactoryFinder.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(
                    new Class<?>[] {BufferedFactory.class,
                            EFeatureContextFactory.class
                    }));
        }
        return registry;
    }    
    
}
