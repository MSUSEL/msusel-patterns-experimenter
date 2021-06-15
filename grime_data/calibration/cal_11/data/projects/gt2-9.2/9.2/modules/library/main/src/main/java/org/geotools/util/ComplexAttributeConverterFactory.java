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
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.util;

import java.util.Collection;

import org.geotools.factory.Hints;
import org.geotools.feature.AttributeImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.filter.identity.FeatureId;

/**
 * This converter retrieves the values out of attributes. 
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering) 
 * @author Niels Charlier
 *
 *
 *
 * @source $URL$
 */
public class ComplexAttributeConverterFactory implements ConverterFactory {

    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (ComplexAttribute.class.isAssignableFrom(source)) {
            return new Converter() {
                public Object convert(Object source, Class target) throws Exception {
                    if (source instanceof ComplexAttribute) {
                        Collection<? extends Property> valueMap = ((ComplexAttribute) source)
                                .getValue();
                        if (valueMap.isEmpty() || valueMap.size() > 1) {
                            return null;
                        } else {
                            // there should only be one value
                            source = valueMap.iterator().next();
                            if (AttributeImpl.class.equals(source.getClass())) {
                                return Converters.convert(((Attribute) source).getValue(), target);
                            }
                        }
                    }
                    return null;
                }
            };
        }
        
        //GeometryAttribute unwrapper
        if (GeometryAttribute.class.isAssignableFrom(source)) {
            return new Converter() {
                public Object convert(Object source, Class target) throws Exception {
                    if (source instanceof GeometryAttribute) {
                        return Converters.convert(((GeometryAttribute) source).getValue(), target);
                            
                    }
                    return null;
                }
            };
        }
        
        // String to FeatureId comparison
        if (FeatureId.class.isAssignableFrom(target) && String.class.isAssignableFrom(source)) {
            return new Converter() {
                public Object convert(Object source, Class target) {
                    if (source != null) {
                        return new FeatureIdImpl((String) source);
                    }
                    return null;
                }
            };
        }
        return null;
    }
}
