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
package org.apache.hadoop.fs;


import junit.framework.TestCase;
import java.io.File;
import java.io.IOException;
import java.io.DataOutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.Trash;
import org.apache.hadoop.fs.LocalFileSystem;

/**
 * This class tests commands from Trash.
 */
public class TestTrash extends TestCase {

  private final static Path TEST_DIR =
    new Path(new File(System.getProperty("test.build.data","/tmp")
          ).toURI().toString().replace(' ', '+'), "testTrash");

  protected static Path writeFile(FileSystem fs, Path f) throws IOException {
    DataOutputStream out = fs.create(f);
    out.writeBytes("dhruba: " + f);
    out.close();
    assertTrue(fs.exists(f));
    return f;
  }

  protected static Path mkdir(FileSystem fs, Path p) throws IOException {
    assertTrue(fs.mkdirs(p));
    assertTrue(fs.exists(p));
    assertTrue(fs.getFileStatus(p).isDir());
    return p;
  }

  // check that the specified file is in Trash
  protected static void checkTrash(FileSystem fs, Path trashRoot,
      Path path) throws IOException {
    Path p = new Path(trashRoot+"/"+ path.toUri().getPath());
    assertTrue(fs.exists(p));
  }

  // check that the specified file is not in Trash
  static void checkNotInTrash(FileSystem fs, Path trashRoot, String pathname)
                              throws IOException {
    Path p = new Path(trashRoot+"/"+ new Path(pathname).getName());
    assertTrue(!fs.exists(p));
  }

  protected static void trashShell(final FileSystem fs, final Path base)
      throws IOException {
    Configuration conf = new Configuration();
    conf.set("fs.trash.interval", "10"); // 10 minute
    conf.set("fs.default.name", fs.getUri().toString());
    FsShell shell = new FsShell();
    shell.setConf(conf);
    Path trashRoot = null;

    // First create a new directory with mkdirs
    Path myPath = new Path(base, "test/mkdirs");
    mkdir(fs, myPath);

    // Second, create a file in that directory.
    Path myFile = new Path(base, "test/mkdirs/myFile");
    writeFile(fs, myFile);

    // Verify that expunge without Trash directory
    // won't throw Exception
    {
      String[] args = new String[1];
      args[0] = "-expunge";
      int val = -1;
      try {
        val = shell.run(args);
      } catch (Exception e) {
        System.err.println("Exception raised from Trash.run " +
                           e.getLocalizedMessage());
      }
      assertTrue(val == 0);
    }

    // Verify that we succeed in removing the file we created.
    // This should go into Trash.
    {
      String[] args = new String[2];
      args[0] = "-rm";
      args[1] = myFile.toString();
      int val = -1;
      try {
        val = shell.run(args);
      } catch (Exception e) {
        System.err.println("Exception raised from Trash.run " +
                           e.getLocalizedMessage());
      }
      assertTrue(val == 0);

      trashRoot = shell.getCurrentTrashDir();
      checkTrash(fs, trashRoot, myFile);
    }

    // Verify that we can recreate the file
    writeFile(fs, myFile);

    // Verify that we succeed in removing the file we re-created
    {
      String[] args = new String[2];
      args[0] = "-rm";
      args[1] = new Path(base, "test/mkdirs/myFile").toString();
      int val = -1;
      try {
        val = shell.run(args);
      } catch (Exception e) {
        System.err.println("Exception raised from Trash.run " +
                           e.getLocalizedMessage());
      }
      assertTrue(val == 0);
    }

    // Verify that we can recreate the file
    writeFile(fs, myFile);

    // Verify that we succeed in removing the whole directory
    // along with the file inside it.
    {
      String[] args = new String[2];
      args[0] = "-rmr";
      args[1] = new Path(base, "test/mkdirs").toString();
      int val = -1;
      try {
        val = shell.run(args);
      } catch (Exception e) {
        System.err.println("Exception raised from Trash.run " +
                           e.getLocalizedMessage());
      }
      assertTrue(val == 0);
    }

    // recreate directory
    mkdir(fs, myPath);

    // Verify that we succeed in removing the whole directory
    {
      String[] args = new String[2];
      args[0] = "-rmr";
      args[1] = new Path(base, "test/mkdirs").toString();
      int val = -1;
      try {
        val = shell.run(args);
      } catch (Exception e) {
        System.err.println("Exception raised from Trash.run " +
                           e.getLocalizedMessage());
      }
      assertTrue(val == 0);
    }

    // Check that we can delete a file from the trash
    {
        Path toErase = new Path(trashRoot, "toErase");
        int retVal = -1;
        writeFile(fs, toErase);
        try {
          retVal = shell.run(new String[] {"-rm", toErase.toString()});
        } catch (Exception e) {
          System.err.println("Exception raised from Trash.run " +
                             e.getLocalizedMessage());
        }
        assertTrue(retVal == 0);
        checkNotInTrash (fs, trashRoot, toErase.toString());
        checkNotInTrash (fs, trashRoot, toErase.toString()+".1");
    }

    // simulate Trash removal
    {
      String[] args = new String[1];
      args[0] = "-expunge";
      int val = -1;
      try {
        val = shell.run(args);
      } catch (Exception e) {
        System.err.println("Exception raised from Trash.run " +
                           e.getLocalizedMessage());
      }
      assertTrue(val == 0);
    }

    // verify that after expunging the Trash, it really goes away
    checkNotInTrash(fs, trashRoot, new Path(base, "test/mkdirs/myFile").toString());

    // recreate directory and file
    mkdir(fs, myPath);
    writeFile(fs, myFile);

    // remove file first, then remove directory
    {
      String[] args = new String[2];
      args[0] = "-rm";
      args[1] = myFile.toString();
      int val = -1;
      try {
        val = shell.run(args);
      } catch (Exception e) {
        System.err.println("Exception raised from Trash.run " +
                           e.getLocalizedMessage());
      }
      assertTrue(val == 0);
      checkTrash(fs, trashRoot, myFile);

      args = new String[2];
      args[0] = "-rmr";
      args[1] = myPath.toString();
      val = -1;
      try {
        val = shell.run(args);
      } catch (Exception e) {
        System.err.println("Exception raised from Trash.run " +
                           e.getLocalizedMessage());
      }
      assertTrue(val == 0);
      checkTrash(fs, trashRoot, myPath);
    }

    // attempt to remove parent of trash
    {
      String[] args = new String[2];
      args[0] = "-rmr";
      args[1] = trashRoot.getParent().getParent().toString();
      int val = -1;
      try {
        val = shell.run(args);
      } catch (Exception e) {
        System.err.println("Exception raised from Trash.run " +
                           e.getLocalizedMessage());
      }
      assertTrue(val == -1);
      assertTrue(fs.exists(trashRoot));
    }
    
    // Verify skip trash option really works
    
    // recreate directory and file
    mkdir(fs, myPath);
    writeFile(fs, myFile);
    
    // Verify that skip trash option really skips the trash for files (rm)
    {
      String[] args = new String[3];
      args[0] = "-rm";
      args[1] = "-skipTrash";
      args[2] = myFile.toString();
      int val = -1;
      try {
        // Clear out trash
        assertEquals(0, shell.run(new String [] { "-expunge" } ));

        val = shell.run(args);
        
      }catch (Exception e) {
        System.err.println("Exception raised from Trash.run " +
            e.getLocalizedMessage());
      }
      
      assertFalse(fs.exists(trashRoot)); // No new Current should be created
      assertFalse(fs.exists(myFile));
      assertTrue(val == 0);
    }
    
    // recreate directory and file
    mkdir(fs, myPath);
    writeFile(fs, myFile);
    
    // Verify that skip trash option really skips the trash for rmr
    {
      String[] args = new String[3];
      args[0] = "-rmr";
      args[1] = "-skipTrash";
      args[2] = myPath.toString();

      int val = -1;
      try {
        // Clear out trash
        assertEquals(0, shell.run(new String [] { "-expunge" } ));
        
        val = shell.run(args);
        
      }catch (Exception e) {
        System.err.println("Exception raised from Trash.run " +
            e.getLocalizedMessage());
      }

      assertFalse(fs.exists(trashRoot)); // No new Current should be created
      assertFalse(fs.exists(myPath));
      assertFalse(fs.exists(myFile));
      assertTrue(val == 0);
    }
  }

