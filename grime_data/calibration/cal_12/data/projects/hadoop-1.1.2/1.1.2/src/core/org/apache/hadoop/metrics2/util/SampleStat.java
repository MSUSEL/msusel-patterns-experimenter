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
package org.apache.hadoop.metrics2.util;

/**
 * Helper to compute running sample stats
 */
public class SampleStat {

  private final MinMax minmax = new MinMax();
  private long numSamples = 0;
  private double a0, a1, s0, s1;

  /**
   * Construct a new running sample stat
   */
  public SampleStat() {
    a0 = s0 = 0.0;
  }

  public void reset() {
    numSamples = 0;
    a0 = s0 = 0.0;
    minmax.reset();
  }

  // We want to reuse the object, sometimes.
  void reset(long numSamples, double a0, double a1, double s0, double s1,
             MinMax minmax) {
    this.numSamples = numSamples;
    this.a0 = a0;
    this.a1 = a1;
    this.s0 = s0;
    this.s1 = s1;
    this.minmax.reset(minmax);
  }

  /**
   * Copy the values to other (saves object creation and gc.)
   * @param other the destination to hold our values
   */
  public void copyTo(SampleStat other) {
    other.reset(numSamples, a0, a1, s0, s1, minmax);
  }

  /**
   * Add a sample the running stat.
   * @param x the sample number
   * @return  self
   */
  public SampleStat add(double x) {
    minmax.add(x);
    return add(1, x);
  }

  /**
   * Add some sample and a partial sum to the running stat.
   * Note, min/max is not evaluated using this method.
   * @param nSamples  number of samples
   * @param x the partial sum
   * @return  self
   */
  public SampleStat add(long nSamples, double x) {
    numSamples += nSamples;

    if (numSamples == 1) {
      a0 = a1 = x;
      s0 = 0.0;
    }
    else {
      // The Welford method for numerical stability
      a1 = a0 + (x - a0) / numSamples;
      s1 = s0 + (x - a0) * (x - a1);
      a0 = a1;
      s0 = s1;
    }
    return this;
  }

  /**
   * @return  the total number of samples
   */
  public long numSamples() {
    return numSamples;
  }

  /**
   * @return  the arithmetic mean of the samples
   */
  public double mean() {
    return numSamples > 0 ? a1 : 0.0;
  }

  /**
   * @return  the variance of the samples
   */
  public double variance() {
    return numSamples > 1 ? s1 / (numSamples - 1) : 0.0;
  }

  /**
   * @return  the standard deviation of the samples
   */
  public double stddev() {
    return Math.sqrt(variance());
  }

  /**
   * @return  the minimum value of the samples
   */
  public double min() {
    return minmax.min();
  }

  /**
   * @return  the maximum value of the samples
   */
  public double max() {
    return minmax.max();
  }

  /**
   * Helper to keep running min/max
   */
  @SuppressWarnings("PublicInnerClass")
  public static class MinMax {

    // Float.MAX_VALUE is used rather than Double.MAX_VALUE, even though the
    // min and max variables are of type double.
    // Float.MAX_VALUE is big enough, and using Double.MAX_VALUE makes 
    // Ganglia core due to buffer overflow.
    // The same reasoning applies to the MIN_VALUE counterparts.
    static final double DEFAULT_MIN_VALUE = Float.MAX_VALUE;
    static final double DEFAULT_MAX_VALUE = Float.MIN_VALUE;

    private double min = DEFAULT_MIN_VALUE;
    private double max = DEFAULT_MAX_VALUE;

    public void add(double value) {
      if (value > max) max = value;
      if (value < min) min = value;
    }

    public double min() { return min; }
    public double max() { return max; }

    public void reset() {
      min = DEFAULT_MIN_VALUE;
      max = DEFAULT_MAX_VALUE;
    }

    public void reset(MinMax other) {
      min = other.min();
      max = other.max();
    }

  }

}
