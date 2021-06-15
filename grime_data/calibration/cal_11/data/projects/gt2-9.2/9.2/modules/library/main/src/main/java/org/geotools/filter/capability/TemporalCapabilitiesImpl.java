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
package org.geotools.filter.capability;

import java.util.Collection;

import org.opengis.filter.capability.TemporalCapabilities;
import org.opengis.filter.capability.TemporalOperator;
import org.opengis.filter.capability.TemporalOperators;

/**
 * 
 *
 * @source $URL$
 */
public class TemporalCapabilitiesImpl implements TemporalCapabilities {

    TemporalOperators temporalOperators;

    public TemporalCapabilitiesImpl() {
        this((TemporalOperators)null);
    }
    
    public TemporalCapabilitiesImpl(Collection<TemporalOperator> operators) {
        this(new TemporalOperatorsImpl(operators));
    }
    
    public TemporalCapabilitiesImpl(TemporalOperators operators) {
        temporalOperators = toTemporalOperatorsImpl(operators);
    }
    
    public TemporalCapabilitiesImpl(TemporalCapabilities capabilities) {
        temporalOperators = toTemporalOperatorsImpl(capabilities.getTemporalOperators());
    }
    
    TemporalOperators toTemporalOperatorsImpl(TemporalOperators operators) {
        if (operators == null) {
            return new TemporalOperatorsImpl();
        }
        if (operators instanceof TemporalOperatorsImpl) {
            return (TemporalOperatorsImpl) operators;
        }
        return new TemporalOperatorsImpl(operators.getOperators());
    }
    
    public TemporalOperators getTemporalOperators() {
        return temporalOperators;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((temporalOperators == null) ? 0 : temporalOperators.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TemporalCapabilitiesImpl other = (TemporalCapabilitiesImpl) obj;
        if (temporalOperators == null) {
            if (other.temporalOperators != null)
                return false;
        } else if (!temporalOperators.equals(other.temporalOperators))
            return false;
        return true;
    }
}
