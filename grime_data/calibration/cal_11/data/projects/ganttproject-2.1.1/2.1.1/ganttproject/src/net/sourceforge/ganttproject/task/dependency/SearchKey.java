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
package net.sourceforge.ganttproject.task.dependency;

import net.sourceforge.ganttproject.task.Task;

public class SearchKey implements Comparable {
    static final int DEPENDANT = 1;

    static final int DEPENDEE = 2;

    final int myFirstTaskID;

    final int myType;

    final int mySecondTaskID;

    public SearchKey(int type, TaskDependencyImpl taskDependency) {
        myType = type;
        Task firstTask, secondTask;
        switch (type) {
        case DEPENDANT: {
            firstTask = taskDependency.getDependant();
            secondTask = taskDependency.getDependee();
            break;
        }
        case DEPENDEE: {
            firstTask = taskDependency.getDependee();
            secondTask = taskDependency.getDependant();
            break;
        }
        default: {
            throw new RuntimeException("Invalid type=" + type);
        }
        }
        myFirstTaskID = firstTask.getTaskID();
        mySecondTaskID = secondTask.getTaskID();
    }

    protected SearchKey(int type, int firstTaskID, int secondTaskID) {
        myType = type;
        myFirstTaskID = firstTaskID;
        mySecondTaskID = secondTaskID;
    }

    public int compareTo(Object o) {
        SearchKey rvalue = (SearchKey) o;
        int result = myFirstTaskID - rvalue.myFirstTaskID;
        if (result == 0) {
            result = myType - rvalue.myType;
        }
        if (result == 0) {
            result = mySecondTaskID - rvalue.mySecondTaskID;
        }
        return result;
    }

    public boolean equals(Object obj) {
        SearchKey rvalue = (SearchKey) obj;
        return myFirstTaskID == rvalue.myFirstTaskID && myType == rvalue.myType
                && mySecondTaskID == rvalue.mySecondTaskID;
    }

    public int hashCode() {
        return 7 * myFirstTaskID + 11 * myType + 13 * mySecondTaskID;
    }
}
