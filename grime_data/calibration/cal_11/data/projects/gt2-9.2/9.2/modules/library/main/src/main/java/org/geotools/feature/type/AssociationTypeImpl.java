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

import java.util.List;

import org.geotools.util.Utilities;
import org.opengis.feature.type.AssociationType;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.util.InternationalString;

/**
 * 
 *
 * @source $URL$
 */
public class AssociationTypeImpl extends PropertyTypeImpl implements AssociationType {

    final protected AttributeType relatedType;
	
	public AssociationTypeImpl(
		Name name, AttributeType referenceType, boolean isAbstract, 
		List<Filter> restrictions, AssociationType superType, InternationalString description		
	) {
		super(name, referenceType.getBinding(), isAbstract, restrictions, superType, description);
		this.relatedType = referenceType;
		
		if ( relatedType == null ) {
		    throw new NullPointerException("relatedType");
		}
	}
	
	public AttributeType getRelatedType() {
        return relatedType;
    }

	public AssociationType getSuper() {
		return (AssociationType) super.getSuper();
	}

	public int hashCode() {
        return super.hashCode() ^ relatedType.hashCode();
	}

	public boolean equals(Object other) {
		if (!(other instanceof AssociationTypeImpl)) {
			return false;
		}

		AssociationType ass /*(tee hee)*/ = (AssociationType) other;

		return super.equals(ass) 
		    && Utilities.equals(relatedType, ass.getRelatedType());
	}

	public String toString(){
		return new StringBuffer(super.toString()).append("; relatedType=[")
		    .append(relatedType).append("]").toString();
	}

}
