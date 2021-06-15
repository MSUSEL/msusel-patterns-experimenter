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
package org.geotools.graph.traverse.basic;

import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.GraphWalker;


/**
 * A simple implementation of GraphWalker that decorates a GraphVisitor.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 *
 * @source $URL$
 */
public class SimpleGraphWalker implements GraphWalker {
    
    /** Underlying visitor */
    private GraphVisitor m_visitor;

    /**
     * Creates a GraphWalker from a preexising GraphVisitor.
     *
     * @param visitor The visitor to decorate
     */
    public SimpleGraphWalker(GraphVisitor visitor) {
        m_visitor = visitor;
    }

    /**
     * Returns the underlying visitor.
     * 
     * @return The visitor being decorated by the walker. 
     */
    public GraphVisitor getVistor() {
      return(m_visitor);  
    }
    
    /**
     * Sets the underlying visitor.
     * 
     * @param visitor The visitor to be decorated by the walker.
     */
    public void setVisitor(GraphVisitor visitor) {
      m_visitor = visitor;  
    }
    
    
    /**
     * Defers to the underlying visitor.
     *
     * @see GraphWalker#visit(Graphable, GraphTraversal)
     */
    public int visit(Graphable element, GraphTraversal traversal) {
      return (m_visitor.visit(element));
    }

    /**
     * Does nothing.
     *
     * @see GraphWalker#finish()
     */
    public void finish() {}
}
