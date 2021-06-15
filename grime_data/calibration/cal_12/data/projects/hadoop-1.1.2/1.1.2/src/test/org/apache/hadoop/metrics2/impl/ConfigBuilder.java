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
package org.apache.hadoop.metrics2.impl;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SubsetConfiguration;

/**
 * Helper class for building configs, mostly used in tests
 */
public class ConfigBuilder {

  /** The built config */
  public final PropertiesConfiguration config;

  /**
   * Default constructor
   */
  public ConfigBuilder() {
    config = new PropertiesConfiguration();
  }

  /**
   * Add a property to the config
   * @param key of the property
   * @param value of the property
   * @return self
   */
  public ConfigBuilder add(String key, Object value) {
    config.addProperty(key, value);
    return this;
  }

  /**
   * Save the config to a file
   * @param filename  to save
   * @return self
   * @throws RuntimeException
   */
  public ConfigBuilder save(String filename) {
    try {
      config.save(filename);
    }
    catch (Exception e) {
      throw new RuntimeException("Error saving config", e);
    }
    return this;
  }

  /**
   * Return a subset configuration (so getParent() can be used.)
   * @param prefix  of the subset
   * @return the subset config
   */
  public SubsetConfiguration subset(String prefix) {
    return new SubsetConfiguration(config, prefix, ".");
  }
}

