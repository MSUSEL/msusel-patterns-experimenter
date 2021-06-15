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

import junit.framework.TestCase;


/**
 * Base class for TestCases that want access to a tmp dir for the writing
 * of files.
 *
 * @author stack
 */
public abstract class TmpDirTestCase extends TestCase
{
    /**
     * Name of the system property that holds pointer to tmp directory into
     * which we can safely write files.
     */
    private static final String TEST_TMP_SYSTEM_PROPERTY_NAME = "testtmpdir";

    /**
     * Default test tmp.
     */
    private static final String DEFAULT_TEST_TMP_DIR = File.separator + "tmp" +
        File.separator + "heritrix-junit-tests";

    /**
     * Directory to write temporary files to.
     */
    private File tmpDir = null;


    public TmpDirTestCase()
    {
        super();
    }

    public TmpDirTestCase(String testName)
    {
        super(testName);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        String tmpDirStr = System.getProperty(TEST_TMP_SYSTEM_PROPERTY_NAME);
        tmpDirStr = (tmpDirStr == null)? DEFAULT_TEST_TMP_DIR: tmpDirStr;
        this.tmpDir = new File(tmpDirStr);
        if (!this.tmpDir.exists())
        {
            this.tmpDir.mkdirs();
        }

        if (!this.tmpDir.canWrite())
        {
            throw new IOException(this.tmpDir.getAbsolutePath() +
                 " is unwriteable.");
        }
    }

    /**
     * @return Returns the tmpDir.
     */
    public File getTmpDir()
    {
        return this.tmpDir;
    }

    /**
     * Delete any files left over from previous run.
     *
     * @param basename Base name of files we're to clean up.
     */
    public void cleanUpOldFiles(String basename) {
        cleanUpOldFiles(getTmpDir(), basename);
    }

    /**
     * Delete any files left over from previous run.
     *
     * @param prefix Base name of files we're to clean up.
     * @param basedir Directory to start cleaning in.
     */
    public void cleanUpOldFiles(File basedir, String prefix) {
        File [] files = FileUtils.getFilesWithPrefix(basedir, prefix);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                FileUtils.deleteDir(files[i]);
            }
        }
    }
}
