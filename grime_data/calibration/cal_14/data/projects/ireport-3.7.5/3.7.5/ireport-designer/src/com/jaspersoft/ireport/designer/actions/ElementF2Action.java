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

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;

/**
 *
 * @version $Id: ElementF2Action.java 0 2009-12-30 11:24:29 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ElementF2Action extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
        // Look for selected elements...
        JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();

        if (view == null) return;

        AbstractReportObjectScene scene = view.getReportDesignerPanel().getActiveScene();
        if (scene == null) return;

        if (scene.getSelectedObjects().isEmpty()) return;

        Object selectedObject = scene.getSelectedObjects().iterator().next();

        if (selectedObject instanceof JRDesignTextField)
        {
           // Find the node for this element...
            Node node = IReportManager.getInstance().findNodeOf(selectedObject, view.getExplorerManager().getRootContext());
            if (node != null)
            {
                SystemAction.get(EditTextfieldExpressionAction.class).performAction(new Node[]{node});
            }
        }


    }

}
