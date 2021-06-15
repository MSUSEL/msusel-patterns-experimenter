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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation.spatial;

import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


/**
 * PolygonBoundaryCoveredByPolygonValidation purpose.
 * 
 * <p>
 * Ensures Polygon Boundary is not covered by the Polygon.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class PolygonNotOverlappingPolygonValidation
    extends PolygonPolygonAbstractValidation {

	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.validation");
	
    /**
     * PolygonBoundaryCoveredByPolygonValidation constructor.
     * 
     * <p>
     * Description
     * </p>
     */
    public PolygonNotOverlappingPolygonValidation() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * Ensure Polygon Boundary is not covered by the Polygon.
     * 
     * <p></p>
     *
     * @param layers a HashMap of key="TypeName" value="FeatureSource"
     * @param envelope The bounding box of modified features
     * @param results Storage for the error and warning messages
     *
     * @return True if no features intersect. If they do then the validation
     *         failed.
     *
     * @throws Exception DOCUMENT ME!
     *
     * @see org.geotools.validation.IntegrityValidation#validate(java.util.Map,
     *      com.vividsolutions.jts.geom.Envelope,
     *      org.geotools.validation.ValidationResults)
     */
    public boolean validate(Map layers, Envelope envelope,
        ValidationResults results) throws Exception {
    	
    	LOGGER.finer("Starting test "+getName()+" ("+getClass().getName()+")" );
    	String typeRef1 = getPolygonTypeRef();
    	LOGGER.finer( typeRef1 +": looking up SimpleFeatureSource " );    	
        SimpleFeatureSource polySource1 = (SimpleFeatureSource) layers.get( typeRef1 );
        LOGGER.finer( typeRef1 +": found "+polySource1.getSchema().getTypeName() );
        
        SimpleFeatureCollection collection1 = polySource1.getFeatures(); // limit with envelope
        Object[] poly1 = collection1.toArray();

        String typeRef2 = getRestrictedPolygonTypeRef();
        LOGGER.finer( typeRef2 +": looking up SimpleFeatureSource " );        
        SimpleFeatureSource polySource2 = (SimpleFeatureSource) layers.get( typeRef2 );
        LOGGER.finer( typeRef2 +": found "+polySource2.getSchema().getTypeName() );
        
        SimpleFeatureCollection collection2 = polySource2.getFeatures(); // limit with envelope
        Object[] poly2 = collection2.toArray();
        
/*        if (!envelope.contains(collection1.getBounds())) {
            results.error((Feature) poly1[0],
                "Polygon Feature Source is not contained within the Envelope provided.");
            return false;
        }

        if (!envelope.contains(collection2.getBounds())) {
            results.error((Feature) poly2[0],
                "Restricted Polygon Feature Source is not contained within the Envelope provided.");
            return true;
        }*/
        boolean success = true;
        for (int i = 0; i < poly1.length; i++) {
        	SimpleFeature tmp = (SimpleFeature) poly1[i];
        	LOGGER.finest("Polgon overlap test for:"+tmp.getID() );
            Geometry gt = (Geometry) tmp.getDefaultGeometry();

            for (int j = 0; j < poly2.length; j++) {
                SimpleFeature tmp2 = (SimpleFeature) poly2[j];
                LOGGER.finest("Polgon overlap test against:"+tmp2.getID() );                
                Geometry gt2 = (Geometry) tmp2.getDefaultGeometry();

                if (gt2.overlaps(gt) != expected) {
                	results.error( tmp, "Polygon "+typeRef1+" overlapped Polygon "+typeRef2+"("+tmp2.getID()+") was not "+expected );
                	success = false;
                }
            }
        }
        return success;
    }
    boolean expected = true;
}
