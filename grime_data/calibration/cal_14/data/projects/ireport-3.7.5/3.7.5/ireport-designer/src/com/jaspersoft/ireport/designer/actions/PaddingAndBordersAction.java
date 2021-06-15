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
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.outline.nodes.CellNode;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.sheet.editors.box.BoxPanel;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

/**
 *
 * @author gtoffoli
 */
public final class PaddingAndBordersAction extends NodeAction {

    public String getName() {
        return I18n.getString("PaddingAndBordersAction.Property.PaddingAndBorders");
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
        
        JasperDesign design = null;
        if (activatedNodes[0] instanceof ElementNode)
        {
            design = ((ElementNode)activatedNodes[0]).getJasperDesign();
        }
        else if (activatedNodes[0] instanceof CellNode)
        {
            design = ((CellNode)activatedNodes[0]).getJasperDesign();
        }

        List<JRLineBox> boxes = new ArrayList<JRLineBox>();
        
        JRBoxContainer firstContainer = null;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (activatedNodes[i] instanceof ElementNode &&
                ((ElementNode)activatedNodes[i]).getElement() instanceof JRBoxContainer)
            {
                boxes.add(((JRBoxContainer) ((ElementNode)activatedNodes[i]).getElement()).getLineBox());
                if (firstContainer == null)
                {
                    firstContainer = (JRBoxContainer) ((ElementNode)activatedNodes[i]).getElement();
                }
            }
            else if (activatedNodes[i] instanceof CellNode)
            {
                boxes.add(((JRBoxContainer) ((CellNode)activatedNodes[i]).getCellContents()).getLineBox());
                if (firstContainer == null)
                {
                    firstContainer = (JRBoxContainer)((CellNode)activatedNodes[i]).getCellContents();
                }
            }
            
        }
        
        
        JDialog dialog = new JDialog(Misc.getMainFrame(), true);
        BoxPanel panel = new BoxPanel();
        
        
        JRLineBox box = createCommonBox(boxes, firstContainer);
        
        panel.setLineBox(box);
        
        box = panel.showDialog(box);
       
        
        if (box != null)
        {
            for (JRLineBox bb : boxes)
            {
                  ModelUtils.applyBoxProperties(bb, box);
            }
            if (boxes.size() > 0) IReportManager.getInstance().notifyReportChange();
        }
        
//        for (int i=0; i<activatedNodes.length; ++i)
//        {
//            if (activatedNodes[i] instanceof ElementNode)
//            {
//                (((ElementNode)activatedNodes[i]).getElement()).getEventSupport().firePropertyChange( JRDesignElement.PROPERTY_PARENT_STYLE, null, null);
//            }
//        }
        
    }

//    private void printBox(JRLineBox box)
//    {
//        if (box == null)
//        {
//            System.out.println("NULL");
//        }
//        else
//        {
//            System.out.println("Padding: " + box.getPadding() + " " + box.getTopPadding() + " " + box.getLeftPadding() + " " + box.getRightPadding() + " " + box.getBottomPadding());
//            System.out.println("Pen:        " + box.getPen().getLineWidth() + " " + box.getPen().getLineStyle() + " " + box.getPen().getLineColor());
//            System.out.println("Pen bottom: " + box.getBottomPen().getLineWidth() + " " + box.getBottomPen().getLineStyle() + " " + box.getBottomPen().getLineColor());
//            System.out.println("Pen top:    " + box.getTopPen().getLineWidth() + " " + box.getTopPen().getLineStyle() + " " + box.getTopPen().getLineColor());
//            
//                    
//        }
//    }
    
    private JRLineBox createCommonBox(List<JRLineBox> boxes, JRBoxContainer container)
    {
        if (boxes == null || boxes.size() == 0) return new JRBaseLineBox(container);
        if (boxes.size() == 1) return (boxes.get(0)).clone(container);
        
        JRBaseLineBox finalbox = new JRBaseLineBox(null);
        
        boolean isFirst = true;
        
        for (JRLineBox box : boxes)
        {
            
            if (isFirst)
            {
                ModelUtils.applyBoxProperties(finalbox, box);
                isFirst = false;
                continue;
            }
            
            ModelUtils.applyDiff(finalbox, box);
        
        }
    
        return finalbox;
    }
    
    
     
     
    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        
        // Check if all the elements are a JRBoxContainer
        for (int i=0; i<activatedNodes.length; ++i)
        {
                if (activatedNodes[i] instanceof ElementNode && ((ElementNode)activatedNodes[i]).getElement() instanceof JRBoxContainer)
                {
                    continue;
                }

                if (activatedNodes[i] instanceof CellNode)
                {
                    continue;
                }
                return false;
        }
        return true;
    }
}
