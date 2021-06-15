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
package net.sourceforge.ganttproject.task.algorithm;

import java.util.ArrayList;

import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskContainmentHierarchyFacade;

/**
 * Created by IntelliJ IDEA. User: bard
 */
public abstract class FindPossibleDependeesAlgorithmImpl implements
        FindPossibleDependeesAlgorithm {
    private TaskContainmentHierarchyFacade myContainmentFacade;

    public FindPossibleDependeesAlgorithmImpl() {
    }

    public Task[] run(Task dependant) {
        myContainmentFacade = createContainmentFacade();
        ArrayList result = new ArrayList();
        Task root = myContainmentFacade.getRootTask();
        Task[] nestedTasks = myContainmentFacade.getNestedTasks(root);
        processTask(nestedTasks, dependant, result);
        return (Task[]) result.toArray(new Task[0]);
    }

    protected abstract TaskContainmentHierarchyFacade createContainmentFacade();

    private void processTask(Task[] taskList, Task dependant, ArrayList result) {
        for (int i = 0; i < taskList.length; i++) {
            Task next = taskList[i];
            if (!next.equals(dependant)) {
                Task[] nested = myContainmentFacade.getNestedTasks(next);
                // if (nested.length==0) {
                result.add(next);
                // }
                // else {
                processTask(nested, dependant, result);
                // }
            }
        }
    }
}
