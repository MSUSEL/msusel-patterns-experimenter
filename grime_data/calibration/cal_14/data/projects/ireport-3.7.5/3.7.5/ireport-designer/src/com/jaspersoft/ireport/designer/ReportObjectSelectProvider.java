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
import com.jaspersoft.ireport.designer.widgets.SelectionWidget;
import java.awt.Point;
import java.util.Collections;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class ReportObjectSelectProvider implements SelectProvider {

        private AbstractReportObjectScene scene = null;

        public ReportObjectSelectProvider(AbstractReportObjectScene scene)
        {
            this.scene = scene;
        }

        public boolean isAimingAllowed (Widget widget, Point localLocation, boolean invertSelection) {
            return false;
        }

        public boolean isSelectionAllowed (Widget widget, Point localLocation, boolean invertSelection) {
            if (widget instanceof SelectionWidget)
            {
                widget = ((SelectionWidget)widget).getRealWidget();
            }

            return scene.findObject (widget) != null;
        }

        public void select (Widget widget, Point localLocation, boolean invertSelection) {

            if (widget instanceof SelectionWidget)
            {
                widget = ((SelectionWidget)widget).getRealWidget();
            }

            Object object = scene.findObject(widget);

            scene.setFocusedObject (object);
            
            if (object != null) {

                if (!invertSelection  &&  scene.getSelectedObjects ().contains (object))
                   return;

                scene.userSelectionSuggested (Collections.singleton (object), invertSelection);
            } else
            {
                if (!invertSelection)
                {
                    scene.userSelectionSuggested (Collections.emptySet (), invertSelection);
                }
            }
        }
}
