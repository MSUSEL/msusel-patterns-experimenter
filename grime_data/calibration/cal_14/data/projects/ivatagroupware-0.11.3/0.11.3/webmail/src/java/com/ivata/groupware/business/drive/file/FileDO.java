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

import com.ivata.groupware.container.persistence.BaseDO;


/**
 * <p>Encapsulates a single file, but doesn't
 * contain its
 * content. For content @link FileContentDO is used</p>
 *
 * @since 2002-12-20
 * @author Peter Illes
 * @version $Revision: 1.3 $
 */
public class FileDO extends BaseDO {

    /**
     * <p>Comment used when working with new library attachments.</p>
     */
    private String comment;

    /**
     * <p>Stores the mime-type text identifier of the content.</p>
     */
    private String mimeType;

    /**
     * </p>The name of the file.</p>
     */
    private String name;

    /**
     * <p>The size of this file in bytes</p>
     */
    private Integer size;

    /**
     * <p>comment used when working with new library attachments</p>
     * @return comment
     * @roseuid 3EBFBE2A0122
     */
    public final String getComment() {
        return comment;
    }

    /**
     * <p>Get the mime type of the file content.</p>
     * @return clear-text mime type of the file content.
     *
     * @roseuid 3E22C256019C
     */
    public final java.lang.String getMimeType() {
        return mimeType;
    }

    /**
     * <p>gets the fileName field</p>
     * @return fileName the filename
     *
     * @roseuid 3E22C2560160
     */
    public final String getName() {
        return name;
    }

    /**
     * <p>getter for size field - the size of the file in bytes</p>
     * @return the size of the file in bytes
     *
     * @roseuid 3E22C256019B
     */
    public final Integer getSize() {
        return size;
    }

    /**
     * <p>comment used when working with new library attachments</p>
     * @param comment
     * @roseuid 3EBFBE390118
     */
    public final void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * <p>sets the mime type of the file content.</p>
     * @param mimeType clear-text mime type identifier of the file
     * content.
     *
     * @roseuid 3E22C256019D
     */
    public final void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * <p>Sets the file name field.</p>
     * @param name new file name.
     *
     * @roseuid 3E22C256015E
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * <p>setter for size field - the size of the file in bytes</p>
     * @param size the size of the file
     *
     * @roseuid 3E22C2560167
     */
    public final void setSize(final Integer size) {
        this.size = size;
    }
}
