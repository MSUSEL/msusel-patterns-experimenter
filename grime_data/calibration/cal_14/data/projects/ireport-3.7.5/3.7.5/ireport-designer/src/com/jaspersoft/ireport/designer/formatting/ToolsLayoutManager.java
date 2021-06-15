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
package com.jaspersoft.ireport.designer.formatting;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 *
 * @author gtoffoli
 */
public class ToolsLayoutManager implements LayoutManager {

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        // Number of columns * heigh....
        int width = parent.getWidth();
        int maxWidth = getMaximumComponentWidth(parent);
        int columns = getColumnsRequired(parent, width);
        
        int component_height = getMaximumComponentHeight(parent);
        int rows = parent.getComponents().length / columns;
        if (parent.getComponentCount() % columns > 0) rows++;
        
        int height = rows * component_height;
        
        if (parent.getParent() != null && parent.getParent() instanceof JScrollPane)
        {
            JScrollPane scrollpane = (JScrollPane)parent.getParent();
            if (height > scrollpane.getVisibleRect().height)
            {
                // need to recalculate all using the new width...
                width -= scrollpane.getVerticalScrollBar().getWidth();
                columns = getColumnsRequired(parent, width);
                rows = parent.getComponents().length / columns;
                if (parent.getComponentCount() % columns > 0) rows++;
            }
        }
        
        return new Dimension(Math.max(maxWidth, width),height);
    }

    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0,0);
    }

    public void layoutContainer(Container parent) {
        
        int width = parent.getWidth();
        int maxWidth = getMaximumComponentWidth(parent);
        
        int columns = getColumnsRequired(parent, width);
        int columnWidth = width/columns;
        
        int component_height = getMaximumComponentHeight(parent);
        int rows = parent.getComponents().length / columns;
        if (parent.getComponentCount() % columns > 0) rows++;
        
        // Check if vertial scrollbar is requires...
        int height = rows * component_height;
        
        if (parent.getParent() != null && parent.getParent() instanceof JScrollPane)
        {
            JScrollPane scrollpane = (JScrollPane)parent.getParent();
            if (height > scrollpane.getVisibleRect().height)
            {
                // need to recalculate all using the new width...
                width -= scrollpane.getVerticalScrollBar().getWidth();
                columns = getColumnsRequired(parent, width);
                columnWidth = width/columns;
                rows = parent.getComponents().length / columns;
                if (parent.getComponentCount() % columns > 0) rows++;
            }
        }

        int current_column = 0;
        int current_row = -1;
        Component[] components = parent.getComponents();
        for (int i=0; i<components.length; ++i)
        {
            Component c = components[i];
            if (current_column == 0) current_row++;
            
            c.setBounds( current_column*columnWidth, current_row*component_height, 
                         columnWidth, component_height);
            
            current_column++;
            current_column %= columns;
        }
        
    }
    
    private int getColumnsRequired(Container parent, int width)
    {
        int maxWidth = getMaximumComponentWidth(parent);
        int columns = 1;
        if (width > maxWidth && maxWidth > 0 && parent.getComponentCount() > 1)
        {
            columns = width/maxWidth;
        }
        return columns;
    }

    private int getMaximumComponentWidth(Container parent)
    {
        int width = 0;
        Component[] components = parent.getComponents();
        for (int i=0; i<components.length; ++i)
        {
            Component c = components[i];
            width = c.getPreferredSize().width > width ? c.getPreferredSize().width : width;
        }
        return width;
    }
    
    private int getMaximumComponentHeight(Container parent)
    {
        int height = 0;
        Component[] components = parent.getComponents();
        for (int i=0; i<components.length; ++i)
        {
            Component c = components[i];
            height = c.getPreferredSize().height > height ? c.getPreferredSize().height : height;
        }
        return height;
    }
    
}
