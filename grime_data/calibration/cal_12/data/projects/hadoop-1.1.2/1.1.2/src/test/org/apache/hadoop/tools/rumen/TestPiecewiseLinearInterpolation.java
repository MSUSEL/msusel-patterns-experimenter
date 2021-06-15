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

import org.junit.Test;
import static org.junit.Assert.*;

public class TestPiecewiseLinearInterpolation {

  static private double maximumRelativeError = 0.002D;

  static private LoggedSingleRelativeRanking makeRR(double ranking, long datum) {
    LoggedSingleRelativeRanking result = new LoggedSingleRelativeRanking();

    result.setDatum(datum);
    result.setRelativeRanking(ranking);

    return result;
  }

  @Test
  public void testOneRun() {
    LoggedDiscreteCDF input = new LoggedDiscreteCDF();

    input.setMinimum(100000L);
    input.setMaximum(1100000L);

    ArrayList<LoggedSingleRelativeRanking> rankings = new ArrayList<LoggedSingleRelativeRanking>();

    rankings.add(makeRR(0.1, 200000L));
    rankings.add(makeRR(0.5, 800000L));
    rankings.add(makeRR(0.9, 1000000L));

    input.setRankings(rankings);
    input.setNumberValues(3);

    CDFRandomGenerator gen = new CDFPiecewiseLinearRandomGenerator(input);
    Histogram values = new Histogram();

    for (int i = 0; i < 1000000; ++i) {
      long value = gen.randomValue();
      values.enter(value);
    }

    /*
     * Now we build a percentiles CDF, and compute the sum of the squares of the
     * actual percentiles vrs. the predicted percentiles
     */
    int[] percentiles = new int[99];

    for (int i = 0; i < 99; ++i) {
      percentiles[i] = i + 1;
    }

    long[] result = values.getCDF(100, percentiles);
    long sumErrorSquares = 0L;

    for (int i = 0; i < 10; ++i) {
      long error = result[i] - (10000L * i + 100000L);
      System.out.println("element " + i + ", got " + result[i] + ", expected "
          + (10000L * i + 100000L) + ", error = " + error);
      sumErrorSquares += error * error;
    }

    for (int i = 10; i < 50; ++i) {
      long error = result[i] - (15000L * i + 50000L);
      System.out.println("element " + i + ", got " + result[i] + ", expected "
          + (15000L * i + 50000L) + ", error = " + error);
      sumErrorSquares += error * error;
    }

    for (int i = 50; i < 90; ++i) {
      long error = result[i] - (5000L * i + 550000L);
      System.out.println("element " + i + ", got " + result[i] + ", expected "
          + (5000L * i + 550000L) + ", error = " + error);
      sumErrorSquares += error * error;
    }

    for (int i = 90; i <= 100; ++i) {
      long error = result[i] - (10000L * i + 100000L);
      System.out.println("element " + i + ", got " + result[i] + ", expected "
          + (10000L * i + 100000L) + ", error = " + error);
      sumErrorSquares += error * error;
    }

    // normalize the error
    double realSumErrorSquares = (double) sumErrorSquares;

    double normalizedError = realSumErrorSquares / 100
        / rankings.get(1).getDatum() / rankings.get(1).getDatum();
    double RMSNormalizedError = Math.sqrt(normalizedError);

    System.out.println("sumErrorSquares = " + sumErrorSquares);

    System.out.println("normalizedError: " + normalizedError
        + ", RMSNormalizedError: " + RMSNormalizedError);

    System.out.println("Cumulative error is " + RMSNormalizedError);

    assertTrue("The RMS relative error per bucket, " + RMSNormalizedError
        + ", exceeds our tolerance of " + maximumRelativeError,
        RMSNormalizedError <= maximumRelativeError);

  }
}
