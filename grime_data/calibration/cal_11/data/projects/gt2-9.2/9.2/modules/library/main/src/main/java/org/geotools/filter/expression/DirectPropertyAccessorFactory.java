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
package org.geotools.filter.expression;

import org.geotools.factory.Hints;
import org.opengis.feature.Property;
import org.opengis.feature.type.Name;

/**
 * This class will *directly* access a Property with the name equal to xpath.
 * 
 * @author Jody Garnett
 *
 *
 *
 * @source $URL$
 */
public class DirectPropertyAccessorFactory implements PropertyAccessorFactory {

    static PropertyAccessor DIRECT = new DirectPropertyAccessor();

    public PropertyAccessor createPropertyAccessor(Class type, String xpath,
            Class target, Hints hints) {
            return DIRECT;
    }

    
    /**
     * Grab a value from a Property with matching name.
     * <p>
     * This restriction is used by Types.validate to ensure
     * the provided value is good.
     * 
     * @author Jody Garnett (Refractions Research Inc)
     */
    static class DirectPropertyAccessor implements PropertyAccessor {
        
        /**
         * We can handle *one* case and one case only 
         */
        public boolean canHandle(Object object, String xpath, Class target) {
            if( object instanceof Property ){
                Property property = (Property) object;
                final Name name = property.getName();
                if( name != null ){
                    return name.getLocalPart().equals( xpath );
                } else {
                    // A property with no name? this is probably a place holder
                    // or Null Object (such as TransactionStateDiff.NULL).
                    return false;
                }
            }
            return false;
        }
        
        public Object get(Object object, String xpath, Class target)
                throws IllegalArgumentException {
            return ((Property)object).getValue();
        }

        public void set(Object object, String xpath, Object value, Class target)
                throws IllegalArgumentException {
            ((Property)object).setValue( value );            
        }        
    }    
}
