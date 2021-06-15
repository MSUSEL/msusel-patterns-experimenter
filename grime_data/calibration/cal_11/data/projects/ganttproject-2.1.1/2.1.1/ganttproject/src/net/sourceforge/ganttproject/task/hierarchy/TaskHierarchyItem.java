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
package net.sourceforge.ganttproject.task.hierarchy;

import java.util.ArrayList;

import net.sourceforge.ganttproject.task.Task;

public class TaskHierarchyItem {
    private Task myTask;

    private TaskHierarchyItem myContainerItem;

    private TaskHierarchyItem myFirstNestedItem;

    private TaskHierarchyItem myNextSiblingItem;

    private static final TaskHierarchyItem[] EMPTY_ARRAY = new TaskHierarchyItem[0];

    public TaskHierarchyItem(Task myTask, TaskHierarchyItem containerItem) {
        this.myTask = myTask;
        this.myContainerItem = containerItem;
        if (myContainerItem != null)
        {
            myContainerItem.addNestedItem(this);
        }
    }

    public Task getTask() {
        return myTask;
    }

    public TaskHierarchyItem getContainerItem() {
        return myContainerItem;
    }

    public TaskHierarchyItem[] getNestedItems() {
        TaskHierarchyItem[] result;
        if (myFirstNestedItem == null) {
            result = EMPTY_ARRAY;
        } else {
            ArrayList tempList = new ArrayList();
            for (TaskHierarchyItem nested = myFirstNestedItem; nested != null; nested = nested.myNextSiblingItem) {
                tempList.add(nested);
            }
            result = (TaskHierarchyItem[]) tempList.toArray(EMPTY_ARRAY);
        }
        return result;
    }

    public void addNestedItem(TaskHierarchyItem nested) {
        nested.myNextSiblingItem = myFirstNestedItem;
        nested.myContainerItem = this;
        myFirstNestedItem = nested;
    }

    public void delete() {
        if (myContainerItem != null) {
            TaskHierarchyItem previousSibling = myContainerItem.myFirstNestedItem;
            if (this == previousSibling) {
                myContainerItem.myFirstNestedItem = myNextSiblingItem;
            } else {
                for (; previousSibling.myNextSiblingItem != this; previousSibling = previousSibling.myNextSiblingItem)
                    ;
                previousSibling.myNextSiblingItem = myNextSiblingItem;
            }
            myContainerItem = null;
        }
        myNextSiblingItem = null;
    }
}
