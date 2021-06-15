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

import com.jaspersoft.ireport.designer.widgets.BandSeparatorWidget;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

import java.awt.event.MouseEvent;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;

/**
 * @author David Kaspar
 */
public class BandDblClickResizeAction extends WidgetAction.Adapter {

    public WidgetAction.State mousePressed(Widget widget,   WidgetAction.WidgetMouseEvent event)
    {

        if (event.getButton() == MouseEvent.BUTTON1 &&
            event.getClickCount() == 2 &&
            widget instanceof BandSeparatorWidget)
        {

            JRDesignBand band = (JRDesignBand) ((BandSeparatorWidget)widget).getBand();
            // Find the lowest coordinate of a contained element...
            int height = 0;

            List children = band.getChildren();
            for (int i=0; i<children.size(); ++i)
            {
                if (children.get(i) instanceof JRDesignElement)
                {
                    JRDesignElement ele = (JRDesignElement)children.get(i);
                    if (ele.getY() + ele.getHeight() > height)
                    {
                        height = ele.getY() + ele.getHeight();
                    }
                }
            }

            if (height> 0 && height != band.getHeight())
            {
                band.setHeight(height);
            }
        }

        return WidgetAction.State.REJECTED; // let someone use it...
    }

}
