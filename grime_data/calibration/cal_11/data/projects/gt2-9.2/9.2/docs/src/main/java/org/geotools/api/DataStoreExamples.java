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
package org.geotools.api;

import java.net.URI;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.opengis.feature.simple.SimpleFeatureType;

public class DataStoreExamples {

DataStore dataStore = null;
SimpleFeatureSource featureSource = null;

void exampleInfo() {
    // exampleInfo start
    ServiceInfo info = dataStore.getInfo();
    
    // Human readable name and description
    String title = info.getTitle();
    String text = info.getDescription();
    
    // keywords (dublin core keywords like a web page)
    Set<String> keywords = info.getKeywords();
    
    // formal metadata
    URI publisher = info.getPublisher(); // authority publishing data
    URI schema = info.getSchema(); // used for data conforming to a standard
    URI source = info.getSource(); // location where information is published from
    
    // exampleInfo end
}

void exampleCreateSchema() throws Exception {
    // exampleCreateSchema start
    SimpleFeatureType schema  = DataUtilities.createType("LINE", "centerline:LineString,name:\"\",id:0");

    dataStore.createSchema( schema );
    // exampleCreateSchema end
    
}

void exampleAllCount() throws Exception {
    // all start
    int count = featureSource.getCount( Query.ALL );
    if( count == -1 ){
        count = featureSource.getFeatures().size();
    }
    // all end
}

void exampleQueryCount() throws Exception {
    // count start
    Query query = new Query( "typeName", CQL.toFilter("REGION = 3") );
    int count = featureSource.getCount( query );
    if( count == -1 ){
        count = featureSource.getFeatures( query ).size();
    }
    // count end
}

}
