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

/**
 * Represents an edge in Graph. An edge is an arc in a graph which 
 * connects exactly two nodes. These two nodes are referred to as 
 * the A node and the B node of the edge. The order of the A node and the B node 
 * is refered to as the <B>node orientation</B> of the edge. 
 *
 * @see Node
 * @see Graph
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 *
 * @source $URL$
 */
public interface Edge extends Graphable {
  
  /** flag to indicate equal node orientation of two edges **/ 
  public static final int EQUAL_NODE_ORIENTATION = 0;
  
  /** flag to indicate unequal node orientation of two edges **/ 
  public static final int UNEQUAL_NODE_ORIENTATION = 1;
  
  /** flag to indicate opposite node orientation of two edges **/
  public static final int OPPOSITE_NODE_ORIENTATION = -1;
  
  /**
   * Returns the A node of the edge.
   * 
   * @return The A node.
   */
  public Node getNodeA();
  
  /**
   * Returns the B node of the edge.
   * 
   * @return The B node.
   */
  public Node getNodeB();
  
  /**
   * Returns one of the two nodes of an edge. If the specified node is node A, 
   * then node B is returned, and vice versa.
   *
   * @param node The node opposite of the node to return.
   * 
   * @return Node A if node B is specified, node B if node A is specified.
   */
  public Node getOtherNode(Node node);
  
  /**
   * Reverses the node orientation of the edge.
   */
  public void reverse();
  
  /** 
   * Compares the node orientation of the edge with another edge.
   * 
   * @return EQUAL_NODE_ORIENTATION : both nodes are equal in the correct order.
   * 		     UNEQUAL_NODE_ORIENTATION: both nodes are not equal
   * 				 OPPOSITE_NODE_ORIENTATION : both nodes are equal in opposite order.  
   */
  public int compareNodes(Edge other);
}
