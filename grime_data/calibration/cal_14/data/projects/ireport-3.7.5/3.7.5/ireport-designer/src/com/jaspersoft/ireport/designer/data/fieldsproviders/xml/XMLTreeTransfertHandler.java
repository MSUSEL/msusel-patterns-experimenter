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
package com.jaspersoft.ireport.designer.data.fieldsproviders.xml;

import com.jaspersoft.ireport.designer.dnd.TransferableObject;
import java.awt.datatransfer.Transferable;
import javax.swing.*;
import javax.swing.tree.*;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.design.JRDesignField;
/**
 *
 * @author  Administrator
 */
public class XMLTreeTransfertHandler extends javax.swing.TransferHandler  
//iR20 implements DragSourceMotionListener, TimingTarget 
{
    XMLFieldMappingEditor xmlEditor = null;

    public XMLFieldMappingEditor getXmlEditor() {
        return xmlEditor;
    }

    public void setXmlEditor(XMLFieldMappingEditor xmlEditor) {
        this.xmlEditor = xmlEditor;
    }
    
    /** Creates a new instance of TreeTransfertHandler 
     * @param xmlEditor 
     */
    public XMLTreeTransfertHandler(XMLFieldMappingEditor xmlEditor) {
        super();
        this.xmlEditor = xmlEditor;
    }
    
    @Override
    public int getSourceActions(JComponent c) 
    {
        return COPY_OR_MOVE;
        
    }
    
    @Override
    protected Transferable createTransferable(JComponent c) 
    {
        if (c instanceof JTree)
        {
            JTree tree = (JTree)c;
            TreePath path = tree.getLeadSelectionPath();
	    DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
            
            JRField field = getXmlEditor().createField(path, true);
            
            return new TransferableObject(field);           
        }
        
        return new TransferableObject(c);
    }
}
