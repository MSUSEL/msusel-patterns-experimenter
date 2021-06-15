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
package org.geotools.graph.structure;

import java.util.List;

/**
 * Represents a node in a graph. A node is a point in a graph which is 
 * iadjacent to 0 or more edges. The collection of 
 * edges that are incident/ adjacent to the node, is referred to as the 
 * "adjacency list" of the node.
 * 
 * @see Graph
 * @see Edge
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 *
 * @source $URL$
 */
public interface Node extends Graphable {

  /**
   * Adds an edge to the adjacency list of the node.
   * 
   * @param e Adjacent edge to add.
   */
  public void add(Edge e);
  
  /**
   * Removes an edge from the adjacency list of the node.
   * 
   * @param e Adjacent edge to remove.
   */
  public void remove(Edge e);
  
  /**
   * Returns an edge in the adjacency list of the node that is adjacent to 
   * another specified node. <BR>
   * <BR>
   * Note: It is possible for two nodes to share multiple edges between them. In
   * this case, getEdges(Node other) can be used to obtain a complete list. 
   * 
   * @param other The other node that the desired edge to return is adjacent to.
   *
   * @return The first edge that is found to be adjacent to the 
   * specified node.
   */
  public Edge getEdge(Node other);
  
  /**
   * Returns a collection of edges in the adjacency list of the node that are 
   * adjacent to another specified node. 
   * 
   * @param other The other node that the desired edges to return are 
   * adjacent to.
   * 
   * @return List of all edges that are found to be adjacent to the specified
   * node.
   */
  public List getEdges(Node other);
  
  /**
   * Returns the edge adjacency list of the node.
   * 
   * @return A list containing all edges that are adjacent to the node.
   */
  public List getEdges();
  
  /**
   * Returns the degree of the node. The degree of a node is defined as the 
   * number of edges that are adjacent to the node.
   * 
   * @return int Degree of node.
   */
  public int getDegree();
  
}
