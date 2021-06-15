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
package org.apache.hadoop.metrics2.sink.ganglia;

import org.apache.hadoop.metrics2.sink.ganglia.AbstractGangliaSink.GangliaSlope;

/**
 * class which is used to store ganglia properties
 */
class GangliaConf {
  private String units = AbstractGangliaSink.DEFAULT_UNITS;
  private GangliaSlope slope;
  private int dmax = AbstractGangliaSink.DEFAULT_DMAX;
  private int tmax = AbstractGangliaSink.DEFAULT_TMAX;

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append("unit=").append(units).append(", slope=").append(slope)
        .append(", dmax=").append(dmax).append(", tmax=").append(tmax);
    return buf.toString();
  }

  /**
   * @return the units
   */
  String getUnits() {
    return units;
  }

  /**
   * @param units the units to set
   */
  void setUnits(String units) {
    this.units = units;
  }

  /**
   * @return the slope
   */
  GangliaSlope getSlope() {
    return slope;
  }

  /**
   * @param slope the slope to set
   */
  void setSlope(GangliaSlope slope) {
    this.slope = slope;
  }

  /**
   * @return the dmax
   */
  int getDmax() {
    return dmax;
  }

  /**
   * @param dmax the dmax to set
   */
  void setDmax(int dmax) {
    this.dmax = dmax;
  }

  /**
   * @return the tmax
   */
  int getTmax() {
    return tmax;
  }

  /**
   * @param tmax the tmax to set
   */
  void setTmax(int tmax) {
    this.tmax = tmax;
  }
}