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
package org.apache.hadoop.io.file.tfile;

import java.io.IOException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.file.tfile.TFile.Reader;
import org.apache.hadoop.io.file.tfile.TFile.Writer;
import org.apache.hadoop.io.file.tfile.TFile.Reader.Scanner;

public class TestTFileUnsortedByteArrays extends TestCase {
  private static String ROOT =
      System.getProperty("test.build.data", "/tmp/tfile-test");


  private final static int BLOCK_SIZE = 512;
  private final static int BUF_SIZE = 64;

  private FileSystem fs;
  private Configuration conf;
  private Path path;
  private FSDataOutputStream out;
  private Writer writer;

  private String compression = Compression.Algorithm.GZ.getName();
  private String outputFile = "TFileTestUnsorted";
  /*
   * pre-sampled numbers of records in one block, based on the given the
   * generated key and value strings
   */
  private int records1stBlock = 4314;
  private int records2ndBlock = 4108;

  public void init(String compression, String outputFile,
      int numRecords1stBlock, int numRecords2ndBlock) {
    this.compression = compression;
    this.outputFile = outputFile;
    this.records1stBlock = numRecords1stBlock;
    this.records2ndBlock = numRecords2ndBlock;
  }

  @Override
  public void setUp() throws IOException {
    conf = new Configuration();
    path = new Path(ROOT, outputFile);
    fs = path.getFileSystem(conf);
    out = fs.create(path);
    writer = new Writer(out, BLOCK_SIZE, compression, null, conf);
    writer.append("keyZ".getBytes(), "valueZ".getBytes());
    writer.append("keyM".getBytes(), "valueM".getBytes());
    writer.append("keyN".getBytes(), "valueN".getBytes());
    writer.append("keyA".getBytes(), "valueA".getBytes());
    closeOutput();
  }

  @Override
  public void tearDown() throws IOException {
    fs.delete(path, true);
  }

  // we still can scan records in an unsorted TFile
  public void testFailureScannerWithKeys() throws IOException {
    Reader reader =
        new Reader(fs.open(path), fs.getFileStatus(path).getLen(), conf);
    Assert.assertFalse(reader.isSorted());
    Assert.assertEquals((int) reader.getEntryCount(), 4);

    try {
      Scanner scanner =
          reader.createScannerByKey("aaa".getBytes(), "zzz".getBytes());
      Assert
          .fail("Failed to catch creating scanner with keys on unsorted file.");
    }
    catch (RuntimeException e) {
    }
    finally {
      reader.close();
    }
  }

  // we still can scan records in an unsorted TFile
  public void testScan() throws IOException {
    Reader reader =
        new Reader(fs.open(path), fs.getFileStatus(path).getLen(), conf);
    Assert.assertFalse(reader.isSorted());
    Assert.assertEquals((int) reader.getEntryCount(), 4);

    Scanner scanner = reader.createScanner();

    try {

      // read key and value
      byte[] kbuf = new byte[BUF_SIZE];
      int klen = scanner.entry().getKeyLength();
      scanner.entry().getKey(kbuf);
      Assert.assertEquals(new String(kbuf, 0, klen), "keyZ");

      byte[] vbuf = new byte[BUF_SIZE];
      int vlen = scanner.entry().getValueLength();
      scanner.entry().getValue(vbuf);
      Assert.assertEquals(new String(vbuf, 0, vlen), "valueZ");

      scanner.advance();

      // now try get value first
      vbuf = new byte[BUF_SIZE];
      vlen = scanner.entry().getValueLength();
      scanner.entry().getValue(vbuf);
      Assert.assertEquals(new String(vbuf, 0, vlen), "valueM");

      kbuf = new byte[BUF_SIZE];
      klen = scanner.entry().getKeyLength();
      scanner.entry().getKey(kbuf);
      Assert.assertEquals(new String(kbuf, 0, klen), "keyM");
    }
    finally {
      scanner.close();
      reader.close();
    }
  }

  // we still can scan records in an unsorted TFile
  public void testScanRange() throws IOException {
    Reader reader =
        new Reader(fs.open(path), fs.getFileStatus(path).getLen(), conf);
    Assert.assertFalse(reader.isSorted());
    Assert.assertEquals((int) reader.getEntryCount(), 4);

    Scanner scanner = reader.createScanner();

    try {

      // read key and value
      byte[] kbuf = new byte[BUF_SIZE];
      int klen = scanner.entry().getKeyLength();
      scanner.entry().getKey(kbuf);
      Assert.assertEquals(new String(kbuf, 0, klen), "keyZ");

      byte[] vbuf = new byte[BUF_SIZE];
      int vlen = scanner.entry().getValueLength();
      scanner.entry().getValue(vbuf);
      Assert.assertEquals(new String(vbuf, 0, vlen), "valueZ");

      scanner.advance();

      // now try get value first
      vbuf = new byte[BUF_SIZE];
      vlen = scanner.entry().getValueLength();
      scanner.entry().getValue(vbuf);
      Assert.assertEquals(new String(vbuf, 0, vlen), "valueM");

      kbuf = new byte[BUF_SIZE];
      klen = scanner.entry().getKeyLength();
      scanner.entry().getKey(kbuf);
      Assert.assertEquals(new String(kbuf, 0, klen), "keyM");
    }
    finally {
      scanner.close();
      reader.close();
    }
  }

  public void testFailureSeek() throws IOException {
    Reader reader =
        new Reader(fs.open(path), fs.getFileStatus(path).getLen(), conf);
    Scanner scanner = reader.createScanner();

    try {
      // can't find ceil
      try {
        scanner.lowerBound("keyN".getBytes());
        Assert.fail("Cannot search in a unsorted TFile!");
      }
      catch (Exception e) {
        // noop, expecting excetions
      }
      finally {
      }

      // can't find higher
      try {
        scanner.upperBound("keyA".getBytes());
        Assert.fail("Cannot search higher in a unsorted TFile!");
      }
      catch (Exception e) {
        // noop, expecting excetions
      }
      finally {
      }

      // can't seek
      try {
        scanner.seekTo("keyM".getBytes());
        Assert.fail("Cannot search a unsorted TFile!");
      }
      catch (Exception e) {
        // noop, expecting excetions
      }
      finally {
      }
    }
    finally {
      scanner.close();
      reader.close();
    }
  }

  private void closeOutput() throws IOException {
    if (writer != null) {
      writer.close();
      writer = null;
      out.close();
      out = null;
    }
  }
}
