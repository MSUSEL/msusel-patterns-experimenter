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
package org.geotools.feature.visitor;

import java.util.Set;

import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.opengis.filter.Id;

/**
 * Gather up all FeatureId strings into a provided HashSet.
 * <p>
 * Example:<code>Set<String> fids = (Set<String>) filter.accept( IdCollectorFilterVisitor.ID_COLLECTOR, new HashSet() );</code>
 *
 *
 *
 * @source $URL$
 */
public class IdCollectorFilterVisitor extends DefaultFilterVisitor {
    public static final IdCollectorFilterVisitor ID_COLLECTOR = new IdCollectorFilterVisitor(true);
    public static final IdCollectorFilterVisitor IDENTIFIER_COLLECTOR = new IdCollectorFilterVisitor(false);
    private final boolean mCollectStringIds;

    /**
     * @deprecated use {@link #IdCollectorFilterVisitor(boolean)}
     */
    protected IdCollectorFilterVisitor(){
        mCollectStringIds = true;
    }
    
    protected IdCollectorFilterVisitor(boolean collectStringIds){
        mCollectStringIds = collectStringIds;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object visit( Id filter, Object data ) {
     
        if( mCollectStringIds){
            Set set = (Set) data;
            set.addAll( filter.getIDs() );        
            return set;
        }else{
            Set set = (Set) data;
            set.addAll( filter.getIdentifiers());        
            return set;
        }

    }
}
