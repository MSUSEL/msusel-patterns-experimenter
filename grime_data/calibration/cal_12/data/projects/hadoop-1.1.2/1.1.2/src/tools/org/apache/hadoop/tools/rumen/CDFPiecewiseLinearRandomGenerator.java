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

public class CDFPiecewiseLinearRandomGenerator extends CDFRandomGenerator {

  /**
   * @param cdf
   *          builds a CDFRandomValue engine around this
   *          {@link LoggedDiscreteCDF}, with a defaultly seeded RNG
   */
  public CDFPiecewiseLinearRandomGenerator(LoggedDiscreteCDF cdf) {
    super(cdf);
  }

  /**
   * @param cdf
   *          builds a CDFRandomValue engine around this
   *          {@link LoggedDiscreteCDF}, with an explicitly seeded RNG
   * @param seed
   *          the random number generator seed
   */
  public CDFPiecewiseLinearRandomGenerator(LoggedDiscreteCDF cdf, long seed) {
    super(cdf, seed);
  }

  /**
   * TODO This code assumes that the empirical minimum resp. maximum is the
   * epistomological minimum resp. maximum. This is probably okay for the
   * minimum, because that likely represents a task where everything went well,
   * but for the maximum we may want to develop a way of extrapolating past the
   * maximum.
   */
  @Override
  public long valueAt(double probability) {
    int rangeFloor = floorIndex(probability);

    double segmentProbMin = getRankingAt(rangeFloor);
    double segmentProbMax = getRankingAt(rangeFloor + 1);

    long segmentMinValue = getDatumAt(rangeFloor);
    long segmentMaxValue = getDatumAt(rangeFloor + 1);

    // If this is zero, this object is based on an ill-formed cdf
    double segmentProbRange = segmentProbMax - segmentProbMin;
    long segmentDatumRange = segmentMaxValue - segmentMinValue;

    long result = (long) ((probability - segmentProbMin) / segmentProbRange * segmentDatumRange)
        + segmentMinValue;

    return result;
  }
}
