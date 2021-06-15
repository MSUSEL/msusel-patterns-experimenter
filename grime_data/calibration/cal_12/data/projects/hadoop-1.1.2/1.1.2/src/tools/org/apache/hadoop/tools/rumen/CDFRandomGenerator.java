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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * An instance of this class generates random values that confirm to the
 * embedded {@link LoggedDiscreteCDF} . The discrete CDF is a pointwise
 * approximation of the "real" CDF. We therefore have a choice of interpolation
 * rules.
 * 
 * A concrete subclass of this abstract class will implement valueAt(double)
 * using a class-dependent interpolation rule.
 * 
 */
public abstract class CDFRandomGenerator {
  final double[] rankings;
  final long[] values;

  final Random random;

  CDFRandomGenerator(LoggedDiscreteCDF cdf) {
    this(cdf, new Random());
  }

  CDFRandomGenerator(LoggedDiscreteCDF cdf, long seed) {
    this(cdf, new Random(seed));
  }

  private CDFRandomGenerator(LoggedDiscreteCDF cdf, Random random) {
    this.random = random;
    rankings = new double[cdf.getRankings().size() + 2];
    values = new long[cdf.getRankings().size() + 2];
    initializeTables(cdf);
  }

  protected final void initializeTables(LoggedDiscreteCDF cdf) {
    rankings[0] = 0.0;
    values[0] = cdf.getMinimum();
    rankings[rankings.length - 1] = 1.0;
    values[rankings.length - 1] = cdf.getMaximum();

    List<LoggedSingleRelativeRanking> subjects = cdf.getRankings();

    for (int i = 0; i < subjects.size(); ++i) {
      rankings[i + 1] = subjects.get(i).getRelativeRanking();
      values[i + 1] = subjects.get(i).getDatum();
    }
  }

  protected int floorIndex(double probe) {
    int result = Arrays.binarySearch(rankings, probe);

    return Math.abs(result + 1) - 1;
  }

  protected double getRankingAt(int index) {
    return rankings[index];
  }

  protected long getDatumAt(int index) {
    return values[index];
  }

  public long randomValue() {
    return valueAt(random.nextDouble());
  }

  public abstract long valueAt(double probability);
}
