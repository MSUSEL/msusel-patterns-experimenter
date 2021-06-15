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
package org.apache.hadoop.test;

import org.apache.hadoop.util.ProgramDriver;
import org.apache.hadoop.mapred.BigMapOutput;
import org.apache.hadoop.mapred.GenericMRLoadGenerator;
import org.apache.hadoop.mapred.MRBench;
import org.apache.hadoop.mapred.ReliabilityTest;
import org.apache.hadoop.mapred.SortValidator;
import org.apache.hadoop.mapred.TestMapRed;
import org.apache.hadoop.mapred.TestSequenceFileInputFormat;
import org.apache.hadoop.mapred.TestTextInputFormat;
import org.apache.hadoop.hdfs.BenchmarkThroughput;
import org.apache.hadoop.hdfs.NNBench;
import org.apache.hadoop.fs.DistributedFSCheck;
import org.apache.hadoop.fs.TestDFSIO;
import org.apache.hadoop.fs.DFSCIOTest;
import org.apache.hadoop.fs.TestFileSystem;
import org.apache.hadoop.io.FileBench;
import org.apache.hadoop.io.TestArrayFile;
import org.apache.hadoop.io.TestSequenceFile;
import org.apache.hadoop.io.TestSetFile;
import org.apache.hadoop.ipc.TestIPC;
import org.apache.hadoop.ipc.TestRPC;
import org.apache.hadoop.mapred.ThreadedMapBenchmark;

public class AllTestDriver {
  
  /**
   * A description of the test program for running all the tests using jar file
   */
  public static void main(String argv[]){
    ProgramDriver pgd = new ProgramDriver();
    try {
      pgd.addClass("threadedmapbench", ThreadedMapBenchmark.class, 
                   "A map/reduce benchmark that compares the performance " + 
                   "of maps with multiple spills over maps with 1 spill");
      pgd.addClass("mrbench", MRBench.class, "A map/reduce benchmark that can create many small jobs");
      pgd.addClass("nnbench", NNBench.class, "A benchmark that stresses the namenode.");
      pgd.addClass("mapredtest", TestMapRed.class, "A map/reduce test check.");
      pgd.addClass("testfilesystem", TestFileSystem.class, "A test for FileSystem read/write.");
      pgd.addClass("testsequencefile", TestSequenceFile.class, "A test for flat files of binary key value pairs.");
      pgd.addClass("testsetfile", TestSetFile.class, "A test for flat files of binary key/value pairs.");
      pgd.addClass("testarrayfile", TestArrayFile.class, "A test for flat files of binary key/value pairs.");
      pgd.addClass("testrpc", TestRPC.class, "A test for rpc.");
      pgd.addClass("testipc", TestIPC.class, "A test for ipc.");
      pgd.addClass("testsequencefileinputformat", TestSequenceFileInputFormat.class, "A test for sequence file input format.");
      pgd.addClass("testtextinputformat", TestTextInputFormat.class, "A test for text input format.");
      pgd.addClass("TestDFSIO", TestDFSIO.class, "Distributed i/o benchmark.");
      pgd.addClass("DFSCIOTest", DFSCIOTest.class, "Distributed i/o benchmark of libhdfs.");
      pgd.addClass("DistributedFSCheck", DistributedFSCheck.class, "Distributed checkup of the file system consistency.");
      pgd.addClass("testmapredsort", SortValidator.class, 
                   "A map/reduce program that validates the map-reduce framework's sort.");
      pgd.addClass("testbigmapoutput", BigMapOutput.class, 
                   "A map/reduce program that works on a very big " + 
                   "non-splittable file and does identity map/reduce");
      pgd.addClass("loadgen", GenericMRLoadGenerator.class, "Generic map/reduce load generator");
      pgd.addClass("filebench", FileBench.class, "Benchmark SequenceFile(Input|Output)Format (block,record compressed and uncompressed), Text(Input|Output)Format (compressed and uncompressed)");
      pgd.addClass("dfsthroughput", BenchmarkThroughput.class, 
                   "measure hdfs throughput");
      pgd.addClass("MRReliabilityTest", ReliabilityTest.class,
          "A program that tests the reliability of the MR framework by " +
          "injecting faults/failures");
      pgd.driver(argv);
    } catch(Throwable e) {
      e.printStackTrace();
    }
  }
}

