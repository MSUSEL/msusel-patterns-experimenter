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
package com.jaspersoft.ireport.jasperserver.validation;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author gtoffoli
 */
public class AlignedTableCellRenderer extends DefaultTableCellRenderer {
    
    private int alignment = JLabel.RIGHT;
    static ImageIcon imageIcon;
    static ImageIcon subreportIcon;
    static ImageIcon unknownIcon;
    static ImageIcon linkIcon;
    static ImageIcon templateIcon;

    /** Creates a new instance of AlignedTableCellRenderer */
    public AlignedTableCellRenderer() {
        this(JLabel.RIGHT);
    }
    
    /** Creates a new instance of AlignedTableCellRenderer */
    public AlignedTableCellRenderer(int alignment) {
        super();
        if (subreportIcon == null) subreportIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/ui/resources/subreport-16.png"));
        if (imageIcon == null) imageIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/ui/resources/image-16.png"));
        if (templateIcon == null) templateIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/style-16.png"));
        if (unknownIcon == null) unknownIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/unknow.png"));
        if (linkIcon == null) linkIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/link.png"));

        this.alignment = alignment;
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (c instanceof JLabel)
                {
                    ((JLabel)c).setHorizontalAlignment( getAlignment());

                    if (value instanceof ElementValidationItem && ((ElementValidationItem)value).isStoreAsLink())
                    {
                        ((JLabel)c).setIcon( linkIcon );
                        ((JLabel)c).setText( "Linked resource" );
                    }
                    else if (value instanceof ImageElementValidationItem)
                    {
                         ((JLabel)c).setIcon( imageIcon );
                         ((JLabel)c).setText( "Image" );
                    }
                    else if (value instanceof SubReportElementValidationItem)
                    {
                         ((JLabel)c).setIcon( subreportIcon );
                         ((JLabel)c).setText( "Subreport" );
                    }
                    else if (value instanceof TemplateElementValidationItem)
                    {
                         ((JLabel)c).setIcon( templateIcon );
                         ((JLabel)c).setText( "Template" );
                    }
                    else if (value instanceof ElementValidationItem)
                    {
                         ((JLabel)c).setIcon( unknownIcon );
                         ((JLabel)c).setText( "Other resource type" );
                    }

                }
                
                return c;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }
    
}
