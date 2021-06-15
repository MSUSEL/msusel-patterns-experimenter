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
package com.jaspersoft.ireport.designer.tools;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.undo.DeleteElementUndoableEdit;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignFrame;

/**
 *
 * @version $Id: DeletePerformer.java 0 2010-09-02 19:49:02 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class DeletePerformer {

    private List<JRDesignElement> elementsToDelete = new ArrayList<JRDesignElement>();
    private Timer t = null;

    public void deleteElement(JRDesignElement element)
    {
        if (!elementsToDelete.contains(element))
        {
            elementsToDelete.add(element);
        }

        if (t == null)
        {
            t = new Timer();
            t.schedule(new TimerTask() {

                @Override
                public void run() {
                    deleteElements();
                }
            }, 100);
        }
    }

    private void deleteElements() {

            List<JRDesignElement> elements = new ArrayList<JRDesignElement>(elementsToDelete);
            elementsToDelete.clear();
            t = null;

            List containers = new ArrayList();

            boolean first = true;
            for (JRDesignElement element : elements)
            {
               Object container = element.getElementGroup();
               if (!containers.contains(container))
               {
                containers.add(container);
               }

               int index = 0;
               if (container instanceof JRDesignElementGroup)
               {
                   index = ((JRDesignElementGroup)container).getChildren().indexOf(element);
                   ((JRDesignElementGroup)container).getChildren().remove(element);
                   element.setElementGroup(null);
               }
               if (container instanceof JRDesignFrame)
               {
                   index = ((JRDesignFrame)container).getChildren().indexOf(element);
                   ((JRDesignFrame)container).getChildren().remove(element);
                   element.setElementGroup(null);
               }

               DeleteElementUndoableEdit edit = new DeleteElementUndoableEdit(element,container,index);
               IReportManager.getInstance().addUndoableEdit(edit, !first);
               first=false;
            }

            // Notify all the containers...
            for (Object container : containers)
            {
                if (container instanceof JRDesignElementGroup)
                {
                    ((JRDesignElementGroup)container).getEventSupport().firePropertyChange(JRDesignElementGroup.PROPERTY_CHILDREN, null, 0);
                }
                else if (container instanceof JRDesignFrame)
                {
                    ((JRDesignFrame)container).getEventSupport().firePropertyChange(JRDesignElementGroup.PROPERTY_CHILDREN, null, 0);
                }
                else {
                    System.out.println("Unknown container: " + container);
                }

            }
    }
    
}
