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

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class CombinerJobCreator {

   public static class MapClass extends MapReduceBase
     implements Mapper<LongWritable, Text, Text, IntWritable> {

     private final static IntWritable one = new IntWritable(1);
     private Text word = new Text();

     public void map(LongWritable key, Text value,
                     OutputCollector<Text, IntWritable> output,
                     Reporter reporter) throws IOException {
       String line = value.toString();
       StringTokenizer itr = new StringTokenizer(line);
       while (itr.hasMoreTokens()) {
         word.set(itr.nextToken());
         output.collect(word, one);
       }
     }
   }

   public static class Reduce extends MapReduceBase
     implements Reducer<Text, IntWritable, Text, IntWritable> {

     public void reduce(Text key, Iterator<IntWritable> values,
                        OutputCollector<Text, IntWritable> output,
                        Reporter reporter) throws IOException {
       int sum = 0;
       while (values.hasNext()) {
         sum += values.next().get();
       }
       output.collect(key, new IntWritable(sum));
     }
   }

  public static JobConf createJob(String[] args) throws Exception {
    JobConf conf = new JobConf(CombinerJobCreator.class);
    conf.setJobName("GridmixCombinerJob");

    // the keys are words (strings)
    conf.setOutputKeyClass(Text.class);
    // the values are counts (ints)
    conf.setOutputValueClass(IntWritable.class);

    conf.setMapperClass(MapClass.class);
    conf.setCombinerClass(Reduce.class);
    conf.setReducerClass(Reduce.class);
    boolean mapoutputCompressed = false;
    boolean outputCompressed = false;
    // List<String> other_args = new ArrayList<String>();
    for (int i = 0; i < args.length; ++i) {
      try {
        if ("-r".equals(args[i])) {
          conf.setNumReduceTasks(Integer.parseInt(args[++i]));
        } else if ("-indir".equals(args[i])) {
          FileInputFormat.setInputPaths(conf, args[++i]);
        } else if ("-outdir".equals(args[i])) {
          FileOutputFormat.setOutputPath(conf, new Path(args[++i]));

        } else if ("-mapoutputCompressed".equals(args[i])) {
          mapoutputCompressed = Boolean.valueOf(args[++i]).booleanValue();
        } else if ("-outputCompressed".equals(args[i])) {
          outputCompressed = Boolean.valueOf(args[++i]).booleanValue();
        }
      } catch (NumberFormatException except) {
        System.out.println("ERROR: Integer expected instead of " + args[i]);
        return null;
      } catch (ArrayIndexOutOfBoundsException except) {
        System.out.println("ERROR: Required parameter missing from "
            + args[i - 1]);
        return null;
      }
    }
    conf.setCompressMapOutput(mapoutputCompressed);
    conf.setBoolean("mapred.output.compress", outputCompressed);
    return conf;
  }
}
