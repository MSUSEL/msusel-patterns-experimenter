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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

/**
 * {@link ZombieCluster} rebuilds the cluster topology using the information
 * obtained from job history logs.
 */
public class ZombieCluster extends AbstractClusterStory {
  private Node root;

  /**
   * Construct a homogeneous cluster. We assume that the leaves on the topology
   * are {@link MachineNode}s, and the parents of {@link MachineNode}s are
   * {@link RackNode}s. We also expect all leaf nodes are on the same level.
   * 
   * @param topology
   *          The network topology.
   * @param defaultNode
   *          The default node setting.
   */
  public ZombieCluster(LoggedNetworkTopology topology, MachineNode defaultNode) {
    buildCluster(topology, defaultNode);
  }

  /**
   * Construct a homogeneous cluster. We assume that the leaves on the topology
   * are {@link MachineNode}s, and the parents of {@link MachineNode}s are
   * {@link RackNode}s. We also expect all leaf nodes are on the same level.
   * 
   * @param path Path to the JSON-encoded topology file.
   * @param conf
   * @param defaultNode
   *          The default node setting.
   * @throws IOException 
   */
  public ZombieCluster(Path path, MachineNode defaultNode, Configuration conf) throws IOException {
    this(new ClusterTopologyReader(path, conf).get(), defaultNode);
  }
  
  /**
   * Construct a homogeneous cluster. We assume that the leaves on the topology
   * are {@link MachineNode}s, and the parents of {@link MachineNode}s are
   * {@link RackNode}s. We also expect all leaf nodes are on the same level.
   * 
   * @param input The input stream for the JSON-encoded topology file.
   * @param defaultNode
   *          The default node setting.
   * @throws IOException 
   */
  public ZombieCluster(InputStream input, MachineNode defaultNode) throws IOException {
    this(new ClusterTopologyReader(input).get(), defaultNode);
  }

  @Override
  public Node getClusterTopology() {
    return root;
  }

  private final void buildCluster(LoggedNetworkTopology topology,
      MachineNode defaultNode) {
    Map<LoggedNetworkTopology, Integer> levelMapping = 
      new IdentityHashMap<LoggedNetworkTopology, Integer>();
    Deque<LoggedNetworkTopology> unvisited = 
      new ArrayDeque<LoggedNetworkTopology>();
    unvisited.add(topology);
    levelMapping.put(topology, 0);
    
    // building levelMapping and determine leafLevel
    int leafLevel = -1; // -1 means leafLevel unknown.
    for (LoggedNetworkTopology n = unvisited.poll(); n != null; 
      n = unvisited.poll()) {
      int level = levelMapping.get(n);
      List<LoggedNetworkTopology> children = n.getChildren();
      if (children == null || children.isEmpty()) {
        if (leafLevel == -1) {
          leafLevel = level;
        } else if (leafLevel != level) {
          throw new IllegalArgumentException(
              "Leaf nodes are not on the same level");
        }
      } else {
        for (LoggedNetworkTopology child : children) {
          levelMapping.put(child, level + 1);
          unvisited.addFirst(child);
        }
      }
    }

    /**
     * A second-pass dfs traverse of topology tree. path[i] contains the parent
     * of the node at level i+1.
     */
    Node[] path = new Node[leafLevel];
    unvisited.add(topology);
    for (LoggedNetworkTopology n = unvisited.poll(); n != null; 
      n = unvisited.poll()) {
      int level = levelMapping.get(n);
      Node current;
      if (level == leafLevel) { // a machine node
        MachineNode.Builder builder = new MachineNode.Builder(n.getName(), level);
        if (defaultNode != null) {
          builder.cloneFrom(defaultNode);
        }
        current = builder.build();
      } else {
        current = (level == leafLevel - 1) 
          ? new RackNode(n.getName(), level) : 
            new Node(n.getName(), level);
        path[level] = current;
        // Add all children to the front of the queue.
        for (LoggedNetworkTopology child : n.getChildren()) {
          unvisited.addFirst(child);
        }
      }
      if (level != 0) {
        path[level - 1].addChild(current);
      }
    }

    root = path[0];
  }
}
