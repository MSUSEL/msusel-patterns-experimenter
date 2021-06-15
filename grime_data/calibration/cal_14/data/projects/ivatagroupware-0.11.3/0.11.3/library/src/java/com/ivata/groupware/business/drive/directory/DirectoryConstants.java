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
// Source file: h:/cvslocal/ivata op/src/com.ivata.portal/business/drive/directory/DirectoryConstants.java
package com.ivata.groupware.business.drive.directory;


/**
 * <p>This class holds the ids of special folders in virtual drive</p>
 */
public class DirectoryConstants {
    /**
     * <p>The root directory, this directory has no parent</p>
     */
    public static final Integer ROOT_DIRECTORY = new Integer(0);

    /**
     * <p>The attached files of library items are kept inside
     * subdirectories of this directory</p>
     */
    public static final Integer LIBRARY_DIRECTORY = new Integer(1);

    /**
     <p>No navigation available (no history)</p>
     */
    public static final Integer NAVIGATION_NONE = new Integer(0);

    /**
     <p>Forward navigation available</p>
     */
    public static final Integer NAVIGATION_FORWARD = new Integer(1);

    /**
     <p>Backward navigation available</p>
     */
    public static final Integer NAVIGATION_BACKWARD = new Integer(2);

    /**
     <p>Forward and backward navigation both available</p>
     */
    public static final Integer NAVIGATION_BOTH = new Integer(3);
}
