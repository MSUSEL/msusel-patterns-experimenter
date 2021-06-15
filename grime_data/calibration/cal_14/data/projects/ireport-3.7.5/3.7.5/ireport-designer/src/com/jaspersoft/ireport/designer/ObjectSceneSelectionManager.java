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
package com.jaspersoft.ireport.designer;

import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.jasperreports.engine.design.JRDesignElement;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.model.ObjectSceneListener;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class ObjectSceneSelectionManager implements ObjectSceneListener{

    private AbstractReportObjectScene scene;

    private List<JRDesignElement> selectedElements = null;

    public ObjectSceneSelectionManager(AbstractReportObjectScene scene)
    {
        this.scene = scene;
        selectedElements = new ArrayList<JRDesignElement>();
        scene.addObjectSceneListener(this, ObjectSceneEventType.OBJECT_SELECTION_CHANGED);
    }

    public void objectAdded(ObjectSceneEvent arg0, Object arg1) {

    }

    public void objectRemoved(ObjectSceneEvent arg0, Object arg1) {
    }

    public void objectStateChanged(ObjectSceneEvent arg0, Object arg1, ObjectState arg2, ObjectState arg3) {
    }

    public void selectionChanged(ObjectSceneEvent event,
                      Set<Object> previousSelection,
                      Set<Object> newSelection)
    {
        // remove from selection what is no longer there...
        for (int i=0; i<selectedElements.size(); ++i)
        {
            if (!newSelection.contains(selectedElements.get(i)))
            {
                selectedElements.remove(i);
                i--;
            }
        }

        // queue the others...
        for (Iterator iter = newSelection.iterator(); iter.hasNext(); )
        {
            Object elem = iter.next();
            if (elem instanceof JRDesignElement)
            {
                JRDesignElement delem = (JRDesignElement)elem;
                selectedElements.add(delem);
            }
        }

        // update nodes...
        if (selectedElements.size() > 0)
        {
            for (JRDesignElement element : selectedElements)
            {
                Widget w = getScene().findWidget(element);
                if (w instanceof JRDesignElementWidget)
                {
                    JRDesignElementWidget dew = (JRDesignElementWidget)w;
                    dew.getSelectionWidget().updateBounds();
                    dew.getSelectionWidget().revalidate(true);
                }
            }
            getScene().validate();
        }
        
    }

    public void highlightingChanged(ObjectSceneEvent arg0, Set<Object> arg1, Set<Object> arg2) {
    }

    public void hoverChanged(ObjectSceneEvent arg0, Object arg1, Object arg2) {
    }

    public void focusChanged(ObjectSceneEvent arg0, Object arg1, Object arg2) {
    }

    /**
     * @return the scene
     */
    public AbstractReportObjectScene getScene() {
        return scene;
    }

    /**
     * @param scene the scene to set
     */
    public void setScene(AbstractReportObjectScene scene) {
        this.scene = scene;
    }

    /**
     * @return the selectedElements
     */
    public List<JRDesignElement> getSelectedElements() {
        return selectedElements;
    }
}
