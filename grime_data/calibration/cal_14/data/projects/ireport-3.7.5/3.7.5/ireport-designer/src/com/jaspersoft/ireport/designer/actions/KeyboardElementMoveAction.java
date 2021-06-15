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
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.WidgetAction.State;
import org.netbeans.api.visual.action.WidgetAction.WidgetKeyEvent;
import org.netbeans.api.visual.widget.Widget;


/**
 * @author Giulio Toffoli
 * 
 * This action moves all the selected widgets/elements of 1 pixel
 * (or 10 pixels if the shift key is down).
 *  
 */
public class KeyboardElementMoveAction extends WidgetAction.Adapter {

    public KeyboardElementMoveAction() {
        super();
    }

    @Override
    public State keyPressed(Widget widget, WidgetKeyEvent event) {
        
        if (!event.isControlDown())
        {
            if (event.getKeyCode() == KeyEvent.VK_LEFT ||
                event.getKeyCode() == KeyEvent.VK_RIGHT ||
                event.getKeyCode() == KeyEvent.VK_UP ||
                event.getKeyCode() == KeyEvent.VK_DOWN)
            {

                int len = (event.isShiftDown()) ? 10 : 1;

                Point delta = new Point(0,0);
                switch (event.getKeyCode())
                {
                    case KeyEvent.VK_LEFT: delta.x -= len; break;
                    case KeyEvent.VK_RIGHT: delta.x = len; break;
                    case KeyEvent.VK_UP: delta.y -= len; break;
                    case KeyEvent.VK_DOWN: delta.y = len; break;
                }

                List<JRDesignElementWidget> widgets = new ArrayList<JRDesignElementWidget>();
                AbstractReportObjectScene scene = (AbstractReportObjectScene)widget.getScene();

                Iterator iterSelectedObject = scene.getSelectedObjects().iterator();
                while (iterSelectedObject.hasNext())
                {
                    Object obj = iterSelectedObject.next();
                    Widget w = scene.findWidget(obj);
                    if (w instanceof JRDesignElementWidget)
                    {
                        widgets.add((JRDesignElementWidget)w);
                    }
                }

                move(widgets, delta);

                return State.CHAIN_ONLY;
            }
        }
        return State.REJECTED;
    }
    
    private void move (List<JRDesignElementWidget> widgets, Point delta) {

        ArrayList<Widget> changedWidgets = new ArrayList<Widget>();

        for (JRDesignElementWidget dew : widgets)
        {
            Point dewloc = dew.getPreferredLocation();
            dewloc.translate(delta.x, delta.y);
            dewloc = dew.convertLocalToModelLocation(dewloc);
            boolean b = dew.setChanging(true);
            try {
                dew.getElement().setX( dewloc.x);
                dew.getElement().setY( dewloc.y);
            } finally {
                dew.setChanging(b);
            }
            dew.updateBounds();

            // Sync the selection...
            dew.getSelectionWidget().updateBounds();
            
            changedWidgets.add(dew);
        }

        for (JRDesignElementWidget dew : widgets)
        {
            if (dew.getChildrenElements() != null)
            {
                updateChildren(dew, (AbstractReportObjectScene)dew.getScene(), changedWidgets);
            }
        }
    }


    private void updateChildren(JRDesignElementWidget dew, AbstractReportObjectScene scene, ArrayList<Widget> changedWidgets)
    {
          List listOfElements = dew.getChildrenElements();
          
          for (int i=0; i < listOfElements.size(); ++i)
          {
               if (listOfElements.get(i) instanceof JRDesignElement)
               {
                   JRDesignElement element = (JRDesignElement)listOfElements.get(i);
                   JRDesignElementWidget w = (JRDesignElementWidget)scene.findWidget(element);
                   if (changedWidgets.contains(w)) continue;
                   w.updateBounds();
                   w.getSelectionWidget().updateBounds();

                   if (w.getChildrenElements() != null)
                   {
                       updateChildren(w, scene, changedWidgets);
                   }

                   changedWidgets.add(w);
               }
          }
    }
}

