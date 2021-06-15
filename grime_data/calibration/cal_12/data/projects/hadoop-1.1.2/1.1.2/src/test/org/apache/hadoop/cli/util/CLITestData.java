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
package org.apache.hadoop.cli.util;

import java.util.ArrayList;

/**
 *
 * Class to store CLI Test Data
 */
public class CLITestData {
  private String testDesc = null;
  private ArrayList<TestCmd> testCommands = null;
  private ArrayList<TestCmd> cleanupCommands = null;
  private ArrayList<ComparatorData> comparatorData = null;
  private boolean testResult = false;
  
  public CLITestData() {

  }

  /**
   * Class to define Test Command. includes type of the command and command itself
   * Valid types FS, DFSADMIN and MRADMIN.
   */
  static public class TestCmd {
    public enum CommandType {
        FS,
        DFSADMIN,
        MRADMIN
    }
    private final CommandType type;
    private final String cmd;

    public TestCmd(String str, CommandType type) {
      cmd = str;
      this.type = type;
    }
    public CommandType getType() {
      return type;
    }
    public String getCmd() {
      return cmd;
    }
    public String toString() {
      return cmd;
    }
  }
  
  /**
   * @return the testDesc
   */
  public String getTestDesc() {
    return testDesc;
  }

  /**
   * @param testDesc the testDesc to set
   */
  public void setTestDesc(String testDesc) {
    this.testDesc = testDesc;
  }

  /**
   * @return the testCommands
   */
  public ArrayList<TestCmd> getTestCommands() {
    return testCommands;
  }

  /**
   * @param testCommands the testCommands to set
   */
  public void setTestCommands(ArrayList<TestCmd> testCommands) {
    this.testCommands = testCommands;
  }

  /**
   * @return the comparatorData
   */
  public ArrayList<ComparatorData> getComparatorData() {
    return comparatorData;
  }

  /**
   * @param comparatorData the comparatorData to set
   */
  public void setComparatorData(ArrayList<ComparatorData> comparatorData) {
    this.comparatorData = comparatorData;
  }

  /**
   * @return the testResult
   */
  public boolean getTestResult() {
    return testResult;
  }

  /**
   * @param testResult the testResult to set
   */
  public void setTestResult(boolean testResult) {
    this.testResult = testResult;
  }

  /**
   * @return the cleanupCommands
   */
  public ArrayList<TestCmd> getCleanupCommands() {
    return cleanupCommands;
  }

  /**
   * @param cleanupCommands the cleanupCommands to set
   */
  public void setCleanupCommands(ArrayList<TestCmd> cleanupCommands) {
    this.cleanupCommands = cleanupCommands;
  }
}
