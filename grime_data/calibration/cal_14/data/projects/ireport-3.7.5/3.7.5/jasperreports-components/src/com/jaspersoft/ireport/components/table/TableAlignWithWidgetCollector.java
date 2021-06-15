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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table;

import com.jaspersoft.ireport.designer.actions.ReportAlignWithWidgetCollector;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @version $Id: TableAlignWithWidgetCollector.java 0 2010-03-31 11:08:27 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class TableAlignWithWidgetCollector extends ReportAlignWithWidgetCollector {

    TableObjectScene scene = null;

    public TableAlignWithWidgetCollector(TableObjectScene scene)
    {
        super(scene);
        this.scene = scene;
    }

    public Collection<Rectangle> getRegions(Widget movingWidget) {

        Collection<Rectangle> regions = super.getRegions(movingWidget);

        // Add all the cells as regions...

        List<TableCell> cells = scene.getTableMatrix().getCells();
       for (TableCell cell : cells)
       {
           regions.add(scene.getTableMatrix().getCellBounds(cell));
       }


        return regions;
    }

    
}
