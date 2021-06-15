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
import java.util.TreeSet;

import org.codehaus.jackson.annotate.JsonAnySetter;

/**
 * A {@link LoggedSingleRelativeRanking} represents an X-Y coordinate of a
 * single point in a discrete CDF.
 * 
 * All of the public methods are simply accessors for the instance variables we
 * want to write out in the JSON files.
 * 
 */
public class LoggedSingleRelativeRanking implements DeepCompare {
  /**
   * The Y coordinate, as a fraction {@code ( 0.0D, 1.0D )}. The default value
   * is there to mark an unfilled-in value.
   */
  double relativeRanking = -1.0D;
  /**
   * The X coordinate
   */
  long datum = -1L;

  static private Set<String> alreadySeenAnySetterAttributes =
      new TreeSet<String>();

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

  public double getRelativeRanking() {
    return relativeRanking;
  }

  void setRelativeRanking(double relativeRanking) {
    this.relativeRanking = relativeRanking;
  }

  public long getDatum() {
    return datum;
  }

  void setDatum(long datum) {
    this.datum = datum;
  }

  private void compare1(long c1, long c2, TreePath loc, String eltname)
      throws DeepInequalityException {
    if (c1 != c2) {
      throw new DeepInequalityException(eltname + " miscompared", new TreePath(
          loc, eltname));
    }
  }

  private void compare1(double c1, double c2, TreePath loc, String eltname)
      throws DeepInequalityException {
    if (c1 != c2) {
      throw new DeepInequalityException(eltname + " miscompared", new TreePath(
          loc, eltname));
    }
  }

  public void deepCompare(DeepCompare comparand, TreePath loc)
      throws DeepInequalityException {
    if (!(comparand instanceof LoggedSingleRelativeRanking)) {
      throw new DeepInequalityException("comparand has wrong type", loc);
    }

    LoggedSingleRelativeRanking other = (LoggedSingleRelativeRanking) comparand;

    compare1(relativeRanking, other.relativeRanking, loc, "relativeRanking");
    compare1(datum, other.datum, loc, "datum");
  }
}
