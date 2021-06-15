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
package com.jaspersoft.ireport.designer.crosstab.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.crosstab.CrosstabObjectScene;
import com.jaspersoft.ireport.designer.outline.OutlineTopComponent;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;

/**
 *
 * @author gtoffoli
 */
public class CellSelectionAction extends WidgetAction.Adapter {

    public WidgetAction.State mousePressed(Widget widget,   WidgetAction.WidgetMouseEvent event)
    {
        
        if (event.getButton() == MouseEvent.BUTTON1 && widget instanceof CrosstabObjectScene)
        {
            // find the correct band...
            JasperDesign jd = ((CrosstabObjectScene)widget).getJasperDesign();
            JRDesignCrosstab crosstab = ((CrosstabObjectScene)widget).getDesignCrosstab();
            if (jd != null && crosstab != null)
            {
                    JRDesignCellContents cellContent = ModelUtils.getCellAt(crosstab, event.getPoint());
                    // If the cell is null, the document root is selected.
                    final Node node = IReportManager.getInstance().findNodeOf(cellContent == null ? crosstab : cellContent, OutlineTopComponent.getDefault().getExplorerManager().getRootContext());

                    SwingUtilities.invokeLater( new Runnable() {

                    public void run() {
                         try {
                            IReportManager.getInstance().getActiveVisualView().getExplorerManager().setSelectedNodes(new Node[]{node});
                            //IReportManager.getInstance().setSelectedObject(cellContent == null ? crosstab : cellContent);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                    //IReportManager.getInstance().setSelectedObject(cellContent == null ? crosstab : cellContent);
            }
        }
        
        return WidgetAction.State.REJECTED; // let someone use it...
    }
    
}
