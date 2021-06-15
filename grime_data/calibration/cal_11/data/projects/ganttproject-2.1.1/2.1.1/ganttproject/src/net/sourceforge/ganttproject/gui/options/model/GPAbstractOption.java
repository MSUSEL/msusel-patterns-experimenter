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
package net.sourceforge.ganttproject.gui.options.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class GPAbstractOption implements GPOption, ChangeValueDispatcher {
    private final String myID;

    protected boolean isLocked;

    private List myListeners = new ArrayList();
    
    protected GPAbstractOption(String id) {
        myID = id;
    }

    public String getID() {
        return myID;
    }

    public void lock() {
        if (isLocked) {
            throw new IllegalStateException("Already locked");
        }
        isLocked = true;
    }

    public void commit() {
        if (!isLocked()) {
            throw new IllegalStateException("Can't commit not locked option");
        }
        setLocked(false);

    }

    public void rollback() {
        if (!isLocked()) {
            throw new IllegalStateException("Can't rollback not locked option");
        }
        setLocked(false);
    }

    protected boolean isLocked() {
        return isLocked;
    }

    protected void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
    
    public void addChangeValueListener(ChangeValueListener listener) {
        myListeners.add(listener);
    }

    protected void fireChangeValueEvent(ChangeValueEvent event) {
        Iterator it = myListeners.iterator();
        while (it.hasNext()) {
            ((ChangeValueListener) it.next()).changeValue(event);
        }
    }
    

}
