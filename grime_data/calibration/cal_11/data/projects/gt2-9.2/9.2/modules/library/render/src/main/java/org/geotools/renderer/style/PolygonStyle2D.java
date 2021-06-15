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

import org.geotools.resources.Classes;


/**
 * A style that contains the specification to renderer both the contour and the interior of a shape
 *
 * @author Andrea Aime
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class PolygonStyle2D extends LineStyle2D {
    protected Paint fill;
    protected Style2D graphicFill;
    protected Composite fillComposite;


    /**
     * Returns a Style2D used for filling the {@linkplain org.geotools.renderer.geom.Polygon polygon}
     * to be rendered, or <code>null</code> if none.
     * 
     * @return the current fill or null if none
     */
    public Style2D getGraphicFill()
    {
    	return graphicFill;
    }
    
    /**
     * Sets a Style2D for filling the {@linkplain org.geotools.renderer.geom.Polygon polygon} to be
     * rendered. Set it to <code>null</code> if no Style2D filling is to be performed.
     *
     * @param graphicFill
     */
    public void setGraphicFill(Style2D graphicFill) {
        this.graphicFill = graphicFill;
    }

    /**
     * Returns the filling color for the {@linkplain org.geotools.renderer.geom.Polygon polygon} to
     * be rendered, or <code>null</code> if none.
     *
     * @return the current fill or null if none
     */
    public Paint getFill() {
        return this.fill;
    }

    /**
     * Sets filling color for the {@linkplain org.geotools.renderer.geom.Polygon polygon} to be
     * rendered. Set it to <code>null</code> if no filling is to be performed.
     *
     * @param fill
     */
    public void setFill(Paint fill) {
        this.fill = fill;
    }

    /**
     * Returns the fill Composite for the {@linkplain org.geotools.renderer.geom.Polyline polyline}
     * to be rendered, or <code>null</code> if the contour is to be opaque
     *
     * @return the current fill composite or null if none
     */
    public Composite getFillComposite() {
        return this.fillComposite;
    }

    /**
     * Sets the fill Composite for the {@linkplain org.geotools.renderer.geom.Polyline polyline} to
     * be rendered. Set it to <code>null</code> if the contour is to be opaque
     *
     * @param fillComposite
     */
    public void setFillComposite(Composite fillComposite) {
        this.fillComposite = fillComposite;
    }

    /**
     * Returns a string representation of this style.
     */
    public String toString() {
        return Classes.getShortClassName(this) + '[' + fill + ']';
    }
}
