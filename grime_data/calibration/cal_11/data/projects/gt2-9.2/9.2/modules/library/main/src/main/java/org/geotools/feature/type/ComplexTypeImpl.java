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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.feature.NameImpl;
import org.geotools.resources.Classes;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.util.InternationalString;

/**
 * Base class for complex types.
 * 
 * @author gabriel
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 *
 *
 *
 * @source $URL$
 */
public class ComplexTypeImpl extends AttributeTypeImpl implements ComplexType {

        /**
         * Immutable copy of the properties list with which we were constructed.
         */
        final private Collection<PropertyDescriptor> properties;
        
	/**
	 * Map to locate properties by name.
	 */
	final private Map<Name, PropertyDescriptor> propertyMap;
	
	public ComplexTypeImpl(
		Name name, Collection<PropertyDescriptor> properties, boolean identified, 
		boolean isAbstract, List<Filter> restrictions, AttributeType superType, 
		InternationalString description
	) {
	super(name, Collection.class, identified, isAbstract, restrictions, superType, description);
	List<PropertyDescriptor> localProperties;
	Map<Name, PropertyDescriptor> localPropertyMap;
	if (properties == null) {
	    localProperties = Collections.emptyList();
            localPropertyMap = Collections.emptyMap();
	} else {
	    localProperties = new ArrayList<PropertyDescriptor>(properties);
            localPropertyMap = new HashMap<Name, PropertyDescriptor>();
            for (PropertyDescriptor pd : properties) {
                if( pd == null ){
                    // descriptor entry may be null if a request was made for a property that does not exist
                    throw new NullPointerException("PropertyDescriptor is null - did you request a property that does not exist?");
                }
                localPropertyMap.put(pd.getName(), pd);
            }
            
        }
	this.properties = Collections.unmodifiableList(localProperties);
        this.propertyMap = Collections.unmodifiableMap(localPropertyMap);
    }

	public Class<Collection<Property>> getBinding() {
	    return (Class<Collection<Property>>) super.getBinding();
	}
	
	public Collection<PropertyDescriptor> getDescriptors() {
		return properties;
	}
	
	public PropertyDescriptor getDescriptor(Name name) {
	    return propertyMap.get(name);
	}
	
    public PropertyDescriptor getDescriptor(String name) {
        PropertyDescriptor result = getDescriptor(new NameImpl(name));
        if (result == null) {
            // look in the same namespace as the complex type
            result = getDescriptor(new NameImpl(getName().getNamespaceURI(), name));
            if (result == null) {
                // full scan
                for (PropertyDescriptor pd : properties) {
                    if (pd.getName().getLocalPart().equals(name)) {
                        return pd;
                    }
                }
            }
        }
        return result;
    }
	
	public boolean isInline() {
	    //JD: at this point "inlining" is unused... we might want to kill it 
	    // from the interface
	    return false;
	}
	
	public boolean equals(Object o){
	    if(this == o) {
	        return true;
	    }
	    if(!super.equals(o)){
	        return false;
	    }
	    if (getClass() != o.getClass()) {
	        return false;
	    }
	    ComplexTypeImpl other = (ComplexTypeImpl)o;
	    if ( !properties.equals(other.properties) ) {
	        return false;
	    }
	    return true;
	}
      
	public String toString() {
		StringBuffer sb = new StringBuffer(Classes.getShortClassName(this));
		sb.append(" ");
		sb.append( getName() );
		if( isAbstract() ){
			sb.append( " abstract" );			
		}
		if( isIdentified() ){
			sb.append( " identified" );
		}
		if( superType != null ){
		    sb.append( " extends ");
			sb.append( superType.getName().getLocalPart() );
		}
		if( List.class.isAssignableFrom( binding )){
			sb.append( "[" );
		}
		else {
			sb.append( "(" );
		}
		boolean first = true;
		for( PropertyDescriptor property : getDescriptors() ){
			if( first ){
				first = false;				
			}
			else {
				sb.append( ",");
			}
			sb.append( property.getName().getLocalPart() );
			sb.append( ":" );
			sb.append( property.getType().getName().getLocalPart() );			
		}
		if( List.class.isAssignableFrom( binding )){
			sb.append( "]" );
		}
		else {
			sb.append( ")" );
		}
		if( description != null ){
            sb.append("\n\tdescription=");
            sb.append( description );            
        }
        if( restrictions != null && !restrictions.isEmpty() ){
            sb.append("\nrestrictions=");
            first = true;
            for( Filter filter : restrictions ){
                if( first ){
                    first = false;
                }
                else {
                    sb.append( " AND " );
                }
                sb.append( filter );
            }
        }
		return sb.toString();
	}
}
