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
package org.apache.hadoop.mapred;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.util.DiskChecker.DiskErrorException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalDirAllocator;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JvmTask;
import org.apache.hadoop.mapreduce.split.JobSplit.TaskSplitIndex;

/**
 * IsolationRunner is intended to facilitate debugging by re-running a specific
 * task, given left-over task files for a (typically failed) past job.  
 * Currently, it is limited to re-running map tasks.
 *
 * Users may coerce MapReduce to keep task files around by setting 
 * keep.failed.task.files.  See mapred_tutorial.xml for more documentation.
 */
public class IsolationRunner {
  private static final Log LOG = 
    LogFactory.getLog(IsolationRunner.class.getName());

  private static class FakeUmbilical implements TaskUmbilicalProtocol {

    public long getProtocolVersion(String protocol, long clientVersion) {
      return TaskUmbilicalProtocol.versionID;
    }
    
    public void done(TaskAttemptID taskid, JvmContext jvmContext)
        throws IOException {
      LOG.info("Task " + taskid + " reporting done.");
    }

    public void fsError(TaskAttemptID taskId, String message,
        JvmContext jvmContext) throws IOException {
      LOG.info("Task " + taskId + " reporting file system error: " + message);
    }

    public void shuffleError(TaskAttemptID taskId, String message,
        JvmContext jvmContext) throws IOException {
      LOG.info("Task " + taskId + " reporting shuffle error: " + message);
    }

    public void fatalError(TaskAttemptID taskId, String msg,
        JvmContext jvmContext) throws IOException {
      LOG.info("Task " + taskId + " reporting fatal error: " + msg);
    }

    public JvmTask getTask(JvmContext context) throws IOException {
      return null;
    }

    public boolean ping(TaskAttemptID taskid, JvmContext jvmContext) throws IOException {
      return true;
    }

    public void commitPending(TaskAttemptID taskId, TaskStatus taskStatus,
        JvmContext jvmContext) throws IOException, InterruptedException {
      statusUpdate(taskId, taskStatus, jvmContext);
    }
    
    public boolean canCommit(TaskAttemptID taskid, JvmContext jvmContext)
        throws IOException {
      return true;
    }
    
    public boolean statusUpdate(TaskAttemptID taskId, TaskStatus taskStatus, JvmContext context) 
    throws IOException, InterruptedException {
      StringBuffer buf = new StringBuffer("Task ");
      buf.append(taskId);
      buf.append(" making progress to ");
      buf.append(taskStatus.getProgress());
      String state = taskStatus.getStateString();
      if (state != null) {
        buf.append(" and state of ");
        buf.append(state);
      }
      LOG.info(buf.toString());
      // ignore phase
      // ignore counters
      return true;
    }

    public void reportDiagnosticInfo(TaskAttemptID taskid, String trace,
        JvmContext jvmContext) throws IOException {
      LOG.info("Task " + taskid + " has problem " + trace);
    }
    
    public MapTaskCompletionEventsUpdate getMapCompletionEvents(JobID jobId,
        int fromEventId, int maxLocs, TaskAttemptID id, JvmContext jvmContext)
        throws IOException {
      return new MapTaskCompletionEventsUpdate(TaskCompletionEvent.EMPTY_ARRAY,
          false);
    }

    public void reportNextRecordRange(TaskAttemptID taskid, 
        SortedRanges.Range range, JvmContext jvmContext) throws IOException {
      LOG.info("Task " + taskid + " reportedNextRecordRange " + range);
    }

    @Override
    public void 
    updatePrivateDistributedCacheSizes(org.apache.hadoop.mapreduce.JobID jobId,
                                       long[] sizes){
      // NOTHING
    }
  }
  
  private ClassLoader makeClassLoader(JobConf conf, 
                                             File workDir) throws IOException {
    List<String> classPaths = new ArrayList<String>();
    // Add jar clas files (includes lib/* and classes/*)
    String jar = conf.getJar();
    if (jar != null) {
      TaskRunner.appendJobJarClasspaths(conf.getJar(), classPaths);
    }
    // Add the workdir, too.
    classPaths.add(workDir.toString());
    // Note: TaskRunner.run() does more, including DistributedCache files.
    
    // Convert to URLs
    URL[] urls = new URL[classPaths.size()];
    for (int i = 0; i < classPaths.size(); ++i) {
      urls[i] = new File(classPaths.get(i)).toURL();
    }
    return new URLClassLoader(urls);
  }
  
  /**
   * Main method.
   */
  boolean run(String[] args) 
      throws ClassNotFoundException, IOException, InterruptedException {
    if (args.length < 1) {
      System.out.println("Usage: IsolationRunner <path>/job.xml " +
               "<optional-user-name>");
      return false;
    }
    File jobFilename = new File(args[0]);
    if (!jobFilename.exists() || !jobFilename.isFile()) {
      System.out.println(jobFilename + " is not a valid job file.");
      return false;
    }
    String user;
    if (args.length > 1) {
      user = args[1];
    } else {
      user = UserGroupInformation.getCurrentUser().getShortUserName();
    }
    JobConf conf = new JobConf(new Path(jobFilename.toString()));
    conf.setUser(user);
    TaskAttemptID taskId = TaskAttemptID.forName(conf.get("mapred.task.id"));
    if (taskId == null) {
      System.out.println("mapred.task.id not found in configuration;" + 
          " job.xml is not a task config");
    }
    boolean isMap = conf.getBoolean("mapred.task.is.map", true);
    if (!isMap) {
      System.out.println("Only map tasks are supported.");
      return false;
    }
    int partition = conf.getInt("mapred.task.partition", 0);
    
    // setup the local and user working directories
    FileSystem local = FileSystem.getLocal(conf);
    LocalDirAllocator lDirAlloc = new LocalDirAllocator("mapred.local.dir");
    Path workDirName;
    boolean workDirExists = lDirAlloc.ifExists(MRConstants.WORKDIR, conf);
    if (workDirExists) {
      workDirName = TaskRunner.formWorkDir(lDirAlloc, conf);
    } else {
      workDirName = lDirAlloc.getLocalPathForWrite(MRConstants.WORKDIR, 
                                                       conf);
    }

    local.setWorkingDirectory(new Path(workDirName.toString()));
    
    FileSystem.get(conf).setWorkingDirectory(conf.getWorkingDirectory());
    
    // set up a classloader with the right classpath
    ClassLoader classLoader =
        makeClassLoader(conf, new File(workDirName.toString()));
    Thread.currentThread().setContextClassLoader(classLoader);
    conf.setClassLoader(classLoader);
    
    // split.dta file is used only by IsolationRunner. The file can now be in
    // any of the configured local disks, so use LocalDirAllocator to find out
    // where it is.
    Path localMetaSplit = 
        new LocalDirAllocator("mapred.local.dir").getLocalPathToRead(
            TaskTracker.getLocalSplitFile(conf.getUser(), taskId.getJobID()
                .toString(), taskId.toString()), conf);
    DataInputStream splitFile = FileSystem.getLocal(conf).open(localMetaSplit);
    TaskSplitIndex splitIndex = new TaskSplitIndex();
    splitIndex.readFields(splitFile);
    splitFile.close();
    Task task =
      new MapTask(jobFilename.toString(), taskId, partition, splitIndex, 1);
    task.setConf(conf);
    task.run(conf, new FakeUmbilical());
    return true;
  }

  /**
   * Run a single task.
   *
   * @param args the first argument is the task directory
   */
  public static void main(String[] args) 
      throws ClassNotFoundException, IOException, InterruptedException {
    if (!new IsolationRunner().run(args)) {
      System.exit(1);
    }
  }
}
