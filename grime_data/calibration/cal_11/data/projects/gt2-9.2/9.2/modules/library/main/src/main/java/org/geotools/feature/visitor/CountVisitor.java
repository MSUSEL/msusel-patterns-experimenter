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

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.visitor.SumVisitor.SumResult;
import org.opengis.feature.simple.SimpleFeature;


/**
 * Determines the number of features in the collection
 *
 * @author Cory Horner, Refractions
 *
 * @since 2.2.M2
 *
 *
 * @source $URL$
 */
public class CountVisitor implements FeatureCalc {
    Integer count = null;

    public void init(SimpleFeatureCollection collection) {
    	//do nothing
    }
    public void visit(SimpleFeature feature) {
        visit((org.opengis.feature.Feature)feature);
    }
    
    public void visit(org.opengis.feature.Feature feature) {
    	if(count == null) {
    		count = 0;
    	}
    	count++;
    }

    public int getCount() {
    	if(count == null) {
    		return 0;
    	}
        return count;
    }

    public void setValue(int count) {
        this.count = count;
    }
    
    public void reset() {
        this.count = null;
    }

    public CalcResult getResult() {
    	if(count == null) {
    		return CalcResult.NULL_RESULT;
    	}
        return new CountResult(count);
    }

    public static class CountResult extends AbstractCalcResult {
        private int count;

        public CountResult(int newcount) {
            count = newcount;
        }

        public Object getValue() {
            return new Integer(count);
        }

        public boolean isCompatible(CalcResult targetResults) {
        	if (targetResults == CalcResult.NULL_RESULT) return true;
        	if (targetResults instanceof CountResult) return true;
        	if (targetResults instanceof SumResult) return true;
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

            if (resultsToAdd instanceof CountResult) {
            	//add the two counts
            	int toAdd = resultsToAdd.toInt();
            	return new CountResult(count + toAdd);
            } else if (resultsToAdd instanceof SumResult) {
            	// we don't want to implement this twice, so we'll call the
				// SumResult version of this function
            	return resultsToAdd.merge(this);
            } else {
            	throw new IllegalArgumentException(
				"The CalcResults claim to be compatible, but the appropriate merge method has not been implemented.");
            }
        }
    }
}
