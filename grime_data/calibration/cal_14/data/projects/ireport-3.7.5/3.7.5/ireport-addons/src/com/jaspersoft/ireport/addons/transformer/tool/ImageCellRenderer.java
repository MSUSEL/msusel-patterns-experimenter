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
package com.jaspersoft.ireport.addons.transformer.tool;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import org.openide.util.ImageUtilities;

/**
 *
 * @author  Administrator
 */
public class ImageCellRenderer extends DefaultTableCellRenderer {
    
    javax.swing.Icon icon1 = null;
    javax.swing.Icon icon2 = null;
    javax.swing.Icon icon3 = null;
    

    /** Creates a new instance of ImageCellRenderer */
    public ImageCellRenderer() {
        
        icon1 = new ImageIcon( ImageUtilities.loadImage("com/jaspersoft/ireport/addons/transformer/tool/docDirty.gif") );
        icon2 = new ImageIcon( ImageUtilities.loadImage("com/jaspersoft/ireport/addons/transformer/tool/doc.gif") );
        icon3 = new ImageIcon( ImageUtilities.loadImage("com/jaspersoft/ireport/addons/transformer/tool/warning.gif") );
        
        //label.setIcon( icon1 );
        //label.setText("");
    }
    
    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof FileEntry)
        {
            if (((FileEntry)value).getStatus() == FileEntry.STATUS_ERROR_TRANSFORMING)
            {
                label.setIcon( icon3 );
            } 
            else if (((FileEntry)value).getStatus() == FileEntry.STATUS_NOT_TRANSFORMED || ((FileEntry)value).getStatus() == FileEntry.STATUS_TRANSFORMING)
            {
                label.setIcon( icon1 );
            }
            else if (((FileEntry)value).getStatus() == FileEntry.STATUS_TRANSFORMED)
            {
                label.setIcon( icon2 );
            }
        }
        else
        {
             label.setIcon( icon1 );
        }
        return label;
    
    }
    
}
