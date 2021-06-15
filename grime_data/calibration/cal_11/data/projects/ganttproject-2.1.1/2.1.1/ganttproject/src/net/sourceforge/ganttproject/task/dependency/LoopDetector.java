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
/* LICENSE: GPL
 * This code is provided under the terms of GPL version 2.
 * Please see LICENSE file for details
 * (C) Dmitry Barashev, GanttProject team, 2004-2008
 */
package net.sourceforge.ganttproject.task.dependency;

import java.util.LinkedHashSet;
import java.util.Set;

import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskManager;

/**
 * Loop detector answers whether a dependency will create a loop in the dependency graph
 * @author dbarashev
 */
public class LoopDetector {
	private final TaskManager myTaskManager;
	public LoopDetector(TaskManager taskManager) {
		myTaskManager = taskManager;
	}
	public boolean isLooping(TaskDependency dep) {
		Set checked = new LinkedHashSet();
		checked.add(dep.getDependee());
		return isLooping(checked, dep.getDependant());
	}

	private boolean isLooping(Set checked, Task incoming) {
		boolean result = false;
		Set newChecked = new LinkedHashSet(checked);
		newChecked.add(incoming);
		TaskDependency[] nextDeps = incoming.getDependenciesAsDependee().toArray();
		for (int i=0; !result && i<nextDeps.length; i++) {
			if (!newChecked.contains(nextDeps[i].getDependant())) {
				result = isLooping(newChecked, nextDeps[i].getDependant());
			}
			else {
				result = true;
			}
		}
		if (!result) {
			Task supertask = myTaskManager.getTaskHierarchy().getContainer(incoming);
			if (supertask!=null && myTaskManager.getTaskHierarchy().getRootTask()!=supertask) {
				result = isLooping(newChecked, supertask);
			}
		}
		return result;
	}
}
