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
package org.apache.hadoop.io;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.nativeio.Errno;
import org.apache.hadoop.io.nativeio.NativeIO;
import org.apache.hadoop.io.nativeio.NativeIOException;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * This class provides secure APIs for opening and creating files on the local
 * disk. The main issue this class tries to handle is that of symlink traversal.
 * <br/>
 * An example of such an attack is:
 * <ol>
 * <li> Malicious user removes his task's syslog file, and puts a link to the
 * jobToken file of a target user.</li>
 * <li> Malicious user tries to open the syslog file via the servlet on the
 * tasktracker.</li>
 * <li> The tasktracker is unaware of the symlink, and simply streams the contents
 * of the jobToken file. The malicious user can now access potentially sensitive
 * map outputs, etc. of the target user's job.</li>
 * </ol>
 * A similar attack is possible involving the
 * {@link org.apache.hadoop.mapred.TaskLogsTruncater}, but here it is to do
 * with unsecure write to a file.
 * <br/>
 */
public class SecureIOUtils {

  /**
   * Ensure that we are set up to run with the appropriate native support code.
   * If security is disabled, and the support code is unavailable, this class
   * still tries its best to be secure, but is vulnerable to some race condition
   * attacks.
   *
   * If security is enabled but the support code is unavailable, throws a
   * RuntimeException since we don't want to run insecurely.
   */
  static {
    boolean shouldBeSecure = UserGroupInformation.isSecurityEnabled();
    boolean canBeSecure = NativeIO.isAvailable();

    if (!canBeSecure && shouldBeSecure) {
      throw new RuntimeException(
        "Secure IO is not possible without native code extensions.");
    }

    // Pre-cache an instance of the raw FileSystem since we sometimes
    // do secure IO in a shutdown hook, where this call could fail.
    try {
      rawFilesystem = FileSystem.getLocal(new Configuration()).getRaw();
    } catch (IOException ie) {
      throw new RuntimeException(
      "Couldn't obtain an instance of RawLocalFileSystem.");
    }

    // SecureIO just skips security checks in the case that security is
    // disabled
    skipSecurity = !canBeSecure;
  }

  private final static boolean skipSecurity;
  private final static FileSystem rawFilesystem;

  /**
   * Open the given File for read access, verifying the expected user
   * constraints.
   * @param f the file that we are trying to open
   * @param expectedOwner the expected user owner for the file
   * @throws IOException if an IO Error occurred, or the user does not 
   * match
   */
  public static FileInputStream openForRead(File f, String expectedOwner) 
  throws IOException {
    FileInputStream fis = new FileInputStream(f);
    if (expectedOwner == null) { //no security checks
      return fis;
    }
    if (skipSecurity) {
      // Subject to race conditions but this is the best we can do
      FileStatus status =
        rawFilesystem.getFileStatus(new Path(f.getAbsolutePath()));
      checkStat(f, status.getOwner(), expectedOwner);
      return fis;
    }

    boolean success = false;
    try {
      String owner = NativeIO.getOwner(fis.getFD());
      checkStat(f, owner, expectedOwner);
      success = true;
      return fis;
    } finally {
      if (!success) fis.close();
    }
  }

  private static FileOutputStream insecureCreateForWrite(File f,
      int permissions) throws IOException {
    // If we can't do real security, do a racy exists check followed by an
    // open and chmod
    if (f.exists()) {
      throw new AlreadyExistsException("File " + f + " already exists");
    }
    FileOutputStream fos = new FileOutputStream(f);
    boolean success = false;
    try {
      rawFilesystem.setPermission(new Path(f.getAbsolutePath()),
        new FsPermission((short)permissions));
      success = true;
      return fos;
    } finally {
      if (!success) {
        fos.close();
      }
    }
  }

  /**
   * Open the specified File for write access, ensuring that it does not exist.
   * @param f the file that we want to create
   * @param permissions we want to have on the file (if security is enabled)
   *
   * @throws AlreadyExistsException if the file already exists
   * @throws IOException if any other error occurred
   */
  public static FileOutputStream createForWrite(File f, int permissions)
  throws IOException {
    if (skipSecurity) {
      return insecureCreateForWrite(f, permissions);
    } else {
      // Use the native wrapper around open(2)
      try {
        FileDescriptor fd = NativeIO.open(f.getAbsolutePath(),
          NativeIO.O_WRONLY | NativeIO.O_CREAT | NativeIO.O_EXCL,
          permissions);
        return new FileOutputStream(fd);
      } catch (NativeIOException nioe) {
        if (nioe.getErrno() == Errno.EEXIST) {
          throw new AlreadyExistsException(nioe);
        }
        throw nioe;
      }
    }
  }

  private static void checkStat(File f, String owner, String expectedOwner) throws IOException {
    if (expectedOwner != null &&
        !expectedOwner.equals(owner)) {
      throw new IOException(
        "Owner '" + owner + "' for path " + f + " did not match " +
        "expected owner '" + expectedOwner + "'");
    }
  }

  /**
   * Signals that an attempt to create a file at a given pathname has failed
   * because another file already existed at that path.
   */
  public static class AlreadyExistsException extends IOException {
    private static final long serialVersionUID = -6615764817774423232L;
    public AlreadyExistsException(String msg) {
      super(msg);
    }

    public AlreadyExistsException(Throwable cause) {
      super(cause);
    }
  }
}
