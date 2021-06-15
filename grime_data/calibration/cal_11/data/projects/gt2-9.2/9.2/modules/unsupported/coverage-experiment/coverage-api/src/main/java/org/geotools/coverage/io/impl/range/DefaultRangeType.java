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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.impl.range;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.geotools.coverage.io.range.FieldType;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.feature.NameImpl;
import org.geotools.util.SimpleInternationalString;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

/**
 * Default implementation of {@link RangeType}
 * 
 * @author Simone Giannecchini, GeoSolutions.
 *
 *
 *
 * @source $URL$
 */
public class DefaultRangeType implements RangeType {
	private InternationalString description;

	private Name name;

	private Set<FieldType> fieldTypes;

	private Set<Name> fieldTypesNames;
	
	public DefaultRangeType(final String name,
			final String description,
			final FieldType fieldType) {
		this(new NameImpl(name), new SimpleInternationalString(description), Collections.singleton(fieldType));
	}	
	
	public DefaultRangeType(final String name,
			final String description,
			final Set<FieldType> fieldTypes) {
		this(new NameImpl(name), new SimpleInternationalString(description), fieldTypes);
	}	
	public DefaultRangeType(final Name name,
			final InternationalString description,
			final Set<FieldType> fieldTypes) {
		this.name = name;
		this.description = description;
		this.fieldTypes = new LinkedHashSet<FieldType>(fieldTypes);
		fieldTypesNames = new LinkedHashSet<Name>(fieldTypes.size());
		for (FieldType fieldType : fieldTypes) {
			fieldTypesNames.add(fieldType.getName());
		}
	}

	public InternationalString getDescription() {
		return description;
	}

	public FieldType getFieldType(String name) {
		for (FieldType fieldType : fieldTypes) {
		    final Name ftName = fieldType.getName();
		    String localPart=name;
		    String nameSpace = "";
		    if (name.contains(":")){
		        final int indexOf = name.lastIndexOf(":");
		        localPart = name.substring(indexOf+1,localPart.length());
		        nameSpace = name.substring(0,indexOf);
		    }
		    final String ftLocalPart = ftName.getLocalPart().toString();
		    final String ftNameSpace = ftName.getNamespaceURI();
		    if (ftLocalPart.equals(localPart)){
		        if (ftNameSpace!=null){
		            if (ftNameSpace.equals(nameSpace))
		                return fieldType;
		        }
		        return fieldType;
		    }
		    
		}
		return null;
	}

	public Set<Name> getFieldTypeNames() {
		return Collections.unmodifiableSet(fieldTypesNames);
	}

	public Set<FieldType> getFieldTypes() {
		return Collections.unmodifiableSet(fieldTypes);
	}

	public Name getName() {
		return name;
	}

	public int getNumFieldTypes() {
		return fieldTypes.size();
	}
	
	/**
	 * Simple Implementation of toString method for debugging purpose.
	 */
	public String toString(){
	    final StringBuilder sb = new StringBuilder();
	        final String lineSeparator = System.getProperty("line.separator", "\n");
	        sb.append("Name:").append(name.toString()).append(lineSeparator);
	        sb.append("Description:").append(description.toString()).append(lineSeparator);
	        sb.append("FieldTypes:").append(lineSeparator);
	        for (FieldType fieldType: fieldTypes) {
	            sb.append("fieldType:").append(fieldType.toString());
	            sb.append(lineSeparator).append(lineSeparator);
	        }
	        return sb.toString();
	}
}
