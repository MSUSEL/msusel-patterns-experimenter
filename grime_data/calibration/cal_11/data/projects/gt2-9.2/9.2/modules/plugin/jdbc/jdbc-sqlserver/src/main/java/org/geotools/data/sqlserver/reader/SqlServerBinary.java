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
package org.geotools.data.sqlserver.reader;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;

/**
 * Represents the information from a binary sqlserver geometry
 *
 * @author Anders Bakkevold, Bouvet
 *
 * @source $URL$
 */
class SqlServerBinary {

    private int srid;
    private int numberOfPoints;
    private Coordinate[] coordinates;
    private Shape[] shapes;
    private Figure[] figures;
    private CoordinateSequence[] sequences;

    public int getSrid() {
        return srid;
    }

    public void setSrid(int srid) {
        this.srid = srid;
    }

    public void setSerializationProperties(byte serializationProperties) {
        this.serializationProperties = serializationProperties;
    }

    public boolean hasZ() {
        return (serializationProperties & 1) == 1;
    }

    public boolean hasM() {
        return (serializationProperties & 2) == 2;
    }

    public boolean isValid(){
        return (serializationProperties & 4) == 4;
    }

    public boolean isSinglePoint() {
        return (serializationProperties & 8) == 8;
    }

    public boolean hasSingleLineSegment() {
        return (serializationProperties & 16) == 16;
    }

    private byte serializationProperties;

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

    public Coordinate[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate[] coordinates) {
        this.coordinates = coordinates;
    }

    public void setShapes(Shape[] shapes) {
        this.shapes = shapes;
    }

    public void setFigures(Figure[] figures) {
        this.figures = figures;
    }

    public Figure[] getFigures() {
        return figures;
    }

    public void setSequences(CoordinateSequence[] sequences) {
        this.sequences = sequences;
    }

    public Shape[] getShapes() {
        return shapes;
    }

    public Shape getShape(int index) {
        return shapes[index];
    }

    public Figure getFigure(int index) {
        return figures[index];
    }

    public CoordinateSequence getSequence(int index) {
        return sequences[index];
    }
}
