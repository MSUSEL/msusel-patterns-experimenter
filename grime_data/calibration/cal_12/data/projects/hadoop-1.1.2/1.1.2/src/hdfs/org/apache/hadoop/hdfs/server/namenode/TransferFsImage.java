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

import java.io.*;
import java.net.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hdfs.protocol.FSConstants;
import org.apache.hadoop.security.SecurityUtil;
import org.apache.hadoop.hdfs.server.namenode.SecondaryNameNode.ErrorSimulator;
import org.apache.hadoop.io.MD5Hash;

/**
 * This class provides fetching a specified file from the NameNode.
 */
class TransferFsImage implements FSConstants {
  private static final Log LOG = LogFactory.getLog(TransferFsImage.class);
  private boolean isGetImage;
  private boolean isGetEdit;
  private boolean isPutImage;
  private int remoteport;
  private String machineName;
  private CheckpointSignature token;
  private MD5Hash newChecksum = null;
  
  /**
   * File downloader.
   * @param pmap key=value[] map that is passed to the http servlet as 
   *        url parameters
   * @param request the object from which this servelet reads the url contents
   * @param response the object into which this servelet writes the url contents
   * @throws IOException
   */
  public TransferFsImage(Map<String,String[]> pmap,
                         HttpServletRequest request,
                         HttpServletResponse response
                         ) throws IOException {
    isGetImage = isGetEdit = isPutImage = false;
    remoteport = 0;
    machineName = null;
    token = null;
    newChecksum = null;

    for (Iterator<String> it = pmap.keySet().iterator(); it.hasNext();) {
      String key = it.next();
      if (key.equals("getimage")) { 
        isGetImage = true;
      } else if (key.equals("getedit")) { 
        isGetEdit = true;
      } else if (key.equals("putimage")) { 
        isPutImage = true;
      } else if (key.equals("port")) { 
        remoteport = new Integer(pmap.get("port")[0]).intValue();
      } else if (key.equals("machine")) { 
        machineName = pmap.get("machine")[0];
      } else if (key.equals("token")) { 
        token = new CheckpointSignature(pmap.get("token")[0]);
      } else if (key.equals("newChecksum")) { 
        newChecksum = new MD5Hash(pmap.get("newChecksum")[0]);
      }
    }

    int numGets = (isGetImage?1:0) + (isGetEdit?1:0);
    if ((numGets > 1) || (numGets == 0) && !isPutImage) {
      throw new IOException("Illegal parameters to TransferFsImage");
    }
  }

  boolean getEdit() {
    return isGetEdit;
  }

  boolean getImage() {
    return isGetImage;
  }

  boolean putImage() {
    return isPutImage;
  }

  CheckpointSignature getToken() {
    return token;
  }
  
  /**
   * Get the MD5 digest of the new image
   * @return the MD5 digest of the new image
   */
  MD5Hash getNewChecksum() {
    return newChecksum;
  }
  
  String getInfoServer() throws IOException{
    if (machineName == null || remoteport == 0) {
      throw new IOException ("MachineName and port undefined");
    }
    return machineName + ":" + remoteport;
  }

  /**
   * A server-side method to respond to a getfile http request
   * Copies the contents of the local file into the output stream.
   */
  static void getFileServer(OutputStream outstream, File localfile) 
    throws IOException {
    byte buf[] = new byte[BUFFER_SIZE];
    FileInputStream infile = null;
    try {
      infile = new FileInputStream(localfile);
      if (ErrorSimulator.getErrorSimulation(2)
          && localfile.getAbsolutePath().contains("secondary")) {
        // throw exception only when the secondary sends its image
        throw new IOException("If this exception is not caught by the " +
            "name-node fs image will be truncated.");
      }
      int num = 1;
      while (num > 0) {
        num = infile.read(buf);
        if (num <= 0) {
          break;
        }
        outstream.write(buf, 0, num);
      }
    } finally {
      if (infile != null) {
        infile.close();
      }
    }
  }

  /**
   * Client-side Method to fetch file from a server
   * Copies the response from the URL to a list of local files.
   * 
   * @Return a digest of the received file if getChecksum is true
   */
  static MD5Hash getFileClient(String fsName, String id, File[] localPath,
      boolean getChecksum) throws IOException {
    byte[] buf = new byte[BUFFER_SIZE];

    String str = NameNode.getHttpUriScheme() + "://" + fsName + "/getimage?" + id;
    LOG.info("Opening connection to " + str);
    //
    // open connection to remote server
    //
    URL url = new URL(str);

    URLConnection connection = SecurityUtil.openSecureHttpConnection(url);
    InputStream stream = connection.getInputStream();
    MessageDigest digester = null;
    if (getChecksum) {
      digester = MD5Hash.getDigester();
      stream = new DigestInputStream(stream, digester);
    }
    FileOutputStream[] output = null;

    try {
      if (localPath != null) {
        output = new FileOutputStream[localPath.length];
        for (int i = 0; i < output.length; i++) {
          output[i] = new FileOutputStream(localPath[i]);
        }
      }
      int num = 1;
      while (num > 0) {
        num = stream.read(buf);
        if (num > 0 && localPath != null) {
          for (int i = 0; i < output.length; i++) {
            output[i].write(buf, 0, num);
          }
        }
      }
    } finally {
      stream.close();
      if (output != null) {
        for (int i = 0; i < output.length; i++) {
          if (output[i] != null) {
            output[i].close();
          }
        }
      }
    }
    return digester == null ? null : new MD5Hash(digester.digest());
  }
}
