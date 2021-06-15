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
import java.util.Date;

/** A minimal Java implementation of /usr/bin/uniq
    Used to test the usage of external applications without adding
    platform-specific dependencies.
    Uniques lines and prepends a header on the line.
 */
public class UniqApp
{

  public UniqApp(String header)
  {
    this.header = header;
  }
  public void go() throws IOException
  {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String line;
    String prevLine = null;
    while ((line = in.readLine()) != null) {
      if (!line.equals(prevLine)) {
        System.out.println(header + line);
      }
      prevLine = line;
    }
  }

  public static void main(String[] args) throws IOException
  {
    String h = (args.length < 1) ? "" : args[0];
    UniqApp app = new UniqApp(h);
    app.go();
  }

  String header;
}
