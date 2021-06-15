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

import java.util.Date;

import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskActivity;

/**
 * Created by IntelliJ IDEA. User: bard Date: 14.02.2004 Time: 2:32:17 To change
 * this template use File | Settings | File Templates.
 */
public interface TaskDependency {
	abstract class Hardness {
		public static final Hardness RUBBER = new Hardness("Rubber"){
			public String toString() {
				return GanttLanguage.getInstance().getText("hardness.rubber");
			}
		};
		public static final Hardness STRONG = new Hardness("Strong"){
			public String toString() {
                return GanttLanguage.getInstance().getText("hardness.strong");
			}
		};
		public static Hardness parse(String hardnessAsString) {
			if (hardnessAsString==null) {
				throw new IllegalArgumentException("Null value is not allowed as hardness");
			}
			if ("Rubber".equals(hardnessAsString.trim())) {
				return RUBBER;
			}
			if ("Strong".equals(hardnessAsString.trim())) {
				return STRONG;
			}
			throw new IllegalArgumentException("Unexpected hardness string value="+hardnessAsString);
		}
        private String myID;
        
        private Hardness(String id) {
            myID = id;
        }
        public String getIdentifier() {
            return myID;
        }
	}
    Task getDependant();

    Task getDependee();

    void setConstraint(TaskDependencyConstraint constraint);

    TaskDependencyConstraint getConstraint();

    ActivityBinding getActivityBinding();

    void delete();

    interface ActivityBinding {
        TaskActivity getDependantActivity();

        TaskActivity getDependeeActivity();

        Date[] getAlignedBounds();
    }

    int getDifference();

    void setDifference(int difference);
    
    Hardness getHardness();
    void setHardness(Hardness hardness);
}
