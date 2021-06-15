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
package org.apache.hadoop.examples.dancing;

import java.io.*;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.IdentityReducer;
import org.apache.hadoop.util.*;

/**
 * Launch a distributed pentomino solver.
 * It generates a complete list of prefixes of length N with each unique prefix
 * as a separate line. A prefix is a sequence of N integers that denote the 
 * index of the row that is choosen for each column in order. Note that the
 * next column is heuristically choosen by the solver, so it is dependant on
 * the previous choice. That file is given as the input to
 * map/reduce. The output key/value are the move prefix/solution as Text/Text.
 */
public class DistributedPentomino extends Configured implements Tool {

  /**
   * Each map takes a line, which represents a prefix move and finds all of 
   * the solutions that start with that prefix. The output is the prefix as
   * the key and the solution as the value.
   */
  public static class PentMap extends MapReduceBase
    implements Mapper<WritableComparable, Text, Text, Text> {
    
    private int width;
    private int height;
    private int depth;
    private Pentomino pent;
    private Text prefixString;
    private OutputCollector<Text, Text> output;
    private Reporter reporter;
    
    /**
     * For each solution, generate the prefix and a string representation
     * of the solution. The solution starts with a newline, so that the output
     * looks like:
     * <prefix>,
     * <solution>
     * 
     */
    class SolutionCatcher 
    implements DancingLinks.SolutionAcceptor<Pentomino.ColumnName> {
      public void solution(List<List<Pentomino.ColumnName>> answer) {
        String board = Pentomino.stringifySolution(width, height, answer);
        try {
          output.collect(prefixString, new Text("\n" + board));
          reporter.incrCounter(pent.getCategory(answer), 1);
        } catch (IOException e) {
          System.err.println(StringUtils.stringifyException(e));
        }
      }
    }
    
    /**
     * Break the prefix string into moves (a sequence of integer row ids that 
     * will be selected for each column in order). Find all solutions with
     * that prefix.
     */
    public void map(WritableComparable key, Text value,
                    OutputCollector<Text, Text> output, Reporter reporter
                    ) throws IOException {
      this.output = output;
      this.reporter = reporter;
      prefixString = value;
      StringTokenizer itr = new StringTokenizer(prefixString.toString(), ",");
      int[] prefix = new int[depth];
      int idx = 0;
      while (itr.hasMoreTokens()) {
        String num = itr.nextToken();
        prefix[idx++] = Integer.parseInt(num);
      }
      pent.solve(prefix);
    }
    
    @Override
    public void configure(JobConf conf) {
      depth = conf.getInt("pent.depth", -1);
      width = conf.getInt("pent.width", -1);
      height = conf.getInt("pent.height", -1);
      pent = (Pentomino) 
        ReflectionUtils.newInstance(conf.getClass("pent.class", 
                                                  OneSidedPentomino.class), 
                                    conf);
      pent.initialize(width, height);
      pent.setPrinter(new SolutionCatcher());
    }
  }
  
  /**
   * Create the input file with all of the possible combinations of the 
   * given depth.
   * @param fs the filesystem to write into
   * @param dir the directory to write the input file into
   * @param pent the puzzle 
   * @param depth the depth to explore when generating prefixes
   */
  private static void createInputDirectory(FileSystem fs, 
                                           Path dir,
                                           Pentomino pent,
                                           int depth
                                           ) throws IOException {
    fs.mkdirs(dir);
    List<int[]> splits = pent.getSplits(depth);
    PrintStream file = 
      new PrintStream(new BufferedOutputStream
                      (fs.create(new Path(dir, "part1")), 64*1024));
    for(int[] prefix: splits) {
      for(int i=0; i < prefix.length; ++i) {
        if (i != 0) {
          file.print(',');          
        }
        file.print(prefix[i]);
      }
      file.print('\n');
    }
    file.close();
  }
  
  /**
   * Launch the solver on 9x10 board and the one sided pentominos.
   * This takes about 2.5 hours on 20 nodes with 2 cpus/node.
   * Splits the job into 2000 maps and 1 reduce.
   */
  public static void main(String[] args) throws Exception {
    int res = ToolRunner.run(new Configuration(), new DistributedPentomino(), args);
    System.exit(res);
  }

  public int run(String[] args) throws Exception {
    JobConf conf;
    int depth = 5;
    int width = 9;
    int height = 10;
    Class<? extends Pentomino> pentClass;
    if (args.length == 0) {
      System.out.println("pentomino <output>");
      ToolRunner.printGenericCommandUsage(System.out);
      return -1;
    }
    
    conf = new JobConf(getConf());
    width = conf.getInt("pent.width", width);
    height = conf.getInt("pent.height", height);
    depth = conf.getInt("pent.depth", depth);
    pentClass = conf.getClass("pent.class", OneSidedPentomino.class, Pentomino.class);
    
    Path output = new Path(args[0]);
    Path input = new Path(output + "_input");
    FileSystem fileSys = FileSystem.get(conf);
    try {
      FileInputFormat.setInputPaths(conf, input);
      FileOutputFormat.setOutputPath(conf, output);
      conf.setJarByClass(PentMap.class);
      
      conf.setJobName("dancingElephant");
      Pentomino pent = ReflectionUtils.newInstance(pentClass, conf);
      pent.initialize(width, height);
      createInputDirectory(fileSys, input, pent, depth);
   
      // the keys are the prefix strings
      conf.setOutputKeyClass(Text.class);
      // the values are puzzle solutions
      conf.setOutputValueClass(Text.class);
      
      conf.setMapperClass(PentMap.class);        
      conf.setReducerClass(IdentityReducer.class);
      
      conf.setNumMapTasks(2000);
      conf.setNumReduceTasks(1);
      
      JobClient.runJob(conf);
      } finally {
      fileSys.delete(input, true);
    }
    return 0;
  }

}
