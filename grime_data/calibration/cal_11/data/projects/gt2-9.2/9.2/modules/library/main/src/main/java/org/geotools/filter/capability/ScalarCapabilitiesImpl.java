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

import org.opengis.filter.capability.ArithmeticOperators;
import org.opengis.filter.capability.ComparisonOperators;
import org.opengis.filter.capability.ScalarCapabilities;

/**
 * Implementation of the ScalarCapabilities interface.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class ScalarCapabilitiesImpl implements ScalarCapabilities {

    ArithmeticOperatorsImpl arithmeticOperators;
    ComparisonOperatorsImpl comparisonOperators;
    boolean logicalOperators;

    public ScalarCapabilitiesImpl() {
        arithmeticOperators = new ArithmeticOperatorsImpl();
        comparisonOperators = new ComparisonOperatorsImpl();
        logicalOperators = false;
    }

    public ScalarCapabilitiesImpl( ComparisonOperators comparisonOperators,
            ArithmeticOperators arithmeticOperators, boolean logicalOperators ) {
        this.arithmeticOperators = toArithmeticOperatorsImpl(arithmeticOperators);
        this.comparisonOperators = toComparisonOperatorsImpl(comparisonOperators);
        this.logicalOperators = logicalOperators;
    }

    public ScalarCapabilitiesImpl( ScalarCapabilities copy ) {
        arithmeticOperators = copy.getArithmeticOperators() == null ?
                new ArithmeticOperatorsImpl() :
                    new ArithmeticOperatorsImpl( copy.getArithmeticOperators() );
                
        comparisonOperators = copy.getComparisonOperators() == null ?
                new ComparisonOperatorsImpl() :
                    new ComparisonOperatorsImpl( copy.getComparisonOperators());
                
        logicalOperators = copy.hasLogicalOperators();
    }

    public void setArithmeticOperators( ArithmeticOperatorsImpl arithmeticOperators ) {
        this.arithmeticOperators = arithmeticOperators;
    }

    public ArithmeticOperatorsImpl getArithmeticOperators() {
        return arithmeticOperators;
    }

    public void setComparisonOperators( ComparisonOperatorsImpl comparisonOperators ) {
        this.comparisonOperators = comparisonOperators;
    }
    public ComparisonOperatorsImpl getComparisonOperators() {
        return comparisonOperators;
    }

    public void setLogicalOperators( boolean logicalOperators ) {
        this.logicalOperators = logicalOperators;
    }
    public boolean hasLogicalOperators() {
        return logicalOperators;
    }

    public static ComparisonOperatorsImpl toComparisonOperatorsImpl(
            ComparisonOperators comparisonOperators ) {
        if (comparisonOperators == null) {
            return new ComparisonOperatorsImpl();
        }
        if (comparisonOperators instanceof ComparisonOperatorsImpl) {
            return (ComparisonOperatorsImpl) comparisonOperators;
        } else {
            return new ComparisonOperatorsImpl(comparisonOperators);
        }
    }
    private static ArithmeticOperatorsImpl toArithmeticOperatorsImpl(
            ArithmeticOperators arithmeticOperators ) {
        if (arithmeticOperators == null) {
            return new ArithmeticOperatorsImpl();
        } else if (arithmeticOperators instanceof ArithmeticOperatorsImpl) {
            return (ArithmeticOperatorsImpl) arithmeticOperators;
        } else {
            return new ArithmeticOperatorsImpl(arithmeticOperators);
        }
    }
    public void addAll( ScalarCapabilities copy ) {
        if( copy == null ) return;
        if( copy.getArithmeticOperators() != null ){
            arithmeticOperators.addAll( copy.getArithmeticOperators() );
        }
        if( copy.getComparisonOperators() != null){
            comparisonOperators.addAll( copy.getComparisonOperators() );    
        }        
        if( copy.hasLogicalOperators() == true ){
            logicalOperators = true;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((arithmeticOperators == null) ? 0 : arithmeticOperators.hashCode());
        result = prime * result
                + ((comparisonOperators == null) ? 0 : comparisonOperators.hashCode());
        result = prime * result + (logicalOperators ? 1231 : 1237);
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
        ScalarCapabilitiesImpl other = (ScalarCapabilitiesImpl) obj;
        if (arithmeticOperators == null) {
            if (other.arithmeticOperators != null)
                return false;
        } else if (!arithmeticOperators.equals(other.arithmeticOperators))
            return false;
        if (comparisonOperators == null) {
            if (other.comparisonOperators != null)
                return false;
        } else if (!comparisonOperators.equals(other.comparisonOperators))
            return false;
        if (logicalOperators != other.logicalOperators)
            return false;
        return true;
    }
}
