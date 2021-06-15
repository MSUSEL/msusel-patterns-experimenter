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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.codehaus.jackson.annotate.JsonAnySetter;

/**
 * A {@link LoggedLocation} is a representation of a point in an hierarchical
 * network, represented as a series of membership names, broadest first.
 * 
 * For example, if your network has <i>hosts</i> grouped into <i>racks</i>, then
 * in onecluster you might have a node {@code node1} on rack {@code rack1}. This
 * would be represented with a ArrayList of two layers, with two {@link String}
 * s being {@code "rack1"} and {@code "node1"}.
 * 
 * The details of this class are set up to meet the requirements of the Jackson
 * JSON parser/generator.
 * 
 * All of the public methods are simply accessors for the instance variables we
 * want to write out in the JSON files.
 * 
 */
public class LoggedLocation implements DeepCompare {
   static final Map<List<String>, List<String>> layersCache = 
    new HashMap<List<String>, List<String>>();

  /**
   * The full path from the root of the network to the host.
   * 
   * NOTE that this assumes that the network topology is a tree.
   */
  List<String> layers = Collections.emptyList();

  static private Set<String> alreadySeenAnySetterAttributes =
      new TreeSet<String>();

  public List<String> getLayers() {
    return layers;
  }

  void setLayers(List<String> layers) {
    if (layers == null || layers.isEmpty()) {
      this.layers = Collections.emptyList();
    } else {
      synchronized (layersCache) {
        List<String> found = layersCache.get(layers);
        if (found == null) {
          // make a copy with interned string.
          List<String> clone = new ArrayList<String>(layers.size());
          for (String s : layers) {
            clone.add(s.intern());
          }
          // making it read-only as we are sharing them.
          List<String> readonlyLayers = Collections.unmodifiableList(clone);
          layersCache.put(readonlyLayers, readonlyLayers);
          this.layers = readonlyLayers;
        } else {
          this.layers = found;
        }
      }
    }
  }

  @SuppressWarnings("unused")
  // for input parameter ignored.
  @JsonAnySetter
  public void setUnknownAttribute(String attributeName, Object ignored) {
    if (!alreadySeenAnySetterAttributes.contains(attributeName)) {
      alreadySeenAnySetterAttributes.add(attributeName);
      System.err.println("In LoggedJob, we saw the unknown attribute "
          + attributeName + ".");
    }
  }

  // I'll treat this as an atomic object type
  private void compareStrings(List<String> c1, List<String> c2, TreePath loc,
      String eltname) throws DeepInequalityException {
    if (c1 == null && c2 == null) {
      return;
    }

    TreePath recursePath = new TreePath(loc, eltname);

    if (c1 == null || c2 == null || !c1.equals(c2)) {
      throw new DeepInequalityException(eltname + " miscompared", recursePath);
    }
  }

  public void deepCompare(DeepCompare comparand, TreePath loc)
      throws DeepInequalityException {
    if (!(comparand instanceof LoggedLocation)) {
      throw new DeepInequalityException("comparand has wrong type", loc);
    }

    LoggedLocation other = (LoggedLocation) comparand;

    compareStrings(layers, other.layers, loc, "layers");

  }
}
