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
package org.geotools.tutorial.datastore;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentState;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.csvreader.CsvReader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class CSVFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    private ContentState state;
    private Query query;
    private CsvReader reader;
    private SimpleFeature next;
    private SimpleFeatureBuilder builder;
    private int row;
    private GeometryFactory geometryFactory;

    public CSVFeatureReader(ContentState contentState, Query query) throws IOException {
        this.state = contentState;
        this.query = query;
        CSVDataStore csv = (CSVDataStore) contentState.getEntry().getDataStore();
        reader = csv.read(); // this may throw an IOException if it could not connect
        boolean header = reader.readHeaders();
        if (! header ){
            throw new IOException("Unable to read csv header");
        }
        builder = new SimpleFeatureBuilder( state.getFeatureType() );
        geometryFactory = JTSFactoryFinder.getGeometryFactory();
        row = 0;
    }

    public SimpleFeatureType getFeatureType() {
        return (SimpleFeatureType) state.getFeatureType();
    }

    public SimpleFeature next() throws IOException, IllegalArgumentException,
            NoSuchElementException {
        SimpleFeature feature;
        if( next != null ){
            feature = next;
            next = null;
        }
        else {
            feature = readFeature();
        }
        return feature;
    }
    
    SimpleFeature readFeature() throws IOException {
        if( reader == null ){
            throw new IOException("FeatureReader is closed; no additional features can be read");
        }
        boolean read = reader.readRecord(); // read the "next" record
        if( read == false ){
            close(); // automatic close to be nice
            return null; // no additional features are available
        }
        Coordinate coordinate = new Coordinate();
        for( String column : reader.getHeaders() ){
            String value = reader.get(column);
            if( "lat".equalsIgnoreCase(column)){
                coordinate.y = Double.valueOf( value.trim() );
            }
            if( "lon".equalsIgnoreCase(column)){
                coordinate.x = Double.valueOf( value.trim() );
            }
            builder.set(column, value );
        }        
        builder.set("Location", geometryFactory.createPoint( coordinate ) );
        
        row += 1;
        return builder.buildFeature( state.getEntry().getTypeName()+"."+row );
    }

    public boolean hasNext() throws IOException {
        if( next != null ){
            return true;
        }
        else {
            next = readFeature(); // read next feature so we can check
            return next != null;
        }
    }

    public void close() throws IOException {
        reader.close();
        reader = null;
        builder = null;
        geometryFactory = null;
        next = null;
    }

}
