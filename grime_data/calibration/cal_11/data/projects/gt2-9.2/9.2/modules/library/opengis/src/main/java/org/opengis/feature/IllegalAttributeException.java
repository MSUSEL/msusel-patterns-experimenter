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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature;

import org.opengis.feature.type.AttributeDescriptor;

/**
 * Indicates a validation check has failed; the provided descriptor and value are available via this
 * exception.
 * 
 * @author Jody Garnett (Refractions Research, Inc.)
 * @since GeoAPI 2.2
 *
 *
 * @source $URL$
 */
public class IllegalAttributeException extends IllegalArgumentException {
    private static final long serialVersionUID = 3373066465585246605L;

    /**
     * AttributeDescriptor being used to validate against.
     */
    final private AttributeDescriptor descriptor;

    /**
     * Object that failed validation.
     */
    final private Object value;

    public IllegalAttributeException(AttributeDescriptor descriptor, Object value) {
        super();
        this.descriptor = descriptor;
        this.value = value;
    }

    public IllegalAttributeException(AttributeDescriptor descriptor, Object value, String message) {
        super(message);
        this.descriptor = descriptor;
        this.value = value;
    }

    public IllegalAttributeException(AttributeDescriptor descriptor, Object value, String message,
            Throwable t) {
        super(message, t);
        this.descriptor = descriptor;
        this.value = value;
    }

    public IllegalAttributeException(AttributeDescriptor descriptor, Object value, Throwable t) {
        super(t);
        this.descriptor = descriptor;
        this.value = value;
    }

    /**
     * AttribtueDescriptor being checked against.
     * 
     * @return AttributeDescriptor being checked.
     */
    public AttributeDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * Attribute value that failed validation.
     * 
     * @return Attribute value
     */
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        
        StringBuffer buf = new StringBuffer();
        buf.append(s);
        if( message != null){
            buf.append(":");
            buf.append(message);
        }
        if( descriptor != null ){
            buf.append(":");
            buf.append( descriptor.getName() );
        }
        buf.append(" value:");
        buf.append( value );
        
        return buf.toString();
    }
}
