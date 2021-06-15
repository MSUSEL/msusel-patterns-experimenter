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
package org.geotools.renderer.style;

import java.awt.Composite;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;

/**
 * Represents a {@link Style2D} backed by an {@link Icon}
 * 
 * @author milton
 *
 *
 * @source $URL$
 */
public class IconStyle2D extends Style2D {
    private static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();

    private Icon icon;

    private float rotation;

    private Composite composite;

    private float displacementX;

    private float displacementY;

    /**
     * Constructor.
     * 
     * @param renderer
     *            GlyphRenderer to be used for rendering this GlyphStyle2D
     * @param graphic
     *            Graphic for defining the glyph.
     * @param externalGraphic
     *            ExternalGraphic for defining the glyph.
     * @param The
     *            rotation of the icon
     * @param The
     *            opacity of the icon
     */
    public IconStyle2D(Icon icon, Object feature, float displacementX, float displacementY,
            float rotation, Composite composite) {
        this.icon = icon;
        this.rotation = rotation;
        this.composite = composite;
        this.displacementX = displacementX;
        this.displacementY = displacementY;
    }

    /**
     * The Icon rotation
     * @return
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * The icon composite
     * @return
     */
    public Composite getComposite() {
        return composite;
    }

    /**
     * The icon x displacement
     * @return
     */
    public float getDisplacementX() {
        return displacementX;
    }

    /**
     * The icon y displacement
     * @return
     */
    public float getDisplacementY() {
        return displacementY;
    }

    /**
     * Returns the icon backing this style
     * 
     * @return
     */
    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setComposite(Composite composite) {
        this.composite = composite;
    }

    public void setDisplacementX(float displacementX) {
        this.displacementX = displacementX;
    }

    public void setDisplacementY(float displacementY) {
        this.displacementY = displacementY;
    };
}
