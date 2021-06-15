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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.StringTokenizer;

import org.apache.hadoop.cli.TestCLI;
import org.apache.hadoop.cli.util.CLITestData.TestCmd;
import org.apache.hadoop.cli.util.CLITestData.TestCmd.CommandType;
import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.hdfs.tools.DFSAdmin;
import org.apache.hadoop.mapred.tools.MRAdmin;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * This class executed commands and captures the output
 */
public class CommandExecutor {
  private static String commandOutput = null;
  private static int exitCode = 0;
  private static Exception lastException = null;
  private static String cmdExecuted = null;
  
  private static String[] getCommandAsArgs(final String cmd, final String masterKey,
		                                       final String master) {
    StringTokenizer tokenizer = new StringTokenizer(cmd, " ");
    String[] args = new String[tokenizer.countTokens()];
    
    int i = 0;
    while (tokenizer.hasMoreTokens()) {
      args[i] = tokenizer.nextToken();

      args[i] = args[i].replaceAll(masterKey, master);
      args[i] = args[i].replaceAll("CLITEST_DATA", 
        new File(TestCLI.TEST_CACHE_DATA_DIR).
        toURI().toString().replace(' ', '+'));
      args[i] = args[i].replaceAll("USERNAME", System.getProperty("user.name"));

      i++;
    }
    
    return args;
  }
  
  public static int executeCommand(final TestCmd cmd, 
                                   final String namenode, final String jobtracker) 
  throws Exception {
    switch(cmd.getType()) {
    case DFSADMIN:
      return CommandExecutor.executeDFSAdminCommand(cmd.getCmd(), namenode);
    case MRADMIN:
      return CommandExecutor.executeMRAdminCommand(cmd.getCmd(), jobtracker);
    case FS:
      return CommandExecutor.executeFSCommand(cmd.getCmd(), namenode);
    default:
      throw new Exception("Unknow type of Test command:"+ cmd.getType()); 
    }
  }
  
  public static int executeDFSAdminCommand(final String cmd, final String namenode) {
      exitCode = 0;
      
      ByteArrayOutputStream bao = new ByteArrayOutputStream();
      PrintStream origOut = System.out;
      PrintStream origErr = System.err;
      
      System.setOut(new PrintStream(bao));
      System.setErr(new PrintStream(bao));
      
      DFSAdmin shell = new DFSAdmin();
      String[] args = getCommandAsArgs(cmd, "NAMENODE", namenode);
      cmdExecuted = cmd;
     
      try {
        ToolRunner.run(shell, args);
      } catch (Exception e) {
        e.printStackTrace();
        lastException = e;
        exitCode = -1;
      } finally {
        System.setOut(origOut);
        System.setErr(origErr);
      }
      
      commandOutput = bao.toString();
      
      return exitCode;
  }
  
  public static int executeMRAdminCommand(final String cmd, 
                                          final String jobtracker) {
    exitCode = 0;
    
    ByteArrayOutputStream bao = new ByteArrayOutputStream();
    PrintStream origOut = System.out;
    PrintStream origErr = System.err;
    
    System.setOut(new PrintStream(bao));
    System.setErr(new PrintStream(bao));
    
    MRAdmin mradmin = new MRAdmin();
    String[] args = getCommandAsArgs(cmd, "JOBTRACKER", jobtracker);
    cmdExecuted = cmd;
   
    try {
      ToolRunner.run(mradmin, args);
    } catch (Exception e) {
      e.printStackTrace();
      lastException = e;
      exitCode = -1;
    } finally {
      System.setOut(origOut);
      System.setErr(origErr);
    }
    
    commandOutput = bao.toString();
    
    return exitCode;
  }

  public static int executeFSCommand(final String cmd, final String namenode) {
    exitCode = 0;
    
    ByteArrayOutputStream bao = new ByteArrayOutputStream();
    PrintStream origOut = System.out;
    PrintStream origErr = System.err;
    
    System.setOut(new PrintStream(bao));
    System.setErr(new PrintStream(bao));
    
    FsShell shell = new FsShell();
    String[] args = getCommandAsArgs(cmd, "NAMENODE", namenode);
    cmdExecuted = cmd;
    
    try {
      ToolRunner.run(shell, args);
    } catch (Exception e) {
      e.printStackTrace();
      lastException = e;
      exitCode = -1;
    } finally {
      System.setOut(origOut);
      System.setErr(origErr);
    }
    
    commandOutput = bao.toString();
    
    return exitCode;
  }
  
  public static String getLastCommandOutput() {
    return commandOutput;
  }

  public static int getLastExitCode() {
    return exitCode;
  }

  public static Exception getLastException() {
    return lastException;
  }

  public static String getLastCommand() {
    return cmdExecuted;
  }
}
