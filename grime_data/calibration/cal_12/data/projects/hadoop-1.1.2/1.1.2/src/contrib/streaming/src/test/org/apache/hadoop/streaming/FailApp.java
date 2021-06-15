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

/**
 * A simple Java app that will consume all input from stdin, echoing
 * it to stdout, and then optionally throw an exception (which should
 * cause a non-zero exit status for the process).
 */
public class FailApp
{

  public FailApp() {
  }

  public void go(boolean fail) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String line;

    // Consume all input (to make sure streaming will still count this
    // task as failed even if all input was consumed).
    while ((line = in.readLine()) != null) {
      System.out.println(line);
    }

    if (fail) {
      throw new RuntimeException("Intentionally failing task");
    }
  }

  public static void main(String[] args) throws IOException {
    boolean fail = true;
    if (args.length >= 1 && "false".equals(args[0])) {
      fail = false;
    }
    
    FailApp app = new FailApp();
    app.go(fail);
  }
}
