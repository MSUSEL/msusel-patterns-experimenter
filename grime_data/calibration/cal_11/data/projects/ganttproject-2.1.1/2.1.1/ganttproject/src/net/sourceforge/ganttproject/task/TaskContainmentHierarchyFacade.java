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
package net.sourceforge.ganttproject.task;

/**
 * Created by IntelliJ IDEA. User: bard
 */
public interface TaskContainmentHierarchyFacade {
	Task[] getNestedTasks(Task container);

	boolean hasNestedTasks(Task container);

	Task getRootTask();

	Task getContainer(Task nestedTask);

	void move(Task whatMove, Task whereMove);

	boolean areUnrelated(Task dependant, Task dependee);

	interface Factory {
		TaskContainmentHierarchyFacade createFacede();
	}

	int getDepth(Task task);

	TaskContainmentHierarchyFacade STUB = new TaskContainmentHierarchyFacade() {

		public Task[] getNestedTasks(Task container) {
			return new Task[0];
		}

		public boolean hasNestedTasks(Task container) {
			return false;
		}

		public Task getRootTask() {
			return null;
		}

		public Task getContainer(Task nestedTask) {
			return null;
		}

		public void move(Task whatMove, Task whereMove) {
		}

		public boolean areUnrelated(Task dependant, Task dependee) {
			return false;
		}

		public int getDepth(Task task) {
			return 0;
		}

        public int compareDocumentOrder(Task next, Task dependeeTask) {
            throw new UnsupportedOperationException();
        }

        public boolean contains(Task task) {
            throw new UnsupportedOperationException();
        }

	};

    int compareDocumentOrder(Task next, Task dependeeTask);

    boolean contains(Task task);

}
