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
package org.hibernate.osgi;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.spi.PersistenceProvider;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.ejb.HibernatePersistence;
import org.hibernate.internal.util.ClassLoaderHelper;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

/**
 * @author Brett Meyer
 * @author Martin Neimeier
 */
public class HibernateBundleActivator
		implements BundleActivator, /*ServiceListener,*/ BundleListener {
	
	private OsgiClassLoader osgiClassLoader;

    @Override
    public void start(BundleContext context) throws Exception {
    	
    	// register this instance as a bundle listener to get informed about all
        // bundle live cycle events
        context.addBundleListener(this);
        
    	osgiClassLoader = new OsgiClassLoader();
    	
    	ClassLoaderHelper.overridenClassLoader = osgiClassLoader;

        for ( Bundle bundle : context.getBundles() ) {
        	handleBundleChange( bundle );
        }
        
        HibernatePersistence hp = new HibernatePersistence();
        Map map = new HashMap();
        map.put( AvailableSettings.JTA_PLATFORM, new OsgiJtaPlatform( context ) );
        hp.setEnvironmentProperties( map );
    	
        Properties properties = new Properties();
        properties.put( "javax.persistence.provider", HibernatePersistence.class.getName() );
        context.registerService( PersistenceProvider.class.getName(), hp, properties );
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        context.removeBundleListener(this);
        
        // Nothing else to do.  When re-activated, this Activator will be
        // re-started and the EMF re-created.
    }

    @Override
    public void bundleChanged(BundleEvent event) {
    	handleBundleChange( event.getBundle() );

    }
    
    private void handleBundleChange( Bundle bundle ) {
    	if ( bundle.getState() == Bundle.ACTIVE ) {
    		osgiClassLoader.registerBundle(bundle);
    	} else {
    		osgiClassLoader.unregisterBundle(bundle);
    	}
    }

}