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
package org.geotools.geometry.jts;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;

/**
 * An efficient ordinate accumulator used by some geometry manipulation class in this package
 * @author Andrea Aime - OpenGeo
 */
class Ordinates {
    /**
     * The current coordinate
     */
    int curr;
    
    /**
     * The ordinates holder
     */
    double[] ordinates;
    
    public Ordinates() {
        ordinates = new double[10];
        curr = -1;
    }
    
    public Ordinates(int capacity) {
        ordinates = new double[capacity];
        curr = -1;
    }
    
    /**
     * Converts the ordinate into a coordinate sequence
     */
    public CoordinateSequence toCoordinateSequence(CoordinateSequenceFactory csfac) {
        CoordinateSequence cs = csfac.create(size(), 2);
        for (int i = 0; i <= curr; i++) {
            cs.setOrdinate(i, 0, ordinates[i * 2]);
            cs.setOrdinate(i, 1, ordinates[i * 2 + 1]);
        }
        
        return cs;
    }

    /**
     * The number of coordinates
     * @return
     */
    int size() {
        return curr + 1;
    }
    
    /**
     * Adds a coordinate to this list
     * @param x
     * @param y
     */
    void add(double x, double y) {
        curr++;
        if((curr * 2 + 1) >= ordinates.length) {
            int newSize = ordinates.length * 3 / 2;
            if(newSize < 10) {
                newSize = 10;
            }
            double[] resized = new double[newSize];
            System.arraycopy(ordinates, 0, resized, 0, ordinates.length);
            ordinates = resized;
        }
        ordinates[curr * 2] = x;
        ordinates[curr * 2 + 1] = y;
    }
    
    /**
     * Resets the ordinates
     */
    void clear() {
        curr = -1;
    }
    
    double getOrdinate(int coordinate, int ordinate) {
        return ordinates[coordinate * 2 + ordinate];
    }

    public void init(CoordinateSequence cs) {
        clear();
        for(int i = 0; i < cs.size(); i++) {
            add(cs.getOrdinate(i, 0), cs.getOrdinate(i, 1));
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Ordinates[");
        for (int i = 0; i <= curr; i++) {
            sb.append(ordinates[i * 2]);
            sb.append(" ");
            sb.append(ordinates[i * 2 + 1]);
            if(i < curr) {
                sb.append(";");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}