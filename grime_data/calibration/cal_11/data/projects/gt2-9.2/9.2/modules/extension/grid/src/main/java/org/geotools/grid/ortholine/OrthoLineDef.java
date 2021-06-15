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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid.ortholine;

/**
 * Defines how to generate a set of regularly-spaced, ortho-line elements with
 * given orientation and level.
 * 
 * @author mbedward
 * @since 8.0
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class OrthoLineDef {

    private final int level;
    private final LineOrientation orientation;
    private final double spacing;

    /**
     * Creates a new ortho-line definition.
     * 
     * @param orientation line orientation
     * @param level an integer level (user-defined values) indicating line precedence
     * @param spacing the spacing between lines in world distance units
     */
    public OrthoLineDef(LineOrientation orientation, int level, double spacing) {
        this.level = level;
        this.orientation = orientation;
        this.spacing = spacing;
    }
    
    /**
     * Creates a copy of an existing line definition.
     * 
     * @param lineDef the definition to copy
     * @throws IllegalArgumentException if {@code lineDef} is {@code null}
     */
    public OrthoLineDef(OrthoLineDef lineDef) {
        if (lineDef == null) {
            throw new IllegalArgumentException("lineDef arg must not be null");
        }
        this.level = lineDef.level;
        this.orientation = lineDef.orientation;
        this.spacing = lineDef.spacing;
    }

    /**
     * Gets the integer level (line precedence).
     * 
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the orientation.
     * 
     * @return orientation
     */
    public LineOrientation getOrientation() {
        return orientation;
    }

    /**
     * Gets the spacing between lines.
     * 
     * @return line spacing
     */
    public double getSpacing() {
        return spacing;
    }

}
