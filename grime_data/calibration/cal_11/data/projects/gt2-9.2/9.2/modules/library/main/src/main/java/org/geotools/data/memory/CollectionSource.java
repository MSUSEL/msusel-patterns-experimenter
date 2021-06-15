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
package org.geotools.data.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.Transaction;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Used to quickly adapt a collection for APIs expecting to
 * be able to query generic content.
 * <p>
 * Please note that this is read-only access.
 * 
 * @author Jody Garnett
 *
 *
 *
 * @source $URL$
 */
public final class CollectionSource {

    private Collection collection;
    private CoordinateReferenceSystem crs;
    
    public CollectionSource( Collection collection ){
        this( collection, null );
    }
    
    public CollectionSource( Collection collection, CoordinateReferenceSystem crs ){
        this.collection = Collections.unmodifiableCollection( collection );
        this.crs = crs;
    }
    
    public Collection content() {
        return collection;
    }

    public Collection content( String query, String queryLanguage ) {
        throw new UnsupportedOperationException("Please help me hook up the parser!");
    }

    public Collection content( Filter filter ) {
        return content(filter, Integer.MAX_VALUE);
    }
    
    public Collection content( Filter filter, int countLimit ) {
        List list = new ArrayList();
        int count = 0;
        for( Iterator i = collection.iterator(); i.hasNext() && count < countLimit;){
            Object obj = i.next();
            if( filter.evaluate( obj )){
                list.add( obj );
                count++;
            }
        }
        return Collections.unmodifiableList( list );
    }

    public Object describe() {
        return Object.class; // TODO: be more specific
    }

    public void dispose() {
        collection = null;
    }

    public FilterCapabilities getFilterCapabilities() {
        return null;
    }

    public Name getName() {
        return new NameImpl("localhost/memory");
    }

    public void setTransaction( Transaction t ) {
        // ignored
    }
    
    public CoordinateReferenceSystem getCRS(){
        return crs;
    }

}
