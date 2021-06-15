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


import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.container.persistence.BaseDO;
import com.ivata.groupware.navigation.menu.MenuDO;


/**
 * <p><code>EntityBean</code> to store items in the users menu system.</p>
 *
 * @since 2002-05-07
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 * @see com.ivata.groupware.menu.MenuBean
 *
 * @hibernate.class
 *      table="navigation_menu_item"
 * @hibernate.cache
 *      usage="read-write"
 */
public class MenuItemDO extends BaseDO {

    /**
     * <p>Image associated with the item.</p>
     */
    private String image;

    /**
     * <p>
     * Menu which contains this item.
     * </p>
     */
    private MenuDO menu;
    /**
     * <p>Order in which this menu item should appear, in relation to the other
     * menu items.<p>
     */
    private Integer priority;

    /**
     * <p>Get the text or caption which should appear in this menu.</p>
     */
    private String text;


    /**
     * <p>Get the <code>URL</code> to link this menu item to.</p>
     */
    private String URL;

    /**
     * <p>User associated with this menu item.</p>
     */
    private UserDO user;

    /**
     * <p>Get the image associated with the item.</p>
     *
     * @return the filename (without path) of an image associated with this
     * item, or <code>null</code> if no image is currently associated with this
     * menu item.
     * @hibernate.property
     */
    public final String getImage() {
        return image;
    }

    /**
     * <p>
     * Menu which contains this item.
     * </p>
     *
     * @return current value of menu.
     * @hibernate.many-to-one
     */
    public final MenuDO getMenu() {
        return menu;
    }

    /**
     * <p>Get the order in which this menu item should appear, in relation to the other
     * menu items.<p>
     *
     * @return the order in which this menu item should appear, in relation
     * to the other menu items. Lower values of this number will appear lower in the
     * list of menu items (and are therefore more significant).
     * @hibernate.property
     */
    public final Integer getPriority() {
        return priority;
    }
    /**
     * <p>Get the text or caption which should appear in this menu.</p>
     *
     * @return the text or caption which should appear in this menu
     * @hibernate.property
     */
    public final String getText() {
        return text;
    }

    /**
     * <p>Get the <code>URL</code> to link this menu item to.</p>
     *
     * @return the <code>URL</code> to link this menu item to
     * @hibernate.property
     */
    public final String getURL() {
        return URL;
    }
    /**
     * <p>Get the user associated with this menu item.</p>
     *
     * @return the user associated with this item, or <code>null</code> if
     * the item should appear in all users' menues.
     *
     * @hibernate.many-to-one
     *      column="person_user"
     */
    public UserDO getUser() {
        return user;
    }

    /**
     * <p>Set the image associated with the item.</p>
     *
     * @param image the filename (without path) of an image associated with this
     * item, or <code>null</code> if no image is currently associated with this
     * menu item.
     */
    public final void setImage(final String image) {
        this.image = image;
    }
    /**
     * <p>
     * Menu which contains this item.
     * </p>
     *
     * @param menu new value of menu.
     */
    public final void setMenu(final MenuDO menu) {
        this.menu = menu;
    }

    /**
     * <p>Set the order in which this menu item should appear, in relation to the other
     * menu items.<p>
     *
     * @param priority the order in which this menu item should appear, in relation
     * to the other menu items. Lower values of this number will appear lower in the
     * list of menu items (and are therefore more significant).
     */
    public final void setPriority(final Integer priority) {
        this.priority = priority;
    }

    /**
     * <p>Set the text or caption which should appear in this menu.</p>
     *
     * @param text the text or caption which should appear in this menu.
     */
    public final void setText(final String text) {
        this.text = text;
    }

    /**
     * <p>Set the <code>URL</code> to link this menu item to.</p>
     *
     * @param URL the <code>URL</code> to link this menu item to.
     */
    public final void setURL(final String URL) {
        this.URL = URL;
    }

    /**
     * <p>Set the user associated with this menu item.</p>
     *
     * @param user the user associated with this item, or <code>null</code> if
     * the item should appear in all users' menues.
     */
    public final void setUser(final UserDO user) {
        this.user = user;
    }
}
