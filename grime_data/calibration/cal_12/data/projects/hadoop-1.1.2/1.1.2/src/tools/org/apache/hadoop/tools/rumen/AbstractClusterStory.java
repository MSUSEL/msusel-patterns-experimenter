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
package org.apache.hadoop.tools.rumen;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * {@link AbstractClusterStory} provides a partial implementation of
 * {@link ClusterStory} by parsing the topology tree.
 */
public abstract class AbstractClusterStory implements ClusterStory {
  protected Set<MachineNode> machineNodes;
  protected Set<RackNode> rackNodes;
  protected MachineNode[] mNodesFlattened;
  protected Map<String, MachineNode> mNodeMap;
  protected Map<String, RackNode> rNodeMap;
  protected int maximumDistance = 0;
  
  @Override
  public Set<MachineNode> getMachines() {
    parseTopologyTree();
    return machineNodes;
  }
  
  @Override
  public synchronized Set<RackNode> getRacks() {
    parseTopologyTree();    
    return rackNodes;
  }
  
  @Override
  public synchronized MachineNode[] getRandomMachines(int expected, 
                                                      Random random) {
    if (expected == 0) {
      return new MachineNode[0];
    }

    parseTopologyTree();
    int total = machineNodes.size();
    int select = Math.min(expected, total);

    if (mNodesFlattened == null) {
      mNodesFlattened = machineNodes.toArray(new MachineNode[total]);
    }

    MachineNode[] retval = new MachineNode[select];
    int i = 0;
    while ((i != select) && (total != i + select)) {
      int index = random.nextInt(total - i);
      MachineNode tmp = mNodesFlattened[index];
      mNodesFlattened[index] = mNodesFlattened[total - i - 1];
      mNodesFlattened[total - i - 1] = tmp;
      ++i;
    }
    if (i == select) {
      System.arraycopy(mNodesFlattened, total - i, retval, 0, select);
    } else {
      System.arraycopy(mNodesFlattened, 0, retval, 0, select);
    }

    return retval;
  }
  
  protected synchronized void buildMachineNodeMap() {
    if (mNodeMap == null) {
      mNodeMap = new HashMap<String, MachineNode>(machineNodes.size());
      for (MachineNode mn : machineNodes) {
        mNodeMap.put(mn.getName(), mn);
      }
    }
  }
  
  @Override
  public MachineNode getMachineByName(String name) {
    buildMachineNodeMap();
    return mNodeMap.get(name);
  }
  
  @Override
  public int distance(Node a, Node b) {
    int lvl_a = a.getLevel();
    int lvl_b = b.getLevel();
    int retval = 0;
    if (lvl_a > lvl_b) {
      retval = lvl_a-lvl_b;
      for (int i=0; i<retval; ++i) {
        a = a.getParent();
      }
    } else if (lvl_a < lvl_b) {
      retval = lvl_b-lvl_a;
      for (int i=0; i<retval; ++i) {
        b = b.getParent();
      }      
    }
    
    while (a != b) {
      a = a.getParent();
      b = b.getParent();
      ++retval;
    }
    
    return retval;
  }
  
  protected synchronized void buildRackNodeMap() {
    if (rNodeMap == null) {
      rNodeMap = new HashMap<String, RackNode>(rackNodes.size());
      for (RackNode rn : rackNodes) {
        rNodeMap.put(rn.getName(), rn);
      }
    }
  }
  
  @Override
  public RackNode getRackByName(String name) {
    buildRackNodeMap();
    return rNodeMap.get(name);
  }
  
  @Override
  public int getMaximumDistance() {
    parseTopologyTree();
    return maximumDistance;
  }
  
  protected synchronized void parseTopologyTree() {
    if (machineNodes == null) {
      Node root = getClusterTopology();
      SortedSet<MachineNode> mNodes = new TreeSet<MachineNode>();
      SortedSet<RackNode> rNodes = new TreeSet<RackNode>();
      // dfs search of the tree.
      Deque<Node> unvisited = new ArrayDeque<Node>();
      Deque<Integer> distUnvisited = new ArrayDeque<Integer>();
      unvisited.add(root);
      distUnvisited.add(0);
      for (Node n = unvisited.poll(); n != null; n = unvisited.poll()) {
        int distance = distUnvisited.poll();
        if (n instanceof RackNode) {
          rNodes.add((RackNode) n);
          mNodes.addAll(((RackNode) n).getMachinesInRack());
          if (distance + 1 > maximumDistance) {
            maximumDistance = distance + 1;
          }
        } else if (n instanceof MachineNode) {
          mNodes.add((MachineNode) n);
          if (distance > maximumDistance) {
            maximumDistance = distance;
          }
        } else {
          for (Node child : n.getChildren()) {
            unvisited.addFirst(child);
            distUnvisited.addFirst(distance+1);
          }
        }
      }

      machineNodes = Collections.unmodifiableSortedSet(mNodes);
      rackNodes = Collections.unmodifiableSortedSet(rNodes);
    }
  }
}
