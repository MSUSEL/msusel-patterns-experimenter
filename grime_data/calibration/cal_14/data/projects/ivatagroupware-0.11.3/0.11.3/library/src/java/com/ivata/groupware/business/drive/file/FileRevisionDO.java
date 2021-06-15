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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * <p>Encapsulates a revision of a file in the virtual drive</p>
 * @since 2002-12-30
 * @author Peter Illes
 * @version $Revision: 1.2 $
 */
public class FileRevisionDO implements Serializable {
    /**
     * <p>Stores the description (comment) of the revision.</p>
     *
     */
    private String comment;

    /**
     * <p>the revision 'code'</p>
     *
     */
    private String revision;

    /**
     * <p>Stores the name of the user who submitted this revision.
     * </p>
     *
     */
    private String userName;

    /**
     * <p>Stores the date and time when this  revision was made</p>
     */
    private java.util.Date revised;

    /**
     * <p>Serialize the object from the input stream provided.</p>
     *
     * @param ois the input stream to serialize the object from
     * @throws IOException thrown by
     * <code>ObjectInputStream.defaultReadObject()</code>.
     * @throws ClassNotFoundException thrown by
     * <code>ObjectInputStream.defaultReadObject()</code>.
     *
     * @roseuid 3E22C2570157
     */
    private void readObject(final ObjectInputStream ois)
        throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

    /**
     * <p>Serialize the object to the output stream provided.</p>
     *
     * @param oos the output stream to serialize the object to
     * @throws IOException thrown by
     * <code>ObjectOutputStream.defaultWriteObject()</code>
     *
     * @roseuid 3E22C2570187
     */
    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * <p>sets the comment field</p>
     * @param comment the descriptive text of this revision
     *
     * @roseuid 3E22C2570189
     */
    public final void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * <p>gets the comment field</p>
     * @return the descriptive text of this revision
     *
     * @roseuid 3E22C257018B
     */
    public final String getComment() {
        return comment;
    }

    /**
     * <p>sets the revision number (code) of this revision</p>
     * @param headRevision the number of the last revision for this file
     *
     * @roseuid 3E22C257018C
     */
    public final void setRevision(final String revision) {
        this.revision = revision;
    }

    /**
     * <p>sets the head revision number - the most recent CVS revision
     * number
     * for this file</p>
     * @return the code of the last CVS revision for this file
     *
     * @roseuid 3E22C257018E
     */
    public final String getRevision() {
        return revision;
    }

    /**
     * <p>getter for the date and time the file was modified to this
     * revision</p>
     * @return the date of this revision
     *
     * @roseuid 3E22C257018F
     */
    public final java.util.Date getRevised() {
        return revised;
    }

    /**
     * <p>setter for the date and time the file was revised</p>
     * @param modified the date of the revision
     *
     * @roseuid 3E22C2570190
     */
    public final void setRevised(final java.util.Date revised) {
        this.revised = revised;
    }

    /**
     * <p>getter of the username of the user who submitted this
     * revision.<p>
     * @return the name of the user who submitted this revision.
     *
     * @roseuid 3E22C2570192
     */
    public final String getUserName() {
        return userName;
    }

    /**
     * <p>sets the username of the user who submitted this revision.<p>
     * @param userName the userName of the user who submitted this
     * revision
     *
     * @roseuid 3E22C25701C2
     */
    public final void setUserName(final String userName) {
        this.userName = userName;
    }
}
