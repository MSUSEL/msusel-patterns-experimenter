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

import net.sourceforge.ganttproject.GanttCalendar;

/**
 * Created by IntelliJ IDEA. User: bard Date: 14.02.2004 Time: 2:35:20 To change
 * this template use File | Settings | File Templates.
 */
public interface TaskDependencyConstraint {
    void setTaskDependency(TaskDependency dependency);

    // boolean isFulfilled();
    // void fulfil();
    Collision getCollision();

    String getName();

    int getID();

    TaskDependency.ActivityBinding getActivityBinding();

    interface Collision {
        GanttCalendar getAcceptableStart();

        int getVariation();

        int NO_VARIATION = 0;

        int START_EARLIER_VARIATION = -1;

        int START_LATER_VARIATION = 1;

        boolean isActive();
    }

    class DefaultCollision implements Collision {
        private final GanttCalendar myAcceptableStart;

        private final int myVariation;

        private final boolean isActive;

        public DefaultCollision(GanttCalendar myAcceptableStart,
                int myVariation, boolean isActive) {
            this.myAcceptableStart = myAcceptableStart;
            this.myVariation = myVariation;
            this.isActive = isActive;
        }

        public GanttCalendar getAcceptableStart() {
            return myAcceptableStart;
        }

        public int getVariation() {
            return myVariation;
        }

        public boolean isActive() {
            return isActive;
        }

    }
}
