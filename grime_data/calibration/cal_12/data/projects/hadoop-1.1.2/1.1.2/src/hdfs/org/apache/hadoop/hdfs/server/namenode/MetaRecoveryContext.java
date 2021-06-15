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
package org.apache.hadoop.hdfs.server.namenode;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Context data for an ongoing NameNode recovery process. */
public final class MetaRecoveryContext  {
  public static final Log LOG = LogFactory.getLog(MetaRecoveryContext.class.getName());
  private int force;
  public static final int FORCE_NONE = 0;
  public static final int FORCE_FIRST_CHOICE = 1;
  public static final int FORCE_ALL = 2;

  public MetaRecoveryContext(int force) {
    this.force = force;
  }
  /** Display a prompt to the user and get his or her choice.
   *  
   * @param prompt      The prompt to display
   * @param c1          Choice 1
   * @param choices     Other choies
   *
   * @return            The choice that was taken
   * @throws IOException
   */
  public String ask(String prompt, String firstChoice, String... choices) 
      throws IOException {
    while (true) {
      LOG.error(prompt);
      if (force > FORCE_NONE) {
        LOG.info("Automatically choosing " + firstChoice);
        return firstChoice;
      }
      StringBuilder responseBuilder = new StringBuilder();
      while (true) {
        int c = System.in.read();
        if (c == -1 || c == '\r' || c == '\n') {
          break;
        }
        responseBuilder.append((char)c);
      }
      String response = responseBuilder.toString();
      if (response.equalsIgnoreCase(firstChoice)) {
        return firstChoice;
      }
      for (String c : choices) {
        if (response.equalsIgnoreCase(c)) {
          return c;
        }
      }
      LOG.error("I'm sorry, I cannot understand your response.\n");
    }
  }
  /** Log a message and quit */
  public void quit() {
    LOG.error("Exiting on user request.");
    System.exit(0);
  }

  static public void editLogLoaderPrompt(String prompt,
      MetaRecoveryContext recovery) throws IOException
  {
    if (recovery == null) {
      throw new IOException(prompt);
    }
    LOG.error(prompt);
    String answer = recovery.ask(
      "\nEnter 's' to stop reading the edit log here, abandoning any later " +
        "edits.\n" +
      "Enter 'q' to quit without saving.\n" +
      "(s/q)", "s", "q");
    if (answer.equals("s")) {
      LOG.error("We will stop reading the edits log here.  "
          + "NOTE: Some edits have been lost!");
      return;
    } else if (answer.equals("q")) {
      recovery.quit();
    }
  }
}
