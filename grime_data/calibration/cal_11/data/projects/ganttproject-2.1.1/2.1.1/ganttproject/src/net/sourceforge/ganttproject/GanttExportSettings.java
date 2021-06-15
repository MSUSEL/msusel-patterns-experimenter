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
package net.sourceforge.ganttproject;

import java.util.Date;

/** Class to store 3 boolean values */
public class GanttExportSettings {
    private Date startDate = null;

    private Date endDate = null;

    public boolean name, percent, depend, border3d, ok;
    
    private boolean onlySelectedItems;

    public GanttExportSettings() {
        name = percent = depend = ok = true;
        onlySelectedItems = false;
    }

    public GanttExportSettings(boolean bName, boolean bPercent,
            boolean bDepend, boolean b3dBorders) {
        name = bName;
        percent = bPercent;
        depend = bDepend;
        border3d = b3dBorders;
        ok = true;
        onlySelectedItems = false;
    }
    
    public void setOnlySelectedItem(boolean selected){
        onlySelectedItems = selected;
    }
    
    public boolean isOnlySelectedItem(){
        return onlySelectedItems;
    }

    public void setStartDate(Date date) {
        startDate = date;
    }

    public void setEndDate(Date date) {
        endDate = date;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
