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
package org.apache.hadoop.mapred.gridmix;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapred.gridmix.Gridmix;
import org.apache.hadoop.mapred.gridmix.test.system.GridMixConfig;
import org.apache.hadoop.mapred.gridmix.test.system.GridMixRunMode;
import org.apache.hadoop.mapred.gridmix.test.system.UtilsForGridmix;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * Verify the Gridmix generated input if compression emulation turn on.
 */
public class TestGridmixCompressedInputGeneration 
    extends GridmixSystemTestCase { 

  private static final Log LOG = 
      LogFactory.getLog("TestGridmixCompressedInputGeneration.class");

  /**
   * Generate input data and verify whether input files are compressed
   * or not.
   * @throws Exception - if an error occurs.
   */
  @Test
  public void testGridmixCompressionInputGeneration() throws Exception {
    final long inputSizeInMB = 1024 * 7;
    final String [] runtimeValues = {"LOADJOB",
                                     SubmitterUserResolver.class.getName(),
                                     "STRESS",
                                     inputSizeInMB  + "m",
                                     "file:///dev/null"};
    final String [] otherArgs = { 
        "-D", GridMixConfig.GRIDMIX_DISTCACHE_ENABLE + "=false",
        "-D", GridMixConfig.GRIDMIX_COMPRESSION_ENABLE + "=true"
    };
    LOG.info("Verify the generated compressed input data.");
    runAndVerify(true, inputSizeInMB, runtimeValues, otherArgs);
  }

  /**
   * Disable compression emulation and verify whether input files are 
   * compressed or not.
   * @throws Exception
   */
  @Test
  public void testGridmixInputGenerationWithoutCompressionEnable() 
      throws Exception { 
    UtilsForGridmix.cleanup(gridmixDir, rtClient.getDaemonConf());
    final long inputSizeInMB = 1024 * 6;
    final String [] runtimeValues = {"LOADJOB",
                                     SubmitterUserResolver.class.getName(),
                                     "STRESS",
                                     inputSizeInMB + "m",
                                     "file:///dev/null"};
    final String [] otherArgs = {
        "-D", GridMixConfig.GRIDMIX_DISTCACHE_ENABLE + "=false",
        "-D", GridMixConfig.GRIDMIX_COMPRESSION_ENABLE + "=false"
    };

    LOG.info("Verify the generated uncompressed input data.");
    runAndVerify(false, inputSizeInMB, runtimeValues, otherArgs);
  }
  
  private void runAndVerify(boolean isCompressed, long INPUT_SIZE, 
      String [] runtimeValues, String [] otherArgs) throws Exception { 
    int exitCode = 
        UtilsForGridmix.runGridmixJob(gridmixDir, conf, 
                                      GridMixRunMode.DATA_GENERATION.getValue(),
                                      runtimeValues,otherArgs);
    Assert.assertEquals("Data generation has failed.", 0, exitCode);
    verifyJobStatus();
    verifyInputDataSize(INPUT_SIZE);
    verifyInputFiles(isCompressed);
  }
  
  private void verifyInputFiles(boolean isCompressed) throws IOException { 
    List<String> inputFiles = 
        getInputFiles(conf, Gridmix.getGridmixInputDataPath(gridmixDir));
    for (String inputFile: inputFiles) {
      boolean fileStatus = (inputFile.contains(".gz") 
                         || inputFile.contains(".tgz")
                         || inputFile.contains(".deflate"))? true : false;
      if (isCompressed) { 
        Assert.assertTrue("Compressed input split file was not found.",
                          fileStatus);
      } else {
        Assert.assertFalse("Uncompressed input split file was not found.",
                           fileStatus);
      }
    }
  }

  private void verifyInputDataSize(long INPUT_SIZE) throws IOException {
    long actDataSize = 
        getInputDataSizeInMB(conf, Gridmix.getGridmixInputDataPath(gridmixDir));
    double ratio = ((double)actDataSize)/INPUT_SIZE;
    long expDataSize = (long)(INPUT_SIZE * ratio);
    Assert.assertEquals("Generated data has not matched with given size.", 
                        expDataSize, actDataSize);
  }

  private void verifyJobStatus() throws IOException { 
    JobClient jobClient = jtClient.getClient();
    int len = jobClient.getAllJobs().length;
    LOG.info("Verify the job status after completion of job...");
    Assert.assertEquals("Job has not succeeded.", JobStatus.SUCCEEDED, 
                        jobClient.getAllJobs()[len -1].getRunState());
  }

  private long getInputDataSizeInMB(Configuration conf, Path inputDir) 
      throws IOException { 
    FileSystem fs = inputDir.getFileSystem(conf);
    ContentSummary csmry = fs.getContentSummary(inputDir);
    long dataSize = csmry.getLength();
    dataSize = dataSize/(1024 * 1024);
    return dataSize;
  }

  private List<String> getInputFiles(Configuration conf, Path inputDir) 
      throws IOException {
    FileSystem fs = inputDir.getFileSystem(conf);
    FileStatus [] listStatus = fs.listStatus(inputDir);
    List<String> files = new ArrayList<String>();
    for (FileStatus fileStat : listStatus) {
      files.add(getInputFile(fileStat, conf));
    }
    return files;
  }

  private String getInputFile(FileStatus fstatus, Configuration conf) 
      throws IOException {
    String fileName = null;
    if (!fstatus.isDir()) {
      fileName = fstatus.getPath().getName();
      LOG.info("fileName++++:" + fileName);
    } else {
      FileSystem fs = fstatus.getPath().getFileSystem(conf);
      FileStatus [] listStatus = fs.listStatus(fstatus.getPath());
      for (FileStatus fileStat : listStatus) {
         return getInputFile(fileStat, conf);
      }
    }
    return fileName;
  }
}

