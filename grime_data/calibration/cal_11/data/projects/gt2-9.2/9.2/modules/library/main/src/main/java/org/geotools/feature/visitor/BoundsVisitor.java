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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.geometry.jts.ReferencedEnvelope;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Calculates the extents (envelope) of the features it visits.
 *
 * @author Cory Horner, Refractions
 *
 * @since 2.2.M2
 *
 *
 * @source $URL$
 */
public class BoundsVisitor implements FeatureCalc {
    ReferencedEnvelope bounds = new ReferencedEnvelope();    
        
    public void visit(org.opengis.feature.Feature feature) {
        bounds.include( feature.getBounds() );
    }

    public ReferencedEnvelope getBounds() {
        return bounds;
    }

    public void reset(Envelope bounds) {
        this.bounds = new ReferencedEnvelope();
    }

    public CalcResult getResult() {
    	if(bounds == null || bounds.isEmpty()) {
    		return CalcResult.NULL_RESULT;
    	}
        return new BoundsResult(bounds);
    }

    public static class BoundsResult extends AbstractCalcResult {
        private ReferencedEnvelope bbox;

        public BoundsResult(ReferencedEnvelope bbox) {
            this.bbox = bbox;
        }

        public ReferencedEnvelope getValue() {
            return new ReferencedEnvelope(bbox);
        }

        public boolean isCompatible(CalcResult targetResults) {
            //list each calculation result which can merge with this type of result
            if (targetResults instanceof BoundsResult || targetResults == CalcResult.NULL_RESULT) {
                return true;
            }

            return false;
        }

        public CalcResult merge(CalcResult resultsToAdd) {
            if (!isCompatible(resultsToAdd)) {
                throw new IllegalArgumentException(
                    "Parameter is not a compatible type");
            }
            
            if(resultsToAdd == CalcResult.NULL_RESULT) {
        		return this;
        	}

            if (resultsToAdd instanceof BoundsResult) {
                BoundsResult boundsToAdd = (BoundsResult) resultsToAdd;
                
                //add one set to the other (to create one big unique list)
                ReferencedEnvelope newBounds = new ReferencedEnvelope(bbox);
                newBounds.include( boundsToAdd.getValue());

                return new BoundsResult(newBounds);
            } else {
                throw new IllegalArgumentException(
                    "The CalcResults claim to be compatible, but the appropriate merge method has not been implemented.");
            }
        }
    }
}
