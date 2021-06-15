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
package org.apache.hadoop.hdfs.server.namenode;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.hadoop.hdfs.protocol.DatanodeID;
import org.junit.Before;
import org.junit.Test;

public class TestHost2NodesMap {
    private Host2NodesMap map = new Host2NodesMap();
    private final DatanodeDescriptor dataNodes[] = new DatanodeDescriptor[] {
    new DatanodeDescriptor(new DatanodeID("h1:5020"), "/d1/r1"),
    new DatanodeDescriptor(new DatanodeID("h2:5020"), "/d1/r1"),
    new DatanodeDescriptor(new DatanodeID("h3:5020"), "/d1/r2"),
    new DatanodeDescriptor(new DatanodeID("h3:5030"), "/d1/r2"),
  };
  private final DatanodeDescriptor NULL_NODE = null; 
  private final DatanodeDescriptor NODE = 
    new DatanodeDescriptor(new DatanodeID("h3:5040"), "/d1/r4");

  @Before
  public void setup() {
    for(DatanodeDescriptor node:dataNodes) {
      map.add(node);
    }
    map.add(NULL_NODE);
  }
  
  @Test
  public void testContains() throws Exception {
    for(int i=0; i<dataNodes.length; i++) {
      assertTrue(map.contains(dataNodes[i]));
    }
    assertFalse(map.contains(NULL_NODE));
    assertFalse(map.contains(NODE));
  }

  @Test
  public void testGetDatanodeByHost() throws Exception {
    assertTrue(map.getDatanodeByHost("h1")==dataNodes[0]);
    assertTrue(map.getDatanodeByHost("h2")==dataNodes[1]);
    DatanodeDescriptor node = map.getDatanodeByHost("h3");
    assertTrue(node==dataNodes[2] || node==dataNodes[3]);
    assertTrue(null==map.getDatanodeByHost("h4"));
  }

  @Test
  public void testGetDatanodeByName() throws Exception {
    assertTrue(map.getDatanodeByName("h1:5020")==dataNodes[0]);
    assertTrue(map.getDatanodeByName("h1:5030")==null);
    assertTrue(map.getDatanodeByName("h2:5020")==dataNodes[1]);
    assertTrue(map.getDatanodeByName("h2:5030")==null);
    assertTrue(map.getDatanodeByName("h3:5020")==dataNodes[2]);
    assertTrue(map.getDatanodeByName("h3:5030")==dataNodes[3]);
    assertTrue(map.getDatanodeByName("h3:5040")==null);
    assertTrue(map.getDatanodeByName("h4")==null);
    assertTrue(map.getDatanodeByName(null)==null);
  }

  @Test
  public void testRemove() throws Exception {
    assertFalse(map.remove(NODE));
    
    assertTrue(map.remove(dataNodes[0]));
    assertTrue(map.getDatanodeByHost("h1")==null);
    assertTrue(map.getDatanodeByHost("h2")==dataNodes[1]);
    DatanodeDescriptor node = map.getDatanodeByHost("h3");
    assertTrue(node==dataNodes[2] || node==dataNodes[3]);
    assertTrue(null==map.getDatanodeByHost("h4"));
    
    assertTrue(map.remove(dataNodes[2]));
    assertTrue(map.getDatanodeByHost("h1")==null);
    assertTrue(map.getDatanodeByHost("h2")==dataNodes[1]);
    assertTrue(map.getDatanodeByHost("h3")==dataNodes[3]);
    
    assertTrue(map.remove(dataNodes[3]));
    assertTrue(map.getDatanodeByHost("h1")==null);
    assertTrue(map.getDatanodeByHost("h2")==dataNodes[1]);
    assertTrue(map.getDatanodeByHost("h3")==null);
    
    assertFalse(map.remove(NULL_NODE));
    assertTrue(map.remove(dataNodes[1]));
    assertFalse(map.remove(dataNodes[1]));
  }

}
