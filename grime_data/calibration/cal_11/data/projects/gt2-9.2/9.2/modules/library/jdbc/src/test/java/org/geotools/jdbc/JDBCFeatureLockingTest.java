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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.jdbc;

import java.util.Collections;

import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLockException;
import org.geotools.data.FeatureLockFactory;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

/**
 * 
 *
 * @source $URL$
 */
public abstract class JDBCFeatureLockingTest extends JDBCTestSupport {

    JDBCFeatureStore store;
    
    protected void connect() throws Exception {
        super.connect();
    
        store = (JDBCFeatureStore) dataStore.getFeatureSource(tname("ft1"));
        store.setFeatureLock(FeatureLock.TRANSACTION);
    }
    
    public void testLockFeatures() throws Exception {
        
        FeatureLock lock = 
            FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);
        
        Transaction tx = new DefaultTransaction();
        store.setTransaction( tx );
        store.setFeatureLock(lock);
        
        //lock features
        int locked = store.lockFeatures();
        assertTrue( locked > 0 );
        
        //grabbing a reader should be no problem
        DefaultQuery query = new DefaultQuery( tname("ft1") );
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader(query,tx);
        
        int count = 0;
        while( reader.hasNext() ) {
            count++;
            reader.next();
        }
        assertTrue( count > 0 );
        reader.close();
        
        //grab a writer
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriter(tname("ft1"), tx );
        assertTrue( writer.hasNext() );
        SimpleFeature feature = writer.next();
        
        feature.setAttribute(aname("intProperty"), new Integer(100) );
        try {
            writer.write();  
            fail( "should have thrown feature lock exception" );
        }
        catch( FeatureLockException e ) {
            //good
        }
        finally {
            writer.close();
        }
        
