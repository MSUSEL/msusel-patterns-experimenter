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
package org.apache.hadoop.util;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class TestShell extends TestCase {

  private static class Command extends Shell {
    private int runCount = 0;

    private Command(long interval) {
      super(interval);
    }

    protected String[] getExecString() {
      return new String[] {"echo", "hello"};
    }

    protected void parseExecResult(BufferedReader lines) throws IOException {
      ++runCount;
    }

    public int getRunCount() {
      return runCount;
    }
  }

  public void testInterval() throws IOException {
    testInterval(Long.MIN_VALUE / 60000);  // test a negative interval
    testInterval(0L);  // test a zero interval
    testInterval(10L); // interval equal to 10mins
    testInterval(System.currentTimeMillis() / 60000 + 60); // test a very big interval
  }

  /**
   * Assert that a string has a substring in it
   * @param string string to search
   * @param search what to search for it
   */
  private void assertInString(String string, String search) {
    assertNotNull("Empty String", string);
    if (!string.contains(search)) {
      fail("Did not find \"" + search + "\" in " + string);
    }
  }

  public void testShellCommandExecutorToString() throws Throwable {
    Shell.ShellCommandExecutor sce=new Shell.ShellCommandExecutor(
            new String[] { "ls","..","arg 2"});
    String command = sce.toString();
    assertInString(command,"ls");
    assertInString(command, " .. ");
    assertInString(command, "\"arg 2\"");
  }
  
  public void testShellCommandTimeout() throws Throwable {
    String rootDir = new File(System.getProperty(
        "test.build.data", "/tmp")).getAbsolutePath();
    File shellFile = new File(rootDir, "timeout.sh");
    String timeoutCommand = "sleep 4; echo \"hello\"";
    PrintWriter writer = new PrintWriter(new FileOutputStream(shellFile));
    writer.println(timeoutCommand);
    writer.close();
    shellFile.setExecutable(true);
    Shell.ShellCommandExecutor shexc 
    = new Shell.ShellCommandExecutor(new String[]{shellFile.getAbsolutePath()},
                                      null, null, 100);
    try {
      shexc.execute();
    } catch (Exception e) {
      //When timing out exception is thrown.
    }
    shellFile.delete();
    assertTrue("Script didnt not timeout" , shexc.isTimedOut());
  }

  private void testInterval(long interval) throws IOException {
    Command command = new Command(interval);

    command.run();
    assertEquals(1, command.getRunCount());

    command.run();
    if (interval > 0) {
      assertEquals(1, command.getRunCount());
    } else {
      assertEquals(2, command.getRunCount());
    }
  }
}
