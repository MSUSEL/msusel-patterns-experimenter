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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskActivity;
import net.sourceforge.ganttproject.task.TaskLength;

/**
 * @author bard
 */
public class SortTasksAlgorithm {
    private Comparator mySortActivitiesByStartDateComparator = new Comparator() {
        public int compare(Object left, Object right) {
            int result = 0;
            TaskActivity leftTask = (TaskActivity) left;
            TaskActivity rightTask = (TaskActivity) right;
            if (!leftTask.equals(rightTask)) {
                result = leftTask.getStart().compareTo(rightTask.getStart());
                if (result == 0) {
                    float longResult = 0;
                    TaskLength leftLength = leftTask.getDuration();
                    TaskLength rightLength = rightTask.getDuration();
                    if (leftLength.getTimeUnit().isConstructedFrom(
                            rightLength.getTimeUnit())) {
                        longResult = leftLength.getLength(rightLength
                                .getTimeUnit())
                                - rightLength.getLength();
                    } else if (rightLength.getTimeUnit().isConstructedFrom(
                            leftLength.getTimeUnit())) {
                        longResult = leftLength.getLength()
                                - rightLength.getLength(leftLength
                                        .getTimeUnit());
                    } else {
                        throw new IllegalArgumentException("Lengths="
                                + leftLength + " and " + rightLength
                                + " are not compatible");
                    }
                    if (longResult != 0) {
                        result = (int) (longResult / Math.abs(longResult));
                    }
                }
            }
            return result;
        }

    };

    private Comparator mySortTasksByStartDateComparator = new Comparator() {
        public int compare(Object left, Object right) {
            int result = 0;
            Task leftTask = (Task) left;
            Task rightTask = (Task) right;
            if (!leftTask.equals(rightTask)) {
                result = leftTask.getStart().compareTo(rightTask.getStart());
                if (result == 0) {
                    float longResult = 0;
                    TaskLength leftLength = leftTask.getDuration();
                    TaskLength rightLength = rightTask.getDuration();
                    if (leftLength.getTimeUnit().isConstructedFrom(
                            rightLength.getTimeUnit())) {
                        longResult = leftLength.getLength(rightLength
                                .getTimeUnit())
                                - rightLength.getLength();
                    } else if (rightLength.getTimeUnit().isConstructedFrom(
                            leftLength.getTimeUnit())) {
                        longResult = leftLength.getLength()
                                - rightLength.getLength(leftLength
                                        .getTimeUnit());
                    } else {
                        throw new IllegalArgumentException("Lengths="
                                + leftLength + " and " + rightLength
                                + " are not compatible");
                    }
                    if (longResult != 0) {
                        result = (int) (longResult / Math.abs(longResult));
                    }
                }
            }
            return result;
        }

    };
    
    public void sortByStartDate(List/* <TaskActivity> */tasks) {
        Collections.sort(tasks, mySortActivitiesByStartDateComparator);
    }
    
    public void sortTasksByStartDate(List/* <Task> */tasks) {
        Collections.sort(tasks, mySortTasksByStartDateComparator);
    }
    
}
