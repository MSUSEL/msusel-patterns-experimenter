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
 */
package org.geotools.renderer.style;

// J2SE dependencies
import java.awt.Composite;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import org.geotools.resources.Classes;


/**
 * A rendered style to draw the contour of shapes  TODO: add support for Graphic rendering (that
 * is, an image painted along the contour)
 *
 * @author aaime
 *
 *
 * @source $URL$
 */
public class LineStyle2D extends Style2D {
    protected Paint contour;
    protected Stroke stroke;
    protected Composite contourComposite;

    /** Holds value of property graphicStroke. */
    private Style2D graphicStroke;
    
    /**
     * Returns the stroke for the {@linkplain org.geotools.renderer.geom.Polyline polyline} to be
     * rendered, or <code>null</code> if none.
     *
     * @return the current stroke or null if none
     */
    public Stroke getStroke() {
        return this.stroke;
    }

    /**
     * Sets the stroke for the {@linkplain org.geotools.renderer.geom.Polyline polyline} to be
     * rendered
     *
     * @param stroke The stroke, or null if the contour doesn't need to be stroked
     */
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    /**
     * Returns the contour color for the {@linkplain org.geotools.renderer.geom.Polyline polyline}
     * to be rendered, or <code>null</code> if none.
     *
     * @return the current contour, or null if none
     */
    public Paint getContour() {
        return this.contour;
    }

    /**
     * Sets the contour color for the {@linkplain org.geotools.renderer.geom.Polyline polyline} to
     * be rendered
     *
     * @param contour
     */
    public void setContour(Paint contour) {
        this.contour = contour;
    }

    /**
     * Returns the contour Composite for the {@linkplain org.geotools.renderer.geom.Polyline
     * polyline} to be rendered, or <code>null</code> if the contour is to be opaque
     *
     * @return the current contour composite, or null if opaque
     */
    public Composite getContourComposite() {
        return this.contourComposite;
    }

    /**
     * Sets the contour Composite for the {@linkplain org.geotools.renderer.geom.Polyline polyline}
     * to be rendered. Set it to <code>null</code> if the contour is to be opaque
     *
     * @param contourComposite
     */
    public void setContourComposite(Composite contourComposite) {
        this.contourComposite = contourComposite;
    }
    
    /** Getter for property graphicStroke.
     * @return Value of property graphicStroke.
     *
     */
    public Style2D getGraphicStroke() {
        return this.graphicStroke;
    }
    
    /** Setter for property graphicStroke.
     * @param graphicStroke New value of property graphicStroke.
     *
     */
    public void setGraphicStroke(Style2D graphicStroke) {
        this.graphicStroke = graphicStroke;
    }
    

    /**
     * Returns a string representation of this style.
     */
    public String toString() {
        return Classes.getShortClassName(this) + '[' + contour + ']';
    }
}
