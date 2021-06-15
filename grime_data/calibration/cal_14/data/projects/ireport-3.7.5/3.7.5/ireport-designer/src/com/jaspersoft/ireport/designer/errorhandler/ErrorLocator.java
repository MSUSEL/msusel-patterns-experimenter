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
package com.jaspersoft.ireport.designer.errorhandler;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.editor.ExpressionEditor;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.outline.nodes.ExpressionHolder;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.beans.PropertyVetoException;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author gtoffoli
 * Please not that this class is not thread safe.
 */
public class ErrorLocator {
    
    public void locateError(JrxmlVisualView view, ProblemItem item) 
    {
        // 1. try to select the source of the problem...
        Node node = IReportManager.getInstance().findNodeOf(item.getProblemReference(), view.getModel());
        if (node != null)
        {
            focusNode(view, node);
        }
        
        // 2. try to check if the reference is an expression (very common case...)
        if (item.getProblemReference() instanceof JRDesignExpression)
        {
            focusExpression(view,  (JRDesignExpression)item.getProblemReference());
        }
    }
    
    
    public void focusNode(JrxmlVisualView view, Node node)
    {
        try {
                view.getExplorerManager().setSelectedNodes(new Node[]{node});
                // if the node is an ElementNode, be sure to make the object visible...
                if (node instanceof ElementNode)
                {
                    JRDesignElement element = ((ElementNode)node).getElement();
                    AbstractReportObjectScene scene = view.getReportDesignerPanel().getSceneOf(element);
                    if (scene != null)
                    {
                        scene.assureVisible(element);
                    }
                }
                
            } catch (PropertyVetoException ex){}
    }
    
    
    public void focusExpression(JrxmlVisualView view, JRDesignExpression expression)
    {
            // 1 look for the node holding the expression...
            Node root = view.getModel();
            Node node = findExpressionNode( root, expression);
        
            if (node != null) focusNode(view, node);
            
            ExpressionEditor editor = new ExpressionEditor();
            
            if (node instanceof ExpressionHolder)
            {
                ExpressionContext context = ((ExpressionHolder)node).getExpressionContext(expression);
                editor.setExpressionContext(context);
            }
            else
            {
                // By default let's use the master dataset as expression context...
                
            }
            // here we should find the ExpressionContext....
            
            editor.setExpression(Misc.getExpressionText(expression));
            if (editor.showDialog(Misc.getMainFrame()) == JOptionPane.OK_OPTION)
            {
                expression.setText( editor.getExpression() );
                IReportManager.getInstance().notifyReportChange();
            }
    }
    
    public Node findExpressionNode( Node node, JRDesignExpression expression)
    {
        if (node instanceof ExpressionHolder)
        {
            if (((ExpressionHolder)node).hasExpression(expression)) return node;
        }
        
        Node[] nodes = node.getChildren().getNodes();
        for (int i=0; i<nodes.length; ++i)
        {
            Node newNode = findExpressionNode( nodes[i], expression);
            if (newNode != null) return newNode;
        }
        
        return null;
    }
}
