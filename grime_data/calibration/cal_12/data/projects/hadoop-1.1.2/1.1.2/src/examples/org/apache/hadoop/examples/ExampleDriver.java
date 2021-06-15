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
package org.apache.hadoop.examples;

import org.apache.hadoop.examples.dancing.DistributedPentomino;
import org.apache.hadoop.examples.dancing.Sudoku;
import org.apache.hadoop.examples.terasort.TeraGen;
import org.apache.hadoop.examples.terasort.TeraSort;
import org.apache.hadoop.examples.terasort.TeraValidate;
import org.apache.hadoop.util.ProgramDriver;

/**
 * A description of an example program based on its class and a 
 * human-readable description.
 */
public class ExampleDriver {
  
  public static void main(String argv[]){
    int exitCode = -1;
    ProgramDriver pgd = new ProgramDriver();
    try {
      pgd.addClass("wordcount", WordCount.class, 
                   "A map/reduce program that counts the words in the input files.");
      pgd.addClass("aggregatewordcount", AggregateWordCount.class, 
                   "An Aggregate based map/reduce program that counts the words in the input files.");
      pgd.addClass("aggregatewordhist", AggregateWordHistogram.class, 
                   "An Aggregate based map/reduce program that computes the histogram of the words in the input files.");
      pgd.addClass("grep", Grep.class, 
                   "A map/reduce program that counts the matches of a regex in the input.");
      pgd.addClass("randomwriter", RandomWriter.class, 
                   "A map/reduce program that writes 10GB of random data per node.");
      pgd.addClass("randomtextwriter", RandomTextWriter.class, 
      "A map/reduce program that writes 10GB of random textual data per node.");
      pgd.addClass("sort", Sort.class, "A map/reduce program that sorts the data written by the random writer.");
      pgd.addClass("pi", PiEstimator.class, "A map/reduce program that estimates Pi using monte-carlo method.");
      pgd.addClass("pentomino", DistributedPentomino.class,
      "A map/reduce tile laying program to find solutions to pentomino problems.");
      pgd.addClass("secondarysort", SecondarySort.class,
                   "An example defining a secondary sort to the reduce.");
      pgd.addClass("sudoku", Sudoku.class, "A sudoku solver.");
      pgd.addClass("sleep", SleepJob.class, "A job that sleeps at each map and reduce task.");
      pgd.addClass("join", Join.class, "A job that effects a join over sorted, equally partitioned datasets");
      pgd.addClass("multifilewc", MultiFileWordCount.class, "A job that counts words from several files.");
      pgd.addClass("dbcount", DBCountPageView.class, "An example job that count the pageview counts from a database.");
      pgd.addClass("teragen", TeraGen.class, "Generate data for the terasort");
      pgd.addClass("terasort", TeraSort.class, "Run the terasort");
      pgd.addClass("teravalidate", TeraValidate.class, "Checking results of terasort");
      pgd.driver(argv);
      
      // Success
      exitCode = 0;
    }
    catch(Throwable e){
      e.printStackTrace();
    }
    
    System.exit(exitCode);
  }
}
	
