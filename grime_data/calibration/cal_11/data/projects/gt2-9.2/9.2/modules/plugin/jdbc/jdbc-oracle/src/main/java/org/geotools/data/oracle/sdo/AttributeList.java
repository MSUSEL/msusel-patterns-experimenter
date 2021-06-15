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
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    Refractions Research Inc. Can be found on the web at:
 *    http://www.refractions.net/
 */
package org.geotools.data.oracle.sdo;

import java.lang.reflect.Array;
import java.util.AbstractList;



/**
 * Used to provide a List API of an ordinate array. 
 * <p>
 * Insertions are not supported</p>
 * 
 * @see net.refractions.jspatial.jts
 * @author jgarnett, Refractions Reasearch Inc.
 *
 *
 * @source $URL$
 * @version CVS Version
 */
public class AttributeList extends AbstractList
{
    final Object ARRAY;
    final int OFFSET;
    final int LEN;
    final int SIZE;
    final int START;
    final int END;
    final int STEP;
    public AttributeList( Object array )
    {
        this( array, 0, 1 );
    }
    public AttributeList( Object array, int offset, int len )
    {
        this( array, offset, len, 0, Array.getLength(array) );        
    }
    public AttributeList( Object array, int offset, int len, int start, int end )
    {
        START = start;
        END = end;
        ARRAY = array;
        OFFSET = offset;
        LEN = len;
        SIZE = Math.abs(START - END)/ LEN;
        STEP = START < END ?  LEN : -LEN;
        
        if (!ARRAY.getClass().isArray())
       		throw new IllegalArgumentException("Provided argument was not an array");
       		
		if( Array.getLength( ARRAY ) % LEN != 0 ){
            throw new IllegalArgumentException(
                "You have requested Coordiantes of "+LEN+" ordinates. " +
                "This is inconsistent with an array of length "+Array.getLength( ARRAY )
            );
        }                        
    }        
    /**
     * Used to grab value from array.
     * <p>
     * Description of get.
     * </p>
     * @param index
     * 
     * @see java.util.List#get(int)
     */
    public Object get(int index)
    {
		rangeCheck(index);
		return Array.get(ARRAY,START+STEP*index + OFFSET);       
    }
    /** Quick double access */
    public double getDouble( int index ){
        rangeCheck(index);
        return Array.getDouble(ARRAY,START+STEP*index + OFFSET );//ARRAY[ START+STEP*index + OFFSET ];
    }
    public String getString( int index){
    	rangeCheck(index);
    	return Array.get(ARRAY,START+STEP*index + OFFSET ).toString();
    }
    public double[] toDoubleArray(){
        double array[] = new double[ size() ];
        for( int i=0; i< size(); i++){
            array[i]=getDouble( i );
        }
        return array;
    }
    public Object[] toObjectArray(){
    	Object array[] = new Object[ size() ];
		for( int i=0; i< size(); i++){
			array[i]=get( i );
		}
		return array;
    }
    /**
     * Check if the given index is in range.  If not, throw an appropriate
     * runtime exception.  This method does *not* check if the index is
     * negative: It is always used immediately prior to an array access,
     * which throws an ArrayIndexOutOfBoundsException if index is negative.
     */
    private void rangeCheck(int index) {
    if (index >= SIZE)
        throw new IndexOutOfBoundsException(
        "Index: "+index+", Size: "+SIZE);
    }    
    /**
     * Used to
     * <p>
     * Description of size.
     * </p>
     * 
     * @see java.util.Collection#size()
     */
    public int size()
    {
        return SIZE;
    }        
}
