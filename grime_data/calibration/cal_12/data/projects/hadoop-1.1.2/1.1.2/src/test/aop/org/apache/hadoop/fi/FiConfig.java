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
package org.apache.hadoop.fi;

import org.apache.hadoop.conf.Configuration;

/**
 * This class wraps the logic around fault injection configuration file
 * Default file is expected to be found in src/test/fi-site.xml
 * This default file should be copied by JUnit Ant's tasks to 
 * build/test/extraconf folder before tests are ran
 * An alternative location can be set through
 *   -Dfi.config=<file_name>
 */
public class FiConfig {
  private static final String CONFIG_PARAMETER = ProbabilityModel.FPROB_NAME + "config";
  private static final String DEFAULT_CONFIG = "fi-site.xml";
  private static Configuration conf;
  static {
    if (conf == null) {
      conf = new Configuration(false);
      String configName = System.getProperty(CONFIG_PARAMETER, DEFAULT_CONFIG);
      conf.addResource(configName);
    }
  }
  
  /**
   * Method provides access to local Configuration 
   * 
   * @return Configuration initialized with fault injection's parameters
   */
  public static Configuration getConfig() {
    return conf;
  }
}
