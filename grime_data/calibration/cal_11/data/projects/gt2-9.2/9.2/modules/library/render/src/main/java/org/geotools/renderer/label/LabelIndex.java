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
package org.geotools.renderer.label;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;

/**
 * Stores label items and helps in finding the interferering ones, either by
 * pure overlap or within a certain distance from the specified bounds
 * 
 * @author Andrea Aime
 * 
 *
 *
 *
 * @source $URL$
 */
public class LabelIndex {

    Quadtree index = new Quadtree();

    /**
     * Returns true if there is any label in the index within the specified
     * distance from the bounds. For speed reasons the bounds will be simply
     * expanded by the distance, no curved buffer will be generated
     * 
     * @param bounds
     * @param distance
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean labelsWithinDistance(Rectangle2D bounds, double distance) {
        if (distance < 0)
            return false;

        Envelope e = toEnvelope(bounds);
        e.expandBy(distance);
        List<InterferenceItem> results = index.query(e);
        if (results.size() == 0)
            return false;
        for (Iterator<InterferenceItem> it = results.iterator(); it.hasNext();) {
            InterferenceItem item = it.next();
            if (item.env.intersects(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a label into the index
     * 
     * @param item
     * @param bounds
     */
    public void addLabel(LabelCacheItem item, Rectangle2D bounds) {
        Envelope e = toEnvelope(bounds);
        index.insert(e, new InterferenceItem(e, item));
    }

    /**
     * Turns the specified Java2D rectangle into a JTS envelope
     * 
     * @param bounds
     * @return
     */
    private Envelope toEnvelope(Rectangle2D bounds) {
        return new Envelope(bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(), bounds.getMaxY());
    }

    /**
     * Simple structure stored into the quadtree (keeping the item around helps
     * in debugging)
     * 
     * @author Andrea Aime
     * 
     */
    static class InterferenceItem {
        Envelope env;

        LabelCacheItem item;

        public InterferenceItem(Envelope env, LabelCacheItem item) {
            super();
            this.env = env;
            this.item = item;
        }

    }

    /**
     * Reserve the area indicated by these Geometry.
     * 
     * @param reserved
     */
    public void reserveArea(List<Rectangle2D> reserved) {
        for( Rectangle2D area : reserved ){
            Envelope env = toEnvelope(area);
            
            InterferenceItem item = new InterferenceItem(env,null);            
            index.insert( env, item );
        }
    }
}
