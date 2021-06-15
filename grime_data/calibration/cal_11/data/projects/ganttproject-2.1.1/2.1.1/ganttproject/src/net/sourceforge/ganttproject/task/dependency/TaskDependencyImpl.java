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
import net.sourceforge.ganttproject.task.dependency.TaskDependency.Hardness;

/**
 * Created by IntelliJ IDEA. User: bard Date: 14.02.2004 Time: 15:28:59 To
 * change this template use File | Settings | File Templates.
 */
public class TaskDependencyImpl implements TaskDependency {
    private TaskDependencyConstraint myConstraint;

    private int myDifference;

    private final Task myDependant;

    private final Task myDependee;

    private Hardness myHardness;
    
    private TaskDependencyCollectionImpl myCollection;

    public TaskDependencyImpl(Task dependant, Task dependee,
            TaskDependencyCollectionImpl collection) {
        myDependant = dependant;
        myDependee = dependee;
        myCollection = collection;
        if (dependee == null || dependant == null) {
            throw new IllegalArgumentException(
                    "invalid participants of dependency: dependee=" + dependee
                            + " dependant=" + dependant);
        }
        myHardness = Hardness.STRONG;
    }

    public Task getDependant() {
        return myDependant;
    }

    public Task getDependee() {
        return myDependee;
    }

    public void setConstraint(TaskDependencyConstraint constraint) {
        myConstraint = constraint;
        constraint.setTaskDependency(this);
        myCollection.fireChanged(this);
    }

    public TaskDependencyConstraint getConstraint() {
        return myConstraint;
    }

    public ActivityBinding getActivityBinding() {
        return getConstraint().getActivityBinding();
    }

    public void delete() {
        myCollection.delete(this);
    }

    public boolean equals(Object obj) {
        boolean result = obj instanceof TaskDependency;
        if (result) {
            TaskDependency rvalue = (TaskDependency) obj;
            result = myDependant.equals(rvalue.getDependant())
                    && myDependee.equals(rvalue.getDependee());
        }
        return result;
    }

    public int hashCode() {
        return 7 * myDependant.hashCode() + 9 * myDependee.hashCode();
    }

    public void setDifference(int difference) {
        myDifference = difference;
    }

    public int getDifference() {
        return myDifference;
    }

	public Hardness getHardness() {
		return myHardness;
	}

	public void setHardness(Hardness hardness) {
		myHardness = hardness;
	}
	
	public String toString() {
		return myDependant+"->"+myDependee;
	}
}
