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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * This class handles the custom columns for one single task. It associate a
 * customColumn name with a value. The name of the custom column has to exist in
 * the CustomColumnsStorage otherwise an exception will be thrown. The type
 * (Class) of the given object as value has to match the class in the
 * CustomColumnsManager.
 *
 * @author bbaranne Mar 2, 2005
 */
public class CustomColumnsValues implements Cloneable {
    /**
     * CustomColumnName(String) -> Value (Object)
     */
    private final Map mapCustomColumnValue = new HashMap();
	private final CustomColumnsStorage myColumnStorage;

    /**
     * Creates an instance of CustomColumnsValues.
     */
    public CustomColumnsValues(CustomColumnsStorage columnStorage) {
        myColumnStorage = columnStorage;
    }

    /**
     * Set the value for the customColumn whose name is given.
     *
     * @param customColName
     *            The name of the CustomColumn.
     * @param value
     *            The associated value.
     * @throws CustomColumnsException
     *             Throws if <code>customColName</code> does not exist or
     *             <code>value</code> class does not match the CustomColum
     *             class.
     */
    public void setValue(String customColName, Object value)
            throws CustomColumnsException {
        if (!myColumnStorage.exists(customColName))
            throw new CustomColumnsException(
                    CustomColumnsException.DO_NOT_EXIST, customColName);

        Class c1 = myColumnStorage.getCustomColumn(customColName).getType();
        if (value==null) {
        	mapCustomColumnValue.remove(customColName);
        	return;
        }
        Class c2 = value.getClass();
        // System.out.println(c1 +" - " + c2);
        if (value != null && !c1.isAssignableFrom(c2))
            throw new CustomColumnsException(
                    CustomColumnsException.CLASS_MISMATCH, null);
        else
            mapCustomColumnValue.put(customColName, value);
    }

    /**
     * Returns the value for the given customColName.
     *
     * @param customColName
     *            The name of the custom column we want to get the value.
     * @return The value for the given customColName.
     */
    public Object getValue(String customColName) {
        return mapCustomColumnValue.get(customColName);
    }

    /**
     * Remove the custom column (and also its value) from this
     * CustomColumnValues.
     *
     * @param colName
     *            Name of the column to remove.
     */
    public void removeCustomColumn(String colName) {
        mapCustomColumnValue.remove(colName);
    }

    public void renameCustomColumn(String oldName, String newName) {
        Object o = mapCustomColumnValue.get(oldName);
        mapCustomColumnValue.put(newName, o);
        mapCustomColumnValue.remove(oldName);
    }

    public Object clone() {
        CustomColumnsValues res = new CustomColumnsValues(myColumnStorage);
        Iterator it = mapCustomColumnValue.keySet().iterator();
        while (it.hasNext()) {
            Object k = it.next();
            res.mapCustomColumnValue.put(k, mapCustomColumnValue.get(k));
        }
        return res;
    }

    public String toString() {
        return mapCustomColumnValue.toString();
    }

}
