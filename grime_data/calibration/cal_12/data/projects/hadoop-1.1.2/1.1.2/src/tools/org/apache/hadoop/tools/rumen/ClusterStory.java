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

import java.util.Set;
import java.util.Random;

/**
 * {@link ClusterStory} represents all configurations of a MapReduce cluster,
 * including nodes, network topology, and slot configurations.
 */
public interface ClusterStory {
  /**
   * Get all machines of the cluster.
   * @return A read-only set that contains all machines of the cluster.
   */
  public Set<MachineNode> getMachines();

  /**
   * Get all racks of the cluster.
   * @return A read-only set that contains all racks of the cluster.
   */
  public Set<RackNode> getRacks();
  
  /**
   * Get the cluster topology tree.
   * @return The root node of the cluster topology tree.
   */
  public Node getClusterTopology();
  
  /**
   * Select a random set of machines.
   * @param expected The expected sample size.
   * @param random Random number generator to use.
   * @return An array of up to expected number of {@link MachineNode}s.
   */
  public MachineNode[] getRandomMachines(int expected, Random random);

  /**
   * Get {@link MachineNode} by its host name.
   * 
   * @return The {@link MachineNode} with the same name. Or null if not found.
   */
  public MachineNode getMachineByName(String name);
  
  /**
   * Get {@link RackNode} by its name.
   * @return The {@link RackNode} with the same name. Or null if not found.
   */
  public RackNode getRackByName(String name);

  /**
   * Determine the distance between two {@link Node}s. Currently, the distance
   * is loosely defined as the length of the longer path for either a or b to
   * reach their common ancestor.
   * 
   * @param a
   * @param b
   * @return The distance between {@link Node} a and {@link Node} b.
   */
  int distance(Node a, Node b);
  
  /**
   * Get the maximum distance possible between any two nodes.
   * @return the maximum distance possible between any two nodes.
   */
  int getMaximumDistance();
}
