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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class has to be used to add or remove new custom columns. It will
 * perform the changes to the linked treetable.
 * 
 * @author bbaranne (Benoit Baranne) Mar 4, 2005
 */
public class CustomColumnsManager {
    private final List myListeners;
	private final CustomColumnsStorage myStorage;

    /**
     * Creates an instance of CustomColumnsManager for the given treetable.
     * 
     * @param treetable
     */
    public CustomColumnsManager(CustomColumnsStorage storage) {
        myListeners = new ArrayList();
        myStorage = storage;
    }
    
    /**
     * Add a new custom column to the treetable.
     */
    public void addNewCustomColumn(CustomColumn customColumn) {
    	assert customColumn!=null;
        myStorage.addCustomColumn(customColumn);
    }

    /**
     * Delete the custom column whose name is given in parameter from the
     * treetable.
     * 
     * @param name
     *            Name of the column to delete.
     */
    public void deleteCustomColumn(String name) {
        myStorage.removeCustomColumn(name);
    }

    public void changeCustomColumnName(String oldName, String newName) {
        //ganttTreeTable.renameCustomcolumn(oldName, newName);
        myStorage.renameCustomColumn(oldName, newName);
    }

    public void changeCustomColumnDefaultValue(String colName,
            Object newDefaultValue) throws CustomColumnsException {
        // ganttTreeTable.changeDefaultValue(colName, newDefaultValue);
        myStorage.changeDefaultValue(colName, newDefaultValue);
    }

    public void addCustomColumnsListener(CustomColumsListener listener) {
        myStorage.addCustomColumnsListener(listener);
    }

}
