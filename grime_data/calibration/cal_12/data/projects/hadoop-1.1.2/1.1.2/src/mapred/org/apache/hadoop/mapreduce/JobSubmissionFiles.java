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
package org.apache.hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.conf.Configuration;
/**
 * A utility to manage job submission files.<br/>
 * <b><i>Note that this class is for framework internal usage only and is
 * not to be used by users directly.</i></b>
 */
public class JobSubmissionFiles {

  // job submission directory is private!
  final public static FsPermission JOB_DIR_PERMISSION =
    FsPermission.createImmutable((short) 0700); // rwx--------
  //job files are world-wide readable and owner writable
  final public static FsPermission JOB_FILE_PERMISSION = 
    FsPermission.createImmutable((short) 0644); // rw-r--r--
  
  public static Path getJobSplitFile(Path jobSubmissionDir) {
    return new Path(jobSubmissionDir, "job.split");
  }

  public static Path getJobSplitMetaFile(Path jobSubmissionDir) {
    return new Path(jobSubmissionDir, "job.splitmetainfo");
  }
  
  /**
   * Get the job conf path.
   */
  public static Path getJobConfPath(Path jobSubmitDir) {
    return new Path(jobSubmitDir, "job.xml");
  }
    
  /**
   * Get the job jar path.
   */
  public static Path getJobJar(Path jobSubmitDir) {
    return new Path(jobSubmitDir, "job.jar");
  }
  
  /**
   * Get the job distributed cache files path.
   * @param jobSubmitDir
   */
  public static Path getJobDistCacheFiles(Path jobSubmitDir) {
    return new Path(jobSubmitDir, "files");
  }
  /**
   * Get the job distributed cache archives path.
   * @param jobSubmitDir 
   */
  public static Path getJobDistCacheArchives(Path jobSubmitDir) {
    return new Path(jobSubmitDir, "archives");
  }
  /**
   * Get the job distributed cache libjars path.
   * @param jobSubmitDir 
   */
  public static Path getJobDistCacheLibjars(Path jobSubmitDir) {
    return new Path(jobSubmitDir, "libjars");
  }

  /**
   * Initializes the staging directory and returns the path. It also
   * keeps track of all necessary ownership & permissions
   * @param client
   * @param conf
   */
  public static Path getStagingDir(JobClient client, Configuration conf) 
  throws IOException, InterruptedException {
    Path stagingArea = client.getStagingAreaDir();
    FileSystem fs = stagingArea.getFileSystem(conf);
    String realUser;
    String currentUser;
    UserGroupInformation ugi = UserGroupInformation.getLoginUser();
    realUser = ugi.getShortUserName();
    currentUser = UserGroupInformation.getCurrentUser().getShortUserName();
    if (fs.exists(stagingArea)) {
      FileStatus fsStatus = fs.getFileStatus(stagingArea);
      String owner = fsStatus.getOwner();
      if (!(owner.equals(currentUser) || owner.equals(realUser)) || 
          !fsStatus.getPermission().equals(JOB_DIR_PERMISSION)) {
         throw new IOException("The ownership/permissions on the staging " +
                      "directory " + stagingArea + " is not as expected. " + 
                      "It is owned by " + owner + " and permissions are "+ 
                      fsStatus.getPermission() + ". The directory must " +
                      "be owned by the submitter " + currentUser + " or " +
                      "by " + realUser + " and permissions must be rwx------");
      }
    } else {
      fs.mkdirs(stagingArea, 
          new FsPermission(JOB_DIR_PERMISSION));
    }
    return stagingArea;
  }
  
}
