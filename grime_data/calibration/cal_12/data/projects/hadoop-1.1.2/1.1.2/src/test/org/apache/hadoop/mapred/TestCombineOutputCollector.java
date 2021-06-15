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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.IFile.Writer;
import org.apache.hadoop.mapred.Task.CombineOutputCollector;
import org.apache.hadoop.mapred.Task.TaskReporter;
import org.junit.Test;

public class TestCombineOutputCollector {
  private CombineOutputCollector<String, Integer> coc;

  @Test
  public void testCustomCollect() throws Throwable {
    //mock creation
    TaskReporter mockTaskReporter = mock(TaskReporter.class);
    Counters.Counter outCounter = new Counters.Counter();
    Writer<String, Integer> mockWriter = mock(Writer.class);

    Configuration conf = new Configuration();
    conf.set("mapred.combine.recordsBeforeProgress", "2");
    
    coc = new CombineOutputCollector<String, Integer>(outCounter, mockTaskReporter, conf);
    coc.setWriter(mockWriter);
    verify(mockTaskReporter, never()).progress();

    coc.collect("dummy", 1);
    verify(mockTaskReporter, never()).progress();
    
    coc.collect("dummy", 2);
    verify(mockTaskReporter, times(1)).progress();
  }
  
  @Test
  public void testDefaultCollect() throws Throwable {
    //mock creation
    TaskReporter mockTaskReporter = mock(TaskReporter.class);
    Counters.Counter outCounter = new Counters.Counter();
    Writer<String, Integer> mockWriter = mock(Writer.class);

    Configuration conf = new Configuration();
    
    coc = new CombineOutputCollector<String, Integer>(outCounter, mockTaskReporter, conf);
    coc.setWriter(mockWriter);
    verify(mockTaskReporter, never()).progress();

    for(int i = 0; i < Task.DEFAULT_MR_COMBINE_RECORDS_BEFORE_PROGRESS; i++) {
    	coc.collect("dummy", i);
    }
    verify(mockTaskReporter, times(1)).progress();
    for(int i = 0; i < Task.DEFAULT_MR_COMBINE_RECORDS_BEFORE_PROGRESS; i++) {
    	coc.collect("dummy", i);
    }
    verify(mockTaskReporter, times(2)).progress();
  }
}
