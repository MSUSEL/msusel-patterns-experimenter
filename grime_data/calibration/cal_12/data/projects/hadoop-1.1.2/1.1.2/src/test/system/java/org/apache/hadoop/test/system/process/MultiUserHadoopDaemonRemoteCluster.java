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
package org.apache.hadoop.test.system.process;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.test.system.process.HadoopDaemonRemoteCluster.HadoopDaemonInfo;

public abstract class MultiUserHadoopDaemonRemoteCluster
    extends HadoopDaemonRemoteCluster {

  public MultiUserHadoopDaemonRemoteCluster(List<HadoopDaemonInfo> daemonInfos) {
    super(daemonInfos);
  }

  @Override
  protected RemoteProcess getProcessManager(
      HadoopDaemonInfo info, String hostName) {
    return new MultiUserScriptDaemon(info.cmd, hostName, info.role);
  }

  @Override
  public boolean isMultiUserSupported() throws IOException {
    return true;
  }

  class MultiUserScriptDaemon extends ScriptDaemon {

    private static final String MULTI_USER_BINARY_PATH_KEY =
        "test.system.hdrc.multi-user.binary.path";
    private static final String MULTI_USER_MANAGING_USER =
        "test.system.hdrc.multi-user.managinguser.";
    private String binaryPath;
    /**
     * Manging user for a particular daemon is gotten by
     * MULTI_USER_MANAGING_USER + daemonname
     */
    private String mangingUser;

    public MultiUserScriptDaemon(
        String daemonName, String hostName, Enum<?> role) {
      super(daemonName, hostName, role);
      initialize(daemonName);
    }

    private void initialize(String daemonName) {
      binaryPath = conf.get(MULTI_USER_BINARY_PATH_KEY);
      if (binaryPath == null || binaryPath.trim().isEmpty()) {
        throw new IllegalArgumentException(
            "Binary path for multi-user path is not present. Please set "
                + MULTI_USER_BINARY_PATH_KEY + " correctly");
      }
      File binaryFile = new File(binaryPath);
      if (!binaryFile.exists() || !binaryFile.canExecute()) {
        throw new IllegalArgumentException(
            "Binary file path is not configured correctly. Please set "
                + MULTI_USER_BINARY_PATH_KEY
                + " to properly configured binary file.");
      }
      mangingUser = conf.get(MULTI_USER_MANAGING_USER + daemonName);
      if (mangingUser == null || mangingUser.trim().isEmpty()) {
        throw new IllegalArgumentException(
            "Manging user for daemon not present please set : "
                + MULTI_USER_MANAGING_USER + daemonName + " to correct value.");
      }
    }

    @Override
    protected String[] getCommand(String command,String confDir) {
      ArrayList<String> commandList = new ArrayList<String>();
      commandList.add(binaryPath);
      commandList.add(mangingUser);
      commandList.add(hostName);
      commandList.add("--config "
          + confDir + " " + command + " " + daemonName);
      return (String[]) commandList.toArray(new String[commandList.size()]);
    }
  }
}
