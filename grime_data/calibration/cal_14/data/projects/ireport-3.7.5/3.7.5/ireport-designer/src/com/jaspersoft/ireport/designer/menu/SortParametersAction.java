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
package com.jaspersoft.ireport.designer.menu;

import com.jaspersoft.ireport.designer.outline.nodes.ParametersNode;
import com.jaspersoft.ireport.designer.outline.nodes.SortableParametersNode;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import org.openide.util.actions.Presenter;

/**
 *
 * @version $Id: SortFieldsAction.java 0 2010-01-05 10:49:06 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class SortParametersAction  extends NodeAction implements Presenter.Menu, Presenter.Popup {

    private static JCheckBoxMenuItem SORT_MENU;

    public String getName() {
        return I18n.getString("Inspector.SortParameters");
    }

    public SortParametersAction()
    {
        super();
        SORT_MENU = new JCheckBoxMenuItem(getName());
        SORT_MENU.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                performAction(getActivatedNodes());
            }
        });
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
        ((SortableParametersNode)activatedNodes[0]).setSort(!((SortableParametersNode)activatedNodes[0]).isSort());
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes != null &&
                activatedNodes.length == 1 &&
                activatedNodes[0] instanceof SortableParametersNode)
        {
            SORT_MENU.setEnabled(true);
            SORT_MENU.setSelected( ((SortableParametersNode)activatedNodes[0]).isSort());
            return true;
        }
        SORT_MENU.setEnabled(false);
        return false;
    }

    @Override
    public JMenuItem getMenuPresenter()
    {
        return SORT_MENU;
    }

    @Override
    public JMenuItem getPopupPresenter()
    {
        return SORT_MENU;
    }
}

