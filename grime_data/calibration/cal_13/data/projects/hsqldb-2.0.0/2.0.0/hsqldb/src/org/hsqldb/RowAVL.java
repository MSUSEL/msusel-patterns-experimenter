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

package org.hsqldb;

import org.hsqldb.index.NodeAVL;
import org.hsqldb.lib.java.JavaSystem;
import org.hsqldb.persist.PersistentStore;

// fredt@users 20020221 - patch 513005 by sqlbob@users (RMP)
// fredt@users 20020920 - patch 1.7.1 - refactoring to cut mamory footprint
// fredt@users 20021215 - doc 1.7.2 - javadoc comments

/**
 * Base class for a database row object implementing rows for
 * memory resident tables.<p>
 *
 * Subclass RowAVLDisk implements rows for CACHED and TEXT tables.<p>
 * New class derived from Hypersonic SQL code and enhanced in HSQLDB.<p>
 *
 * @author Thomas Mueller (Hypersonic SQL Group)
 * @version 1.9.0
 * @since Hypersonic SQL
 */
public class RowAVL extends Row {

    public NodeAVL nPrimaryNode;

    /**
     *  Default constructor used only in subclasses.
     */
    protected RowAVL(TableBase table, Object[] data) {
        super(table, data);
    }

    /**
     *  Constructor for MEMORY table Row. The result is a Row with Nodes that
     *  are not yet linked with other Nodes in the AVL indexes.
     */
    public RowAVL(TableBase table, Object[] data, int position) {

        super(table, data);

        this.position = position;

        setNewNodes();
    }

    public void setNewNodes() {

        int indexCount = table.getIndexCount();

        nPrimaryNode = new NodeAVL(this);

        NodeAVL n = nPrimaryNode;

        for (int i = 1; i < indexCount; i++) {
            n.nNext = new NodeAVL(this);
            n       = n.nNext;
        }
    }

    /**
     * Returns the Node for a given Index, using the ordinal position of the
     * Index within the Table Object.
     */
    public NodeAVL getNode(int index) {

        NodeAVL n = nPrimaryNode;

        while (index-- > 0) {
            n = n.nNext;
        }

        return n;
    }

    /**
     *  Returns the Node for the next Index on this database row, given the
     *  Node for any Index.
     */
    NodeAVL getNextNode(NodeAVL n) {

        if (n == null) {
            n = nPrimaryNode;
        } else {
            n = n.nNext;
        }

        return n;
    }

    public NodeAVL insertNode(int index) {

        NodeAVL backnode = getNode(index - 1);
        NodeAVL newnode  = new NodeAVL(this);

        newnode.nNext  = backnode.nNext;
        backnode.nNext = newnode;

        return newnode;
    }

    public void clearNonPrimaryNodes() {

        NodeAVL n = nPrimaryNode.nNext;

        while (n != null) {
            n.delete();

            n.iBalance = 0;
            n          = n.nNext;
        }
    }

    public void delete(PersistentStore store) {

        NodeAVL n = nPrimaryNode;

        while (n != null) {
            n.delete();

            n = n.nNext;
        }
    }

    public void restore() {}

    public void destroy() {

        JavaSystem.memoryRecords++;

        clearNonPrimaryNodes();

        NodeAVL n = nPrimaryNode.nNext;

        while (n != null) {
            NodeAVL last = n;

            n          = n.nNext;
            last.nNext = null;
        }

        nPrimaryNode = null;
    }
}
