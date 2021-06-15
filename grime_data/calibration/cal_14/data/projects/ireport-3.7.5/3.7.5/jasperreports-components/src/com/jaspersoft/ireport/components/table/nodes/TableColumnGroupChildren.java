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

import java.util.List;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class TableColumnGroupChildren extends AbstractTableChildren {

    private StandardColumnGroup columnGroup = null;
        
    @SuppressWarnings("unchecked")
    public TableColumnGroupChildren(JasperDesign jd, JRDesignComponentElement componentElement, StandardColumnGroup columnGroup, byte section, JRDesignGroup group, Lookup doLkp) {
        super(jd,componentElement,section,group,doLkp);
        this.columnGroup = columnGroup;
        columnGroup.getEventSupport().addPropertyChangeListener(this);
    }


    
    @SuppressWarnings("unchecked")
    public void recalculateKeysImpl() {
        
        List l = (List)lock();

        for (int i=0; i<l.size(); ++i)
        {
            StandardBaseColumn col = (StandardBaseColumn)l.get(i);
            col.getEventSupport().removePropertyChangeListener(getCellPropertyName(), this);
        }

        l.clear();

        List<BaseColumn> columns = columnGroup.getColumns();
        l.addAll(columns);

        for (int i=0; i<l.size(); ++i)
        {
            StandardBaseColumn col = (StandardBaseColumn)l.get(i);
            col.getEventSupport().addPropertyChangeListener(getCellPropertyName(), this);
        }
        
        // update(); Performed by the abstract superclass
    }
    
}
