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
import java.util.List;

/**
 * A {@link LoggedDiscreteCDF} is a discrete approximation of a cumulative
 * distribution function, with this class set up to meet the requirements of the
 * Jackson JSON parser/generator.
 * 
 * All of the public methods are simply accessors for the instance variables we
 * want to write out in the JSON files.
 * 
 */
public class LoggedDiscreteCDF implements DeepCompare {
  /**
   * The number of values this CDF is built on
   */
  long numberValues = -1L;
  /**
   * The least {@code X} value
   */
  long minimum = Long.MIN_VALUE;
  /**
   * The coordinates of the bulk of the CDF
   */
  List<LoggedSingleRelativeRanking> rankings = new ArrayList<LoggedSingleRelativeRanking>();
  /**
   * The greatest {@code X} value
   */
  long maximum = Long.MAX_VALUE;

  void setCDF(Histogram data, int[] steps, int modulus) {

    numberValues = data.getTotalCount();
    long[] CDF = data.getCDF(modulus, steps);

    if (CDF != null) {
      minimum = CDF[0];
      maximum = CDF[CDF.length - 1];

      rankings = new ArrayList<LoggedSingleRelativeRanking>();

      for (int i = 1; i < CDF.length - 1; ++i) {
        LoggedSingleRelativeRanking srr = new LoggedSingleRelativeRanking();

        srr.setRelativeRanking(((double) steps[i - 1]) / modulus);
        srr.setDatum(CDF[i]);

        rankings.add(srr);
      }
    }
  }

  public long getMinimum() {
    return minimum;
  }

  void setMinimum(long minimum) {
    this.minimum = minimum;
  }

  public List<LoggedSingleRelativeRanking> getRankings() {
    return rankings;
  }

  void setRankings(List<LoggedSingleRelativeRanking> rankings) {
    this.rankings = rankings;
  }

  public long getMaximum() {
    return maximum;
  }

  void setMaximum(long maximum) {
    this.maximum = maximum;
  }

  public long getNumberValues() {
    return numberValues;
  }

  void setNumberValues(long numberValues) {
    this.numberValues = numberValues;
  }

  private void compare1(long c1, long c2, TreePath loc, String eltname)
      throws DeepInequalityException {
    if (c1 != c2) {
      throw new DeepInequalityException(eltname + " miscompared", new TreePath(
          loc, eltname));
    }
  }

  private void compare1(List<LoggedSingleRelativeRanking> c1,
      List<LoggedSingleRelativeRanking> c2, TreePath loc, String eltname)
      throws DeepInequalityException {
    if (c1 == null && c2 == null) {
      return;
    }

    if (c1 == null || c2 == null || c1.size() != c2.size()) {
      throw new DeepInequalityException(eltname + " miscompared", new TreePath(
          loc, eltname));
    }

    for (int i = 0; i < c1.size(); ++i) {
      c1.get(i).deepCompare(c2.get(i), new TreePath(loc, eltname, i));
    }
  }

  public void deepCompare(DeepCompare comparand, TreePath loc)
      throws DeepInequalityException {
    if (!(comparand instanceof LoggedDiscreteCDF)) {
      throw new DeepInequalityException("comparand has wrong type", loc);
    }

    LoggedDiscreteCDF other = (LoggedDiscreteCDF) comparand;

    compare1(numberValues, other.numberValues, loc, "numberValues");

    compare1(minimum, other.minimum, loc, "minimum");
    compare1(maximum, other.maximum, loc, "maximum");

    compare1(rankings, other.rankings, loc, "rankings");
  }
}
