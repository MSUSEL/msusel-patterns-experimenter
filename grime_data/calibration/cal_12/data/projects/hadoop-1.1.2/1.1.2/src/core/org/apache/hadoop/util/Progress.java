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
package org.apache.hadoop.util;

import java.util.ArrayList;

/** Utility to assist with generation of progress reports.  Applications build
 * a hierarchy of {@link Progress} instances, each modelling a phase of
 * execution.  The root is constructed with {@link #Progress()}.  Nodes for
 * sub-phases are created by calling {@link #addPhase()}.
 */
public class Progress {
  private String status = "";
  private float progress;
  private int currentPhase;
  private ArrayList<Progress> phases = new ArrayList<Progress>();
  private Progress parent;
  private float progressPerPhase;

  /** Creates a new root node. */
  public Progress() {}

  /** Adds a named node to the tree. */
  public Progress addPhase(String status) {
    Progress phase = addPhase();
    phase.setStatus(status);
    return phase;
  }

  /** Adds a node to the tree. */
  public synchronized Progress addPhase() {
    Progress phase = new Progress();
    phases.add(phase);
    phase.parent = this;
    progressPerPhase = 1.0f / (float)phases.size();
    return phase;
  }

  /** Called during execution to move to the next phase at this level in the
   * tree. */
  public synchronized void startNextPhase() {
    currentPhase++;
  }

  /** Returns the current sub-node executing. */
  public synchronized Progress phase() {
    return phases.get(currentPhase);
  }

  /** Completes this node, moving the parent node to its next child. */
  public void complete() {
    // we have to traverse up to our parent, so be careful about locking.
    Progress myParent;
    synchronized(this) {
      progress = 1.0f;
      myParent = parent;
    }
    if (myParent != null) {
      // this will synchronize on the parent, so we make sure we release
      // our lock before getting the parent's, since we're traversing 
      // against the normal traversal direction used by get() or toString().
      // We don't need transactional semantics, so we're OK doing this. 
      myParent.startNextPhase();
    }
  }

  /** Called during execution on a leaf node to set its progress. */
  public synchronized void set(float progress) {
    this.progress = progress;
  }

  /** Returns the overall progress of the root. */
  // this method probably does not need to be synchronized as getINternal() is synchronized 
  // and the node's parent never changes. Still, it doesn't hurt. 
  public synchronized float get() {
    Progress node = this;
    while (node.parent != null) {                 // find the root
      node = parent;
    }
    return node.getInternal();
  }

  
  /** Computes progress in this node. */
  private synchronized float getInternal() {
    int phaseCount = phases.size();
    if (phaseCount != 0) {
      float subProgress =
        currentPhase < phaseCount ? phase().getInternal() : 0.0f;
      return progressPerPhase*(currentPhase + subProgress);
    } else {
      return progress;
    }
  }

  /**
   * Returns progress in this node. get() would give overall progress of the
   * root node(not just given current node).
   */
  public synchronized float getProgress() {
    return getInternal();
  }

  public synchronized void setStatus(String status) {
    this.status = status;
  }

  public String toString() {
    StringBuffer result = new StringBuffer();
    toString(result);
    return result.toString();
  }

  private synchronized void toString(StringBuffer buffer) {
    buffer.append(status);
    if (phases.size() != 0 && currentPhase < phases.size()) {
      buffer.append(" > ");
      phase().toString(buffer);
    }
  }

}
