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
package org.geotools.styling;


import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.opengis.filter.expression.Expression;
import org.opengis.style.StyleVisitor;
import org.opengis.util.Cloneable;


/**
 * Provides a representation of a PolygonSymbolizer in an SLD Document.  A
 * PolygonSymbolizer defines how a polygon geometry should be rendered.
 *
 * @author James Macgill, CCG
 * @author Johann Sorel (Geomatys)
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class PolygonSymbolizerImpl extends AbstractSymbolizer implements PolygonSymbolizer, Cloneable {
    
    private Expression offset;
    private DisplacementImpl disp;
    
    private Fill fill = new FillImpl();
    private StrokeImpl stroke = new StrokeImpl();

    /**
     * Creates a new instance of DefaultPolygonStyler
     */
    protected PolygonSymbolizerImpl() {
        this(null,null,null,null,null,null,null,null);
    }

    protected PolygonSymbolizerImpl(Stroke stroke, 
            Fill fill, 
            Displacement disp, 
            Expression offset, 
            Unit<Length> uom, 
            String geom, 
            String name, 
            Description desc) {
        super(name, desc, geom, uom);
        this.stroke = StrokeImpl.cast( stroke );
        this.fill = fill;
        this.disp = DisplacementImpl.cast( disp );
        this.offset = offset;
    }
    
    public Expression getPerpendicularOffset() {
        return offset;
    }

    public void setPerpendicularOffset(Expression offset ) {
        this.offset = offset;
    }
    
    public Displacement getDisplacement() {
        return disp;
    }

    public void setDisplacement(org.opengis.style.Displacement displacement) {
        this.disp = DisplacementImpl.cast( displacement );
    }
    /**
     * Provides the graphical-symbolization parameter to use to fill the area
     * of the geometry.
     *
     * @return The Fill style to use when rendering the area.
     */
    public Fill getFill() {
        return fill;
    }

    /**
     * Sets the graphical-symbolization parameter to use to fill the area of
     * the geometry.
     *
     * @param fill The Fill style to use when rendering the area.
     */
    public void setFill(org.opengis.style.Fill fill) {
        if (this.fill == fill) {
            return;
        }
        this.fill = FillImpl.cast(fill);
    }

    /**
     * Provides the graphical-symbolization parameter to use for the outline of
     * the Polygon.
     *
     * @return The Stroke style to use when rendering lines.
     */
    public StrokeImpl getStroke() {
        return stroke;
    }

    /**
     * Sets the graphical-symbolization parameter to use for the outline of the
     * Polygon.
     *
     * @param stroke The Stroke style to use when rendering lines.
     */
    public void setStroke(org.opengis.style.Stroke stroke) {
        if (this.stroke == stroke) {
            return;
        }
        this.stroke = StrokeImpl.cast( stroke );
    }

    /**
     * Accepts a StyleVisitor to perform some operation on this LineSymbolizer.
     *
     * @param visitor The visitor to accept.
     */
    public Object accept(StyleVisitor visitor,Object data) {
        return visitor.visit(this,data);
    }

    public void accept(org.geotools.styling.StyleVisitor visitor) {
        visitor.visit(this);
    }
    
    /**
     * Creates a deep copy clone.   TODO: Need to complete the deep copy,
     * currently only shallow copy.
     *
     * @return The deep copy clone.
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public Object clone() {
        PolygonSymbolizerImpl clone;

        try {
            clone = (PolygonSymbolizerImpl) super.clone();

            if (fill != null) {
                clone.fill = (Fill) ((Cloneable) fill).clone();
            }

            if (stroke != null) {
                clone.stroke = (StrokeImpl) ((Cloneable) stroke).clone();
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // this should never happen.
        }

        return clone;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((disp == null) ? 0 : disp.hashCode());
        result = prime * result + ((fill == null) ? 0 : fill.hashCode());
        result = prime * result + ((offset == null) ? 0 : offset.hashCode());
        result = prime * result + ((stroke == null) ? 0 : stroke.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PolygonSymbolizerImpl other = (PolygonSymbolizerImpl) obj;
        if (disp == null) {
            if (other.disp != null)
                return false;
        } else if (!disp.equals(other.disp))
            return false;
        if (fill == null) {
            if (other.fill != null)
                return false;
        } else if (!fill.equals(other.fill))
            return false;
        if (offset == null) {
            if (other.offset != null)
                return false;
        } else if (!offset.equals(other.offset))
            return false;
        if (stroke == null) {
            if (other.stroke != null)
                return false;
        } else if (!stroke.equals(other.stroke))
            return false;
        return true;
    }

    static PolygonSymbolizerImpl cast(org.opengis.style.Symbolizer symbolizer) {
        if( symbolizer == null ){
            return null;
        }
        else if (symbolizer instanceof PolygonSymbolizerImpl){
            return (PolygonSymbolizerImpl) symbolizer;
        }
        else if( symbolizer instanceof org.opengis.style.PolygonSymbolizer ){
            org.opengis.style.PolygonSymbolizer polygonSymbolizer = (org.opengis.style.PolygonSymbolizer) symbolizer;
            PolygonSymbolizerImpl copy = new PolygonSymbolizerImpl();
            copy.setStroke( StrokeImpl.cast(polygonSymbolizer.getStroke()));
            copy.setDescription( polygonSymbolizer.getDescription() );
            copy.setDisplacement( polygonSymbolizer.getDisplacement());
            copy.setFill(polygonSymbolizer.getFill());
            copy.setGeometryPropertyName( polygonSymbolizer.getGeometryPropertyName());
            copy.setName(polygonSymbolizer.getName());
            copy.setPerpendicularOffset(polygonSymbolizer.getPerpendicularOffset());
            copy.setUnitOfMeasure( polygonSymbolizer.getUnitOfMeasure());
            return copy;
        }
        else {
            return null; // not possible
        }
    }



}
