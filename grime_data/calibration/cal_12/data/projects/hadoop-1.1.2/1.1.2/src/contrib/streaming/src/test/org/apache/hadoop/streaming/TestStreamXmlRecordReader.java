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
package org.apache.hadoop.streaming;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.hadoop.fs.FileUtil;

/**
 * This class tests StreamXmlRecordReader
 * The test creates an XML file, uses StreamXmlRecordReader and compares
 * the expected output against the generated output
 */
public class TestStreamXmlRecordReader extends TestStreaming
{

  private StreamJob job;

  public TestStreamXmlRecordReader() throws IOException {
    INPUT_FILE = new File("input.xml");
    input = "<xmltag>\t\nroses.are.red\t\nviolets.are.blue\t\nbunnies.are.pink\t\n</xmltag>\t\n";
  }
  
  protected void createInput() throws IOException
  {
    FileOutputStream out = new FileOutputStream(INPUT_FILE.getAbsoluteFile());
    String dummyXmlStartTag = "<PATTERN>\n";
    String dummyXmlEndTag = "</PATTERN>\n";
    out.write(dummyXmlStartTag.getBytes("UTF-8"));
    out.write(input.getBytes("UTF-8"));
    out.write(dummyXmlEndTag.getBytes("UTF-8"));
    out.close();
  }

  protected String[] genArgs() {
    return new String[] {
      "-input", INPUT_FILE.getAbsolutePath(),
      "-output", OUTPUT_DIR.getAbsolutePath(),
      "-mapper","cat", 
      "-reducer", "NONE", 
      "-inputreader", "StreamXmlRecordReader,begin=<xmltag>,end=</xmltag>"
    };
  }

  public void testCommandLine() {
    try {
      try {
        FileUtil.fullyDelete(OUTPUT_DIR.getAbsoluteFile());
      } catch (Exception e) {
      }
      createInput();
      job = new StreamJob(genArgs(), false);
      job.go();
      File outFile = new File(OUTPUT_DIR, "part-00000").getAbsoluteFile();
      String output = StreamUtil.slurp(outFile);
      outFile.delete();
      assertEquals(input, output);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        INPUT_FILE.delete();
        FileUtil.fullyDelete(OUTPUT_DIR.getAbsoluteFile());
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[]args) throws Exception
  {
    new TestStreamXmlRecordReader().testCommandLine();
  }
}
