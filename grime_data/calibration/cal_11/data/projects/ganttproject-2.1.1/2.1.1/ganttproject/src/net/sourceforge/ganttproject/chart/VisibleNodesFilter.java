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
package net.sourceforge.ganttproject.chart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.sourceforge.ganttproject.task.Task;

/**
 * Created by IntelliJ IDEA. User: bard
 */
public class VisibleNodesFilter {
    public List/* <Task> */getVisibleNodes(JTree jtree, int minHeight,
            int maxHeight, int nodeHeight) {
        List preorderedNodes = Collections.list(((DefaultMutableTreeNode) jtree
                .getModel().getRoot()).preorderEnumeration());
        List result = new ArrayList();
        int currentHeight = 0;
        for (int i = 1; i < preorderedNodes.size(); i++) {
            DefaultMutableTreeNode nextNode = (DefaultMutableTreeNode) preorderedNodes
                    .get(i);
            if (false==nextNode.getUserObject() instanceof Task) {
                continue;
            }
            if ((currentHeight+nodeHeight) > minHeight
                    && jtree.isVisible(new TreePath(nextNode.getPath()))) {
                result.add(nextNode.getUserObject());
            }
            if (jtree.isVisible(new TreePath(nextNode.getPath()))) {
                currentHeight += nodeHeight;
            }
            if(currentHeight > minHeight + maxHeight) {
                break;
            }
        }
        return result;
    }

}
