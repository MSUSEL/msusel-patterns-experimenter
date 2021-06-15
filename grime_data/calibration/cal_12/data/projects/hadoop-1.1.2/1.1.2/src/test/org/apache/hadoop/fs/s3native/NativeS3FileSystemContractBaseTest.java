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
package org.apache.hadoop.fs.s3native;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystemContractBaseTest;
import org.apache.hadoop.fs.Path;

public abstract class NativeS3FileSystemContractBaseTest
  extends FileSystemContractBaseTest {
  
  private NativeFileSystemStore store;
  
  abstract NativeFileSystemStore getNativeFileSystemStore() throws IOException;

  @Override
  protected void setUp() throws Exception {
    Configuration conf = new Configuration();
    store = getNativeFileSystemStore();
    fs = new NativeS3FileSystem(store);
    fs.initialize(URI.create(conf.get("test.fs.s3n.name")), conf);
  }
  
  @Override
  protected void tearDown() throws Exception {
    store.purge("test");
    super.tearDown();
  }
  
  public void testListStatusForRoot() throws Exception {
    Path testDir = path("/test");
    assertTrue(fs.mkdirs(testDir));
    
    FileStatus[] paths = fs.listStatus(path("/"));
    assertEquals(1, paths.length);
    assertEquals(path("/test"), paths[0].getPath());
  }

  private void createTestFiles(String base) throws IOException {
    store.storeEmptyFile(base + "/file1");
    store.storeEmptyFile(base + "/dir/file2");
    store.storeEmptyFile(base + "/dir/file3");
  }

  public void testDirWithDifferentMarkersWorks() throws Exception {

    for (int i = 0; i < 3; i++) {
      String base = "test/hadoop" + i;
      Path path = path("/" + base);

      createTestFiles(base);

      if (i == 0 ) {
        //do nothing, we are testing correctness with no markers
      }
      else if (i == 1) {
        // test for _$folder$ marker
        store.storeEmptyFile(base + "_$folder$");
        store.storeEmptyFile(base + "/dir_$folder$");
      }
      else if (i == 2) {
        // test the end slash file marker
        store.storeEmptyFile(base + "/");
        store.storeEmptyFile(base + "/dir/");
      }
      else if (i == 3) {
        // test both markers
        store.storeEmptyFile(base + "_$folder$");
        store.storeEmptyFile(base + "/dir_$folder$");
        store.storeEmptyFile(base + "/");
        store.storeEmptyFile(base + "/dir/");
      }

      assertTrue(fs.getFileStatus(path).isDir());
      assertEquals(2, fs.listStatus(path).length);
    }
  }

  public void testDeleteWithNoMarker() throws Exception {
    String base = "test/hadoop";
    Path path = path("/" + base);

    createTestFiles(base);

    fs.delete(path, true);

    path = path("/test");
  }

  public void testRenameWithNoMarker() throws Exception {
    String base = "test/hadoop";
    Path dest = path("/test/hadoop2");

    createTestFiles(base);

    fs.rename(path("/" + base), dest);

    Path path = path("/test");
    assertTrue(fs.getFileStatus(path).isDir());
    assertEquals(1, fs.listStatus(path).length);
    assertTrue(fs.getFileStatus(dest).isDir());
    assertEquals(2, fs.listStatus(dest).length);
  }

  public void testEmptyFile() throws Exception {
    store.storeEmptyFile("test/hadoop/file1");
    fs.open(path("/test/hadoop/file1")).close();
  }
}