        tx.addAuthorization(lock.getAuthorization());
        writer = dataStore.getFeatureWriter(tname("ft1"), tx);
        assertTrue( writer.hasNext() );
        feature = writer.next();
        feature.setAttribute(aname("intProperty"), new Integer(100) );
        writer.write();
        writer.close();
        tx.close();
    }
    
    public void testLockFeaturesWithFilter() throws Exception {
        
        FeatureLock lock = 
            FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);
        
        Transaction tx = new DefaultTransaction();
        store.setTransaction( tx );
        store.setFeatureLock(lock);
        
        //lock features
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo f = ff.equals(ff.property(aname("intProperty")), ff.literal(1));
        
        int locked = store.lockFeatures( f );
        assertEquals( 1, locked );
        
        //grabbing a reader should be no problem
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriter(tname("ft1"), tx);
        boolean failure = false;
        while( writer.hasNext() ) {
            SimpleFeature feature = (SimpleFeature) writer.next();
            Number old = (Number) feature.getAttribute(aname("intProperty"));
            
            feature.setAttribute(aname("intProperty"), new Integer(100));
            if ( new Integer(1).equals( old.intValue() ) ) {
                try {
                    writer.write();
                    fail( "writer should have thrown exception for locked feature");
                }
                catch( FeatureLockException e ) {
                    failure = true;
                } 
            }
            else {
                writer.write();
            }
        }
        writer.close();
        
        assertTrue( failure );
        tx.close();
    }
    
    public void testLockFeaturesWithInvalidFilter() throws Exception {
        
        FeatureLock lock = FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);
        
        Transaction tx = new DefaultTransaction();
        store.setTransaction( tx );
        store.setFeatureLock(lock);
        
        //lock features
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo f = ff.equals(ff.property(aname("invalidProperty")), ff.literal(1));
        
        try {
            store.lockFeatures( f );
            fail("Should have failed with an exception, the filter is not valid");
        } catch(Exception e) {
            // fine
        }
        
        tx.close();
    }
    
    public void testLockFeaturesWithInvalidQuery() throws Exception {
        FeatureLock lock = FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);
        
        Transaction tx = new DefaultTransaction();
        store.setTransaction( tx );
        store.setFeatureLock(lock);
        
        //lock features
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo f = ff.equals(ff.property(aname("invalidProperty")), ff.literal(1));
        
        try {
            store.lockFeatures( new DefaultQuery(store.getSchema().getTypeName(), f) );
            fail("Should have failed with an exception, the filter is not valid");
        } catch(Exception e) {
            // fine
        }
        
        tx.close();
    }
    
    public void testUnlockFeatures() throws Exception {
        FeatureLock lock = 
            FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);
        
        Transaction tx = new DefaultTransaction();
        store.setTransaction( tx );
        store.setFeatureLock(lock);
        
        store.lockFeatures();
        
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriter(tname("ft1"), tx );
        assertTrue( writer.hasNext() );
        SimpleFeature feature = writer.next();
        feature.setAttribute( aname("intProperty"), new Integer(100) );
        try {
            writer.write();
            fail( "write should have thrown exception");
        }
        catch( FeatureLockException e) {
            
        }
        finally {
            writer.close();
        }
        
        try {
            store.unLockFeatures();    
            fail( "unlock should have thrown an exception");
        }
        catch( FeatureLockException e ) {}
        
        tx.addAuthorization(lock.getAuthorization());
        store.unLockFeatures();
        
        writer = dataStore.getFeatureWriter(tname("ft1"), tx );
        assertTrue( writer.hasNext() );
        
        feature = writer.next();
        feature.setAttribute( aname("intProperty"), new Integer(100) );
        
        writer.write();
        writer.close();
        tx.close();
    }
    
    public void testUnlockFeaturesInvalidFilter() throws Exception {
        FeatureLock lock = 
            FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);
        
        Transaction tx = new DefaultTransaction();
        store.setTransaction( tx );
        store.setFeatureLock(lock);
        tx.addAuthorization(lock.getAuthorization());
        
        store.lockFeatures();
        
        // uhnlock features
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo f = ff.equals(ff.property(aname("invalidProperty")), ff.literal(1));
        
        try {
            store.unLockFeatures(new DefaultQuery(store.getSchema().getTypeName(), f) );
            fail("Should have failed with an exception, the filter is not valid");
        } catch(Exception e) {
            // fine
        }

        store.unLockFeatures();
        tx.close();
    }
    
    public void testDeleteLockedFeatures() throws Exception {
        FeatureLock lock = 
            FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);
        
        Transaction tx = new DefaultTransaction();
        store.setTransaction( tx );
        store.setFeatureLock(lock);
        tx.addAuthorization(lock.getAuthorization());
        
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f1 = ff.id( Collections.singleton( ff.featureId( tname("ft1")+".1")));
        
        assertEquals( 1, store.lockFeatures(f1) );
        
        Transaction tx1 = new DefaultTransaction();
        store.setTransaction( tx1 );
        try {
            store.removeFeatures( f1 );
            fail( "Locked feature should not be deleted.");
        }
        catch( FeatureLockException e ) {}
        
        tx1.close();
        
        store.setTransaction( tx );
        store.removeFeatures( f1 );
        
        tx.commit();
        tx.close();
    }
    
    public void testModifyLockedFeatures() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        
        Filter f0 = ff.equal( ff.property( aname("intProperty" ) ), ff.literal(1000), true);
        assertEquals( 0 , store.getCount( new DefaultQuery( tname("ft1"), f0 ) ));
        
        FeatureLock lock = 
            FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);
        
        Transaction tx = new DefaultTransaction();
        store.setTransaction( tx );
        store.setFeatureLock(lock);
        tx.addAuthorization(lock.getAuthorization());
        
        Filter f1 = ff.id( Collections.singleton( ff.featureId( tname("ft1")+".1")));
        store.lockFeatures(f1);
        
        Transaction tx1 = new DefaultTransaction();
        store.setTransaction( tx1 );
        
        AttributeDescriptor ad = store.getSchema().getDescriptor( aname( "intProperty") );
        Integer v = new Integer(1000);
        
        try {
            store.modifyFeatures(ad, v, f1 ); 
            fail( "Locked feature should not be modified.");
        }
        catch( FeatureLockException e ) {}
        
        tx1.close();
        
        store.setTransaction( tx );
        store.modifyFeatures(ad, v, f1 );
        tx.commit();
       
        assertEquals( 1 , store.getCount( new DefaultQuery( tname("ft1"), f0 ) ));
        tx.close();
        
    }
}
