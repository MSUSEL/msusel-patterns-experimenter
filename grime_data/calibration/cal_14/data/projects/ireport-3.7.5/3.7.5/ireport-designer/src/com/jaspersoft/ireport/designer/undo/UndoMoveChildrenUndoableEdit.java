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
package com.jaspersoft.ireport.designer.undo;

import com.jaspersoft.ireport.designer.AbstractReportObjectScene;
import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.util.List;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.engine.design.JRDesignElement;

/**
 *
 * @author gtoffoli
 */
public class UndoMoveChildrenUndoableEdit  extends AggregatedUndoableEdit {

    private JRDesignElementWidget widget = null;


    public UndoMoveChildrenUndoableEdit(JRDesignElementWidget widget)
    {
        this.widget = widget;
    }

    @Override
    public void undo() throws CannotUndoException {

        super.undo();
        updateChildren();
    }

    @Override
    public void redo() throws CannotRedoException {

        super.redo();
        updateChildren();
    }



    @Override
    public String getPresentationName() {
        return "Element childrens update";
    }

    /**
     * @return the widget
     */
    public JRDesignElementWidget getWidget() {
        return widget;
    }

    /**
     * @param widget the widget to set
     */
    public void setWidget(JRDesignElementWidget widget) {
        this.widget = widget;
    }

    private void updateChildren() {
        updateChildren(widget);
        AbstractReportObjectScene scene = (AbstractReportObjectScene) widget.getScene();
        scene.validate();
    }
    private void updateChildren(JRDesignElementWidget wid) {

          List listOfElements = wid.getChildrenElements();
          AbstractReportObjectScene scene = (AbstractReportObjectScene) widget.getScene();

          for (int i=0; i < listOfElements.size(); ++i)
          {
               if (listOfElements.get(i) instanceof JRDesignElement)
               {
                   JRDesignElement element = (JRDesignElement)listOfElements.get(i);
                   JRDesignElementWidget w = (JRDesignElementWidget)scene.findWidget(element);
                   w.updateBounds();
                   w.getSelectionWidget().updateBounds();

                   if (w.getChildrenElements() != null)
                   {
                       updateChildren(w);
                   }
               }
          }


    }



}
