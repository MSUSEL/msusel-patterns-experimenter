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
package org.geotools.feature;

import java.io.Serializable;

import org.geotools.util.Utilities;
import org.opengis.feature.type.Name;


/**
 * Simple implementation of Name.
 * <p>
 * This class emulates QName, and is used as the implementation of both AttributeName and
 * TypeName (so when the API settles down we should have a quick fix.
 * <p>
 * Its is advantageous to us to be able to:
 * <ul>
 * <li>Have a API in agreement with QName - considering our target audience
 * <li>Strongly type AttributeName and TypeName separately
 * </ul>
 * The ISO interface move towards combining the AttributeName and Attribute classes,
 * and TypeName and Type classes, while we understand the attractiveness of this on a
 * UML diagram it is very helpful to keep these concepts separate when playing with
 * a strongly typed language like java.
 * </p>
 * <p>
 * It case it is not obvious this is a value object and equality is based on
 * namespace and name.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 *
 * @source $URL$
 */
public class NameImpl implements org.opengis.feature.type.Name, Serializable, Comparable<NameImpl> {
    private static final long serialVersionUID = 4564070184645559899L;

    /** namespace / scope */
    protected String namespace;

    /** local part */
    protected String local;

    private String separator;

    /**
     * Constructs an instance with the local part set. Namespace / scope is
     * set to null.
     *
     * @param local The local part of the name.
     */
    public NameImpl(String local) {
        this(null, local);
    }

    /**
     * Constructs an instance with the local part and namespace set.
     *
     * @param namespace The namespace or scope of the name.
     * @param local The local part of the name.
     *
     */
    public NameImpl(String namespace, String local) {
        this( namespace, ":", local );
    }
    /**
     * Constructs an instance with the local part and namespace set.
     *
     * @param namespace The namespace or scope of the name.
     * @param local The local part of the name.
     *
     */
    public NameImpl(String namespace, String separator, String local) {
        this.namespace = namespace;
        this.separator = separator;
        this.local = local;
    }

    /**
     * Constract an instance from the provided QName. 
     */
    public NameImpl( javax.xml.namespace.QName qName ){
        this( qName.getNamespaceURI(), qName.getLocalPart() );
    }
    
    public boolean isGlobal() {
        return getNamespaceURI() == null;
    }
	public String getSeparator() {
		return separator;
	}
    public String getNamespaceURI() {
        return namespace;
    }

    public String getLocalPart() {
        return local;
    }

    public String getURI() {
        if ((namespace == null) && (local == null)) {
            return null;
        }
        if (namespace == null) {
            return local;
        }
        if (local == null) {
            return namespace;
        }
        return new StringBuffer(namespace).append(separator).append(local).toString();
    }

    /**
     * Returns a hash code value for this operand.
     */
    @Override
    public int hashCode() {
    	return (namespace== null ? 0 : namespace.hashCode()) +
    	        37*(local== null ? 0 : local.hashCode());
    }

    /**
     * value object with equality based on name and namespace.
     */
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        
        if (obj instanceof Name) {
            NameImpl other = (NameImpl) obj;            
            if(!Utilities.equals(this.namespace, other.getNamespaceURI())){
            	return false;
            }
            if(!Utilities.equals(this.local, other.getLocalPart())){
                return false;                
            }
            return true;
        }
        return false;
    }

    /** name or namespace:name */
    public String toString() {
        return getURI();
    }

    public int compareTo(NameImpl other) {
        if( other == null ){
            return 1; // we are greater than null!
        }
        return getURI().compareTo(other.getURI());
    }
}
