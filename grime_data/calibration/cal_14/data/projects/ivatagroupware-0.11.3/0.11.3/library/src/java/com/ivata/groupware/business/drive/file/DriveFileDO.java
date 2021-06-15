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
package com.ivata.groupware.business.drive.file;

import com.ivata.groupware.business.drive.directory.DirectoryDO;


/**
 * <p>Encapsulates a single file of the drive, but doesn't
 * contain its
 * content. For content @link FileContentDO is used</p>
 *
 * @since 2003-04-10
 * @author Peter Illes
 * @version $Revision: 1.2 $
 */
public class DriveFileDO extends FileDO {
    /**
     * <p>The parent directory</p>
     */
    private DirectoryDO directory;

    /**
     * <p>The most recent revision of this file.</p>
     */
    private FileRevisionDO headRevision;

    /**
     * </p>the path (directory) of the file</p>
     */
    private String path;

    /**
     * <p>The parent directory</p>
     *
     * @return the parent directory
     * @hibernate.many-to-one
     */
    public final DirectoryDO getDirectory() {
        return directory;
    }

    /**
     * <p>Get the head revision  - the most recent CVS revision info
     * for this file</p>
     *
     * @return the last CVS revision info for this file
     * @hibernate.many-to-one
     */
    public FileRevisionDO getHeadRevision() {
        return this.headRevision;
    }

    /**
     * <p>Get the path of the file</p>
     *
     * @return the path of the file
     * @hibernate.property
     */
    public final String getPath() {
        return this.path;
    }

    /**
     * <p>The parent directory</p>
     *
     * @param directory the parent directory
     */
    public final void setDirectory(final DirectoryDO directory) {
        this.directory = directory;
    }

    /**
     * <p>Get the head revision  - the most recent CVS revision info
     * for this file</p>
     *
     * @param headRevision the last CVS revision info for this file
     */
    public final void setHeadRevision(final FileRevisionDO headRevision) {
        this.headRevision = headRevision;
    }

    /**
     * <p>Set the path of the file.</p>
     *
     * @param path the file path (directory)
     */
    public final void setPath(final String path) {
        this.path = path;
    }
}
