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
 * Immutable tag for metrics (for grouping on host/queue/username etc.)
 */
public class MetricsTag {

  private final String name;
  private final String description;
  private final String value;

  /**
   * Construct the tag with name, description and value
   * @param name  of the tag
   * @param description of the tag
   * @param value of the tag
   */
  public MetricsTag(String name, String description, String value) {
    this.name = name;
    this.description = description;
    this.value = value;
  }

  /**
   * Get the name of the tag
   * @return  the name
   */
  public String name() {
    return name;
  }

  /**
   * Get the description of the tag
   * @return  the description
   */
  public String description() {
    return description;
  }

  /**
   * Get the value of the tag
   * @return  the value
   */
  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MetricsTag other = (MetricsTag) obj;
    if (!this.name.equals(other.name())) {
      return false;
    }
    if (!this.description.equals(other.description())) {
      return false;
    }
    if (this.value == null || other.value() == null) {
      if (this.value == null && other.value() == null) return true;
      return false;
    }
    if (!this.value.equals(other.value())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return name.hashCode() ^ (value == null ? 0 : value.hashCode());
  }

  @Override
  public String toString() {
    return "MetricsTag{" + "name='" + name + "' description='" + description +
           "' value='" + value + "'}";
  }

}
