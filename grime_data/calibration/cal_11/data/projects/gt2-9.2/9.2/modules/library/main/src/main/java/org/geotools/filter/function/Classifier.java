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
package org.geotools.filter.function;


/**
 * The data structure returned by classification functions. We can take this
 * object, tweak it, and then pass it to a ClassifyFunction.
 * 
 * @author Cory Horner, Refractions Research
 *
 *
 *
 * @source $URL$
 */
public abstract class Classifier {

    //TODO: handle null, NaN, else
    //TODO: simply this by just adding labelled bins
    //TODO: add size
    
    String[] titles;
    
    public String[] getTitles() {
        return titles;
    }
    
    public void setTitles(String[] titles) {
        this.titles = titles;
    }
    
    public void setTitle(int slot, String title) {
        titles[slot] = title;
    }
    
    public String getTitle(int slot) {
        return titles[slot];
    }
    
    /**
     * Returns the slot containing the passed expression's value. 
     */
    public int classify(org.opengis.filter.expression.Expression expr, Object feature) {
        Object value = expr.evaluate(feature); // retrive value from context
        return classify(value);
    }
    
    /**
     * Returns the slot this value belongs in.
     * 
     * @param value
     * @return index, starting from zero
     */
    public abstract int classify(Object value);
    
    /**
     * @return the number of bins
     */
    public abstract int getSize();
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        
        return super.toString();
    }
}
