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
package com.jaspersoft.ireport.components.table.nodes;

import com.jaspersoft.ireport.components.table.TableCell;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class TableSectionChildren extends AbstractTableChildren {

   
    private StandardTable table = null;

    
    @SuppressWarnings("unchecked")
    public TableSectionChildren(JasperDesign jd, JRDesignComponentElement componentElement, byte section, JRDesignGroup group, Lookup doLkp) {
        super(jd,componentElement,section,group,doLkp);
        this.table = (StandardTable)componentElement.getComponent();
        table.getEventSupport().addPropertyChangeListener(this);
    }



    @SuppressWarnings("unchecked")
    public void recalculateKeysImpl() {
        
        List l = (List)lock();

        if (getSection() == TableCell.DETAIL)
        {
            List<StandardBaseColumn> columns = getAllColumns();
            for (StandardBaseColumn col : columns)
            {
                col.getEventSupport().removePropertyChangeListener(getCellPropertyName(), this);
                col.getEventSupport().addPropertyChangeListener(getCellPropertyName(), this);
                col.getEventSupport().removePropertyChangeListener(StandardColumnGroup.PROPERTY_COLUMNS, this);
                col.getEventSupport().addPropertyChangeListener(StandardColumnGroup.PROPERTY_COLUMNS, this);
            }
            l.clear();
            l.addAll(getStandardColumns());
        }
        else
        {

            for (int i=0; i<l.size(); ++i)
            {
                StandardBaseColumn col = (StandardBaseColumn)l.get(i);
                col.getEventSupport().removePropertyChangeListener(getCellPropertyName(), this);
            }

            l.clear();
            l.addAll(table.getColumns());

            for (int i=0; i<l.size(); ++i)
            {
                StandardBaseColumn col = (StandardBaseColumn)l.get(i);
                col.getEventSupport().addPropertyChangeListener(getCellPropertyName(), this);
            }
        }

        // update(); performed by the super class
    }


    public List<StandardColumn> getStandardColumns() {

        List<StandardColumn> standardColumns = new ArrayList<StandardColumn>();
        List<BaseColumn> columns = table.getColumns();
        for (BaseColumn c : columns)
        {
            standardColumns.addAll(getStandardColumns(c));
        }
        return standardColumns;
    }

    /**
     * REturns the position of each line.
     * startingWidth is the starting point from left.
     * @param col
     * @return
     */
    private List<StandardColumn> getStandardColumns(BaseColumn col) {

        List<StandardColumn> standardColumns = new ArrayList<StandardColumn>();

        if (col instanceof StandardColumn)
        {
            standardColumns.add((StandardColumn) col);
        }
        else if (col instanceof StandardColumnGroup)
        {
            StandardColumnGroup columnGroup = (StandardColumnGroup)col;
            List<BaseColumn> columns = columnGroup.getColumns();
            for (BaseColumn c : columns)
            {
                standardColumns.addAll(getStandardColumns(c));
            }
        }
        return standardColumns;
    }


    public List<StandardBaseColumn> getAllColumns() {

        List<StandardBaseColumn> allColumns = new ArrayList<StandardBaseColumn>();
        List<BaseColumn> columns = table.getColumns();
        for (BaseColumn c : columns)
        {
            allColumns.addAll(getAllColumns((StandardBaseColumn) c));
        }
        return allColumns;
    }

    /**
     * REturns the position of each line.
     * startingWidth is the starting point from left.
     * @param col
     * @return
     */
    private List<StandardBaseColumn> getAllColumns(StandardBaseColumn col) {

        List<StandardBaseColumn> allColumns = new ArrayList<StandardBaseColumn>();

        allColumns.add(col);
        if (col instanceof StandardColumnGroup)
        {
            StandardColumnGroup columnGroup = (StandardColumnGroup)col;
            List<BaseColumn> columns = columnGroup.getColumns();
            for (BaseColumn c : columns)
            {
                allColumns.addAll(getAllColumns((StandardBaseColumn)c));
            }
        }
        return allColumns;
    }


    

}
