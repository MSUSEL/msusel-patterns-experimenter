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
package com.jaspersoft.ireport.jasperserver.ui.actions;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.palette.PaletteItemAction;
import com.jaspersoft.ireport.designer.tools.HyperlinkPanel;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTargetDropEvent;
import java.util.List;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;
import net.sf.jasperreports.engine.design.JRDesignHyperlinkParameter;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class CreateDrillDownAction extends PaletteItemAction  {

    @Override
    public void drop(DropTargetDropEvent dtde) {
        
        RepositoryReportUnit ru = (RepositoryReportUnit)getPaletteItem().getData();

        LayerWidget elementsLayer = ((AbstractReportObjectScene)this.getScene()).getElementsLayer();
        
        Point p = getScene().convertViewToScene( dtde.getLocation() );
            
        List<Widget> children = elementsLayer.getChildren();
        for (Widget w : children)
        {
            if (w instanceof JRDesignElementWidget)
            {
                System.out.println("check if hit: " + w + " " + p + " " + w.getLocation() + "  " + w.getBounds());
                Rectangle r = w.getBounds();
                r.x = w.getLocation().x;
                r.y = w.getLocation().y;

                if (r.contains(p))
                {
                    JRDesignElement element = ((JRDesignElementWidget)w).getElement();
                    System.out.println("Element hit at: " + element);
                    if (element instanceof JRHyperlink)
                    {
                        System.out.println("Element JRHyperlink hit at: " + element);
                        JRHyperlink hl = (JRHyperlink)element;
                        JRDesignHyperlink newHl = new JRDesignHyperlink();
                        newHl.setLinkType("ReportExecution");
                    
                        List linkParameters = new java.util.ArrayList();
                        JRDesignHyperlinkParameter p1 = new JRDesignHyperlinkParameter();
                        p1.setName("_report");
                        JRDesignExpression p1_exp = Misc.createExpression("java.lang.String", "\"" + ru.getDescriptor().getUriString() + "\"");
                        p1.setValueExpression(p1_exp);
                        newHl.addHyperlinkParameter(p1);
                    
                        List listChildren = ru.getDescriptor().getChildren();
                        
                        if (!ru.isLoaded() || ru.getChildren().size() == 0)
                        {
                            ResourceDescriptor descriptor = new ResourceDescriptor();
                            descriptor.setWsType( descriptor.TYPE_REPORTUNIT);
                            descriptor.setUriString(ru.getDescriptor().getUriString());
                            try {

                                listChildren = ru.getServer().getWSClient().list(descriptor);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //Exceptions.printStackTrace(ex);
                            }
                        }
                    
                    
                        // Look for all the Input controls availables....
                        for (int i=0; i<listChildren.size(); ++i)
                        {
                            ResourceDescriptor childRd = (ResourceDescriptor)listChildren.get(i);
                            if (childRd.getWsType().equals( childRd.TYPE_INPUT_CONTROL ))
                            {
                                JRDesignHyperlinkParameter ptmp = new JRDesignHyperlinkParameter();
                                ptmp.setName(childRd.getName());
                                JRDesignExpression ptmp_exp = Misc.createExpression("java.lang.Object", null);
                                ptmp.setValueExpression(ptmp_exp);
                                newHl.addHyperlinkParameter(ptmp);
                            }
                        }
                    
                        ModelUtils.copyHyperlink(newHl, hl);

                        JasperDesign design = getJasperDesign();

                        HyperlinkPanel pd = new HyperlinkPanel();
                        pd.setExpressionContext(new ExpressionContext( ModelUtils.getElementDataset(element, design)) );
                        pd.setHyperlink((JRHyperlink)element);
                        pd.showDialog( Misc.getMainFrame() );
                    }
                }
            }
        }
        
    }

}
