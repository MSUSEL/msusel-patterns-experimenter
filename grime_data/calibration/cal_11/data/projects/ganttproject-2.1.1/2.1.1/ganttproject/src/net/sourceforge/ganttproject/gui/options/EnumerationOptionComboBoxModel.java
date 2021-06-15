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
 * Created on 02.04.2005
 */
package net.sourceforge.ganttproject.gui.options;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import net.sourceforge.ganttproject.gui.options.model.EnumerationOption;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author bard
 */
class EnumerationOptionComboBoxModel extends AbstractListModel implements
        ComboBoxModel {
    private final List myValues;

    private Item mySelectedItem;

    private final EnumerationOption myOption;

    public EnumerationOptionComboBoxModel(EnumerationOption option) {
        myOption = option;
        String currentValue = option.getValue();
        Item currentItem = null;
        String[] ids = option.getAvailableValues();
        String[] i18nedValues = geti18nedValues(option);

        myValues = new ArrayList(ids.length);
        for (int i = 0; i < ids.length; i++) {
            Item nextItem = new Item(ids[i], i18nedValues[i]);
            myValues.add(nextItem);
            if (ids[i].equals(currentValue)) {
                currentItem = nextItem;
            }
        }
        if (currentItem != null) {
            setSelectedItem(currentItem);
        }
    }

    public void setSelectedItem(Object item) {
        mySelectedItem = (Item) item;
        myOption.setValue(mySelectedItem.myID);
    }

    public Object getSelectedItem() {
        return mySelectedItem;
    }

    public int getSize() {
        return myValues.size();
    }

    public Object getElementAt(int index) {
        return myValues.get(index);
    }

    String[] geti18nedValues(EnumerationOption option) {
        String[] ids = option.getAvailableValues();
        String[] result = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            result[i] = GanttLanguage.getInstance().getText(
                    "optionValue." + ids[i] + ".label");

            if (result[i].startsWith(GanttLanguage.MISSING_RESOURCE)) {
//                System.err.println(result[i]);
                result[i] = ids[i];
            }
        }
        return result;
    }

    private static class Item {
        private final String myID;

        private final String myDisplayValue;

        public Item(String id, String displayValue) {
            myID = id;
            myDisplayValue = displayValue;
        }

        public String toString() {
            return myDisplayValue;
        }
    }
}
