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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.layers;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JasperDesignActivatedListener;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignPropertyExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.actions.NodeAction;

/**
 *
 * @version $Id: LayersAction.java 0 2010-02-27 10:32:49 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class LayersAction extends NodeAction {

    JMenu menu = null;

    @Override
    protected void performAction(Node[] nodes) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendElementsToLayer(Layer layer)
    {
        Node[] nodes = getActivatedNodes();
        for (int i=0; i<nodes.length; ++i)
        {
            if (nodes[i] instanceof ElementNode)
            {
                ElementNode node = (ElementNode) nodes[i];
                // Set property and print when expression...
                LayersSupport.getInstance().addElementToLayer(node.getElement(), layer);
                
                // find the element in the layer...
                ReportObjectScene scene = IReportManager.getInstance().getActiveVisualView().getReportDesignerPanel().getScene();
                List<Widget> list = scene.getElementsLayer().getChildren();
                for (Widget w : list)
                {
                    if (w instanceof JRDesignElementWidget)
                    {
                        JRDesignElementWidget ew = (JRDesignElementWidget)w;
                        if (ew.getElement() == node.getElement())
                        {
                            if (!layer.isVisible())
                            {
                                IReportManager.getInstance().removeSelectedObject(ew.getElement());
                            }
                            ew.setVisible( layer.isVisible() );
                            ew.getSelectionWidget().setVisible( layer.isVisible() );
                            scene.revalidate(true);
                            scene.validate();
                            break;
                        }
                    }
                }

            }
        }
    }

    @Override
    protected boolean enable(Node[] nodes) {

        getMenu().setEnabled(false);
        if (nodes == null || nodes.length < 1) return false;

        for (int i=0; i<nodes.length; ++i)
        {
            if (!(nodes[i] instanceof ElementNode))
            {
                return false;
            }
        }
        getMenu().setEnabled(true);
        rebuildMenu();
        return true;
    }


    @Override
    public String getName() {
        return org.openide.util.NbBundle.getMessage(LayersAction.class, "LayersAction.name");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    @Override
    public JMenuItem getMenuPresenter() {
        return getMenu();
    }

    @Override
    public JMenuItem getPopupPresenter() {
        return getMenu();
    }

    public void jasperDesignActivated(JasperDesign jd) {

        menu.removeAll();



    }
/**
     * @return the menu
     */
    public JMenu getMenu() {
        if (menu == null)
        {
            menu = new JMenu(getName());
        }
        return menu;
    }

    /**
     * @param menu the menu to set
     */
    public void setMenu(JMenu menu) {
        this.menu = menu;
    }

    private void rebuildMenu() {

        getMenu().removeAll();

        List<Layer> layers = LayersSupport.getInstance().getLayers();
        
        for (final Layer layer : layers)
        {
            JMenuItem item = new JMenuItem(layer.getName());
            getMenu().add(item, 0);
            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    sendElementsToLayer(layer);
                }
            });
        }
    }

}
