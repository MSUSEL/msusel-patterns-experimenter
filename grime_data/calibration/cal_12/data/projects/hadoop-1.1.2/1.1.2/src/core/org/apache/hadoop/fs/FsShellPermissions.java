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
package org.apache.hadoop.fs;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.fs.FsShell.CmdHandler;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.fs.permission.ChmodParser;


/**
 * This class is the home for file permissions related commands.
 * Moved to this seperate class since FsShell is getting too large.
 */
class FsShellPermissions {
  
  /*========== chmod ==========*/
   
  /* The pattern is alsmost as flexible as mode allowed by 
   * chmod shell command. The main restriction is that we recognize only rwxX.
   * To reduce errors we also enforce 3 digits for octal mode.
   */  
  
  static String CHMOD_USAGE = 
                            "-chmod [-R] <MODE[,MODE]... | OCTALMODE> PATH...";

  private static  ChmodParser pp;
  
  private static class ChmodHandler extends CmdHandler {

    ChmodHandler(FileSystem fs, String modeStr) throws IOException {
      super("chmod", fs);
      try {
        pp = new ChmodParser(modeStr);
      } catch(IllegalArgumentException iea) {
        patternError(iea.getMessage());
      }
    }

    private void patternError(String mode) throws IOException {
     throw new IOException("chmod : mode '" + mode + 
         "' does not match the expected pattern.");      
    }
    
    @Override
    public void run(FileStatus file, FileSystem srcFs) throws IOException {
      int newperms = pp.applyNewPermission(file);

      if (file.getPermission().toShort() != newperms) {
        try {
          srcFs.setPermission(file.getPath(), 
                                new FsPermission((short)newperms));
        } catch (IOException e) {
          System.err.println(getName() + ": changing permissions of '" + 
                             file.getPath() + "':" + e.getMessage().split("\n")[0]);
        }
      }
    }
  }

  /*========== chown ==========*/
  
  static private String allowedChars = "[-_./@a-zA-Z0-9]";
  ///allows only "allowedChars" above in names for owner and group
  static private Pattern chownPattern = 
         Pattern.compile("^\\s*(" + allowedChars + "+)?" +
                          "([:](" + allowedChars + "*))?\\s*$");
  static private Pattern chgrpPattern = 
         Pattern.compile("^\\s*(" + allowedChars + "+)\\s*$");
  
  static String CHOWN_USAGE = "-chown [-R] [OWNER][:[GROUP]] PATH...";
  static String CHGRP_USAGE = "-chgrp [-R] GROUP PATH...";  

  private static class ChownHandler extends CmdHandler {
    protected String owner = null;
    protected String group = null;

    protected ChownHandler(String cmd, FileSystem fs) { //for chgrp
      super(cmd, fs);
    }

    ChownHandler(FileSystem fs, String ownerStr) throws IOException {
      super("chown", fs);
      Matcher matcher = chownPattern.matcher(ownerStr);
      if (!matcher.matches()) {
        throw new IOException("'" + ownerStr + "' does not match " +
                              "expected pattern for [owner][:group].");
      }
      owner = matcher.group(1);
      group = matcher.group(3);
      if (group != null && group.length() == 0) {
        group = null;
      }
      if (owner == null && group == null) {
        throw new IOException("'" + ownerStr + "' does not specify " +
                              " owner or group.");
      }
    }

    @Override
    public void run(FileStatus file, FileSystem srcFs) throws IOException {
      //Should we do case insensitive match?  
      String newOwner = (owner == null || owner.equals(file.getOwner())) ?
                        null : owner;
      String newGroup = (group == null || group.equals(file.getGroup())) ?
                        null : group;

      if (newOwner != null || newGroup != null) {
        try {
          srcFs.setOwner(file.getPath(), newOwner, newGroup);
        } catch (IOException e) {
          System.err.println(getName() + ": changing ownership of '" + 
                             file.getPath() + "':" + e.getMessage().split("\n")[0]);

        }
      }
    }
  }

  /*========== chgrp ==========*/    
  
  private static class ChgrpHandler extends ChownHandler {
    ChgrpHandler(FileSystem fs, String groupStr) throws IOException {
      super("chgrp", fs);

      Matcher matcher = chgrpPattern.matcher(groupStr);
      if (!matcher.matches()) {
        throw new IOException("'" + groupStr + "' does not match " +
        "expected pattern for group");
      }
      group = matcher.group(1);
    }
  }

  static int changePermissions(FileSystem fs, String cmd, 
                                String argv[], int startIndex, FsShell shell)
                                throws IOException {
    CmdHandler handler = null;
    boolean recursive = false;

    // handle common arguments, currently only "-R" 
    for (; startIndex < argv.length && argv[startIndex].equals("-R"); 
    startIndex++) {
      recursive = true;
    }

    if ( startIndex >= argv.length ) {
      throw new IOException("Not enough arguments for the command");
    }

    if (cmd.equals("-chmod")) {
      handler = new ChmodHandler(fs, argv[startIndex++]);
    } else if (cmd.equals("-chown")) {
      handler = new ChownHandler(fs, argv[startIndex++]);
    } else if (cmd.equals("-chgrp")) {
      handler = new ChgrpHandler(fs, argv[startIndex++]);
    }

    return shell.runCmdHandler(handler, argv, startIndex, recursive);
  } 
}
