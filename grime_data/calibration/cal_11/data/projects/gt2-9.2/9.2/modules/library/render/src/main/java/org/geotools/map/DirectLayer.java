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
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.map;

import java.awt.Graphics2D;

/**
 * A Layer directly responsible for its own rendering.
 * <p>
 * Direct layers are responsible for their own rendering and are useful for:
 * <ul>
 * <li>Map Decorations such as legends or scalebars that depend only on the map and viewport and do
 * not actually make use of external data.</li>
 * <li>Data services that are visual in nature, such as a Web Map Service (which you can think of as
 * an external renderer)</li>
 * <li>You may also consider data formats, such as CAD files, where the style information is
 * "baked into" the data format as suitable for a DirectLayer. In these cases you are only going for
 * a visual display and are not making the raw features available to the geotools library for use.
 * </ul>
 * While any and all data sources could be wrapped up as a DirectLayer we encourage you to consider
 * a separate data mode, style model and renderer. 
 * @author Jody
 * @since 2.7
 * @version 8.0
 *
 * @source $URL$
 */
public abstract class DirectLayer extends Layer {

    protected DirectLayer() {
    }

    /**
     * Draw layer contents onto screen
     * 
     * @param map
     *            Map being drawn; check map bounds and crs
     * @param graphics
     *            Graphics to draw into
     * @param viewport
     *            Area to draw the map into; including screen area
     */
    public abstract void draw(Graphics2D graphics, MapContent map, MapViewport viewport);
    
}
