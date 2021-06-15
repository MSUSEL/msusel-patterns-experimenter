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
package com.vividsolutions.jts.geom;

/**
 * 
 *
 * @source $URL$
 */
public class EmptyGeometry extends Geometry {

    public EmptyGeometry() {
        super(new GeometryFactory());
    }

    @Override
    public String getGeometryType() {       
        return null;
    }

    @Override
    public Coordinate getCoordinate() {        
        return null;
    }

    @Override
    public Coordinate[] getCoordinates() {
        return null;
    }

    @Override
    public int getNumPoints() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public int getDimension() {
        return 0;
    }

    @Override
    public Geometry getBoundary() {
        return null;
    }

    @Override
    public int getBoundaryDimension() {
        return 0;
    }

    @Override
    public Geometry reverse() {
        return null;
    }

    @Override
    public boolean equalsExact(Geometry other, double tolerance) {
        return false;
    }

    @Override
    public void apply(CoordinateFilter filter) {
        

    }

    @Override
    public void apply(CoordinateSequenceFilter filter) {
        
    }

    @Override
    public void apply(GeometryFilter filter) {
        
    }

    @Override
    public void apply(GeometryComponentFilter filter) {
        
    }

    @Override
    public void normalize() {
        
    }

    @Override
    protected Envelope computeEnvelopeInternal() {
        return null;
    }

    @Override
    protected int compareToSameClass(Object o) {
        return 0;
    }

    @Override
    protected int compareToSameClass(Object o, CoordinateSequenceComparator comp) {
        return 0;
    }
    
    @Override
    public String toString()
    {
        return "";
    }
    

}
