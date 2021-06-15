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
import org.opengis.filter.capability.Functions;

/**
 * Implementation of the ArithmeticOperators interface.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 *
 * @source $URL$
 */
public class ArithmeticOperatorsImpl implements ArithmeticOperators {

    boolean simpleArithmetic;
    FunctionsImpl functions;

    public ArithmeticOperatorsImpl() {
        this.simpleArithmetic = false;
        this.functions = new FunctionsImpl();
    }

    public ArithmeticOperatorsImpl( boolean simpleArtithmetic, Functions functions ) {
        this.simpleArithmetic = simpleArtithmetic;
        this.functions = toFunctionsImpl( functions );
    }
    
    public ArithmeticOperatorsImpl( ArithmeticOperators copy ) {
        this.simpleArithmetic = copy.hasSimpleArithmetic();
        this.functions = copy.getFunctions() == null ? new FunctionsImpl() :
            new FunctionsImpl( copy.getFunctions() );
    }

    public void setSimpleArithmetic( boolean simpleArithmetic ) {
        this.simpleArithmetic = simpleArithmetic;
    }
    public boolean hasSimpleArithmetic() {
        return simpleArithmetic;
    }

    public FunctionsImpl getFunctions() {
        return functions;
    }
    
    private static FunctionsImpl toFunctionsImpl( Functions functions ) {
        if( functions == null ){
            return new FunctionsImpl();
        }
        if( functions instanceof FunctionsImpl ){
            return (FunctionsImpl) functions;
        }
        else {
            return new FunctionsImpl( functions );
        }
    }
    
    public void addAll( ArithmeticOperators copy ){
        if( copy == null ) return;        
        getFunctions().addAll( copy.getFunctions());
        if( copy.hasSimpleArithmetic() ){
            this.simpleArithmetic = true;
        }        
    }
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ArithmeticOperators[");
        if( simpleArithmetic ){
            buf.append("simpleArithmetic=true");
        }
        buf.append("]");
        if( functions != null ){
            buf.append(" with functions");            
        }
        return buf.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((functions == null) ? 0 : functions.hashCode());
        result = prime * result + (simpleArithmetic ? 1231 : 1237);
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
        ArithmeticOperatorsImpl other = (ArithmeticOperatorsImpl) obj;
        if (functions == null) {
            if (other.functions != null)
                return false;
        } else if (!functions.equals(other.functions))
            return false;
        if (simpleArithmetic != other.simpleArithmetic)
            return false;
        return true;
    }
}
