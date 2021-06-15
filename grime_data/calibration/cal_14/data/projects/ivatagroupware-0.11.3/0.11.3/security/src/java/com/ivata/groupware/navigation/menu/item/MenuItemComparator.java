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
package com.ivata.groupware.navigation.menu.item;

import java.util.Comparator;

/**
 * Sorts menu items by their <code>priority</code> properties.
 *
 * @since ivata groupware 0.10 (Feb 14, 2005)
 * @author Colin MacLeod
 * <a href="mailto:colin.macleod@ivata.com">colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */

public class MenuItemComparator implements Comparator {
    /**
     * Sorts menu items by their <code>priority</code> properties.
     * Refer to {@link Comparator}.
     *
     * @param item0Object first menu item to compare.
     * @param item1Object first menu item to compare.
     * @return Refer to {@link Comparator}.
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object item0Object, Object item1Object) {
        MenuItemDO menuItem0 = (MenuItemDO)item0Object;
        MenuItemDO menuItem1 = (MenuItemDO)item1Object;

        // first rule out whichever is null (if one is or both are)
        Integer compareNull = compareNull(menuItem0, menuItem1);
        if (compareNull != null) {
            return compareNull.intValue();
        }

        // try to use priority - if they are the same, compare id values
        compareNull = compareNull(menuItem0.getPriority(),
                menuItem1.getPriority());
        if (compareNull != null) {
            return compareNull.intValue();
        }
        int returnValue = menuItem0.getPriority().compareTo(
                menuItem1.getPriority());
        if (returnValue == 0) {
            compareNull = compareNull(menuItem0.getId(),
                    menuItem1.getId());
            if (compareNull != null) {
                return compareNull.intValue();
            }
            return menuItem0.getId().compareTo(menuItem1.getId());
        }
        return returnValue;
    }
    /**
     * Private helper. Compare two objects to see if either is null and return
     * as appropriate, otherwise return <code>null</code>.
     *
     * @param object0 first object to compare.
     * @param object1 second object to compare.
     * @return <code>0</code> if both are <code>null</code>, <code>1</code> if
     * only <code>object0</code> is <code>null</code>,   <code>-1</code> if only
     * <code>object1</code> is <code>null</code>, otherwise <code>null</code>.
     */
    private Integer compareNull(final Object object0, final Object object1) {
        if (object0 == null) {
            if ((object1 == null))  {
                return new Integer(0);
            }
            return new Integer(1);
        } else if (object1 == null) {
            if ((object0 == null))  {
                return new Integer(0);
            }
            return new Integer(-1);
        }
        return null;
    }
}
