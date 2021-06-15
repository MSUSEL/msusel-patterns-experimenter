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

import java.io.*;

import org.apache.hadoop.streaming.Environment;

/** A minimal Java implementation of /usr/bin/tr.
    Used to test the usage of external applications without adding
    platform-specific dependencies.
 */
public class TrApp
{

  public TrApp(char find, char replace)
  {
    this.find = find;
    this.replace = replace;
  }

  void testParentJobConfToEnvVars() throws IOException
  {
    env = new Environment();
    // test that some JobConf properties are exposed as expected     
    // Note the dots translated to underscore: 
    // property names have been escaped in PipeMapRed.safeEnvVarName()
    expectDefined("mapred_local_dir");
    expect("mapred_output_format_class", "org.apache.hadoop.mapred.TextOutputFormat");
    expect("mapred_output_key_class", "org.apache.hadoop.io.Text");
    expect("mapred_output_value_class", "org.apache.hadoop.io.Text");

    expect("mapred_task_is_map", "true");
    expectDefined("mapred_task_id");

    expectDefined("map_input_file");
    expectDefined("map_input_length");

    expectDefined("io_sort_factor");

    // the FileSplit context properties are not available in local hadoop..
    // so can't check them in this test.

  }

  // this runs in a subprocess; won't use JUnit's assertTrue()    
  void expect(String evName, String evVal) throws IOException
  {
    String got = env.getProperty(evName);
    if (!evVal.equals(got)) {
      String msg = "FAIL evName=" + evName + " got=" + got + " expect=" + evVal;
      throw new IOException(msg);
    }
  }

  void expectDefined(String evName) throws IOException
  {
    String got = env.getProperty(evName);
    if (got == null) {
      String msg = "FAIL evName=" + evName + " is undefined. Expect defined.";
      throw new IOException(msg);
    }
  }

  public void go() throws IOException
  {
    testParentJobConfToEnvVars();
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String line;

    while ((line = in.readLine()) != null) {
      String out = line.replace(find, replace);
      System.out.println(out);
      System.err.println("reporter:counter:UserCounters,InputLines,1");
    }
  }

  public static void main(String[] args) throws IOException
  {
    args[0] = CUnescape(args[0]);
    args[1] = CUnescape(args[1]);
    TrApp app = new TrApp(args[0].charAt(0), args[1].charAt(0));
    app.go();
  }

  public static String CUnescape(String s)
  {
    if (s.equals("\\n")) {
      return "\n";
    } else {
      return s;
    }
  }
  char find;
  char replace;
  Environment env;
}
