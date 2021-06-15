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
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts.spatialschema.geometry.aggregate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.geotools.factory.Factory;
import org.geotools.factory.Hints;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.aggregate.AggregateFactory;
import org.opengis.geometry.aggregate.MultiCurve;
import org.opengis.geometry.aggregate.MultiPoint;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.aggregate.MultiSurface;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Implementation of AggregateFactory able to make MultiPointImpl but
 * little else.
 * 
 * @author Jody Garnett
 *
 *
 *
 *
 * @source $URL$
 */
public class JTSAggregateFactory implements Factory,  AggregateFactory {
    private CoordinateReferenceSystem crs;
    private final Map usedHints = new LinkedHashMap();
    /**
     * No argument constructor for FactorySPI
     */
    public JTSAggregateFactory(){
        this( DefaultGeographicCRS.WGS84);
    }
    /**
     * Hints constructor for FactoryRegistry
     */
    public JTSAggregateFactory( Hints hints ){
        this( (CoordinateReferenceSystem) hints.get( Hints.CRS ) );
    }
    /**
     * Direct constructor for test cases
     */
    public JTSAggregateFactory( CoordinateReferenceSystem crs ) {
        this.crs = crs;
        usedHints.put( Hints.CRS, crs );
    }
    public Map getImplementationHints() {
        return usedHints;
    }
    public MultiCurve createMultiCurve( Set arg0 ) {
        throw new UnsupportedOperationException("MultiCurve not implemented");
    }
    public MultiPoint createMultiPoint( Set arg0 ) {
        return new MultiPointImpl( crs );
    }
    public MultiPrimitive createMultiPrimitive( Set arg0 ) {
        throw new UnsupportedOperationException("MultiPrimitive not implemented");
    }
    public MultiSurface createMultiSurface( Set arg0 ) {
        throw new UnsupportedOperationException("MultiSurface not implemented");
    }
}
