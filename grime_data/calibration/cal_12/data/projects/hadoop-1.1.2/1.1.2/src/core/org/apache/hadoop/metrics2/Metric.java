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
package org.apache.hadoop.metrics2;

/**
 * The immutable metric
 */
public abstract class Metric {

  public static final String NO_DESCRIPTION = "<<no description>>";
  private final String name;
  private final String description;

  /**
   * Construct the metric with name only
   * @param name  of the metric
   */
  public Metric(String name) {
    this.name = name;
    this.description = NO_DESCRIPTION;
  }

  /**
   * Construct the metric with a name and a description
   * @param name  of the metric
   * @param desc  description of the metric
   */
  public Metric(String name, String desc) {
    this.name = name;
    this.description = desc;
  }

  /**
   * Get the name of the metric
   * @return  the name
   */
  public String name() {
    return name;
  }

  /**
   * Get the description of the metric
   * @return  the description
   */
  public String description() {
    return description;
  }

  /**
   * Get the value of the metric
   * @return  the value of the metric
   */
  public abstract Number value();

  /**
   * Accept a visitor interface
   * @param visitor of the metric
   */
  public abstract void visit(MetricsVisitor visitor);

  // Mostly for testing
  @Override public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Metric other = (Metric) obj;
    if (!this.name.equals(other.name())) {
      return false;
    }
    if (!this.description.equals(other.description())) {
      return false;
    }
    if (!value().equals(other.value())) {
      return false;
    }
    return true;
  }

  @Override public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return "Metric{" + "name='" + name + "' description='" + description +
           "' value="+ value() +'}';
  }

}
