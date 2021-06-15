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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.TrackerDistributedCacheManager;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.mapred.JvmManager.JvmManagerForType;
import org.apache.hadoop.mapred.JvmManager.JvmManagerForType.JvmRunner;
import org.apache.hadoop.mapred.JvmManager.JvmEnv;
import org.apache.hadoop.mapred.TaskTracker.RunningJob;
import org.apache.hadoop.mapred.TaskTracker.TaskInProgress;
import org.apache.hadoop.mapreduce.TaskType;
import org.apache.hadoop.mapreduce.server.tasktracker.userlogs.UserLogManager;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestTaskEnvironment {
  private static File TEST_DIR = new File(System.getProperty("test.build.data",
      "/tmp"), TestJvmManager.class.getSimpleName());
  private static int MAP_SLOTS = 1;
  private static int REDUCE_SLOTS = 1;
  private TaskTracker tt;
  private JvmManager jvmManager;
  private JobConf ttConf;

  @Before
  public void setUp() {
    TEST_DIR.mkdirs();
  }

  @After
  public void tearDown() throws IOException {
    FileUtil.fullyDelete(TEST_DIR);
  }

  public TestTaskEnvironment() throws Exception {
    tt = new TaskTracker();
    ttConf = new JobConf();
    ttConf.setLong("mapred.tasktracker.tasks.sleeptime-before-sigkill", 2000);
    tt.setConf(ttConf);
    tt.setMaxMapSlots(MAP_SLOTS);
    tt.setMaxReduceSlots(REDUCE_SLOTS);
    tt.setTaskController(new DefaultTaskController());
    jvmManager = new JvmManager(tt);
    tt.setJvmManagerInstance(jvmManager);
    tt.setUserLogManager(new UserLogManager(ttConf));
  }

  // write a shell script to execute the command.
  private File writeScript(String fileName, String cmd, File pidFile) throws IOException {
    File script = new File(TEST_DIR, fileName);
    FileOutputStream out = new FileOutputStream(script);
    // write pid into a file
    out.write(("echo $$ >" + pidFile.toString() + ";").getBytes());
    // ignore SIGTERM
    out.write(("trap '' 15\n").getBytes());
    // write the actual command it self.
    out.write(cmd.getBytes());
    out.close();
    script.setExecutable(true);
    return script;
  }

  @Test
  public void testTaskEnv() throws Throwable {
    ttConf.set("mapreduce.admin.user.shell", "/bin/testshell");
    ttConf.set("mapreduce.admin.user.env", "key1=value1,key2=value2");
    ttConf.set("mapred.child.env", "ROOT=$HOME");
    final Map<String, String> env = new HashMap<String, String>();
    String user = "test";
    JobConf taskConf = new JobConf(ttConf);
    TaskAttemptID attemptID = new TaskAttemptID("test", 0, true, 0, 0);
    Task task = new MapTask(null, attemptID, 0, null, MAP_SLOTS);
    task.setConf(taskConf);
    TaskInProgress tip = tt.new TaskInProgress(task, taskConf);
    RunningJob rjob = new RunningJob(attemptID.getJobID());
    TaskController taskController = new DefaultTaskController();
    taskController.setConf(ttConf);
    rjob.distCacheMgr = 
      new TrackerDistributedCacheManager(ttConf, taskController).
      newTaskDistributedCacheManager(attemptID.getJobID(), taskConf);
      
    final TaskRunner taskRunner = task.createRunner(tt, tip, rjob);
    String errorInfo = "Child error";
    String mapredChildEnv = taskRunner.getChildEnv(taskConf);
    taskRunner.updateUserLoginEnv(errorInfo, user, taskConf, env);
    taskRunner.setEnvFromInputString(errorInfo, env, mapredChildEnv);

    final Vector<String> vargs = new Vector<String>(1);
    File pidFile = new File(TEST_DIR, "pid");
    vargs.add(writeScript("ENV", "/bin/env ", pidFile).getAbsolutePath());
    final File workDir = new File(TEST_DIR, "work");
    workDir.mkdir();
    final File stdout = new File(TEST_DIR, "stdout");
    final File stderr = new File(TEST_DIR, "stderr");
    Map<String, String> jvmenvmap = env;
    String javaOpts = taskRunner.getChildJavaOpts(ttConf, 
      JobConf.MAPRED_MAP_TASK_JAVA_OPTS);

    assertTrue(jvmenvmap.containsKey("SHELL"));
    assertTrue(jvmenvmap.containsValue("/bin/testshell"));
    assertTrue(jvmenvmap.containsKey("key2"));
    assertTrue(jvmenvmap.containsValue("value2"));
    assertTrue(javaOpts, javaOpts.contains("Xmx"));
    assertTrue(javaOpts, javaOpts.contains("IPv4"));
    
    String root = jvmenvmap.get("ROOT");
    assertTrue(root, root.contentEquals(System.getenv("HOME")));
  }
}
