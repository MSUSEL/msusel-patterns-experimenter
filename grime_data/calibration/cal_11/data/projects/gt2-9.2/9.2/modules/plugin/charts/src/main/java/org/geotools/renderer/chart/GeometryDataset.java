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
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.chart;

import java.util.Arrays;
import java.util.List;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.xy.AbstractXYDataset;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * A dataset for plotting Geometry objects .
 * <pre>
 *   Geometry g1 = ...
 *   Geometry g2 = ...
 *   
 *   GeometryDataset data = new GeometryDataset(g1,g2);
 *   GeometryRenderer renderer = new GeometryRenderer();
 *   
 *   XYPlot plot = new XYPlot(dataset, dataset.getDomain(), dataset.getRange(), renderer);
 *   ...
 * </pre>
 * @author Justin Deoliveira, OpenGeo
 *
 *
 *
 * @source $URL$
 */
public class GeometryDataset extends AbstractXYDataset {
    
    double buffer;
    double buf;
    
    List<Geometry> geometries;
    Envelope bounds;
    
    public GeometryDataset(Geometry... geometries) {
        this.geometries = Arrays.asList(geometries);
        this.bounds = new Envelope();
        for (Geometry g : geometries) {
            bounds.expandToInclude(g.getEnvelopeInternal());
        }
        setBuffer(0.1d);
    }
    
    public List<Geometry> getGeometries() {
        return geometries;
    }
    
    public void setBuffer(double buffer) {
        this.buffer = buffer;
        this.buf = Math.max(buffer, Math.max(bounds.getWidth()*buffer, bounds.getHeight()*buffer));
    }
    
    @Override
    public int getSeriesCount() {
        return geometries.size();
    }

    @Override
    public Comparable getSeriesKey(int series) {
        Geometry g = geometries.get(series);
        String wkt = g.toText();
        if (g instanceof Point) {
            return wkt;
        }
        else {
            int i = wkt.indexOf(',');
            int j = wkt.lastIndexOf(',');
            
            return wkt.substring(0, i) + " ... " + wkt.substring(j+1);
        }
    }

    public int getItemCount(int series) {
        return 1;
    }

    public Number getX(int series, int item) {
        //TODO: return the centroid
        return geometries.get(series).getCoordinate().x;
    }

    public Number getY(int series, int item) {
        return geometries.get(series).getCoordinate().x;
    }
    
    public ValueAxis getDomain() {
        NumberAxis domain = new NumberAxis();
        domain.setRange(bounds.getMinX()-buf, bounds.getMaxX()+buf);
        return domain;
    }
    
    public ValueAxis getRange() {
        NumberAxis range = new NumberAxis();
        range.setRange(bounds.getMinY()-buf, bounds.getMaxY()+buf);
        return range;
    }
}
