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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * 
 *
 * @source $URL$
 */
public abstract class JDBCTransactionTest extends JDBCTestSupport {
    public void testCommit() throws IOException {
        //JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("ft1"));

        Transaction tx = new DefaultTransaction();

        //tx.putState( fs, new JDBCTransactionState( fs ) );
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriterAppend(tname("ft1"), tx);
        SimpleFeature feature = writer.next();
        feature.setAttribute(aname("intProperty"), new Integer(100));
        writer.write();
        writer.close();
        tx.commit();
        tx.close();

        SimpleFeatureCollection fc = dataStore.getFeatureSource(tname("ft1")).getFeatures();
        assertEquals(4, fc.size());
    }

    public void testNoCommit() throws IOException {
        //JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("ft1"));

        Transaction tx = new DefaultTransaction();

        //tx.putState( fs, new JDBCTransactionState( fs ) );
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriterAppend(tname("ft1"), tx);
        SimpleFeature feature = writer.next();
        feature.setAttribute(aname("intProperty"), new Integer(100));
        writer.write();
        writer.close();
        tx.rollback();
        tx.close();

        SimpleFeatureCollection fc = dataStore.getFeatureSource(tname("ft1")).getFeatures();
        assertEquals(3, fc.size());
    }

    public void testConcurrentTransactions() throws IOException {
        //JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("ft1"));

        Transaction tx1 = new DefaultTransaction();

        //tx1.putState( fs, new JDBCTransactionState( fs ) );
        Transaction tx2 = new DefaultTransaction();

        //tx2.putState( fs, new JDBCTransactionState( fs ) );
        FeatureWriter<SimpleFeatureType, SimpleFeature> w1 = dataStore.getFeatureWriterAppend(tname("ft1"), tx1);
        FeatureWriter<SimpleFeatureType, SimpleFeature> w2 = dataStore.getFeatureWriterAppend(tname("ft1"), tx2);

        SimpleFeature f1 = w1.next();
        SimpleFeature f2 = w2.next();

        f1.setAttribute(aname("intProperty"), new Integer(100));
        f2.setAttribute(aname("intProperty"), new Integer(101));

        w1.write();
        w2.write();

        w1.close();
        w2.close();

        tx1.commit();
        tx2.commit();
        tx1.close();
        tx2.close();

        SimpleFeatureCollection fc = dataStore.getFeatureSource(tname("ft1")).getFeatures();
        assertEquals(5, fc.size());
    }
    
    public void testSerialTransactions() throws IOException {
        SimpleFeatureStore st = (SimpleFeatureStore) dataStore.getFeatureSource( tname("ft1") );
        
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(st.getSchema());
        b.set(aname("intProperty"), new Integer(100));
        SimpleFeature f1 = b.buildFeature(null);
        DefaultFeatureCollection features = new DefaultFeatureCollection(null,null);
        features.add( f1 );

        Transaction tx1 = new DefaultTransaction();
        st.setTransaction(tx1);
        st.addFeatures( features );
        tx1.commit();
        tx1.close();
        assertEquals(4, dataStore.getFeatureSource(tname("ft1")).getCount(Query.ALL));
        
        Transaction tx2 = new DefaultTransaction();
        st.setTransaction(tx2);
        st.addFeatures( features );
        tx2.commit();
        tx2.close();
        assertEquals(5, dataStore.getFeatureSource(tname("ft1")).getCount(Query.ALL));
    }
}
