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
package org.apache.hadoop.hdfs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.server.namenode.NameNode;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.util.ToolRunner;

public class TestDFSShellGenericOptions extends TestCase {

  public void testDFSCommand() throws IOException {
    String namenode = null;
    MiniDFSCluster cluster = null;
    try {
      Configuration conf = new Configuration();
      cluster = new MiniDFSCluster(conf, 1, true, null);
      namenode = FileSystem.getDefaultUri(conf).toString();
      String [] args = new String[4];
      args[2] = "-mkdir";
      args[3] = "/data";
      testFsOption(args, namenode);
      testConfOption(args, namenode);
      testPropertyOption(args, namenode);
    } finally {
      if (cluster != null) { cluster.shutdown(); }
    }
  }

  private void testFsOption(String [] args, String namenode) {        
    // prepare arguments to create a directory /data
    args[0] = "-fs";
    args[1] = namenode;
    execute(args, namenode);
  }
    
  private void testConfOption(String[] args, String namenode) {
    // prepare configuration hdfs-site.xml
    File configDir = new File(new File("build", "test"), "minidfs");
    assertTrue(configDir.mkdirs());
    File siteFile = new File(configDir, "hdfs-site.xml");
    PrintWriter pw;
    try {
      pw = new PrintWriter(siteFile);
      pw.print("<?xml version=\"1.0\"?>\n"+
               "<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>\n"+
               "<configuration>\n"+
               " <property>\n"+
               "   <name>fs.default.name</name>\n"+
               "   <value>"+namenode+"</value>\n"+
               " </property>\n"+
               "</configuration>\n");
      pw.close();
    
      // prepare arguments to create a directory /data
      args[0] = "-conf";
      args[1] = siteFile.getPath();
      execute(args, namenode); 
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } finally {
      siteFile.delete();
      configDir.delete();
    }
  }
    
  private void testPropertyOption(String[] args, String namenode) {
    // prepare arguments to create a directory /data
    args[0] = "-D";
    args[1] = "fs.default.name="+namenode;
    execute(args, namenode);        
  }
    
  private void execute(String [] args, String namenode) {
    FsShell shell=new FsShell();
    FileSystem fs=null;
    try {
      ToolRunner.run(shell, args);
      fs = new DistributedFileSystem(NameNode.getAddress(namenode), 
                                     shell.getConf());
      assertTrue("Directory does not get created", 
                 fs.isDirectory(new Path("/data")));
      fs.delete(new Path("/data"), true);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    } finally {
      if (fs!=null) {
        try {
          fs.close();
        } catch (IOException ignored) {
        }
      }
    }
  }

}
