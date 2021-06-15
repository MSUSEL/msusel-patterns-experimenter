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
/*
 * Created on 18.06.2005
 */
package net.sourceforge.ganttproject.gui.options.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DefaultEnumerationOption extends GPAbstractOption implements
        EnumerationOption, ChangeValueDispatcher {


    private final String[] myValues;

    private String myValue;

    private String myLockedValue;

    public DefaultEnumerationOption(String id, String[] values) {
        super(id);
        myValues = values; 
    }

    public DefaultEnumerationOption(String id, List values) {
        super(id);
        myValues = (String[]) values.toArray(new String[0]);
    }

    public String[] getAvailableValues() {
        return myValues;
    }

    public void setValue(String value) {
        if (!isLocked()) {
            throw new IllegalStateException("Lock option before setting value");
        }

        ChangeValueEvent event = new ChangeValueEvent(getID(), myLockedValue,
                value);
        myLockedValue = value;
        fireChangeValueEvent(event);
    }

    public String getValue() {
        return myValue;
    }

    public void commit() {
        super.commit();
        myValue = myLockedValue;
    }


    public String getPersistentValue() {
        return getValue();
    }

    public void loadPersistentValue(String value) {
        setValue(value);
    }    

    public boolean isChanged() {
        if (isLocked()) {
            if (myValue!=null) {
                return false==myValue.equals(myLockedValue);
            }
        }
        return false;
    }

}
