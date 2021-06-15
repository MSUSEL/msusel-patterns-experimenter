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
package com.jaspersoft.ireport.designer.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.editor.ExpressionEditor;
import com.jaspersoft.ireport.designer.undo.PropertyUndoableEdit;
import com.jaspersoft.ireport.designer.utils.Misc;
import javax.swing.JOptionPane;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import com.jaspersoft.ireport.designer.sheet.properties.AbstractProperty;

public final class EditTextfieldExpressionAction extends NodeAction {

        
    public String getName() {
        return I18n.getString("EditTextfieldExpressionAction.name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {
        ElementNode node = (ElementNode)activatedNodes[0];
        //ExpressionEditor2 editor = new ExpressionEditor2();
        ExpressionEditor editor = new ExpressionEditor();

        JRDesignTextField element = (JRDesignTextField)node.getElement();

        if (ModelUtils.getTopElementGroup(element) instanceof JRDesignCellContents)
        {
            JRDesignCellContents contents = (JRDesignCellContents) ModelUtils.getTopElementGroup(element);
            editor.setExpressionContext(new ExpressionContext(contents.getOrigin().getCrosstab()));
        }
        else
        {
            JRDesignDataset dataset = ModelUtils.getElementDataset(element, node.getJasperDesign());
            if (dataset != null)
            {
                editor.setExpressionContext(new ExpressionContext(dataset));
            }
        }

        if (element.getExpression() != null)
        {
            editor.setExpression( Misc.getExpressionText( element.getExpression() ));
        }

        if (editor.showDialog(Misc.getMainFrame()) == JOptionPane.OK_OPTION)
        {
            JRDesignExpression oldExp = (JRDesignExpression) element.getExpression();
            JRDesignExpression newExp = new JRDesignExpression();
            newExp.setValueClassName(  getExpressionClassName(element) );
            newExp.setText( editor.getExpression() );
            element.setExpression(newExp);

            Object obj = ModelUtils.findProperty(node.getPropertySets(), JRDesignTextField.PROPERTY_EXPRESSION);
            if (obj != null && obj instanceof AbstractProperty)
            {
                PropertyUndoableEdit edit = new PropertyUndoableEdit((AbstractProperty)obj, oldExp, newExp);
                IReportManager.getInstance().addUndoableEdit(edit);
                IReportManager.getInstance().notifyReportChange();
            }
        }


    }

    private String getExpressionClassName(JRDesignTextField element)
    {
        if (element.getExpression() == null) return "java.lang.String";
        if (element.getExpression().getValueClassName() == null) return "java.lang.String";
        return element.getExpression().getValueClassName();
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        if (!(activatedNodes[0] instanceof ElementNode)) return false;
        ElementNode node = (ElementNode)activatedNodes[0];
        if (node.getElement() instanceof JRDesignTextField)
        {
            return true;
        }
        return false;
    }
}