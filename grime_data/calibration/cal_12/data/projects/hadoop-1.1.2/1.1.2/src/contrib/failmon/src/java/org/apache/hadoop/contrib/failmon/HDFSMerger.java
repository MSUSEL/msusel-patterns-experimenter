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
package org.apache.hadoop.contrib.failmon;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FSDataInputStream;

public class HDFSMerger {

  Configuration hadoopConf;
  FileSystem hdfs;
  
  String hdfsDir;
  
  FileStatus [] inputFiles;

  Path outputFilePath;
  FSDataOutputStream outputFile;
    
  boolean compress;

  FileWriter fw;

  BufferedWriter writer;

  public HDFSMerger() throws IOException {

    String hadoopConfPath; 

    if (Environment.getProperty("hadoop.conf.path") == null)
      hadoopConfPath = "../../../conf";
    else
      hadoopConfPath = Environment.getProperty("hadoop.conf.path");

    // Read the configuration for the Hadoop environment
    Configuration hadoopConf = new Configuration();
    hadoopConf.addResource(new Path(hadoopConfPath + "/hadoop-default.xml"));
    hadoopConf.addResource(new Path(hadoopConfPath + "/hadoop-site.xml"));
    
    // determine the local output file name
    if (Environment.getProperty("local.tmp.filename") == null)
      Environment.setProperty("local.tmp.filename", "failmon.dat");
    
    // determine the upload location
    hdfsDir = Environment.getProperty("hdfs.upload.dir");
    if (hdfsDir == null)
      hdfsDir = "/failmon";

    hdfs = FileSystem.get(hadoopConf);
    
    Path hdfsDirPath = new Path(hadoopConf.get("fs.default.name") + hdfsDir);

    try {
      if (!hdfs.getFileStatus(hdfsDirPath).isDir()) {
	Environment.logInfo("HDFSMerger: Not an HDFS directory: " + hdfsDirPath.toString());
	System.exit(0);
      }
    } catch (FileNotFoundException e) {
      Environment.logInfo("HDFSMerger: Directory not found: " + hdfsDirPath.toString());
    }

    inputFiles = hdfs.listStatus(hdfsDirPath);

    outputFilePath = new Path(hdfsDirPath.toString() + "/" + "merge-"
			  + Calendar.getInstance().getTimeInMillis() + ".dat");
    outputFile = hdfs.create(outputFilePath);
    
    for (FileStatus fstatus : inputFiles) {
      appendFile(fstatus.getPath());
      hdfs.delete(fstatus.getPath());
    }

    outputFile.close();

    Environment.logInfo("HDFS file merging complete!");
  }

  private void appendFile (Path inputPath) throws IOException {
    
    FSDataInputStream anyInputFile = hdfs.open(inputPath);
    InputStream inputFile;
    byte buffer[] = new byte[4096];
    
    if (inputPath.toString().endsWith(LocalStore.COMPRESSION_SUFFIX)) {
      // the file is compressed
      inputFile = new ZipInputStream(anyInputFile);
      ((ZipInputStream) inputFile).getNextEntry();
    } else {
      inputFile = anyInputFile;
    }
    
    try {
      int bytesRead = 0;
      while ((bytesRead = inputFile.read(buffer)) > 0) {
	outputFile.write(buffer, 0, bytesRead);
      }
    } catch (IOException e) {
      Environment.logInfo("Error while copying file:" + inputPath.toString());
    } finally {
      inputFile.close();
    }    
  }

  
  public static void main(String [] args) {

    Environment.prepare("./conf/failmon.properties");

    try {
      new HDFSMerger();
    } catch (IOException e) {
      e.printStackTrace();
      }

  }
}
