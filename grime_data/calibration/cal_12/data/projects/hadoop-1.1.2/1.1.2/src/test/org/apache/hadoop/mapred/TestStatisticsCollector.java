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

import junit.framework.TestCase;

import org.apache.hadoop.mapred.StatisticsCollector.TimeWindow;
import org.apache.hadoop.mapred.StatisticsCollector.Stat;

public class TestStatisticsCollector extends TestCase{

  public void testMovingWindow() throws Exception {
    StatisticsCollector collector = new StatisticsCollector(1);
    TimeWindow window = new TimeWindow("test", 6, 2);
    TimeWindow sincStart = StatisticsCollector.SINCE_START;
    TimeWindow[] windows = {sincStart, window};
    
    Stat stat = collector.createStat("m1", windows);
    
    stat.inc(3);
    collector.update();
    assertEquals(0, stat.getValues().get(window).getValue());
    assertEquals(3, stat.getValues().get(sincStart).getValue());
    
    stat.inc(3);
    collector.update();
    assertEquals((3+3), stat.getValues().get(window).getValue());
    assertEquals(6, stat.getValues().get(sincStart).getValue());
    
    stat.inc(10);
    collector.update();
    assertEquals((3+3), stat.getValues().get(window).getValue());
    assertEquals(16, stat.getValues().get(sincStart).getValue());
    
    stat.inc(10);
    collector.update();
    assertEquals((3+3+10+10), stat.getValues().get(window).getValue());
    assertEquals(26, stat.getValues().get(sincStart).getValue());
    
    stat.inc(10);
    collector.update();
    stat.inc(10);
    collector.update();
    assertEquals((3+3+10+10+10+10), stat.getValues().get(window).getValue());
    assertEquals(46, stat.getValues().get(sincStart).getValue());
    
    stat.inc(10);
    collector.update();
    assertEquals((3+3+10+10+10+10), stat.getValues().get(window).getValue());
    assertEquals(56, stat.getValues().get(sincStart).getValue());
    
    stat.inc(12);
    collector.update();
    assertEquals((10+10+10+10+10+12), stat.getValues().get(window).getValue());
    assertEquals(68, stat.getValues().get(sincStart).getValue());
    
    stat.inc(13);
    collector.update();
    assertEquals((10+10+10+10+10+12), stat.getValues().get(window).getValue());
    assertEquals(81, stat.getValues().get(sincStart).getValue());
    
    stat.inc(14);
    collector.update();
    assertEquals((10+10+10+12+13+14), stat.getValues().get(window).getValue());
    assertEquals(95, stat.getValues().get(sincStart).getValue());
  }

}
