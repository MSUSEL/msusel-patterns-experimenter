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
package org.apache.james.imapserver;

import java.util.Iterator;

/**
 * Interface for objects representing a Repository of FolderRecords.
 * There should be a RecordRepository for every Host.
 * <p>Note that there is no method for removing Records: an IMAP host is
 * meant to retain information about deleted folders.
 *
 * @version 0.1 on 14 Dec 2000
 * @see FolderRecord
 */
public interface RecordRepository {

    String RECORD = "RECORD";

    /**
     * Sets the location of this repository.
     *
     * @param rootPath String location of this repository
     */
    void setPath( String rootPath );

    /**
     * Stores a folder record in this repository.
     *
     * @param fr FolderRecord to be stored
     */
    void store( FolderRecord fr );
      
    /**
     * Returns Iterator over names of folders in repository
     *
     * @return Iterator over Strings of AbsoluteNames of Folders. Calling
     * objects cannot change contents of Iterator.
     */
    Iterator getAbsoluteNames();

    /**
     * Retrieves a folder record given the folder's full name. 
     *
     * @param folderAbsoluteName String name of a folder
     * @return FolderRecord for specified folder, null if no such FolderRecord
     */
    FolderRecord retrieve( String folderAbsoluteName );
    
    /**
     * Tests if there is a folder record for the given folder name.
     *
     * @param folderAbsoluteName String name of a folder
     * @return boolean True if there is a record for the specified folder.
     */
    boolean containsRecord( String folderAbsoluteName );

    /**
     * Returns the a unique UID validity value for this Host.
     * UID validity values are used to differentiate messages in 2 mailboxes with the same names
     * (when one is deleted).
     */
    int nextUIDValidity();

    /**
     * Deletes the FolderRecord from the repository.
     */
    void deleteRecord( FolderRecord record );
}

    
