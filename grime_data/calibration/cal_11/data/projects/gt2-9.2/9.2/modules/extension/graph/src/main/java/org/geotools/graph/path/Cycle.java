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
package org.geotools.graph.path;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Node;

/**
 * 
 * Represents a cycle in a graph. A <B>cycle</B> C is defined as a closed walk 
 * of size n in which nodes 1 through n-1 form a path. 
 *   
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public class Cycle extends Walk {
  
   //TODO: DOCUMENT ME!
  public Cycle(Collection nodes) {
    super(nodes);  
  }
  
  /**
   * Tests if the cycle is valid. A valid cycle satisfies two conditions: <BR>
   * <BR>
   * 1. Each pair of adjacent nodes share an edge.<BR>
   * 2. The first and last nodes share an edge.
   * 3. The only node repetition is the first and last nodes.
   */
  public boolean isValid() {
    if (super.isValid()) {
      
      //ensure first and last nodes are same
      if (isClosed()) {
        //ensure no node repetitions except for first and last
        return(new HashSet(this).size() == size()-1);
      }  
    }
    return(false);
  }
  
  protected List buildEdges() {
    List edges = super.buildEdges();
    
    //get the edge between the first and last nodes
    Node first = (Node)get(0);
    Node last = (Node)get(size()-1);
    
    Edge e = first.getEdge(last);
    if (e != null) {
      edges.add(e);
      return(edges);
    }
    
    return(null);
  }
}
