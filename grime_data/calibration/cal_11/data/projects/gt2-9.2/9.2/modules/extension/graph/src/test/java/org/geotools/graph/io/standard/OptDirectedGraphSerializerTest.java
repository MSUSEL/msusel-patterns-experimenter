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
package org.geotools.graph.io.standard;

import java.io.File;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.opt.OptDirectedGraphBuilder;
import org.geotools.graph.build.opt.OptGraphBuilder;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;


/**
 * 
 *
 * @source $URL$
 */
public class OptDirectedGraphSerializerTest extends TestCase {
  private OptDirectedGraphBuilder m_builder;
  private OptDirectedGraphBuilder m_rebuilder;
  private SerializedReaderWriter m_serializer;
  
  public OptDirectedGraphSerializerTest(String name) {
    super(name);
  }
 
  protected void setUp() throws Exception {
    super.setUp();
    
    m_builder = createBuilder();
    m_rebuilder = createBuilder();
		m_serializer = new SerializedReaderWriter();
		m_serializer.setProperty(SerializedReaderWriter.BUILDER, rebuilder());
  }

  /**
   * Create a simple graph with no bifurcations and serialize, then deserialize 
   * <BR>
   * <BR>
   * Expected: 1. before and after graph should have same structure.
   *
   */
  public void test_0() {
    final int nnodes = 100;
    Object[] obj = GraphTestUtil.buildNoBifurcations(builder(), nnodes);    
    
    final Map node2id = (Map)obj[2];
    final Map edge2id = (Map)obj[3];
    
    try {
  	  File victim = File.createTempFile( "graph", null );
      victim.deleteOnExit();
      serializer().setProperty(SerializedReaderWriter.FILENAME, victim.getAbsolutePath());
      
      serializer().write(builder().getGraph());
      
      Graph before = builder().getGraph();
      Graph after = serializer().read();
      
      //ensure same number of nodes and edges
      assertTrue(before.getNodes().size() == after.getNodes().size());
      assertTrue(before.getEdges().size() == after.getEdges().size());

      //ensure two nodes of degree 1, and nnodes-2 nodes of degree 2
      GraphVisitor visitor = new GraphVisitor() {
        public int visit(Graphable component) {
          DirectedNode node = (DirectedNode)component;
          if (node.getInDegree() == 0 || node.getOutDegree() == 0)
            return(Graph.PASS_AND_CONTINUE);
          return(Graph.FAIL_QUERY); 
        }
      };
      assertTrue(after.queryNodes(visitor).size() == 2);
      
      visitor = new GraphVisitor() {
        public int visit(Graphable component) {
          DirectedNode node = (DirectedNode)component;
          if (node.getInDegree() == 1 || node.getOutDegree() == 1)
            return(Graph.PASS_AND_CONTINUE);
          return(Graph.FAIL_QUERY); 
        }
      };
      
      assertTrue(after.getNodesOfDegree(2).size() == nnodes-2);
            
    }
    catch(Exception e) {
      e.printStackTrace();
      assertTrue(false);  
    }
  }
  
  /**
   * Create a perfect binary tree, serialize it and deserialize it. <BR>
   * <BR>
   * Expected: 1. Same structure before and after.
   *
   */
  public void test_1() {
    final int k = 5;
    GraphTestUtil.buildPerfectBinaryTree(builder(), k);
    
    try {
  	  File victim = File.createTempFile( "graph", null );
      victim.deleteOnExit();
      serializer().setProperty(SerializedReaderWriter.FILENAME, victim.getAbsolutePath());
      
      serializer().write(builder().getGraph());
      
      Graph before = builder().getGraph();
      Graph after = serializer().read();
      
       //ensure same number of nodes and edges
      assertTrue(before.getNodes().size() == after.getNodes().size());
      assertTrue(before.getEdges().size() == after.getEdges().size());

      GraphVisitor visitor = new GraphVisitor() {
        public int visit(Graphable component) {
          DirectedNode node = (DirectedNode)component;
          if (node.getInDegree() == 0 && node.getOutDegree() == 2)
            return(Graph.PASS_AND_CONTINUE);
          return(Graph.FAIL_QUERY); 
        }
      };
      assertTrue(after.queryNodes(visitor).size() == 1); //root
      
      visitor = new GraphVisitor() {
        public int visit(Graphable component) {
          DirectedNode node = (DirectedNode)component;
          if (node.getInDegree() == 1 && node.getOutDegree() == 2)
            return(Graph.PASS_AND_CONTINUE);
          return(Graph.FAIL_QUERY); 
        }
      };
      assertTrue(after.queryNodes(visitor).size() == Math.pow(2,k)-2); //internal
      
      visitor = new GraphVisitor() {
        public int visit(Graphable component) {
          DirectedNode node = (DirectedNode)component;
          if (node.getInDegree() == 1 && node.getOutDegree() == 0)
            return(Graph.PASS_AND_CONTINUE);
          return(Graph.FAIL_QUERY); 
        }
      };
      assertTrue(after.queryNodes(visitor).size() == Math.pow(2,k)); //leaves
    }
    catch(Exception e) {
      e.printStackTrace();
      assertTrue(false);
    }
  }
  
  protected OptDirectedGraphBuilder createBuilder() {
    return(new OptDirectedGraphBuilder());  
  }
  
  protected OptDirectedGraphBuilder builder() {
    return(m_builder);  
  }
   
  protected OptGraphBuilder createRebuilder() {
    return(new OptGraphBuilder());
  }

  protected OptDirectedGraphBuilder rebuilder() {
    return(m_rebuilder);  
  }
  
  protected SerializedReaderWriter serializer() {
    return(m_serializer);  
  }
}
