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

/**
 * Created by IntelliJ IDEA. User: bard
 */
public class AlgorithmCollection {
    private final FindPossibleDependeesAlgorithm myFindPossibleDependeesAlgorithm;

    private final RecalculateTaskScheduleAlgorithm myRecalculateTaskScheduleAlgorithm;

    private final AdjustTaskBoundsAlgorithm myAdjustTaskBoundsAlgorithm;

    private final RecalculateTaskCompletionPercentageAlgorithm myCompletionPercentageAlgorithm;

    private final ProjectBoundsAlgorithm myProjectBoundsAlgorithm;

    private final ShiftTaskTreeAlgorithm myShiftTaskTreeAlgorithm;

    public AlgorithmCollection(
            FindPossibleDependeesAlgorithm myFindPossibleDependeesAlgorithm,
            RecalculateTaskScheduleAlgorithm recalculateTaskScheduleAlgorithm,
            AdjustTaskBoundsAlgorithm adjustTaskBoundsAlgorithm,
            RecalculateTaskCompletionPercentageAlgorithm completionPercentageAlgorithm,
            ProjectBoundsAlgorithm projectBoundsAlgorithm) {
        this.myFindPossibleDependeesAlgorithm = myFindPossibleDependeesAlgorithm;
        myRecalculateTaskScheduleAlgorithm = recalculateTaskScheduleAlgorithm;
        myAdjustTaskBoundsAlgorithm = adjustTaskBoundsAlgorithm;
        myCompletionPercentageAlgorithm = completionPercentageAlgorithm;
        myProjectBoundsAlgorithm = projectBoundsAlgorithm;
        myShiftTaskTreeAlgorithm = new ShiftTaskTreeAlgorithm();
    }

    public FindPossibleDependeesAlgorithm getFindPossibleDependeesAlgorithm() {
        return myFindPossibleDependeesAlgorithm;
    }

    public RecalculateTaskScheduleAlgorithm getRecalculateTaskScheduleAlgorithm() {
        return myRecalculateTaskScheduleAlgorithm;
    }

    public AdjustTaskBoundsAlgorithm getAdjustTaskBoundsAlgorithm() {
        return myAdjustTaskBoundsAlgorithm;
    }

    public RecalculateTaskCompletionPercentageAlgorithm getRecalculateTaskCompletionPercentageAlgorithm() {
        return myCompletionPercentageAlgorithm;
    }

    public ProjectBoundsAlgorithm getProjectBoundsAlgorithm() {
        return myProjectBoundsAlgorithm;
    }

    public ShiftTaskTreeAlgorithm getShiftTaskTreeAlgorithm() {
        return myShiftTaskTreeAlgorithm;
    }

}