  public static void trashNonDefaultFS(Configuration conf) throws IOException {
    conf.set("fs.trash.interval", "10"); // 10 minute
    // attempt non-default FileSystem trash
    {
      final FileSystem lfs = FileSystem.getLocal(conf);
      Path p = TEST_DIR;
      Path f = new Path(p, "foo/bar");
      if (lfs.exists(p)) {
        lfs.delete(p, true);
      }
      try {
        f = writeFile(lfs, f);

        FileSystem.closeAll();
        FileSystem localFs = FileSystem.get(URI.create("file:///"), conf);
        Trash lTrash = new Trash(localFs, conf);
        lTrash.moveToTrash(f.getParent());
        checkTrash(localFs, lTrash.getCurrentTrashDir(), f);
      } finally {
        if (lfs.exists(p)) {
          lfs.delete(p, true);
        }
      }
    }
  }

  public void testTrash() throws IOException {
    Configuration conf = new Configuration();
    conf.setClass("fs.file.impl", TestLFS.class, FileSystem.class);
    trashShell(FileSystem.getLocal(conf), TEST_DIR);
  }

  public void testNonDefaultFS() throws IOException {
    Configuration conf = new Configuration();
    conf.setClass("fs.file.impl", TestLFS.class, FileSystem.class);
    conf.set("fs.default.name", "invalid://host/bar/foo");
    trashNonDefaultFS(conf);
  }

  static class TestLFS extends LocalFileSystem {
    Path home;
    TestLFS() {
      this(TEST_DIR);
    }
    TestLFS(Path home) {
      super();
      this.home = home;
    }
    public Path getHomeDirectory() {
      return home;
    }
  }
}
