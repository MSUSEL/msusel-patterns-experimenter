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
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotools.swing.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

/**
 * A status bar item with an icon which displays a pop-up menu when clicked.
 *
 * @see JMapStatusBar
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class JMenuStatusBarItem extends StatusBarItem {

    /**
     * Creates a new item to display the given menu;
     *
     * @param name the item name
     * @param icon the icon to display
     * @param toolTip tool tip text (may be {@code null}
     * @param menu the pop-up menu to launch when the item is clicked
     *
     * @throws IllegalArgumentException if {@code icon} or {@code menu} are {@code null}
     */
    public JMenuStatusBarItem(String name, final ImageIcon icon, final String toolTip,
            final JPopupMenu menu) {
        this(name, icon, toolTip, new PopupMenuProvider() {
            {
                if (menu == null) {
                    throw new IllegalArgumentException("menu must not be null");
                }
            }

            @Override
            public JPopupMenu getMenu() {
                return menu;
            }
        });
    }

    /**
     * Creates a new item to display a menu which will be supplired by
     * {@code menuProvider}.
     *
     * @param name the item name
     * @param icon the icon to display
     * @param menuProvider an object to provide a (possibly dynamic) pop-up menu
     *
     * @throws IllegalArgumentException if {@code icon} or {@code menuPRovider}
     *     are {@code null}
     */
    public JMenuStatusBarItem(String name, final ImageIcon icon, String toolTip,
            final PopupMenuProvider menuProvider) {

        super(name, false);

        if (icon == null) {
            throw new IllegalArgumentException("icon must not be null");
        }
        if (menuProvider == null) {
            throw new IllegalArgumentException("menuProvider must not be null");
        }

        final JButton btn = new JButton(icon);
        btn.setBorderPainted(false);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                menuProvider.getMenu().show(btn, 0, 0);
            }
        });

        if (toolTip != null && toolTip.trim().length() > 0) {
            btn.setToolTipText(toolTip);
        }

        add(btn);
    }

}
