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

/**
 * {@link MachineNode} represents the configuration of a cluster node.
 * {@link MachineNode} should be constructed by {@link MachineNode.Builder}.
 */
public final class MachineNode extends Node {
  long memory = -1; // in KB
  int mapSlots = 1;
  int reduceSlots = 1;
  long memoryPerMapSlot = -1; // in KB
  long memoryPerReduceSlot = -1; // in KB
  int numCores = 1;
  
  MachineNode(String name, int level) {
    super(name, level);
  }
  
  @Override
  public boolean equals(Object obj) {
    // name/level sufficient
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    // match equals
    return super.hashCode();
  }

  /**
   * Get the available physical RAM of the node.
   * @return The available physical RAM of the node, in KB.
   */
  public long getMemory() {
    return memory;
  }
  
  /**
   * Get the number of map slots of the node.
   * @return The number of map slots of the node.
   */
  public int getMapSlots() {
    return mapSlots;
  }
  
  /**
   * Get the number of reduce slots of the node.
   * @return The number of reduce slots fo the node.
   */
  public int getReduceSlots() {
    return reduceSlots;
  }
  
  /**
   * Get the amount of RAM reserved for each map slot.
   * @return the amount of RAM reserved for each map slot, in KB.
   */
  public long getMemoryPerMapSlot() {
    return memoryPerMapSlot;
  }

  /**
   * Get the amount of RAM reserved for each reduce slot.
   * @return the amount of RAM reserved for each reduce slot, in KB.
   */
  public long getMemoryPerReduceSlot() {
    return memoryPerReduceSlot;
  }
  
  /**
   * Get the number of cores of the node.
   * @return the number of cores of the node.
   */
  public int getNumCores() {
    return numCores;
  }

  /**
   * Get the rack node that the machine belongs to.
   * 
   * @return The rack node that the machine belongs to. Returns null if the
   *         machine does not belong to any rack.
   */
  public RackNode getRackNode() {
    return (RackNode)getParent();
  }
  
  @Override
  public synchronized boolean addChild(Node child) {
    throw new IllegalStateException("Cannot add child to MachineNode");
  }

  /**
   * Builder for a NodeInfo object
   */
  public static final class Builder {
    private MachineNode node;
    
    /**
     * Start building a new NodeInfo object.
     * @param name
     *          Unique name of the node. Typically the fully qualified domain
     *          name.
     */
    public Builder(String name, int level) {
      node = new MachineNode(name, level);
    }

    /**
     * Set the physical memory of the node.
     * @param memory Available RAM in KB.
     */
    public Builder setMemory(long memory) {
      node.memory = memory;
      return this;
    }
    
    /**
     * Set the number of map slot for the node.
     * @param mapSlots The number of map slots for the node.
     */
    public Builder setMapSlots(int mapSlots) {
      node.mapSlots = mapSlots;
      return this;
    }
    
    /**
     * Set the number of reduce slot for the node.
     * @param reduceSlots The number of reduce slots for the node.
     */   
    public Builder setReduceSlots(int reduceSlots) {
      node.reduceSlots = reduceSlots;
      return this;
    }
    
    /**
     * Set the amount of RAM reserved for each map slot.
     * @param memoryPerMapSlot The amount of RAM reserved for each map slot, in KB.
     */
    public Builder setMemoryPerMapSlot(long memoryPerMapSlot) {
      node.memoryPerMapSlot = memoryPerMapSlot;
      return this;
    }
    
    /**
     * Set the amount of RAM reserved for each reduce slot.
     * @param memoryPerReduceSlot The amount of RAM reserved for each reduce slot, in KB.
     */
    public Builder setMemoryPerReduceSlot(long memoryPerReduceSlot) {
      node.memoryPerReduceSlot = memoryPerReduceSlot;
      return this;
    }
    
    /**
     * Set the number of cores for the node.
     * @param numCores Number of cores for the node.
     */
    public Builder setNumCores(int numCores) {
      node.numCores = numCores;
      return this;
    }
    
    /**
     * Clone the settings from a reference {@link MachineNode} object.
     * @param ref The reference {@link MachineNode} object.
     */
    public Builder cloneFrom(MachineNode ref) {
      node.memory = ref.memory;
      node.mapSlots = ref.mapSlots;
      node.reduceSlots = ref.reduceSlots;
      node.memoryPerMapSlot = ref.memoryPerMapSlot;
      node.memoryPerReduceSlot = ref.memoryPerReduceSlot;
      node.numCores = ref.numCores;
      return this;
    }
    
    /**
     * Build the {@link MachineNode} object.
     * @return The {@link MachineNode} object being built.
     */
    public MachineNode build() {
      MachineNode retVal = node;
      node = null;
      return retVal;
    }
  }
}
