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
package org.apache.hadoop.streaming;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PrivilegedExceptionAction;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.mapred.ClusterWithLinuxTaskController;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.security.Groups;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.util.Shell;
import org.apache.hadoop.util.ToolRunner;

/**
 * Test Streaming with LinuxTaskController running the jobs as a user different
 * from the user running the cluster. See {@link ClusterWithLinuxTaskController}
 */
public class TestStreamingAsDifferentUser extends
    ClusterWithLinuxTaskController {

  private Path inputPath = new Path("input");
  private Path outputPath = new Path("output");
  private String input = "roses.are.red\nviolets.are.blue\nbunnies.are.pink\n";
  private String map =
      StreamUtil.makeJavaCommand(TrApp.class, new String[] { ".", "\\n" });
  private String reduce =
      StreamUtil.makeJavaCommand(UniqApp.class, new String[] { "R" });

  public void testStreaming()
      throws Exception {
    if (!shouldRun()) {
      return;
    }
    startCluster();
    final JobConf myConf = getClusterConf();
    myConf.set("hadoop.job.history.user.location","none");
    jobOwner.doAs(new PrivilegedExceptionAction<Void>() {
      public Void run() throws IOException{

        FileSystem inFs = inputPath.getFileSystem(myConf);
        FileSystem outFs = outputPath.getFileSystem(myConf);
        outFs.delete(outputPath, true);
        if (!inFs.mkdirs(inputPath)) {
          throw new IOException("Mkdirs failed to create " + inFs.toString());
        }
        DataOutputStream file = inFs.create(new Path(inputPath, "part-0"));
        file.writeBytes(input);
        file.close();
        final String[] args =
          new String[] { "-input", inputPath.makeQualified(inFs).toString(),
            "-output", outputPath.makeQualified(outFs).toString(), "-mapper",
            map, "-reducer", reduce, "-jobconf",
            "keep.failed.task.files=true", "-jobconf",
            "stream.tmpdir=" + System.getProperty("test.build.data", "/tmp") };

        StreamJob streamJob = new StreamJob(args, true);
        streamJob.setConf(myConf);
        assertTrue("Job has not succeeded", streamJob.go() == 0);
        assertOwnerShip(outputPath);
        return null;
      }
    });
  }
  
  /**
   * Verify if the permissions of distcache dir contents are valid once the job
   * is finished
   */
  public void testStreamingWithDistCache()
  throws Exception {
    if (!shouldRun()) {
      return;
    }
    startCluster();
    final String[] localDirs = mrCluster.getTaskTrackerLocalDirs(0);
    final JobConf myConf = getClusterConf();

    // create file that will go into public distributed cache
    File publicFile = new File(System.getProperty(
        "test.build.data", "/tmp"), "publicFile");
    FileOutputStream fstream = new FileOutputStream(publicFile);
    fstream.write("public file contents".getBytes());
    fstream.close();

    // put the file(that should go into public dist cache) in dfs and set
    // read and exe permissions for others
    FileSystem dfs = dfsCluster.getFileSystem();
    final String publicCacheFile = dfs.getDefaultUri(myConf).toString()
                             + "/tmp/publicFile";
    dfs.copyFromLocalFile(new Path(publicFile.getAbsolutePath()),
        new Path(publicCacheFile));
    dfs.setPermission(new Path(dfs.getDefaultUri(myConf).toString() + "/tmp"),
        new FsPermission((short)0755));
    dfs.setPermission(new Path(publicCacheFile), new FsPermission((short)0755));
    final String taskTrackerUser 
      = UserGroupInformation.getCurrentUser().getShortUserName();
    
    jobOwner.doAs(new PrivilegedExceptionAction<Void>() {
      public Void run() throws Exception{

        FileSystem inFs = inputPath.getFileSystem(myConf);
        FileSystem outFs = outputPath.getFileSystem(myConf);
        outFs.delete(outputPath, true);
        if (!inFs.mkdirs(inputPath)) {
          throw new IOException("Mkdirs failed to create " + inFs.toString());
        }

        // create input file
        DataOutputStream file = inFs.create(new Path(inputPath, "part-0"));
        file.writeBytes(input);
        file.close();

        // Create file that will be passed using -files option.
        // This is private dist cache file
        File privateFile = new File(System.getProperty(
            "test.build.data", "/tmp"), "test.sh");
        privateFile.createNewFile();

        String[] args =
          new String[] {
            "-files", privateFile.toString() + "," + publicCacheFile,
            "-Dmapreduce.task.files.preserve.failedtasks=true",
            "-Dstream.tmpdir=" + System.getProperty("test.build.data", "/tmp"),
            "-input", inputPath.makeQualified(inFs).toString(),
            "-output", outputPath.makeQualified(outFs).toString(),
            "-mapper", "pwd",
            "-reducer", StreamJob.REDUCE_NONE
          };
        StreamJob streamJob = new StreamJob();
        streamJob.setConf(myConf);

        assertTrue("Job failed", ToolRunner.run(streamJob, args)==0);

        // validate private cache files' permissions
        checkPermissionsOnPrivateDistCache(localDirs,
            jobOwner.getShortUserName(), taskTrackerUser,
            taskTrackerSpecialGroup);
        
        // check the file is present even after the job is over.
        // work directory symlink cleanup should not have removed the target 
        // files.
        checkPresenceOfPrivateDistCacheFiles(localDirs, 
            jobOwner.getShortUserName(), new String[] {"test.sh"});

        // validate private cache files' permissions
        checkPermissionsOnPublicDistCache(FileSystem.getLocal(myConf),
            localDirs, taskTrackerUser, taskTrackerPrimaryGroup);

        checkPresenceOfPublicDistCacheFiles(localDirs, 
            new String[] {"publicFile"});
        assertOwnerShip(outputPath);
        return null;
      }
    });
  }
}
