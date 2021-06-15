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
package org.apache.hadoop.ipc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.SaslRpcServer.AuthMethod;

/**
 * The IPC connection header sent by the client to the server
 * on connection establishment.
 */
class ConnectionHeader implements Writable {
  public static final Log LOG = LogFactory.getLog(ConnectionHeader.class);
  
  private String protocol;
  private UserGroupInformation ugi = null;
  private AuthMethod authMethod;
  
  public ConnectionHeader() {}
  
  /**
   * Create a new {@link ConnectionHeader} with the given <code>protocol</code>
   * and {@link UserGroupInformation}. 
   * @param protocol protocol used for communication between the IPC client
   *                 and the server
   * @param ugi {@link UserGroupInformation} of the client communicating with
   *            the server
   */
  public ConnectionHeader(String protocol, UserGroupInformation ugi, AuthMethod authMethod) {
    this.protocol = protocol;
    this.ugi = ugi;
    this.authMethod = authMethod;
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    protocol = Text.readString(in);
    if (protocol.isEmpty()) {
      protocol = null;
    }
    
    boolean ugiUsernamePresent = in.readBoolean();
    if (ugiUsernamePresent) {
      String username = in.readUTF();
      boolean realUserNamePresent = in.readBoolean();
      if (realUserNamePresent) {
        String realUserName = in.readUTF();
        UserGroupInformation realUserUgi = UserGroupInformation
            .createRemoteUser(realUserName);
        ugi = UserGroupInformation.createProxyUser(username, realUserUgi);
      } else {
      	ugi = UserGroupInformation.createRemoteUser(username);
      }
    } else {
      ugi = null;
    }
  }

  @Override
  public void write(DataOutput out) throws IOException {
    Text.writeString(out, (protocol == null) ? "" : protocol);
    if (ugi != null) {
      if (authMethod == AuthMethod.KERBEROS) {
        // Send effective user for Kerberos auth
        out.writeBoolean(true);
        out.writeUTF(ugi.getUserName());
        out.writeBoolean(false);
      } else if (authMethod == AuthMethod.DIGEST) {
        // Don't send user for token auth
        out.writeBoolean(false);
      } else {
        //Send both effective user and real user for simple auth
        out.writeBoolean(true);
        out.writeUTF(ugi.getUserName());
        if (ugi.getRealUser() != null) {
          out.writeBoolean(true);
          out.writeUTF(ugi.getRealUser().getUserName());
        } else {
          out.writeBoolean(false);
        }
      }
    } else {
      out.writeBoolean(false);
    }
  }

  public String getProtocol() {
    return protocol;
  }

  public UserGroupInformation getUgi() {
    return ugi;
  }

  public String toString() {
    return protocol + "-" + ugi;
  }
}
