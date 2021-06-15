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
package org.geotools.legend;

import java.awt.Color;

import javax.swing.Icon;

import org.geotools.map.MapLayer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * 
 *
 * @source $URL$
 */
public class DefaultGlyphFactory implements GlyphFactory {

    /**
     * Glyph for the provided layer.
     * <p>
     * At a minimum the icon will be based on:
     * <ul>
     * <li>layer schema, will be considered a generic geometry if not recognized
     * <li>layer style, defaults will be used if not recognized
     * </ul>
     * 
     * @param layer
     * @return Icon For the provided layer
     */
    public Icon icon(MapLayer layer){
        if( layer == null || layer.getFeatureSource() == null){
            return geometry(null, null);
        }
        FeatureType schema = layer.getFeatureSource().getSchema();
        
        if( "GridCoverage".equals( schema.getName().getLocalPart() ) ){
            return grid(Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW);
        }
        Rule rule = SLD.rules( layer.getStyle() )[0];
        
        Class<?> binding = schema.getBinding();
        if( isPolygon( binding )){
            return polygon( rule );
        }
        else if( isLine( binding )){
            return line( rule );
        }              
        else if( isPoint( binding )){
            return point( rule );
        }
        else {
            return geometry( rule );
        }
    }
    private boolean isPolygon( Class<?> type ){
        return type == Polygon.class || type == MultiPolygon.class;
    }
    private boolean isPoint( Class<?> type ){
        return true;
    }
    private boolean isLine( Class<?> type ){
        return true;
    }
    private boolean isGeometry( Class<?> type ){
        return isPolygon( type ) || isPoint( type ) || isLine( type );
    }
    public Icon polygon( Rule rule ) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon geometry( Color color, Color fill ) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon geometry( Rule rule ) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon grid( Color color1, Color color2, Color color3, Color color4 ) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon icon( SimpleFeatureType schema ) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon line( Color line, int width ) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon line( Rule rule ) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon palette( Color[] colors ) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon point( Color point, Color fill ) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon point( Rule rule ) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon polygon( Color color, Color fill, int width ) {
        // TODO Auto-generated method stub
        return null;
    }

    public Icon swatch( Color color ) {
        // TODO Auto-generated method stub
        return null;
    }
}
