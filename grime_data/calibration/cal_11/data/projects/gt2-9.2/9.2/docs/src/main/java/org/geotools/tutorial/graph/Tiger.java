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
package org.geotools.tutorial.graph;

import org.geotools.swing.JMapFrame;

/**
 * This is a quick visual example of how to use the graph module using the TIGER roads data.
 * <p>
 * The gt-graph module builds ontop of core GeoTools concepts (so you should be familiar with DataStore, FeatureCollection, Query, Geometry,
 * MapContent prior to starting this tutorial).
 * <p>
 * This example consists of a *simple* JMapFrame with a number of actions allowing you to load a shapefile; convert it to an internal graph; select
 * "waypoints"; and use the way points to calculate a route; use the route to create a FeatureCollection; and display that FeatureCollection as a new
 * layer.
 * </p>
 * 
 * @author Jody Garnett
 */
public class Tiger extends JMapFrame {

    public Tiger() {
    }

    public static void main(String args[]){
        Tiger tiger = new Tiger();
        
        tiger.setVisible(true);
    }
}
