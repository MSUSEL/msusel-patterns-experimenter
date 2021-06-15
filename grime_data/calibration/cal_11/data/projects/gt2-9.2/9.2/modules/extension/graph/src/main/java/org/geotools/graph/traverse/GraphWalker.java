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
package org.geotools.graph.traverse;

import org.geotools.graph.structure.Graphable;


/**
 * Iterated over the components of a graph using a standard visitor 
 * pattern.
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 *
 * @source $URL$
 */
public interface GraphWalker {
    
    /**
     * Visits a graph component.
     *
     * @param element The component being visited.
     * @param traversal The traversal controlling the sequence of graph
     *        component visits.
     *
     * @return GraphTraversal#CONTINUE to signal that the traversal should 
     *         continue.<BR> 
     *         GraphTraversal#CONTINUE to signal that the traversal should 
     *         suspend.<BR>
     *         GraphTraversal#KILL_BRANCH to signal that the traversal should
     *         kill its current branch.<BR>
     *         GraphTraversal#STOP to signal that the traversal should stop.<BR>
     * 
     * @see GraphTraversal
     * @see GraphIterator
     */
    public int visit(Graphable element, GraphTraversal traversal);

    /**
     * Called when the graph traversal is completed. Wether this method is called
     * after a traversal has been stopped with a return signal is up to the 
     * implementation of GraphTraversal.
     * 
     * @see GraphTraversal
     */
    public void finish();
}
