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
package org.apache.hadoop.mapred;

import org.apache.hadoop.mapred.StatisticsCollector.TimeWindow;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * This class will change the number of jobs time windows 
 * of all task trackers <br/> 
 * Last Day time window will be changed from 24 hours to 2 minutes <br/> 
 * Last Hour time window will be changed from 1 hour to 1 minute <br/>
 */

public privileged aspect StatisticsCollectorAspect {

  //last day is changed to 120 seconds instead of 24 hours, 
  //with a 10 seconds refresh rate 
  static final TimeWindow 
    LAST_DAY_ASPECT = new TimeWindow("Last Day", 2 * 60, 10);

  //last day is changed to 60 seconds instead of 1 hour, 
  //with a 10 seconds refresh rate 
  static final TimeWindow 
    LAST_HOUR_ASPECT = new TimeWindow("Last Hour", 60, 10);

  private static final Log LOG = LogFactory
      .getLog(StatisticsCollectorAspect.class);

  pointcut createStatExecution(String name, TimeWindow[] window) : 
     call(* StatisticsCollector.createStat(String, TimeWindow[])) 
    && args(name, window);

  //This will change the timewindow to have last day and last hour changed.
  before(String name, TimeWindow[] window) : createStatExecution(name, window) {
      window[1] = LAST_DAY_ASPECT;
      window[2] = LAST_HOUR_ASPECT;
  }

}
