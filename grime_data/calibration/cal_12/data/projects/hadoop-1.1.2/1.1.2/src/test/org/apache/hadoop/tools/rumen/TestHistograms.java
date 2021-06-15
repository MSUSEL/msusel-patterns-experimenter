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
package org.apache.hadoop.tools.rumen;
import java.io.IOException;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestHistograms {

  /**
   * @throws IOException
   * 
   *           There should be files in the directory named by
   *           ${test.build.data}/rumen/histogram-test .
   * 
   *           There will be pairs of files, inputXxx.json and goldXxx.json .
   * 
   *           We read the input file as a HistogramRawTestData in json. Then we
   *           create a Histogram using the data field, and then a
   *           LoggedDiscreteCDF using the percentiles and scale field. Finally,
   *           we read the corresponding goldXxx.json as a LoggedDiscreteCDF and
   *           deepCompare them.
   */
  @Test
  public void testHistograms() throws IOException {
    final Configuration conf = new Configuration();
    final FileSystem lfs = FileSystem.getLocal(conf);
    final Path rootInputDir = new Path(
        System.getProperty("test.tools.input.dir", "")).makeQualified(lfs);
    final Path rootInputFile = new Path(rootInputDir, "rumen/histogram-tests");


    FileStatus[] tests = lfs.listStatus(rootInputFile);

    for (int i = 0; i < tests.length; ++i) {
      Path filePath = tests[i].getPath();
      String fileName = filePath.getName();
      if (fileName.startsWith("input")) {
        String testName = fileName.substring("input".length());
        Path goldFilePath = new Path(rootInputFile, "gold"+testName);
        assertTrue("Gold file dies not exist", lfs.exists(goldFilePath));
        LoggedDiscreteCDF newResult = histogramFileToCDF(filePath, lfs);
        System.out.println("Testing a Histogram for " + fileName);
        FSDataInputStream goldStream = lfs.open(goldFilePath);
        JsonObjectMapperParser<LoggedDiscreteCDF> parser = new JsonObjectMapperParser<LoggedDiscreteCDF>(
            goldStream, LoggedDiscreteCDF.class); 
        try {
          LoggedDiscreteCDF dcdf = parser.getNext();
          dcdf.deepCompare(newResult, new TreePath(null, "<root>"));
        } catch (DeepInequalityException e) {
          fail(e.path.toString());
        }
        finally {
            parser.close();
        }
      }
    }
  }

  private static LoggedDiscreteCDF histogramFileToCDF(Path path, FileSystem fs)
      throws IOException {
    FSDataInputStream dataStream = fs.open(path);
    JsonObjectMapperParser<HistogramRawTestData> parser = new JsonObjectMapperParser<HistogramRawTestData>(
        dataStream, HistogramRawTestData.class);
    HistogramRawTestData data;
    try {
      data = parser.getNext();
    } finally {
      parser.close();
    }
    
    Histogram hist = new Histogram();
    List<Long> measurements = data.getData();
    List<Long> typeProbeData = new HistogramRawTestData().getData();

    assertTrue(
        "The data attribute of a jackson-reconstructed HistogramRawTestData "
            + " should be a " + typeProbeData.getClass().getName()
            + ", like a virgin HistogramRawTestData, but it's a "
            + measurements.getClass().getName(),
        measurements.getClass() == typeProbeData.getClass());

    for (int j = 0; j < measurements.size(); ++j) {
      hist.enter(measurements.get(j));
    }

    LoggedDiscreteCDF result = new LoggedDiscreteCDF();
    int[] percentiles = new int[data.getPercentiles().size()];

    for (int j = 0; j < data.getPercentiles().size(); ++j) {
      percentiles[j] = data.getPercentiles().get(j);
    }

    result.setCDF(hist, percentiles, data.getScale());
    return result;
  }
  
  public static void main(String[] args) throws IOException {
    final Configuration conf = new Configuration();
    final FileSystem lfs = FileSystem.getLocal(conf);

    for (String arg : args) {
      Path filePath = new Path(arg).makeQualified(lfs);
      String fileName = filePath.getName();
      if (fileName.startsWith("input")) {
        LoggedDiscreteCDF newResult = histogramFileToCDF(filePath, lfs);
        String testName = fileName.substring("input".length());
        Path goldFilePath = new Path(filePath.getParent(), "gold"+testName);

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getJsonFactory();
        FSDataOutputStream ostream = lfs.create(goldFilePath, true);
        JsonGenerator gen = factory.createJsonGenerator(ostream,
            JsonEncoding.UTF8);
        gen.useDefaultPrettyPrinter();
        
        gen.writeObject(newResult);
        
        gen.close();
      } else {
        System.err.println("Input file not started with \"input\". File "+fileName+" skipped.");
      }
    }
  }
}
