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

public class CustomColumEvent {

    public static final int EVENT_ADD = 0;

    public static final int EVENT_REMOVE = 1;

    public static final int EVENT_REBUILD = 2;

	public static final int EVENT_RENAME = 3;
    protected final int myType;

    protected final String myColName;

    private final String myOldName;

	private final CustomColumn myColumn;

    public CustomColumEvent(int type, String colName) {
        myType = type;
        myColName = colName;
        myColumn = null;
        myOldName = colName;
    }

    public CustomColumEvent(int type, CustomColumn column) {
    	myType = type;
    	myColumn = column;
    	myColName = column.getName();
    	myOldName = myColName;
	}

    CustomColumEvent(String oldName, CustomColumn column) {
    	myOldName = oldName;
    	myType = EVENT_RENAME;
    	myColName = column.getName();
    	myColumn = column;
    }

    public CustomColumn getColumn() {
    	return myColumn;
    }

	public String getColName() {
        return myColName;
    }

    public int getType() {
        return myType;
    }

	public String getOldName() {
		return myOldName;
	}

}

