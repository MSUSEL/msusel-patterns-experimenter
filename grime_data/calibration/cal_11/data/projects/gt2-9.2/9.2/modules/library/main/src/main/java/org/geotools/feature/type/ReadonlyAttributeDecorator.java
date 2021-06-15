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
package org.geotools.feature.type;

import java.util.Map;

import org.opengis.feature.Attribute;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.Identifier;

/**
 * Readonly wrapper around the provided Attribute.
 * <p>
 * This class is used by Types in order to protect provided attributes from modification
 * during evaluation.
 *
 *
 *
 * @source $URL$
 */
public final class ReadonlyAttributeDecorator implements Attribute {
    private final Attribute delegate;

    public ReadonlyAttributeDecorator(Attribute delegate) {
        this.delegate = delegate;
    }

    public AttributeType getType() {
        return delegate.getType();
    }

    public Identifier getIdentifier() {
        return delegate.getIdentifier();
    }

    public Object getValue() {
        return delegate.getValue();
    }

    public void setValue(Object newValue) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Modification is not supported");
    }

    public AttributeDescriptor getDescriptor() {
        return delegate.getDescriptor();
    }

    public Name getName() {
        return delegate.getName();
    }

    public Map<Object, Object> getUserData() {
        return delegate.getUserData();
    }

    public boolean isNillable() {
        return delegate.isNillable();
    }
    public void validate() {
        delegate.validate();
    }
}
