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
package org.geotools.feature.type;

import java.util.HashMap;
import java.util.Map;

import org.geotools.resources.Classes;
import org.geotools.util.Utilities;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.PropertyType;

/**
 * 
 *
 * @source $URL$
 */
public class PropertyDescriptorImpl implements PropertyDescriptor {

    final protected PropertyType type;
    final protected Name name;
    final protected int minOccurs;
    final protected int maxOccurs;
    final protected boolean isNillable;
    final Map<Object, Object> userData;
    
    protected PropertyDescriptorImpl(PropertyType type, Name name, int min, int max, boolean isNillable) {
        this.type = type;
        this.name = name;
        this.minOccurs = min;
        this.maxOccurs = max;
        this.isNillable = isNillable;
        userData = new HashMap();
        
        if ( type == null ) {
            throw new NullPointerException("type");
        }
        
        if ( name == null ) {
            throw new NullPointerException("name");
        }
        
        if (type == null) {
            throw new NullPointerException();
        }
        
        if (max > 0 && (max < min) ) {
            throw new IllegalArgumentException("max must be -1, or >= min");
        }
    }
    
    public PropertyType getType() {
        return type;
    }
    
    public Name getName() {
        return name;
    }

    public int getMinOccurs() {
        return minOccurs;
    }

    public int getMaxOccurs() {
        return maxOccurs;
    }
    
    public boolean isNillable() {
        return isNillable;
    }
    
    public Map<Object, Object> getUserData() {
        return userData;
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof PropertyDescriptorImpl)) {
            return false;
        }
        
        PropertyDescriptorImpl other = (PropertyDescriptorImpl) obj;
        return Utilities.equals(type,other.type) && 
            Utilities.equals(name,other.name) && 
            (minOccurs == other.minOccurs) && (maxOccurs == other.maxOccurs) &&
            (isNillable == other.isNillable);
    }
    
    public int hashCode() {
        return (37 * minOccurs + 37 * maxOccurs ) ^ type.hashCode() ^ name.hashCode();
    }

    public String toString() {        
        StringBuffer sb = new StringBuffer(Classes.getShortClassName(this));
        sb.append(" ");
        sb.append( getName() );
        if( type != null ){
            sb.append( " <" );
            sb.append( type.getName().getLocalPart()  );
            sb.append(":");
            sb.append( Classes.getShortName( type.getBinding() ));
            sb.append( ">" );
        }
        if( isNillable  ){
            sb.append( " nillable" );            
        }
        if( minOccurs == 1 && maxOccurs == 1 ){
            // ignore the 1:1
        }
        else {
            sb.append( " " );
            sb.append( minOccurs );
            sb.append(  ":" );
            sb.append( maxOccurs );
        }
        if( userData != null && !userData.isEmpty() ){
            sb.append("\nuserData=(");
            for( Map.Entry entry : userData.entrySet() ){
                sb.append("\n\t");
                sb.append( entry.getKey() );
                sb.append( " ==> " );
                sb.append( entry.getValue() );
            }
            sb.append(")");
        }
        return sb.toString();
    }
    
}
