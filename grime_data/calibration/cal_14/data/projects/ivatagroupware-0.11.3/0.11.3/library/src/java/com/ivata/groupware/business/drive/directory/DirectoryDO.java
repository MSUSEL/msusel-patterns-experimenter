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
package com.ivata.groupware.business.drive.directory;

import com.ivata.groupware.container.persistence.TimestampDO;
import com.ivata.groupware.web.tree.TreeNode;


/**
 * <p>Stores a directory of the virtual drive</p>
 *
 * @author  Peter Illes
 * @since   2003-07-18
 * @version $Revision: 1.3 $
 */
public class DirectoryDO extends TimestampDO implements TreeNode {
    /**
     * <p>Name of the directory</p>
     */
    private String name;

    /**
     * <p>Parent directory</p>
     */
    private DirectoryDO parent;

    /**
     * <p>Get the name of the directory</p>
     *
     * @return the directory name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Get the parent directory</p>
     *
     * @return the parent directory
     */
    public DirectoryDO getParent() {
        return parent;
    }

    /**
     * <p>Set the name of the directory</p>
     *
     * @param name the directory name
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * <p>Set the parent directory</p>
     *
     * @param parent the id of parent directory
     */
    public final void setParent(final DirectoryDO parent) {
        this.parent = parent;
    }
}
