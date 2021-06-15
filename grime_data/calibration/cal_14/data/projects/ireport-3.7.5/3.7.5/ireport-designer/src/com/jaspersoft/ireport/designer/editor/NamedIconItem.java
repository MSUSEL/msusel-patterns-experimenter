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
package com.jaspersoft.ireport.designer.editor;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;

/**
 *
 * @author gtoffoli
 */
public class NamedIconItem {
    
    public static final String ICON_FOLDER_FIELDS = "com/jaspersoft/ireport/designer/resources/fields-16.png";
    public static final String ICON_FOLDER_PARAMETERS = "com/jaspersoft/ireport/designer/resources/parameters-16.png";
    public static final String ICON_FOLDER_VARIABLES = "com/jaspersoft/ireport/designer/resources/variables-16.png";
    public static final String ICON_FOLDER = "com/jaspersoft/ireport/designer/resources/folder.png";
    public static final String ICON_FOLDER_WIZARDS = ICON_FOLDER;
    public static final String ICON_FOLDER_RECENT_EXPRESSIONS = ICON_FOLDER;
    public static final String ICON_FOLDER_FORMULAS = ICON_FOLDER;
    public static final String ICON_CROSSTAB = "com/jaspersoft/ireport/designer/resources/crosstab-16.png";
    
    private Object item = null;
    private String displayName = null;
    private Icon icon = null;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }
    
    public NamedIconItem(Object item)
    {
        this(item, null);
    }
    
    public NamedIconItem(Object item, String displayName)
    {
        this(item, displayName, (Icon)null);
    }
    
    public NamedIconItem(Object item, String displayName, Icon icon)
    {
        this.item = item;
        this.displayName = displayName;
        this.icon = icon;
    }
    
    public NamedIconItem(Object item, String displayName, String iconName)
    {
        this(item, displayName);
        try {
            this.icon = new ImageIcon( ImageUtilities.loadImage(iconName) );
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    @Override
    public String toString()
    {
        return (displayName == null) ? item+"" : displayName;
    }
}
