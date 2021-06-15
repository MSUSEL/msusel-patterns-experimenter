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


import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.w3c.dom.Node;

/**
 *
 * @author gtoffoli
 */
public class XMLDocumentTreeCellRenderer extends DefaultTreeCellRenderer {

    static ImageIcon tagIcon;
    static ImageIcon attributeIcon;
    static ImageIcon errorIcon;
    
    
    XMLFieldMappingEditor mappingEditor = null;

    public XMLFieldMappingEditor getMappingEditor() {
        return mappingEditor;
    }

    public void setMappingEditor(XMLFieldMappingEditor mappingEditor) {
        this.mappingEditor = mappingEditor;
    }
    
    public XMLDocumentTreeCellRenderer(XMLFieldMappingEditor mappingEditor) {
        super();
        
        this.mappingEditor = mappingEditor;
        if (tagIcon == null) tagIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/data/fieldsproviders/xml/tag.png"));
        if (attributeIcon == null) attributeIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/data/fieldsproviders/xml/attribute.png"));
        if (errorIcon == null) errorIcon = new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/data/fieldsproviders/xml/error.png"));
        

    }

    @Override
    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        
            
        
        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
        
        
          try {
            if (value != null && value instanceof DefaultMutableTreeNode)
            {
                DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)value;
                if (dmtn.getUserObject() != null && dmtn.getUserObject() instanceof Node)
                {
                    Node node = (Node)dmtn.getUserObject();
                    String s = node.getNodeName();
                    if (node.getNodeValue() != null){
                        s += " (" + node.getNodeValue() + ")";
                    }
                    
                    if (node.getNodeType() == Node.ELEMENT_NODE)
                    {
                        setIcon(tagIcon);
                    }
                    if (node.getNodeType() == Node.ATTRIBUTE_NODE)
                    {
                        setIcon(attributeIcon);
                    }
                    
                    boolean needBold = false;
                    if (getMappingEditor() != null &&
                        getMappingEditor().getRecordNodes().contains(node))
                    {
                        needBold = true;
                    }  
                    
                    java.awt.Font f = getFont();
                    
                    if (f.isBold() && !needBold)
                    {
                        setFont( f.deriveFont( Font.PLAIN) );
                    }
                    else if (!f.isBold() && needBold)
                    {
                        setFont( f.deriveFont( Font.BOLD ) );
                    }
                     
                    setText(s);
                } else
                {
                    setIcon(errorIcon);
                }
            }
        }
        catch (Exception ex)
        {
            //ex.printStackTrace();
        }
         
        return this;
    }


}


