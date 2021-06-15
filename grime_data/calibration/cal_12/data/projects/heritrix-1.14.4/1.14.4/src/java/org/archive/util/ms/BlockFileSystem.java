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
/* BlockFileSystem
*
* Created on September 12, 2006
*
* Copyright (C) 2006 Internet Archive.
*
* This file is part of the Heritrix web crawler (crawler.archive.org).
*
* Heritrix is free software; you can redistribute it and/or modify
* it under the terms of the GNU Lesser Public License as published by
* the Free Software Foundation; either version 2.1 of the License, or
* any later version.
*
* Heritrix is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser Public License for more details.
*
* You should have received a copy of the GNU Lesser Public License
* along with Heritrix; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org.archive.util.ms;

import java.io.IOException;

import org.archive.io.SeekInputStream;


/**
 * Describes the internal file system contained in .doc files.
 */
public interface BlockFileSystem {

    
    /**
     * The size of a block in bytes.
     */
    int BLOCK_SIZE = 512;


    /**
     * Returns the root entry of the file system.  Subfiles and directories
     * can be found by searching the returned entry.
     * 
     * @return  the root entry
     * @throws IOException  if an IO error occurs
     */
    public abstract Entry getRoot() throws IOException;

    
    /**
     * Returns the number of the block that follows the given block.
     * The internal block allocation tables are consulted to determine the
     * next block.  A return value that is less than zero indicates that
     * there is no next block.
     * 
     * @param block   the number of block whose successor to return
     * @return  the successor of that block
     * @throws IOException  if an IO error occurs
     */
    public abstract int getNextBlock(int block) throws IOException;


    /**
     * Returns the raw input stream for this file system.  
     * Typically this will be the random access file containing the .doc.
     * 
     * @return  the raw input stream for this file system
     */
    public abstract SeekInputStream getRawInput();

}