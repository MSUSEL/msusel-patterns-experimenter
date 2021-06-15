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
package org.apache.hadoop.mapred.gridmix;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.util.LineReader;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RoundRobinUserResolver implements UserResolver {
  public static final Log LOG = LogFactory.getLog(RoundRobinUserResolver.class);

  private int uidx = 0;
  private List<UserGroupInformation> users = Collections.emptyList();

  /**
   *  Mapping between user names of original cluster and UGIs of proxy users of
   *  simulated cluster
   */
  private final HashMap<String,UserGroupInformation> usercache =
      new HashMap<String,UserGroupInformation>();
  
  /**
   * Userlist assumes one user per line.
   * Each line in users-list-file is of the form &lt;username&gt;[,group]* 
   * <br> Group names are ignored(they are not parsed at all).
   */
  private List<UserGroupInformation> parseUserList(
      URI userUri, Configuration conf) throws IOException {
    if (null == userUri) {
      return Collections.emptyList();
    }
    
    final Path userloc = new Path(userUri.toString());
    final Text rawUgi = new Text();
    final FileSystem fs = userloc.getFileSystem(conf);
    final ArrayList<UserGroupInformation> ugiList =
        new ArrayList<UserGroupInformation>();

    LineReader in = null;
    try {
      in = new LineReader(fs.open(userloc));
      while (in.readLine(rawUgi) > 0) {//line is of the form username[,group]*
        // e is end position of user name in this line
        int e = rawUgi.find(",");
        if (rawUgi.getLength() == 0 || e == 0) {
          throw new IOException("Missing username: " + rawUgi);
        }
        if (e == -1) {
          e = rawUgi.getLength();
        }
        final String username = Text.decode(rawUgi.getBytes(), 0, e);
        UserGroupInformation ugi = null;
        try {
          ugi = UserGroupInformation.createProxyUser(username,
                    UserGroupInformation.getLoginUser());
        } catch (IOException ioe) {
          LOG.error("Error while creating a proxy user " ,ioe);
        }
        if (ugi != null) {
          ugiList.add(ugi);
        }
        // No need to parse groups, even if they exist. Go to next line
      }
    } finally {
      if (in != null) {
        in.close();
      }
    }
    return ugiList;
  }

  @Override
  public synchronized boolean setTargetUsers(URI userloc, Configuration conf)
      throws IOException {
    uidx = 0;
    users = parseUserList(userloc, conf);
    if (users.size() == 0) {
      throw new IOException(buildEmptyUsersErrorMsg(userloc));
    }
    usercache.clear();
    return true;
  }

  static String buildEmptyUsersErrorMsg(URI userloc) {
    return "Empty user list is not allowed for RoundRobinUserResolver. Provided"
    + " user resource URI '" + userloc + "' resulted in an empty user list.";
  }

  @Override
  public synchronized UserGroupInformation getTargetUgi(
      UserGroupInformation ugi) {
    // UGI of proxy user
    UserGroupInformation targetUGI = usercache.get(ugi.getUserName());
    if (targetUGI == null) {
      targetUGI = users.get(uidx++ % users.size());
      usercache.put(ugi.getUserName(), targetUGI);
    }
    return targetUGI;
  }

  /**
   * {@inheritDoc}
   * <p>
   * {@link RoundRobinUserResolver} needs to map the users in the
   * trace to the provided list of target users. So user list is needed.
   */
  public boolean needsTargetUsersList() {
    return true;
  }
}
