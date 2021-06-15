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

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.geotools.swing.locale.LocaleUtils;
import org.geotools.swing.MapPane;
import org.geotools.swing.event.MapPaneAdapter;
import org.geotools.swing.event.MapPaneEvent;

/**
 * A status bar item that displays an animated icon to indicate renderer activity.
 *
 * @see JMapStatusBar
 *
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class JRendererStatusBarItem extends StatusBarItem {
    private static final String TOOL_TIP = LocaleUtils.getValue("StatusBar", "RendererTooltip");
    private static final String BUSY_IMAGE = "icons/busy.gif";
    private static final String IDLE_IMAGE = "icons/idle.gif";

    private final ImageIcon busyIcon;
    private final ImageIcon idleIcon;

    /*
     * Creates a new item associated with teh given map.
     */
    public JRendererStatusBarItem(MapPane mapPane) {
        super("Busy", false);

        busyIcon = new ImageIcon(JRendererStatusBarItem.class.getResource(BUSY_IMAGE));
        idleIcon = new ImageIcon(JRendererStatusBarItem.class.getResource(IDLE_IMAGE));

        final JLabel renderLabel = new JLabel();
        renderLabel.setIcon(idleIcon);
        renderLabel.setToolTipText(TOOL_TIP);

        Insets insets = getInsets();
        renderLabel.setMinimumSize(new Dimension(
                busyIcon.getIconWidth() + insets.left + insets.right,
                busyIcon.getIconHeight() + insets.top + insets.bottom));

        add(renderLabel);

        mapPane.addMapPaneListener(new MapPaneAdapter() {
            @Override
            public void onRenderingStarted(MapPaneEvent ev) {
                renderLabel.setIcon(busyIcon);
            }

            @Override
            public void onRenderingStopped(MapPaneEvent ev) {
                renderLabel.setIcon(idleIcon);
            }
        });
    }

}
