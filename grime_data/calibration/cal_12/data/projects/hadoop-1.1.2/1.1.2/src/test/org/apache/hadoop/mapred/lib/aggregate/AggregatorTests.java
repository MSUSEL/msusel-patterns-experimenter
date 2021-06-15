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
package org.apache.hadoop.mapred.lib.aggregate;

import org.apache.hadoop.io.Text;
import java.util.ArrayList;
import java.util.Map.Entry;

public class AggregatorTests extends ValueAggregatorBaseDescriptor {
  
  public ArrayList<Entry<Text, Text>> generateKeyValPairs(Object key, Object val) {
    ArrayList<Entry<Text, Text>> retv = new ArrayList<Entry<Text, Text>>();
    String [] words = val.toString().split(" ");
    
    String countType;
    String id;
    Entry<Text, Text> e;
    
    for (String word: words) {
      long numVal = Long.parseLong(word);
      countType = LONG_VALUE_SUM;
      id = "count_" + word;
      e = generateEntry(countType, id, ONE);
      if (e != null) {
        retv.add(e);
      }
      countType = LONG_VALUE_MAX;
      id = "max";
      e = generateEntry(countType, id, new Text(word));
      if (e != null) {
        retv.add(e);
      }
      
      countType = LONG_VALUE_MIN;
      id = "min";
      e = generateEntry(countType, id, new Text(word));
      if (e != null) {
        retv.add(e);
      }
      
      countType = STRING_VALUE_MAX;
      id = "value_as_string_max";
      e = generateEntry(countType, id, new Text(""+numVal));
      if (e != null) {
        retv.add(e);
      }
      
      countType = STRING_VALUE_MIN;
      id = "value_as_string_min";
      e = generateEntry(countType, id, new Text(""+numVal));
      if (e != null) {
        retv.add(e);
      }
      
      countType = UNIQ_VALUE_COUNT;
      id = "uniq_count";
      e = generateEntry(countType, id, new Text(word));
      if (e != null) {
        retv.add(e);
      }
      
      countType = VALUE_HISTOGRAM;
      id = "histogram";
      e = generateEntry(countType, id, new Text(word));
      if (e != null) {
        retv.add(e);
      }
    }
    return retv;
  }

}
