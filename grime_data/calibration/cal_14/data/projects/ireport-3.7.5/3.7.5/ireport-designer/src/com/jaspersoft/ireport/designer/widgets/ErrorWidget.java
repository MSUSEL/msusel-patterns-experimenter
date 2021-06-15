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
package com.jaspersoft.ireport.designer.widgets;

import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @version $Id: ErrorWidget.java 0 2009-12-02 18:18:15 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ErrorWidget extends Widget {

    private Widget parentWidget = null;

    public ErrorWidget(Scene scene, Widget parentWidget)
    {
        super(scene);
        this.parentWidget = parentWidget;
        createDependency();
    }

    /**
     * @return the parentWidget
     */
    public Widget getReferringWidget() {
        return parentWidget;
    }

    /**
     * @param parentWidget the parentWidget to set
     */
    public void setReferringWidget(Widget parentWidget) {
        this.parentWidget = parentWidget;
    }

    private void createDependency()
    {
        parentWidget.addDependency(new Dependency() {

            public void revalidateDependency() {
                if (parentWidget.getParentWidget() == null)
                {
                    ErrorWidget.this.removeFromParent();
                }
                else
                {
                    ErrorWidget.this.setPreferredLocation(parentWidget.getLocation());
                    ErrorWidget.this.setPreferredBounds( parentWidget.getBounds() );
                }
            }
        });


    }


}
