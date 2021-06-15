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
package org.apache.hadoop.fs.shell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parse the args of a command and check the format of args.
 */
public class CommandFormat {
  final String name;
  final int minPar, maxPar;
  final Map<String, Boolean> options = new HashMap<String, Boolean>();

  /** constructor */
  public CommandFormat(String n, int min, int max, String ... possibleOpt) {
    name = n;
    minPar = min;
    maxPar = max;
    for(String opt : possibleOpt)
      options.put(opt, Boolean.FALSE);
  }

  /** Parse parameters starting from the given position
   * 
   * @param args an array of input arguments
   * @param pos the position at which starts to parse
   * @return a list of parameters
   */
  public List<String> parse(String[] args, int pos) {
    List<String> parameters = new ArrayList<String>();
    for(; pos < args.length; pos++) {
      if (args[pos].charAt(0) == '-' && args[pos].length() > 1) {
        String opt = args[pos].substring(1);
        if (options.containsKey(opt))
          options.put(opt, Boolean.TRUE);
        else
          throw new IllegalArgumentException("Illegal option " + args[pos]);
      }
      else
        parameters.add(args[pos]);
    }
    int psize = parameters.size();
    if (psize < minPar || psize > maxPar)
      throw new IllegalArgumentException("Illegal number of arguments");
    return parameters;
  }
  
  /** Return if the option is set or not
   * 
   * @param option String representation of an option
   * @return true is the option is set; false otherwise
   */
  public boolean getOpt(String option) {
    return options.get(option);
  }
}
