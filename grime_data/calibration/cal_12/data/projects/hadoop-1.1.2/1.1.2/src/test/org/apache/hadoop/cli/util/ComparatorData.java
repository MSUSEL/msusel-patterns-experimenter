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

import java.util.Vector;

/**
 *
 * Class to store CLI Test Comparators Data
 */
public class ComparatorData {
  private String expectedOutput = null;
  private String actualOutput = null;
  private boolean testResult = false;
  private int exitCode = 0;
  private String comparatorType = null;
  
  public ComparatorData() {

  }

  /**
   * @return the expectedOutput
   */
  public String getExpectedOutput() {
    return expectedOutput;
  }

  /**
   * @param expectedOutput the expectedOutput to set
   */
  public void setExpectedOutput(String expectedOutput) {
    this.expectedOutput = expectedOutput;
  }

  /**
   * @return the actualOutput
   */
  public String getActualOutput() {
    return actualOutput;
  }

  /**
   * @param actualOutput the actualOutput to set
   */
  public void setActualOutput(String actualOutput) {
    this.actualOutput = actualOutput;
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
   * @return the exitCode
   */
  public int getExitCode() {
    return exitCode;
  }

  /**
   * @param exitCode the exitCode to set
   */
  public void setExitCode(int exitCode) {
    this.exitCode = exitCode;
  }

  /**
   * @return the comparatorType
   */
  public String getComparatorType() {
    return comparatorType;
  }

  /**
   * @param comparatorType the comparatorType to set
   */
  public void setComparatorType(String comparatorType) {
    this.comparatorType = comparatorType;
  }

}
