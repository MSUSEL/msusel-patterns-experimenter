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
package com.jaspersoft.ireport.designer.crosstab.actions;

import com.jaspersoft.ireport.designer.widgets.*;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.crosstab.CrosstabObjectScene;
import com.jaspersoft.ireport.designer.crosstab.widgets.CellSeparatorWidget;
import java.awt.Color;
import java.awt.Point;
import java.util.List;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.widget.SeparatorWidget.Orientation;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author gtoffoli
 */
public class CellSeparatorMoveStrategy implements MoveStrategy {

    boolean reversOrder = false;
    
    public CellSeparatorMoveStrategy()
    {
        this(false);
    }
    
    public CellSeparatorMoveStrategy( boolean reversOrder)
    {
        this.reversOrder =  reversOrder;
    }
    
    public Point locationSuggested(Widget w, Point originalLocation, Point suggestedLocation) {

        if (w instanceof CellSeparatorWidget)
        {
            CellSeparatorWidget separator = (CellSeparatorWidget)w;
            CrosstabObjectScene scene = (CrosstabObjectScene)w.getScene();
            Orientation orientation = ((CellSeparatorWidget)w).getOrientation();
            if (orientation == Orientation.HORIZONTAL)
            {
                suggestedLocation.x = 0;
                int previousY = separator.getIndex() > 0 ? scene.getHorizontalSeparators().get(  separator.getIndex() -1 ) : 0 ;
                suggestedLocation.y = Math.max(previousY, suggestedLocation.y);
            }
            else
            {
                suggestedLocation.y = 0;
                int previousX = separator.getIndex() > 0 ? scene.getVerticalSeparators().get(  separator.getIndex() -1  ) : 0;
                suggestedLocation.x = Math.max(previousX, suggestedLocation.x);
            }
        }
        /*
        JRBand b = ((BandSeparatorWidget)w).getBand();
        JasperDesign jd = ((ReportObjectScene)w.getScene()).getJasperDesign();
        
        if (!reversOrder && b.getHeight() == 0)
        {
            // Look for the right band...
             List<JRBand> bands = ModelUtils.getBands(jd);
             JRBand rightBand = bands.get(0);
             for (JRBand tmpBand : bands)
             {
                 if (tmpBand == b) break;
                 if (tmpBand.getHeight() == 0) continue;
                 rightBand = tmpBand;
             }
             b = rightBand;
        }
        
        // y must be between the bottom of the previous band and max design height + band height + current band height

        int bLocation = ModelUtils.getBandLocation(b, jd);
        int maxY = bLocation + ModelUtils.getMaxBandHeight((JRDesignBand)b, jd); 
       
        w.setForeground(ReportObjectScene.EDITING_DESIGN_LINE_COLOR);
        if (bLocation >= suggestedLocation.y) 
        {
            suggestedLocation.y = bLocation;
            w.setForeground(Color.RED);
        }
        else
        {
            suggestedLocation.y = Math.min(maxY, suggestedLocation.y);
            if (suggestedLocation.y == maxY)
            {
                w.setForeground(Color.RED);
            }
        }
        */
        return suggestedLocation;
    }
}
