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

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;

/**
 * This class is responsible for the decision of when a fault 
 * has to be triggered within a class of Hadoop
 * 
 *  Default probability of injection is set to 0%. To change it
 *  one can set the sys. prop. -Dfi.*=<new probability level>
 *  Another way to do so is to set this level through FI config file,
 *  located under src/test/fi-site.conf
 *  
 *  To change the level one has to specify the following sys,prop.:
 *  -Dfi.<name of fault location>=<probability level> in the runtime
 *  Probability level is specified by a float between 0.0 and 1.0
 *  
 *  <name of fault location> might be represented by a short classname
 *  or otherwise. This decision is left up to the discretion of aspects
 *  developer, but has to be consistent through the code 
 */
public class ProbabilityModel {
  private static Random generator = new Random();
  private static final Log LOG = LogFactory.getLog(ProbabilityModel.class);

  static final String FPROB_NAME = "fi.";
  private static final String ALL_PROBABILITIES = FPROB_NAME + "*";
  private static final float DEFAULT_PROB = 0.00f; //Default probability is 0%
  private static final float MAX_PROB = 1.00f; // Max probability is 100%

  private static Configuration conf = FiConfig.getConfig();

  static {
    // Set new default probability if specified through a system.property
    // If neither is specified set default probability to DEFAULT_PROB 
    conf.set(ALL_PROBABILITIES, 
        System.getProperty(ALL_PROBABILITIES, 
            conf.get(ALL_PROBABILITIES, Float.toString(DEFAULT_PROB))));

    LOG.info(ALL_PROBABILITIES + "=" + conf.get(ALL_PROBABILITIES));
  }

  /**
   * Simplistic method to check if we have reached the point of injection
   * @param klassName is the name of the probability level to check. 
   *  If a configuration has been set for "fi.myClass" then you can check if the
   *  inject criteria has been reached by calling this method with "myClass"
   *  string as its parameter
   * @return true if the probability threshold has been reached; false otherwise
   */
  public static boolean injectCriteria(String klassName) {
    boolean trigger = false;
    if (generator.nextFloat() < getProbability(klassName)) {
      trigger = true;
    }
    return trigger;
  }

  /**
   * This primitive checks for arbitrary set of desired probability. If the 
   * level hasn't been set method will return default setting.
   * The probability expected to be set as an float between 0.0 and 1.0
   * @param klass is the name of the resource
   * @return float representation of configured probability level of 
   *  the requested resource or default value if hasn't been set
   */
  protected static float getProbability(final String klass) {
    String newProbName = FPROB_NAME + klass;

    String newValue = System.getProperty(newProbName, conf.get(ALL_PROBABILITIES));
    if (newValue != null && !newValue.equals(conf.get(newProbName)))
      conf.set(newProbName, newValue);

    float ret = conf.getFloat(newProbName,
        conf.getFloat(ALL_PROBABILITIES, DEFAULT_PROB));
    LOG.debug("Request for " + newProbName + " returns=" + ret);
    // Make sure that probability level is valid.
    if (ret < DEFAULT_PROB || ret > MAX_PROB) {
      LOG.info("Probability level is incorrect. Default value is set");
      ret = conf.getFloat(ALL_PROBABILITIES, DEFAULT_PROB);
    }
    
    return ret;
  }
}
