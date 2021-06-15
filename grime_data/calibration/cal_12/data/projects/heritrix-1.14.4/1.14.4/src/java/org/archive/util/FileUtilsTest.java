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
package org.archive.util;

import java.io.File;
import java.io.IOException;


/**
 * @author stack
 * @version $Date: 2006-09-20 22:40:21 +0000 (Wed, 20 Sep 2006) $, $Revision: 4644 $
 */
public class FileUtilsTest extends TmpDirTestCase {
    private String srcDirName = FileUtilsTest.class.getName() + ".srcdir";
    private File srcDirFile = null;
    private String tgtDirName = FileUtilsTest.class.getName() + ".tgtdir";
    private File tgtDirFile = null;
    
    protected void setUp() throws Exception {
        super.setUp();
        this.srcDirFile = new File(getTmpDir(), srcDirName);
        this.srcDirFile.mkdirs();
        this.tgtDirFile = new File(getTmpDir(), tgtDirName);
        this.tgtDirFile.mkdirs();
        addFiles();
    }
 
    private void addFiles() throws IOException {
        addFiles(3, this.getName());
    }
    
    private void addFiles(final int howMany, final String baseName)
    throws IOException {
        for (int i = 0; i < howMany; i++) {
            File.createTempFile(baseName, null, this.srcDirFile);
        }
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        FileUtils.deleteDir(this.srcDirFile);
        FileUtils.deleteDir(this.tgtDirFile);
    }

    public void testCopyFiles() throws IOException {
        FileUtils.copyFiles(this.srcDirFile, this.tgtDirFile);
        File [] srcFiles = this.srcDirFile.listFiles();
        for (int i = 0; i < srcFiles.length; i++) {
            File tgt = new File(this.tgtDirFile, srcFiles[i].getName());
            assertTrue("Tgt doesn't exist " + tgt.getAbsolutePath(),
                tgt.exists());
        }
    }
    
    public void testCopyFile() {
        // Test exception copying nonexistent file.
        File [] srcFiles = this.srcDirFile.listFiles();
        srcFiles[0].delete();
        IOException e = null;
        try {
        FileUtils.copyFile(srcFiles[0],
            new File(this.tgtDirFile, srcFiles[0].getName()));
        } catch (IOException ioe) {
            e = ioe;
        }
        assertNotNull("Didn't get expected IOE", e);
    }
    
    public void testSyncDirectories() throws IOException {
        FileUtils.syncDirectories(this.srcDirFile, null, this.tgtDirFile);
        addFiles(1, "xxxxxx");
        FileUtils.syncDirectories(this.srcDirFile, null, this.tgtDirFile);
        assertEquals("Not equal", this.srcDirFile.list().length,
            this.tgtDirFile.list().length);
    }
}
