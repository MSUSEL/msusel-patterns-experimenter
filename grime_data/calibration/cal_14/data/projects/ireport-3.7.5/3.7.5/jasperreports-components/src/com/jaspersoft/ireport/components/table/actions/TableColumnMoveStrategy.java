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
package com.jaspersoft.ireport.components.table.actions;

import com.jaspersoft.ireport.components.table.TableObjectScene;
import com.jaspersoft.ireport.components.table.widgets.IndicatorWidget;
import java.awt.Point;
import java.util.List;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class TableColumnMoveStrategy implements MoveStrategy {
    
    public Point locationSuggested(Widget w, Point originalLocation, Point suggestedLocation) {

        if (w instanceof IndicatorWidget && ((IndicatorWidget)w).getType() == IndicatorWidget.COLUMN)
        {
            IndicatorWidget columnIndicator = (IndicatorWidget)w;
            suggestedLocation.y = originalLocation.y; // Which should be always 0...

            if (suggestedLocation.x < - (w.getBounds().width/2)) suggestedLocation.x = - (w.getBounds().width/2);
            else
            {
                List<Integer> verticalSeparators = ((TableObjectScene)w.getScene()).getVerticalSeparators();

                int tableEnd = verticalSeparators.get( verticalSeparators.size() - 1);

                if (suggestedLocation.x > tableEnd - (w.getBounds().width/2)) suggestedLocation.x = tableEnd - (w.getBounds().width/2);
            }
        }
        
        return suggestedLocation;
    }
}
