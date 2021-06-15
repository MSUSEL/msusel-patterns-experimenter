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
package com.ivata.groupware.navigation.menu;


import java.util.Set;

import com.ivata.groupware.container.persistence.BaseDO;


/**
 * <p><code>EntityBean</code> to store the users menu system.</p>
 *
 * <p>This is a dependent value class, used to pass data back from the.</p>
 * {@link MenuBean MenuBean} to a client application.</p>
 *
 * <p><strong>Note:</strong> This class provides data from {@link MenuBean MenuBean}.
 * This is no local copy of the bean class, however, and changes here
 * will not be automatically reflected in {@link MenuBean MenuBean}.</p>
 *
 * @since 2002-05-07
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 * @see MenuBean
 *
 * @hibernate.class
 *      table="navigation_menu"
 * @hibernate.cache
 *      usage="read-write"
 */
public class MenuDO extends BaseDO {

    /**
     * <p>All the menu items under this menu.</p>
     */
    private Set items;
    /**
     * <p>Order in which this menu should appear, in relation to the other
     * menus.<p>
     */
    private Integer priority;
    /**
     * <p>Text or caption which should appear on this menu.</p>
     */
    private String text;

    /**
     * <p>Comparison method. See if the object supplied is a menu dependent
     * object and, if so, whether or not its contents are the same as this one.
     * Only the <code>id</code> fields are compared.</p>
     *
     * @param compare the object to compare with this one.
     * @return <code>true</code> if the object supplied in <code>compare</code>
     *     is effectively the same as this one, otherwise false.
     */
    public boolean equals(final Object compare) {
        // first check it is non-null and the class is right
        if ((compare == null) ||
            !(this.getClass().isInstance(compare))) {
            return false;
        }
        MenuDO menuDO = (MenuDO) compare;
        Integer id = getId();
        Integer menuDOId = menuDO.getId();

        // check that the ids are the same
        return (((id == null) ?
                    (menuDOId == null) :
                    id.equals(menuDOId)));
    }
    /**
     * <p>Get all the menu items under this menu.</p>
     *
     * @return all items in this menu as a <code>Collection</code> of
     * <code>MenuItemDO</code> instances.
     *
     * @hibernate.set
     *      sort="com.ivata.groupware.navigation.menu.item.MenuItemComparator"
     * @hibernate.collection-key
     *      column="menu"
     * @hibernate.collection-one-to-many
     *      class="com.ivata.groupware.navigation.menu.item.MenuItemDO"
     */
    public final Set getItems() {
        return items;
    }
    /**
     * <p>Get the order in which this menu should appear, in relation to the other
     * menus.<p>
     *
     * @return the order in which this menu should appear, in relation
     * to the other menus. Lower values of this number will appear lower in the
     * Set of menus (and are therefore more significant).
     * @hibernate.property
     */
    public final Integer getPriority() {
        return priority;
    }

    /**
     * <p>Get the text or caption which should appear on this menu.</p>
     *
     * @return the text or caption which should appear on this menu
     * @hibernate.property
     */
    public final String getText() {
        return text;
    }

    /**
     * <p>Set all the menu items under this menu.</p>
     *
     * @param items all items in this menu as a <code>Collection</code> of
     * <code>MenuItemDO</code> instances.
     */
    public final void setItems(final Set items) {
        this.items = items;
    }
    /**
     * <p>Set the order in which this menu should appear, in relation to the other
     * menus.<p>
     *
     * @param priority the order in which this menu should appear, in relation
     * to the other menus. Lower values of this number will appear lower in the
     * list of menus (and are therefore more significant).
     */
    public final void setPriority(final Integer priority) {
        this.priority = priority;
    }

    /**
     * <p>Set the text or caption which should appear on this menu.</p>
     *
     * @param text the text or caption which should appear on this menu
     */
    public final void setText(final String text) {
        this.text = text;
    }

}
