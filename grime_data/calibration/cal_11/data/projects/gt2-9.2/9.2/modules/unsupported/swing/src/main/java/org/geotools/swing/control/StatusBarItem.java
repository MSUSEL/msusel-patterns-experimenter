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

package org.geotools.swing.control;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Base class for items in {@linkplain JMapStatusBar}.
 *
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public abstract class StatusBarItem extends JPanel {
    private static int ITEM_ID = 0;

    private final int id;
    private final boolean hasBorder;
    private final int minHeight;

    /**
     * Creates a new item with the given name. A border will be drawn for
     * the item.
     *
     * @param name item name; or {@code null} for a default name
     */
    public StatusBarItem(String name) {
        this(name, true);
    }

    /**
     * Creates a new item with the given name.
     *
     * @param name item name; or {@code null} for a default name
     * @param border whether to draw a border around this item
     */
    public StatusBarItem(String name, boolean border) {
        this.id = ITEM_ID++ ;
        
        if (name == null || name.trim().length() == 0) {
            setName("Item_" + id);
        } else {
            setName(name);
        }

        this.hasBorder = border;
        if (hasBorder) {
            setBorder(BorderFactory.createEtchedBorder());
        }

        setOpaque(false);
        minHeight = getHeight(); 
    }

    /**
     * Gets the unique integer ID assigned to this item.
     *
     * @return the item ID
     */
    public int getID() {
        return id;
    }

    /**
     * Gets the minimum height of this item.
     *
     * @return minimum height
     */
    public int getMinimumHeight() {
        return minHeight;
    }

    /**
     * For items that display numeric values, sets the number
     * of digits to show to the right of the decimal place. This
     * base implementation does nothing.
     *
     * @param numDecimals number of digits after decimal place
     */
    public void setNumDecimals(int numDecimals) {}

}
