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
package com.jaspersoft.ireport.designer.utils;

import org.jdesktop.swingx.decorator.SortOrder;

/**
 *
 * @author  Administrator
 */
public class SortChangedEvent {
       
    
    private Object source = null;
    private int sortColumn = -1;
    private SortOrder sortType = SortOrder.UNSORTED;

    /**
     * Get the sorted column.
     * UNSORTED means no column is sorted currently
     * @return the sorted column
     */ 
    public int getSortColumn() {
        return sortColumn;
    }

    /**
     * Sort type can be 
     * UP, DOWN or UNSORTED
     * @return the sort type
     */ 
    public SortOrder getSortType() {
        return sortType;
    }
    
    
    /** Creates a new instance of ValueChangedEvent 
     * @param source 
     * @param sortColumn 
     * @param sortType 
     */
    public SortChangedEvent(Object source, int sortColumn, SortOrder sortType) {
        
        this.source = source;
        this.sortColumn = sortColumn;
        this.sortType = sortType;
    }
    
    /** Getter for property source.
     * @return Value of property source.
     *
     */
    public Object getSource() {
        return source;
    }
    
    /** Setter for property source.
     * @param source New value of property source.
     *
     */
    public void setSource(Object source) {
        this.source = source;
    }
    
     
}
